package org.sonatype.nexus.support;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ludo.jar2pom.core.model.Dependency;
import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.SearchNGResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchNGResponseTransformer implements Function<SearchNGResponse, List<Dependency>> {

    private final Function<NexusNGArtifact, Dependency> nexusNGArtifactTransformer;

    public SearchNGResponseTransformer(final Function<NexusNGArtifact, Dependency> nexusNGArtifactTransformer) {
        super();
        this.nexusNGArtifactTransformer = nexusNGArtifactTransformer;
    }

    @Override
    public final List<Dependency> apply(final SearchNGResponse entity) {

        final List<Dependency> dependencies = new ArrayList<>();
        if (entity != null) {
            final List<NexusNGArtifact> artifacts = entity.getData().getArtifact();
            final List<Dependency> depends = Lists.transform(artifacts, this.nexusNGArtifactTransformer);
            dependencies.addAll(depends);
        }
        return dependencies;
    }

}
