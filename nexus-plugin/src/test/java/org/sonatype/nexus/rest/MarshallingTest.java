package org.sonatype.nexus.rest;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonatype.nexus.rest.model.NexusNGArtifact;
import org.sonatype.nexus.rest.model.ObjectFactory;
import org.sonatype.nexus.rest.model.SearchNGResponse;
import org.sonatype.nexus.rest.model.SearchNGResponse.Data;
import org.xml.sax.SAXException;

import com.ludo.jaxb.support.CustomValidationEventHandler;

public class MarshallingTest {

    public static final String XML_SCHEMA = "ns0.xsd";

    public static final String XML_SOURCE = "sample.xml";
    public static final String XML_RESULT = "marshalresult.xml";

    private final ObjectFactory jaxbObjectFactory = new ObjectFactory();

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void marshal() throws Exception {
        final File tempFolder = this.temporaryFolder.getRoot();
        final File outputFile = new File(tempFolder, "marshalresult.xml");
        final FileOutputStream os = new FileOutputStream(outputFile);
        // final FileOutputStream os = new FileOutputStream("c:/marshalresult01.xml");

        final SearchNGResponse response = new SearchNGResponse();

        final NexusNGArtifact nexusNGArtifact = new NexusNGArtifact();

        nexusNGArtifact.setArtifactId("jetty-webapp");
        nexusNGArtifact.setGroupId("org.eclipse.jetty");
        nexusNGArtifact.setVersion("7.3.0.v20110203");

        final Data data = new Data().withNexusNGArtifact(nexusNGArtifact);
        response.setData(data);

        // System.out.println(response);

        final JAXBElement<SearchNGResponse> root = this.jaxbObjectFactory.createSearchNGResponse(response);

        // Marshalling
        final Marshaller marshaller = this.createMarshaller(SearchNGResponse.class);
        marshaller.marshal(root, os);

        // shortcut : JAXB.marshal(project, os);

        final URL url = this.getClass().getClassLoader().getResource(XML_RESULT);
        final FileReader expectedReader = new FileReader(new File(url.toURI()));
        final FileReader actualReader = new FileReader(outputFile);

        assertXMLEqual(expectedReader, actualReader);
    }

    @Ignore("https://issues.sonatype.org/browse/NEXUS-6755")
    @Test
    public void unmarshal() throws Exception {
        // FIXME : ldez - 8 août 2014 : ce test fail car le schema fournit est pourri : https://issues.sonatype.org/browse/NEXUS-6755

        final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(XML_SOURCE);
        final StreamSource xmlStream = new StreamSource(inputStream);

        // Unmarshalling
        final Package packageName = SearchNGResponse.class.getPackage();
        final Unmarshaller unmarshaller = this.createUnmarshaller(packageName);

        @SuppressWarnings("unchecked")
        final JAXBElement<SearchNGResponse> response = (JAXBElement<SearchNGResponse>) unmarshaller.unmarshal(xmlStream);

        assertNotNull("SearchNGResponse", response);

        final SearchNGResponse searchNGResponse = response.getValue();

        final List<NexusNGArtifact> artifacts = searchNGResponse.getData().getNexusNGArtifact();
        assertThat(artifacts, hasSize(1));

        final NexusNGArtifact artifact = artifacts.get(0);
        assertNotNull(artifact);
    }

    protected <T> Marshaller createMarshaller(final Class<T> clazz) throws JAXBException, SAXException {
        // Create Marsharller process
        final JAXBContext context = JAXBContext.newInstance(clazz);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // add schema validation
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final URL url = this.getClass().getClassLoader().getResource(XML_SCHEMA);
        final Schema schema = factory.newSchema(url);
        marshaller.setSchema(schema);
        marshaller.setEventHandler(new CustomValidationEventHandler());

        return marshaller;
    }

    protected <T> Unmarshaller createUnmarshaller(final Class<T> clazz) throws JAXBException, SAXException {
        final JAXBContext context = JAXBContext.newInstance(clazz);
        return this.createUnmarshaller(context);
    }

    protected <T> Unmarshaller createUnmarshaller(final Package packag) throws JAXBException, SAXException {
        final JAXBContext context = JAXBContext.newInstance(packag.getName());
        return this.createUnmarshaller(context);
    }

    private <T> Unmarshaller createUnmarshaller(final JAXBContext context) throws JAXBException, SAXException {

        final Unmarshaller unmarshaller = context.createUnmarshaller();

        // add schema validation
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final URL url = this.getClass().getClassLoader().getResource(XML_SCHEMA);
        final Schema schema = factory.newSchema(url);
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(new CustomValidationEventHandler());

        return unmarshaller;
    }
}
