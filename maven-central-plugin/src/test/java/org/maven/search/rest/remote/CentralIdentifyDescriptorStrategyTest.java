package org.maven.search.rest.remote;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CentralIdentifyDescriptorStrategyTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    CentralIdentifyDescriptorStrategy descriptorStrategy = new CentralIdentifyDescriptorStrategy();

    @Test
    public void createUriSuccess() throws Exception {
        final URI uri = this.descriptorStrategy.createUri("host", "parameter");

        final URI uriExpected = new URI("http://host/solrsearch/select?q=1:parameter&rows=20&wt=json");
        assertEquals(uriExpected, uri);
    }

    @Test
    public void extractParameter() throws Exception {
        final Path file = this.temporaryFolder.newFile("foobar.jar").toPath();

        final String parameter = this.descriptorStrategy.extractParameter(file);

        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", parameter);
    }

}
