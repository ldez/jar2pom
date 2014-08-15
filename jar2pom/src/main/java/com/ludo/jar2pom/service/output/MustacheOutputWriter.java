package com.ludo.jar2pom.service.output;

import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.ludo.jar2pom.core.model.Descriptor;

public class MustacheOutputWriter implements OutputWriter {

    private static final Logger LOG = LoggerFactory.getLogger(MustacheOutputWriter.class);

    private static final Set<PosixFilePermission> PERMS = EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE);

    public static final String OUTPUT_POM_PATTERN = "pom_%1$tY-%<tm-%<td_%<tH-%<tM-%<tS.xml";

    public static final String OUTPUT_POM_TEMPLATE = "template.mustache";

    private final Mustache mustache;

    public MustacheOutputWriter() {
        super();
        // create and load template
        final MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        this.mustache = mustacheFactory.compile(OUTPUT_POM_TEMPLATE);
    }

    @Override
    public final void writeOutputFile(final List<Descriptor> descriptors, final Path file) throws IOException {
        Objects.requireNonNull(descriptors, "Descriptors cannot be null.");
        Objects.requireNonNull(file, "Output file cannot be null.");

        // check output Directory, create if not exists
        this.createDirectory(file);

        // create model
        final Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("descriptors", descriptors);

        // create output file path
        final Path outputPom = createOutputPath(file);

        // write file
        try (Writer writer = new FileWriter(outputPom.toFile())) {
            this.mustache.execute(writer, scopes);
        } catch (final IOException e) {
            LOG.debug("Writer error.", e);
            throw e;
        }
    }

    public static final Path createOutputPath(final Path file) {
        Objects.requireNonNull(file, "Output file cannot be null.");

        final String fileName = String.format(OUTPUT_POM_PATTERN, new Date());
        return file.resolve(fileName);
    }

    protected final void createDirectory(final Path file) throws IOException {
        Objects.requireNonNull(file, "Output path cannot be null.");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Output directory : {}", file.toAbsolutePath());
        }

        if (!Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
            // create directory
            final boolean posix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
            // check id system supporting POSIX
            if (posix) {
                Files.createDirectories(file, PosixFilePermissions.asFileAttribute(PERMS));
            } else {
                Files.createDirectories(file);
            }
        }
    }

}
