package com.ludo.jar2pom.service.output;

import com.ludo.jar2pom.core.model.Descriptor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MustacheOutputWriterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final MustacheOutputWriter outputWriter = new MustacheOutputWriter();

    @Test(expected = IOException.class)
    public void should_fail_to_create_directory_when_file_exists() throws Exception {

        final Path file = this.temporaryFolder.newFile("foobar.xml").toPath();

        this.outputWriter.createDirectory(file);
    }

    @Test
    public void should_create_directory_when_file_is_a_directory_and_not_exists() throws Exception {

        final Path file = this.temporaryFolder.getRoot().toPath().resolve("foobar");

        this.outputWriter.createDirectory(file);

        assertThat(file).exists();
    }

    @Test
    public void should_create_directory_when_file_is_a_directory_and_exists() throws Exception {

        final Path file = this.temporaryFolder.getRoot().toPath();

        this.outputWriter.createDirectory(file);

        assertThat(file).exists();
    }

    @Test
    public void should_write_a_file_when_descriptors_list_is_empty() throws Exception {
        final List<Descriptor> descriptors = new ArrayList<>();

        final Path file = this.temporaryFolder.getRoot().toPath();

        this.outputWriter.writeOutputFile(descriptors, file);

        final Path outputPom = MustacheOutputWriter.createOutputPath(file);

        assertThat(outputPom).exists();
    }

}
