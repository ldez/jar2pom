package com.ludo.jar2pom.core.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.nio.file.Path;
import java.util.Objects;

/**
 * The Class Descriptor.
 */
public class Descriptor {

    /**
     * The source name.
     */
    private final String sourceName;

    /**
     * The file.
     */
    private final Path file;

    /**
     * The dependency.
     */
    private final Dependency dependency;

    /**
     * The found.
     */
    private boolean found = true;

    /**
     * Instantiates a new descriptor.
     *
     * @param sourceName the source name
     * @param file       the file
     * @param dependency the dependency
     */
    public Descriptor(final String sourceName, final Path file, final Dependency dependency) {
        this.sourceName = sourceName;

        Objects.requireNonNull(file, "File cannot be null.");
        this.file = file;

        Objects.requireNonNull(dependency, "Dependency cannot be null.");
        this.dependency = dependency;
    }

    /**
     * Instantiates a new descriptor.
     *
     * @param sourceName the source name
     * @param file       the file
     * @param dependency the dependency
     * @param found      the found
     */
    public Descriptor(final String sourceName, final Path file, final Dependency dependency, final boolean found) {
        this(sourceName, file, dependency);
        this.found = found;
    }

    /**
     * Gets the source name.
     *
     * @return the source name
     */
    public final String getSourceName() {
        return this.sourceName;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public final Path getFile() {
        return this.file;
    }

    /**
     * Gets the dependency.
     *
     * @return the dependency
     */
    public final Dependency getDependency() {
        return this.dependency;
    }

    /**
     * Checks if is found.
     *
     * @return true, if is found
     */
    public final boolean isFound() {
        return this.found;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
