package com.ludo.jar2pom.core.file;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;

@RunWith(MockitoJUnitRunner.class)
public class FileJarVisitorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    DescriptorStrategy descriptorStrategy;

    @Captor
    ArgumentCaptor<Path> pathCaptor;

    @Test
    public void testName() throws Exception {

        Mockito.when(this.descriptorStrategy.search(this.pathCaptor.capture(), Matchers.anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "foobar";
                final Path file = FileJarVisitorTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency);
            }
        });

        final Path start = this.temporaryFolder.getRoot().toPath();
        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final String customHost = null;
        final FileJarVisitor fileJarVisitor = new FileJarVisitor(this.descriptorStrategy, customHost);

        Files.walkFileTree(start, fileJarVisitor);

        assertThat(fileJarVisitor.getDescriptors(), hasSize(1));
    }

}
