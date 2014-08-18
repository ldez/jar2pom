package org.sonatype.nexus.support;

import org.sonatype.nexus.rest.custom.NexusNGArtifact;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;

public class NexusNGArtifactTransformer implements Function<NexusNGArtifact, Dependency> {

    @Override
    public final Dependency apply(final NexusNGArtifact input) {

        Dependency dependency = null;
        if (input != null) {
            final String groupId = input.getGroupId();
            final String artifactId = input.getArtifactId();
            final String version = input.getVersion();
            dependency = new Dependency(groupId, artifactId, version);
        }

        return dependency;
    }

}
