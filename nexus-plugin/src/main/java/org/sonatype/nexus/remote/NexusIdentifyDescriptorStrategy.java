package org.sonatype.nexus.remote;

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
import org.sonatype.nexus.json.Answer;
import org.sonatype.nexus.support.AnswerTransformer;

import com.google.common.base.Function;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.ludo.jar2pom.core.model.Dependency;
import com.ludo.jar2pom.core.remote.AbstractDescriptorStrategy;

public class NexusIdentifyDescriptorStrategy extends AbstractDescriptorStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(NexusIdentifyDescriptorStrategy.class);

    public static final String URI_PATTERN = "https://{host}/service/local/identify/{algorithm}/{hash}";

    public static final String ALGORITHM = "sha1";

    private final Function<Answer, Dependency> answerTransformer = new AnswerTransformer();

    public NexusIdentifyDescriptorStrategy() {
        super(MediaType.APPLICATION_JSON_TYPE);
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
        uriVariables.put("algorithm", ALGORITHM);
        uriVariables.put("hash", parameter);

        // Build URI
        return UriBuilder.fromUri(URI_PATTERN).resolveTemplates(uriVariables).build();
    }

    @Override
    protected final Dependency extractDependency(final Response response) {

        // get entity from response
        final Answer entity = response.readEntity(Answer.class);

        // transform to Dependency object
        return this.answerTransformer.apply(entity);
    }

}
