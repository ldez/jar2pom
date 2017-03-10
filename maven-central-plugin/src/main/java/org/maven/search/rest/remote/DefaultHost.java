package org.maven.search.rest.remote;

import java.util.Collections;
import java.util.List;

/**
 * The Class DefaultHost.
 */
public final class DefaultHost {

    /**
     * The Constant HOSTS.
     */
    public static final List<String> HOSTS = Collections.singletonList("search.maven.org");

    /**
     * Instantiates a new default host.
     *
     * @throws InstantiationException the instantiation exception
     */
    private DefaultHost() throws InstantiationException {
        throw new InstantiationException();
    }

}
