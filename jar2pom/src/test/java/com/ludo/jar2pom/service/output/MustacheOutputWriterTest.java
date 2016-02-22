package com.ludo.jar2pom.service.output;

import com.ludo.jar2pom.core.model.Descriptor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MustacheOutputWriterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final MustacheOutputWriter outputWriter = new MustacheOutputWriter();

    @Test(expected = IOException.class)
    public void createDirectoryFailExistFile() throws Exception {

        final Path file = this.temporaryFolder.newFile("foobar.xml").toPath();

        this.outputWriter.createDirectory(file);
    }

    @Test
    public void createDirectoryCreateDirectory() throws Exception {

        final Path file = this.temporaryFolder.getRoot().toPath().resolve("foobar");

        this.outputWriter.createDirectory(file);

        assertTrue(Files.exists(file, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    public void createDirectoryDirectoryExist() throws Exception {

        final Path file = this.temporaryFolder.getRoot().toPath();

        this.outputWriter.createDirectory(file);

        assertTrue(Files.exists(file, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    public void writeOutputFile() throws Exception {
        final List<Descriptor> descriptors = new ArrayList<>();

        final Path file = this.temporaryFolder.getRoot().toPath();

        this.outputWriter.writeOutputFile(descriptors, file);

        final Path outputPom = MustacheOutputWriter.createOutputPath(file);

        assertTrue(Files.exists(outputPom, LinkOption.NOFOLLOW_LINKS));
    }

}
