package org.maven.search.rest.remote;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.remote.AbstractDescriptorStrategy;

public class CentralChecksumDescriptorStrategy extends AbstractDescriptorStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(CentralChecksumDescriptorStrategy.class);

    public static final String URI_PATTERN = "http://{host}/solrsearch/select?q=1:{hash}&rows=20&wt=json";

    public CentralChecksumDescriptorStrategy() {
        super(MediaType.APPLICATION_XML_TYPE, DefaultHost.HOSTS);
    }

    @Override
    protected String extractParameter(final Path file) throws IOException {
        // create Hash
        final HashCode hash = Files.asByteSource(file.toFile()).hash(Hashing.sha1());

        LOG.debug("Hash: {}", hash);
        return hash.toString();
    }

    @Override
    protected URI createUri(final String host, final String parameter) {

        // URI variables
        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("host", host);
        uriVariables.put("hash", parameter);

        // Build URI
        return UriBuilder.fromUri(URI_PATTERN).resolveTemplates(uriVariables).build();
    }

    @Override
    protected Dependency extractDependency(final Response response) {

        // TODO : ldez - 18 août 2014 : refactor Dependency class before implement

        return null;
    }

}
