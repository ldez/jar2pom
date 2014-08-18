package org.sonatype.nexus.remote;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.SearchNGResponse;
import org.sonatype.nexus.rest.custom.SearchNGResponse.Data;

public class SearchNGResponseFixture {

    public static final SearchNGResponse standardSearchNGResponse() {
        final SearchNGResponse searchNGResponse = new SearchNGResponse();
        searchNGResponse.setData(standardData());
        return searchNGResponse;
    }

    public static final Data standardData() {
        final List<NexusNGArtifact> artifacts = new ArrayList<>();
        artifacts.add(standardNexusNGArtifact());
        return new Data(artifacts);
    }

    public static final NexusNGArtifact standardNexusNGArtifact() {
        return new NexusNGArtifact("groupId", "artifactId", "version", null, null, null, null, null, null);
    }

}
