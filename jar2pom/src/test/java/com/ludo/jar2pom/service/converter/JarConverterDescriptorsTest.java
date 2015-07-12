package com.ludo.jar2pom.service.converter;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import com.ludo.jar2pom.service.output.OutputWriter;

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
        when(this.descriptorStrategy.search(this.pathCaptor.capture(), anyString())).then(invocation -> {
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
    public void getDescriptorsEmptyNotRecursive() throws Exception {

        final Path input = this.temporaryFolder.getRoot().toPath();
        final String customHost = null;
        final boolean recursive = false;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors, hasSize(0));
    }

    @Test
    public void getDescriptorsNotRecursive() throws Exception {

        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final Path input = this.temporaryFolder.getRoot().toPath();

        final String customHost = null;
        final boolean recursive = false;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors, hasSize(1));
    }

    @Test
    public void getDescriptorsRecursive() throws Exception {

        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final Path input = this.temporaryFolder.getRoot().toPath();

        final String customHost = null;
        final boolean recursive = true;

        final List<Descriptor> descriptors = this.converter.getDescriptors(input, customHost, recursive);

        assertThat(descriptors, hasSize(1));
    }

}
