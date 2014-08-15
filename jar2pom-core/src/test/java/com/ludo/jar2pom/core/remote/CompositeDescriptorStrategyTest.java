package com.ludo.jar2pom.core.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;

@RunWith(MockitoJUnitRunner.class)
public class CompositeDescriptorStrategyTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    DescriptorStrategy descriptorStrategy01;

    @Mock
    DescriptorStrategy descriptorStrategy02;

    @Captor
    ArgumentCaptor<Path> pathCaptor;

    @Test
    public void twoStrategiesFail() throws Exception {
        when(this.descriptorStrategy01.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy01";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, false);
            }
        });
        when(this.descriptorStrategy02.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy02";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, true);
            }
        });

        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertNotNull(descriptor);
    }

    @Test
    public void twoStrategiesSecondFound() throws Exception {
        when(this.descriptorStrategy01.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy01";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, false);
            }
        });
        when(this.descriptorStrategy02.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy02";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, true);
            }
        });

        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertEquals("descriptorStrategy02", descriptor.getSourceName());
    }

    @Test
    public void twoStrategiesFirstFound() throws Exception {
        when(this.descriptorStrategy01.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy01";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, true);
            }
        });
        when(this.descriptorStrategy02.search(this.pathCaptor.capture(), anyString())).then(new Answer<Descriptor>() {
            @Override
            public Descriptor answer(final InvocationOnMock invocation) throws Throwable {
                final String sourceName = "descriptorStrategy02";
                final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
                final Dependency dependency = new Dependency("artifactId");
                return new Descriptor(sourceName, file, dependency, false);
            }
        });

        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertEquals("descriptorStrategy01", descriptor.getSourceName());
    }

}
