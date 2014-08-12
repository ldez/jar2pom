package com.ludo.jar2pom.core.remote;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.ludo.jar2pom.core.model.Descriptor;

public class CompositeDescriptorStrategy implements DescriptorStrategy {

    private final Set<DescriptorStrategy> descriptorStrategies = new HashSet<>();

    public CompositeDescriptorStrategy(final DescriptorStrategy descriptorStrategy) {
        super();
        Objects.requireNonNull(descriptorStrategy, "DescriptorStrategy cannot be null.");
        this.descriptorStrategies.add(descriptorStrategy);
    }

    public final boolean add(final DescriptorStrategy descriptorStrategy) {
        return CollectionUtils.addIgnoreNull(this.descriptorStrategies, descriptorStrategy);
    }

    @Override
    public final Descriptor search(final Path file, final String customHost) throws IOException {

        Descriptor descriptor = null;
        for (final Iterator<DescriptorStrategy> iterator = this.descriptorStrategies.iterator(); iterator.hasNext() && (descriptor == null || !descriptor.isFound());) {
            final DescriptorStrategy descriptorStrategy = iterator.next();
            descriptor = descriptorStrategy.search(file, customHost);
        }

        return descriptor;
    }

    @Override
    public final Descriptor search(final Path file) throws IOException {
        return this.search(file, null);
    }

}
