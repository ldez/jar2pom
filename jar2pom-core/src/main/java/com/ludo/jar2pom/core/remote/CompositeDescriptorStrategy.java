package com.ludo.jar2pom.core.remote;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.ludo.jar2pom.core.model.Descriptor;

/**
 * The Class CompositeDescriptorStrategy.
 */
public class CompositeDescriptorStrategy implements DescriptorStrategy {

    /** The descriptor strategies. */
    private final Set<DescriptorStrategy> descriptorStrategies = new HashSet<>();

    /**
     * Instantiates a new composite descriptor strategy.
     *
     * @param descriptorStrategy the descriptor strategy
     */
    public CompositeDescriptorStrategy(final DescriptorStrategy descriptorStrategy) {
        super();
        Objects.requireNonNull(descriptorStrategy, "DescriptorStrategy cannot be null.");
        this.descriptorStrategies.add(descriptorStrategy);
    }

    /**
     * Adds the DescriptorStrategy.
     *
     * @param descriptorStrategy the descriptor strategy
     * @return true, if successful
     */
    public final boolean add(final DescriptorStrategy descriptorStrategy) {
        return CollectionUtils.addIgnoreNull(this.descriptorStrategies, descriptorStrategy);
    }

    /*
     * (non-Javadoc)
     * @see com.ludo.jar2pom.core.remote.DescriptorStrategy#search(java.nio.file.Path, java.lang.String)
     */
    @Override
    public final Descriptor search(final Path file, final String customHost) throws IOException {

        Descriptor descriptor = null;
        for (final Iterator<DescriptorStrategy> iterator = this.descriptorStrategies.iterator(); iterator.hasNext() && (descriptor == null || !descriptor.isFound());) {
            final DescriptorStrategy descriptorStrategy = iterator.next();
            descriptor = descriptorStrategy.search(file, customHost);
        }

        return descriptor;
    }

    /*
     * (non-Javadoc)
     * @see com.ludo.jar2pom.core.remote.DescriptorStrategy#search(java.nio.file.Path)
     */
    @Override
    public final Descriptor search(final Path file) throws IOException {
        return this.search(file, null);
    }

}
