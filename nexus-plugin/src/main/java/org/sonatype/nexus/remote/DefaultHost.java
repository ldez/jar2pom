package org.sonatype.nexus.remote;

import java.util.Arrays;
import java.util.List;

/**
 * The Class DefaultHost.
 */
public final class DefaultHost {

    /**
     * The Constant HOSTS.
     */
    public static final List<String> HOSTS = Arrays.asList("oss.sonatype.org", "repository.sonatype.org", "maven.java.net", "maven.atlassian.com", "nexus.codehaus.org", "repository.apache.org");

    /**
     * Instantiates a new default host.
     *
     * @throws InstantiationException the instantiation exception
     */
    private DefaultHost() throws InstantiationException {
        throw new InstantiationException();
    }

}
