package com.ludo.jar2pom.core.file;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileJarVisitorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private DescriptorStrategy descriptorStrategy;

    @Captor
    private ArgumentCaptor<Path> pathCaptor;

    @Test
    public void should_exclude_non_jar_file_when_directory_contains_several_extensions() throws Exception {

        when(this.descriptorStrategy.search(this.pathCaptor.capture(), nullable(String.class))).then(invocation -> {
            final String sourceName = "foobar";
            final Path file = FileJarVisitorTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency);
        });

        final Path start = this.temporaryFolder.getRoot().toPath();
        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final String customHost = null;
        final FileJarVisitor fileJarVisitor = new FileJarVisitor(this.descriptorStrategy, customHost);

        Files.walkFileTree(start, fileJarVisitor);

        assertThat(fileJarVisitor.getDescriptors()).hasSize(1);
    }

}
