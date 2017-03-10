package com.ludo.jar2pom;

import com.ludo.jar2pom.core.remote.CompositeDescriptorStrategy;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import com.ludo.jar2pom.model.Arguments;
import com.ludo.jar2pom.service.converter.Converter;
import com.ludo.jar2pom.service.converter.JarConverter;
import com.ludo.jar2pom.service.output.MustacheOutputWriter;
import com.ludo.jar2pom.service.output.OutputWriter;
import com.ludo.jar2pom.support.PathOptionHandler;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.ParserProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.remote.NexusIdentifyDescriptorStrategy;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CmdLine implements Arguments {
    // TODO : ldez - 9 août 2014 : ajouter l'option du type de recherche : checksum/checksum&artifactId

    private static final Logger LOG = LoggerFactory.getLogger(CmdLine.class);

    @Option(name = "-i", usage = "input path (file or directory).", metaVar = "INPUT", required = false, handler = PathOptionHandler.class)
    private Path input = Paths.get(".");

    @Option(name = "-r", usage = "inspect input path recursively.")
    private boolean recursive;

    @Option(name = "-o", usage = "directory output path.\n(default output is console)", metaVar = "OUTPUT", required = false, handler = PathOptionHandler.class)
    private Path output = Paths.get(".");

    @Option(name = "-host", usage = "defined custom Nexus host.\n(ex: oss.sonatype.org)", metaVar = "HOST", required = false)
    private String customHost = null;

    @Option(name = "-p", aliases = {"--proxy"}, usage = "Use system proxies.", required = false)
    private boolean systemProxy;

    @Option(name = "-h", aliases = {"--help"}, usage = "display help.", help = true)
    private boolean help;

    private final Converter converter;

    public CmdLine(final Converter converter) {
        super();
        this.converter = converter;
    }

    @Override
    public final Path getInput() {
        return this.input;
    }

    @Override
    public final boolean isRecursive() {
        return this.recursive;
    }

    @Override
    public final Path getOutput() {
        return this.output;
    }

    @Override
    public final String getCustomHost() {
        return this.customHost;
    }

    @Override
    public final boolean isSystemProxy() {
        return this.systemProxy;
    }

    @Override
    public final boolean isHelp() {
        return this.help;
    }

    public final void doMain(final String[] args) {

        // Parser properties
        final ParserProperties parserProperties = ParserProperties.defaults();
        parserProperties.withUsageWidth(100);

        // Parser
        final CmdLineParser parser = new CmdLineParser(this, parserProperties);

        try {
            // parse the arguments.
            parser.parseArgument(args);

            if (this.isHelp()) {
                printHelp(parser, System.out);
            } else {
                LOG.debug("Input: {}", this.input);

                // set the use of system proxies
                if (this.isSystemProxy()) {
                    System.setProperty("java.net.useSystemProxies", "true");
                }

                // convert
                this.converter.process(this);
            }
        } catch (final CmdLineException | IOException e) {
            LOG.trace("CmdLine error.", e);
            // if there's a problem in the command line, you'll get this exception. this will report an error message.
            printError(parser, e.getMessage());
        }
    }

    private static void printError(final CmdLineParser parser, final String msg) {
        System.err.println(msg);
        System.err.println();
        printHelp(parser, System.err);
    }

    private static void printHelp(final CmdLineParser parser, final PrintStream printStream) {
        // print help header
        printStream.println("java -jar jar2pom.jar [options...]");
        // print the list of available options
        parser.printUsage(printStream);
        // print options sample
        printStream.printf("%n Example: java -jar jar2pom.jar%1$s", parser.printExample(OptionHandlerFilter.ALL));
    }

    public static void main(final String[] args) throws IOException {
        // TODO : ldez - 12 août 2014 : add multiple strategy
        // create remote search strategy
        final DescriptorStrategy nexusIdentifyDescriptorStrategy = new NexusIdentifyDescriptorStrategy();
        final DescriptorStrategy descriptorStrategy = new CompositeDescriptorStrategy(nexusIdentifyDescriptorStrategy);

        // output file writer
        final OutputWriter outputWriter = new MustacheOutputWriter();

        // Converter
        final Converter converter = new JarConverter(descriptorStrategy, outputWriter);

        // process
        final CmdLine cmdLine = new CmdLine(converter);
        cmdLine.doMain(args);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
