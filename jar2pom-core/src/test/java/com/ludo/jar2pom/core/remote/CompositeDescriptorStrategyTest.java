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
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
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
    public void should_return_null_when_all_strategies_fail() throws Exception {
        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertThat(descriptor).isNull();
    }

    @Test
    public void should_use_the_second_strategy_when_the_first_strategy_fail() throws Exception {
        when(this.descriptorStrategy01.search(this.pathCaptor.capture(), nullable(String.class))).then(invocation -> {
            final String sourceName = "descriptorStrategy01";
            final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency, false);
        });
        when(this.descriptorStrategy02.search(this.pathCaptor.capture(), nullable(String.class))).then(invocation -> {
            final String sourceName = "descriptorStrategy02";
            final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency, true);
        });

        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertThat(descriptor.getSourceName())
                .isEqualTo("descriptorStrategy02");
    }

    @Test
    public void should_use_the_first_strategy_when_the_first_strategy_find_a_result() throws Exception {
        when(this.descriptorStrategy01.search(this.pathCaptor.capture(), nullable(String.class))).then(invocation -> {
            final String sourceName = "descriptorStrategy01";
            final Path file = CompositeDescriptorStrategyTest.this.pathCaptor.getValue();
            final Dependency dependency = new Dependency("artifactId");
            return new Descriptor(sourceName, file, dependency, true);
        });

        final CompositeDescriptorStrategy compositeDescriptorStrategy = new CompositeDescriptorStrategy(this.descriptorStrategy01);
        compositeDescriptorStrategy.add(this.descriptorStrategy02);

        final Path file = this.temporaryFolder.getRoot().toPath();

        final Descriptor descriptor = compositeDescriptorStrategy.search(file);

        assertThat(descriptor.getSourceName())
                .isEqualTo("descriptorStrategy01");
    }

}
