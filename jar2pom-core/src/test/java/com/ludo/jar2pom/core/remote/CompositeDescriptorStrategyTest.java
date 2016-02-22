package com.ludo.jar2pom.core.remote;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
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

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositeDescriptorStrategyTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private DescriptorStrategy descriptorStrategy01;

    @Mock
    private DescriptorStrategy descriptorStrategy02;

    @Captor
    private ArgumentCaptor<Path> pathCaptor;

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
