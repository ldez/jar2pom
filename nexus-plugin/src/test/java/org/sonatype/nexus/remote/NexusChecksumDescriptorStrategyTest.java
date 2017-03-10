package org.sonatype.nexus.remote;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.SearchNGResponse;
import org.sonatype.nexus.support.NexusNGArtifactTransformer;
import org.sonatype.nexus.support.SearchNGResponseTransformer;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NexusChecksumDescriptorStrategyTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private Response response;

    private final Function<NexusNGArtifact, Dependency> nexusNGArtifactTransformer = new NexusNGArtifactTransformer();
    private final SearchNGResponseTransformer searchNGResponseTransformer = new SearchNGResponseTransformer(this.nexusNGArtifactTransformer);
    private final NexusChecksumDescriptorStrategy descriptorStrategy = new NexusChecksumDescriptorStrategy(this.searchNGResponseTransformer);

    @Test
    public void createUriSuccess() throws Exception {
        final URI uri = this.descriptorStrategy.createUri("host", "parameter");

        final URI uriExpected = new URI("https://host/service/local/lucene/search?sha1=parameter");
        assertEquals(uriExpected, uri);
    }

    @Test
    public void extractParameter() throws Exception {
        final Path file = this.temporaryFolder.newFile("foobar.jar").toPath();

        final String parameter = this.descriptorStrategy.extractParameter(file);

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", parameter);
    }

    @Test
    public void extractDependency() throws Exception {
        final SearchNGResponse searchNGResponse = SearchNGResponseFixture.standardSearchNGResponse();

        when(this.response.readEntity(eq(SearchNGResponse.class))).thenReturn(searchNGResponse);

        final Dependency dependency = this.descriptorStrategy.extractDependency(this.response);

        assertEquals("artifactId", dependency.getArtifactId());
    }

    @Test
    public void search() throws Exception {
        final Path file = this.temporaryFolder.newFile("foobar.jar").toPath();

        final Descriptor descriptor = this.descriptorStrategy.search(file);

        assertTrue("descriptor", descriptor.isFound());
    }

}
