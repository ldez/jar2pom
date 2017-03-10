package org.sonatype.nexus.remote;

import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.SearchNGResponse;
import org.sonatype.nexus.rest.custom.SearchNGResponse.Data;

import java.util.Collections;
import java.util.List;

public class SearchNGResponseFixture {

    public static SearchNGResponse standardSearchNGResponse() {
        final SearchNGResponse searchNGResponse = new SearchNGResponse();
        searchNGResponse.setData(standardData());
        return searchNGResponse;
    }

    public static Data standardData() {
        final List<NexusNGArtifact> artifacts = Collections.singletonList(standardNexusNGArtifact());
        return new Data(artifacts);
    }

    public static NexusNGArtifact standardNexusNGArtifact() {
        return new NexusNGArtifact("groupId", "artifactId", "version", null, null, null, null, null, null);
    }

}
