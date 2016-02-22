package org.sonatype.nexus.rest;

import com.ludo.jaxb.support.JaxbHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonatype.nexus.rest.custom.NexusNGArtifact;
import org.sonatype.nexus.rest.custom.ObjectFactory;
import org.sonatype.nexus.rest.custom.SearchNGResponse;
import org.sonatype.nexus.rest.custom.SearchNGResponse.Data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MarshallingCustomTest {

    public static final String XML_SCHEMA = "ns0-custom.xsd";

    public static final String XML_SOURCE = "sample.xml";
    public static final String XML_RESULT = "marshalresult-custom.xml";

    private final ObjectFactory jaxbObjectFactory = new ObjectFactory();

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void marshal() throws Exception {
        final File tempFolder = this.temporaryFolder.getRoot();
        final Path outputFile = tempFolder.toPath().resolve("marshalresult.xml");
        final OutputStream os = Files.newOutputStream(outputFile);
        // final OutputStream os = new FileOutputStream("c:/marshalresult01.xml");

        final SearchNGResponse response = new SearchNGResponse();

        final NexusNGArtifact nexusNGArtifact = new NexusNGArtifact();

        nexusNGArtifact.setArtifactId("jetty-webapp");
        nexusNGArtifact.setGroupId("org.eclipse.jetty");
        nexusNGArtifact.setVersion("7.3.0.v20110203");

        final Data data = new Data().withArtifact(nexusNGArtifact);
        response.setData(data);

        final JAXBElement<SearchNGResponse> root = this.jaxbObjectFactory.createSearchNGResponse(response);

        // Marshalling
        // shortcut : JAXB.marshal(project, os);
        final Marshaller marshaller = JaxbHelper.createMarshaller(SearchNGResponse.class, XML_SCHEMA, true);
        marshaller.marshal(root, os);

        final URL url = this.getClass().getClassLoader().getResource(XML_RESULT);

        final Reader expectedReader = Files.newBufferedReader(Paths.get(url.toURI()), StandardCharsets.UTF_8);
        final Reader actualReader = Files.newBufferedReader(outputFile, StandardCharsets.UTF_8);

        assertXMLEqual(expectedReader, actualReader);
    }

    @Test
    public void unmarshal() throws Exception {
        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(XML_SOURCE);
        final StreamSource xmlStream = new StreamSource(inputStream);

        // Unmarshalling
        final Package packag = SearchNGResponse.class.getPackage();
        final Unmarshaller unmarshaller = JaxbHelper.createUnmarshaller(packag, XML_SCHEMA, true);

        @SuppressWarnings("unchecked")
        final JAXBElement<SearchNGResponse> response = (JAXBElement<SearchNGResponse>) unmarshaller.unmarshal(xmlStream);

        assertNotNull("SearchNGResponse", response);

        final SearchNGResponse searchNGResponse = response.getValue();

        final List<NexusNGArtifact> artifacts = searchNGResponse.getData().getArtifact();
        assertThat(artifacts, hasSize(1));

        final NexusNGArtifact artifact = artifacts.get(0);
        assertNotNull(artifact);
    }
}
