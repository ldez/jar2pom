package com.ludo.jar2pom.service.converter;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import com.ludo.jar2pom.service.output.OutputWriter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JarConverterDescriptorsTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private DescriptorStrategy descriptorStrategy;

    @Mock
    private OutputWriter outputWriter;

    @InjectMocks
    private JarConverter converter;

    @Captor
    private ArgumentCaptor<Path> pathCaptor;

    @Before
    public void setup() throws IOException {
        when(this.descriptorStrategy.search(this.pathCaptor.capture(), nullable(String.class))).then(invocation -> {
            final String sourceName = "foobar";
            final Path file = JarConverterDescriptorsTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency);
        });
        when(this.descriptorStrategy.search(this.pathCaptor.capture())).then(invocation -> {
            final String sourceName = "foobar";
            final Path file = JarConverterDescriptorsTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency);
        });
    }

    @Test
    public void should_return_empty_list_of_descriptors_when_no_recursive_mode_and_no_jar_exists() throws Exception {

        final Path input = this.temporaryFolder.getRoot().toPath();
        final String customHost = null;
        final boolean recursive = false;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors).isEmpty();
    }

    @Test
    public void should_return_one_descriptor_when_no_recursive_mode_and_jar_exists() throws Exception {

        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final Path input = this.temporaryFolder.getRoot().toPath();

        final String customHost = null;
        final boolean recursive = false;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors).hasSize(1);
    }

    @Test
    public void should_return_one_descriptor_when_recursive_mode_and_jar_exists() throws Exception {

        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final Path input = this.temporaryFolder.getRoot().toPath();

        final String customHost = null;
        final boolean recursive = true;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors).hasSize(1);
    }

}
