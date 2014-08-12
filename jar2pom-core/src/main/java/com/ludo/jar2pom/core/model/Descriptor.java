package com.ludo.jar2pom.core.model;

import java.nio.file.Path;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Descriptor {

    private final String sourceName;

    private final Path file;

    private final Dependency dependency;

    private boolean found = true;

    public Descriptor(final String sourceName, final Path file, final Dependency dependency) {
        super();
        this.sourceName = sourceName;

        Objects.requireNonNull(file, "File cannot be null.");
        this.file = file;

        Objects.requireNonNull(dependency, "Dependency cannot be null.");
        this.dependency = dependency;
    }

    public Descriptor(final String sourceName, final Path file, final Dependency dependency, final boolean found) {
        this(sourceName, file, dependency);
        this.found = found;
    }

    public final String getSourceName() {
        return this.sourceName;
    }

    public final Path getFile() {
        return this.file;
    }

    public final Dependency getDependency() {
        return this.dependency;
    }

    public final boolean isFound() {
        return this.found;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
