package org.sonatype.nexus.support;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;
import org.sonatype.nexus.rest.custom.NexusNGArtifact;

import java.util.Optional;

public class NexusNGArtifactTransformer implements Function<NexusNGArtifact, Dependency> {

    @Override
    public final Dependency apply(final NexusNGArtifact artifact) {
        return Optional.ofNullable(artifact)
                .map(input -> new Dependency(
                                input.getGroupId(),
                                input.getArtifactId(),
                                input.getVersion()
                        )
                )
                .orElse(null);
    }

}
