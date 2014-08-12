package com.ludo.jar2pom.core.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Dependency {

    public static final String DEFAULT_GROUP_ID = "unknown.group";

    public static final String DEFAULT_VERSION_ID = "0.0.0";

    private String groupId = DEFAULT_GROUP_ID;

    private final String artifactId;

    private String version = DEFAULT_VERSION_ID;

    private String type;

    private String classifier;

    public Dependency(final String groupId, final String artifactId, final String version, final String type, final String classifier) {
        this(groupId, artifactId, version);
        this.type = type;
        this.classifier = classifier;
    }

    public Dependency(final String groupId, final String artifactId, final String version) {
        this(artifactId);
        this.groupId = groupId;
        this.version = version;
    }

    public Dependency(final String artifactId) {
        super();
        Validate.notBlank(artifactId, "Artifact id cannot be blank.");
        this.artifactId = artifactId;
    }

    public final String getGroupId() {
        return this.groupId;
    }

    public final String getArtifactId() {
        return this.artifactId;
    }

    public final String getVersion() {
        return this.version;
    }

    public final String getType() {
        return this.type;
    }

    public final String getClassifier() {
        return this.classifier;
    }

    public final void setClassifier(final String classifier) {
        this.classifier = classifier;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.groupId);
        builder.append(this.artifactId);
        builder.append(this.version);
        return builder.build();
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (obj instanceof Dependency) {
            final EqualsBuilder builder = new EqualsBuilder();
            final Dependency dependency = (Dependency) obj;
            builder.append(this.groupId, dependency.getGroupId());
            builder.append(this.artifactId, dependency.getArtifactId());
            builder.append(this.version, dependency.getVersion());
            result = builder.build();
        }
        return result;
    }
}
