package org.sonatype.nexus.remote;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.rest.custom.SearchNGResponse;

import com.google.common.base.Function;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.remote.AbstractDescriptorStrategy;

public class NexusChecksumDescriptorStrategy extends AbstractDescriptorStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(NexusChecksumDescriptorStrategy.class);

    public static final String URI_PATTERN = "https://{host}/service/local/lucene/search?sha1={hash}";

    private final Function<SearchNGResponse, List<Dependency>> searchNGResponseTransformer;

    public NexusChecksumDescriptorStrategy(final Function<SearchNGResponse, List<Dependency>> searchNGResponseTransformer) {
        super(MediaType.APPLICATION_XML_TYPE, DefaultHost.HOSTS);
        this.searchNGResponseTransformer = searchNGResponseTransformer;
    }

    @Override
    protected final String extractParameter(final Path file) throws IOException {

        // create Hash
        final HashCode hash = Files.asByteSource(file.toFile()).hash(Hashing.sha1());

        LOG.debug("Hash: {}", hash);
        return hash.toString();
    }

    @Override
    protected final URI createUri(final String host, final String parameter) {

        // URI variables
        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("host", host);
        uriVariables.put("hash", parameter);

        // Build URI
        return UriBuilder.fromUri(URI_PATTERN).resolveTemplates(uriVariables).build();
    }

    @Override
    protected final Dependency extractDependency(final Response response) {

        final SearchNGResponse entity = response.readEntity(SearchNGResponse.class);
        final List<Dependency> dependencies = this.searchNGResponseTransformer.apply(entity);

        // FIXME : ldez - 18 août 2014 : if several artifacts ?
        Dependency dependency = null;
        if (CollectionUtils.isNotEmpty(dependencies)) {
            dependency = dependencies.get(0);
        }

        return dependency;
    }

}
