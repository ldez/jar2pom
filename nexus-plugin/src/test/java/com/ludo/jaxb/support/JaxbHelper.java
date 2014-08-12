package com.ludo.jaxb.support;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang3.Validate;
import org.xml.sax.SAXException;

public final class JaxbHelper {

    private JaxbHelper() throws InstantiationException {
        throw new InstantiationException();
    }

    public static final <T> Marshaller createMarshaller(final Class<T> clazz, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(clazz, "Class cannot be null.");

        // Create JaxB context
        final JAXBContext context = JAXBContext.newInstance(clazz);

        return createMarshaller(context, clazz.getClassLoader(), xmlSchema, validate);
    }

    public static final <T> Marshaller createMarshaller(final Package packag, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(packag, "Package cannot be null.");

        // Create JaxB context
        final JAXBContext context = JAXBContext.newInstance(packag.getName());

        return createMarshaller(context, JaxbHelper.class.getClassLoader(), xmlSchema, validate);
    }

    public static final <T> Marshaller createMarshaller(final JAXBContext context, final ClassLoader classLoader, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(classLoader, "ClassLoader cannot be null.");
        Objects.requireNonNull(context, "JAXBContext cannot be null.");
        Validate.notBlank(xmlSchema, "Xml schema path cannot be blank.");

        // Create Marshaller
        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        if (validate) {
            // add schema validation
            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final URL url = classLoader.getResource(xmlSchema);
            final Schema schema = factory.newSchema(url);
            marshaller.setSchema(schema);
            marshaller.setEventHandler(new CustomValidationEventHandler());
        }
        return marshaller;
    }

    public static final <T> String mashallToString(final Object jaxbElement, final Class<T> clazz, final String xmlSchema, final boolean validate) throws JAXBException, IOException, SAXException {
        Objects.requireNonNull(clazz, "Class cannot be null.");
        Validate.notBlank(xmlSchema, "Xml schema path cannot be blank.");

        final Marshaller marshaller = createMarshaller(clazz, xmlSchema, validate);
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(jaxbElement, stringWriter);
        stringWriter.close();

        return stringWriter.getBuffer().toString();
    }

    public static final <T> Unmarshaller createUnmarshaller(final Class<T> clazz, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(clazz, "Class cannot be null.");

        final JAXBContext context = JAXBContext.newInstance(clazz);
        return createUnmarshaller(context, clazz.getClassLoader(), xmlSchema, validate);
    }

    public static final <T> Unmarshaller createUnmarshaller(final Package packag, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(packag, "Package cannot be null.");

        final JAXBContext context = JAXBContext.newInstance(packag.getName());
        return createUnmarshaller(context, JaxbHelper.class.getClassLoader(), xmlSchema, validate);
    }

    public static final <T> Unmarshaller createUnmarshaller(final JAXBContext context, final ClassLoader classLoader, final String xmlSchema, final boolean validate) throws JAXBException, SAXException {
        Objects.requireNonNull(classLoader, "ClassLoader cannot be null.");
        Objects.requireNonNull(context, "JAXBContext cannot be null.");
        Validate.notBlank(xmlSchema, "Xml schema path cannot be blank.");

        final Unmarshaller unmarshaller = context.createUnmarshaller();

        if (validate) {
            // add schema validation
            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final URL url = classLoader.getResource(xmlSchema);
            final Schema schema = factory.newSchema(url);
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(new CustomValidationEventHandler());
        }
        return unmarshaller;
    }
}
