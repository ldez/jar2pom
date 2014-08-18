package com.ludo.jar2pom.core.remote;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.model.Descriptor;

public abstract class AbstractDescriptorStrategy implements DescriptorStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDescriptorStrategy.class);

    private final List<String> predefinedHosts = new ArrayList<>();

    private final ClientConfig clientConfig = new ClientConfig();

    private MediaType mediaType;

    public AbstractDescriptorStrategy(final MediaType mediaType, final Collection<String> predefinedHosts) {
        super();
        Objects.requireNonNull(predefinedHosts, "Predefined hosts cannot be null.");
        this.predefinedHosts.addAll(predefinedHosts);

        this.setMediaType(mediaType);
        this.clientConfig.register(JacksonFeature.class);
    }

    protected final void setMediaType(final MediaType mediaType) {
        Objects.requireNonNull(mediaType, "MediaType cannot be null.");
        this.mediaType = mediaType;
    }

    @Override
    public final Descriptor search(final Path file) throws IOException {
        return this.search(file, null);
    }

    @Override
    public final Descriptor search(final Path file, final String customHost) throws IOException {
        Objects.requireNonNull(file, "File cannot be null.");

        final List<String> hosts = new ArrayList<>();
        // TODO : ldez - 12 août 2014 : additivity option management
        if (StringUtils.isBlank(customHost)) {
            hosts.addAll(this.predefinedHosts);
        } else {
            hosts.add(customHost);
        }

        // create Hash
        final String parameter = this.extractParameter(file);
        Validate.notBlank(parameter, "Parameter cannot be blank.");

        // Client
        final Client client = ClientBuilder.newClient(this.clientConfig);

        Descriptor descriptor = null;
        for (int i = 0; i < hosts.size() && descriptor == null; i++) {
            final String host = hosts.get(i);

            // Create URI
            Validate.notBlank(host, "Host cannot be blank.");
            final URI uri = this.createUri(host, parameter);
            LOG.debug("Host: {}", uri);

            // Request
            final Response response = this.get(client, uri);
            Objects.requireNonNull(response, "Response cannot be null.");

            // Response
            if (response.getStatus() == 200) {
                final Dependency dependency = this.extractDependency(response);
                if (dependency != null) {
                    descriptor = new Descriptor(host, file, dependency);
                }
            }
            response.close();
        }

        client.close();

        if (descriptor == null) {
            final Dependency dependency = new Dependency(file.getFileName().toString());
            descriptor = new Descriptor(null, file, dependency, false);
        }

        return descriptor;
    }

    private Response get(final Client client, final URI uri) {
        Objects.requireNonNull(client, "Client cannot be null.");
        Objects.requireNonNull(uri, "URI cannot be null.");

        // create request
        final WebTarget target = client.target(uri);
        final Builder request = target.request(this.mediaType);
        request.accept(this.mediaType);

        // TODO : ldez - 10 août 2014 : catch ProcessingException
        return request.get();
    }

    protected abstract String extractParameter(Path file) throws IOException;

    protected abstract URI createUri(String host, String parameter);

    protected abstract Dependency extractDependency(Response response);
}
