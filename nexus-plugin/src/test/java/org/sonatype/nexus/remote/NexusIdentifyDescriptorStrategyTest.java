package org.sonatype.nexus.remote;

import com.ludo.jar2pom.core.model.Dependency;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonatype.nexus.json.Answer;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NexusIdentifyDescriptorStrategyTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final NexusIdentifyDescriptorStrategy descriptorStrategy = new NexusIdentifyDescriptorStrategy();

    @Mock
    Response response;

    @Test
    public void createUriSuccess() throws Exception {
        final URI uri = this.descriptorStrategy.createUri("host", "parameter");

        final URI uriExpected = new URI("https://host/service/local/identify/sha1/parameter");
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
        final Answer answer = new Answer();
        answer.setArtifactId("artifactId");

        when(this.response.readEntity(eq(Answer.class))).thenReturn(answer);

        final Dependency dependency = this.descriptorStrategy.extractDependency(this.response);

        assertEquals("artifactId", dependency.getArtifactId());
    }

}
