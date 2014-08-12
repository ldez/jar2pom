package com.ludo.jar2pom;

import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.ParserProperties;
import org.kohsuke.args4j.spi.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.remote.NexusIdentifyDescriptorStrategy;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Sets;
import com.ludo.jar2pom.core.file.FileJarVisitor;
import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.CompositeDescriptorStrategy;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import com.ludo.jar2pom.support.PathOptionHandler;

public class CmdLine {
    // TODO : ldez - 9 août 2014 : ajouter l'option du type de recherche : checksum/checksum&artifactId

    private static final Logger LOG = LoggerFactory.getLogger(CmdLine.class);

    public static final String OUTPUT_POM_PATTERN = "pom_%1$tY-%<tm-%<td_%<tH-%<tM-%<tS_%<tL%<tN.xml";

    public static final String OUTPUT_POM_TEMPLATE = "template.mustache";

    @Option(name = "-i", usage = "input path (file or directory).", metaVar = "INPUT", required = false, handler = PathOptionHandler.class)
    private final Path input = Paths.get(".");

    @Option(name = "-r", usage = "inspect input path recursively.")
    private boolean recursive;

    @Option(name = "-o", usage = "directory output path.\n(default output is console)", metaVar = "OUTPUT", required = false, handler = PathOptionHandler.class)
    private final Path output = Paths.get(".");

    @Option(name = "-host", usage = "defined custom Nexus host.\n(ex: oss.sonatype.org)", required = false)
    private final String customHost = null;

    @Option(name = "-p", aliases = { "--proxy" }, usage = "Use system proxies.", required = false)
    private boolean systemProxy;

    @Option(name = "-h", aliases = { "--help" }, usage = "display help.", help = true)
    private boolean help;

    private final DescriptorStrategy descriptorStrategy;

    public CmdLine(final DescriptorStrategy descriptorStrategy) {
        super();
        this.descriptorStrategy = descriptorStrategy;
    }

    public static void main(final String[] args) throws IOException {
        // TODO : Sauron - 12 août 2014 : add multiple strategy
        // create remote search strategy
        final DescriptorStrategy nexusIdentifyDescriptorStrategy = new NexusIdentifyDescriptorStrategy();
        final DescriptorStrategy descriptorStrategy = new CompositeDescriptorStrategy(nexusIdentifyDescriptorStrategy);

        final CmdLine cmdLine = new CmdLine(descriptorStrategy);

        // process
        cmdLine.doMain(args);
    }

    public final void doMain(final String[] args) throws IOException {

        // Parser properties
        final ParserProperties parserProperties = ParserProperties.defaults();
        parserProperties.withUsageWidth(100);

        // Parser
        final CmdLineParser parser = new CmdLineParser(this, parserProperties);

        try {
            // parse the arguments.
            parser.parseArgument(args);

            Files.exists(this.output, LinkOption.NOFOLLOW_LINKS);
            Files.isDirectory(this.output, LinkOption.NOFOLLOW_LINKS);

            if (this.help) {
                this.printHelp(parser, System.out);
            } else {
                // check output Directory, create if not exists
                this.createDirectory(parser, this.output);

                // set the use of system proxies
                if (this.systemProxy) {
                    System.setProperty("java.net.useSystemProxies", "true");
                }

                // create file visitor
                final FileJarVisitor fileVisitor = new FileJarVisitor(this.descriptorStrategy, this.customHost);

                // browse file and create Descriptor
                if (this.recursive) {
                    Files.walkFileTree(this.input, fileVisitor);
                } else {
                    final Set<FileVisitOption> options = Sets.newHashSet(FileVisitOption.FOLLOW_LINKS);
                    Files.walkFileTree(this.input, options, 0, fileVisitor);
                }

                // get all Descriptor
                final List<Descriptor> descriptors = fileVisitor.getDescriptors();

                // write output file
                this.writeOutputFile(descriptors);
            }
        } catch (final CmdLineException e) {
            // if there's a problem in the command line, you'll get this exception. this will report an error message.
            System.err.println(e.getMessage());
            System.err.println();
            this.printHelp(parser, System.err);
        }
    }

    private void printHelp(final CmdLineParser parser, final PrintStream printStream) {
        printStream.printf("java %s [options...]%n", CmdLine.class.getSimpleName());
        // print the list of available options
        parser.printUsage(printStream);
        // print options sample
        printStream.printf("%n Example: java %1$s%2$s", CmdLine.class.getSimpleName(), parser.printExample(OptionHandlerFilter.ALL));
    }

    protected final void createDirectory(final CmdLineParser parser, final Path file) throws CmdLineException {
        LOG.debug("Output directory : {}", file.toAbsolutePath());
        if (Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
            // FIXME : Sauron - 12 août 2014 : no action
        } else if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
            // is not directory : FAIL
            throw new CmdLineException(parser, Messages.ILLEGAL_PATH, this.output.toString());
        } else {
            final Set<PosixFilePermission> perms = EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE);
            try {
                Files.createDirectories(file, PosixFilePermissions.asFileAttribute(perms));
            } catch (final IOException e) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, this.output.toString());
            }
        }
    }

    protected final void writeOutputFile(final List<Descriptor> descriptors) {
        Objects.requireNonNull(descriptors, "Descriptors cannot be null.");

        // create and load template
        final MustacheFactory mf = new DefaultMustacheFactory();
        final Mustache mustache = mf.compile(OUTPUT_POM_TEMPLATE);

        // create model
        final Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("descriptors", descriptors);

        // create output file path
        final String fileName = String.format(OUTPUT_POM_PATTERN, new Date());
        final Path outputPom = this.output.resolve(fileName);

        // write file
        try (Writer writer = new FileWriter(outputPom.toFile())) {
            mustache.execute(writer, scopes);
        } catch (final IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
