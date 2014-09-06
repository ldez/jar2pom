package org.maven.search.rest.remote;

import java.util.Arrays;
import java.util.List;

/**
 * The Class DefaultHost.
 */
public final class DefaultHost {

    /**
     * Instantiates a new default host.
     *
     * @throws InstantiationException the instantiation exception
     */
    private DefaultHost() throws InstantiationException {
        throw new InstantiationException();
    }

    /** The Constant HOSTS. */
    public static final List<String> HOSTS = Arrays.asList("search.maven.org");

}
