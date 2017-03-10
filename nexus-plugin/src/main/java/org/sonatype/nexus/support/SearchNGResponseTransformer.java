package org.sonatype.nexus.support;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ludo.jar2pom.core.model.Dependency;
import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.SearchNGResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SearchNGResponseTransformer implements Function<SearchNGResponse, List<Dependency>> {

    private final Function<NexusNGArtifact, Dependency> nexusNGArtifactTransformer;

    public SearchNGResponseTransformer(final Function<NexusNGArtifact, Dependency> nexusNGArtifactTransformer) {
        this.nexusNGArtifactTransformer = nexusNGArtifactTransformer;
    }

    @Override
    public final List<Dependency> apply(final SearchNGResponse searchResponse) {
        return Optional.ofNullable(searchResponse)
                .map(SearchNGResponse::getData)
                .map(SearchNGResponse.Data::getArtifact)
                .map(artifacts -> Lists.transform(artifacts, this.nexusNGArtifactTransformer))
                .orElseGet(Collections::emptyList);
    }

}
