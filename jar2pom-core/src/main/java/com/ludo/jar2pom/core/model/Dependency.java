package com.ludo.jar2pom.core.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class Dependency.
 */
public class Dependency {

    /** The Constant DEFAULT_GROUP_ID. */
    public static final String DEFAULT_GROUP_ID = "unknown.group";

    /** The Constant DEFAULT_VERSION_ID. */
    public static final String DEFAULT_VERSION_ID = "0.0.0";

    /** The group id. */
    private String groupId = DEFAULT_GROUP_ID;

    /** The artifact id. */
    private final String artifactId;

    /** The version. */
    private String version = DEFAULT_VERSION_ID;

    /** The type. */
    private String type;

    /** The classifier. */
    private String classifier;

    /**
     * Instantiates a new dependency.
     *
     * @param groupId the group id
     * @param artifactId the artifact id
     * @param version the version
     * @param type the type
     * @param classifier the classifier
     */
    public Dependency(final String groupId, final String artifactId, final String version, final String type, final String classifier) {
        this(groupId, artifactId, version);
        this.type = type;
        this.classifier = classifier;
    }

    /**
     * Instantiates a new dependency.
     *
     * @param groupId the group id
     * @param artifactId the artifact id
     * @param version the version
     */
    public Dependency(final String groupId, final String artifactId, final String version) {
        this(artifactId);
        this.groupId = groupId;
        this.version = version;
    }

    /**
     * Instantiates a new dependency.
     *
     * @param artifactId the artifact id
     */
    public Dependency(final String artifactId) {
        super();
        Validate.notBlank(artifactId, "Artifact id cannot be blank.");
        this.artifactId = artifactId;
    }

    /**
     * Gets the group id.
     *
     * @return the group id
     */
    public final String getGroupId() {
        return this.groupId;
    }

    /**
     * Gets the artifact id.
     *
     * @return the artifact id
     */
    public final String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public final String getVersion() {
        return this.version;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public final String getType() {
        return this.type;
    }

    /**
     * Gets the classifier.
     *
     * @return the classifier
     */
    public final String getClassifier() {
        return this.classifier;
    }

    /**
     * Sets the classifier.
     *
     * @param classifier the new classifier
     */
    public final void setClassifier(final String classifier) {
        this.classifier = classifier;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.groupId);
        builder.append(this.artifactId);
        builder.append(this.version);
        return builder.build();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
