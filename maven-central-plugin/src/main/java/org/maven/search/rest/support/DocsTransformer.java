package org.maven.search.rest.support;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;
import org.maven.search.rest.model.Docs;

public class DocsTransformer implements Function<Docs, Dependency> {

    @Override
    public final Dependency apply(final Docs input) {
        Dependency dependency = null;

        if (input != null) {
            final String groupId = input.getG();
            final String artifactId = input.getA();
            final String version = input.getV();
            dependency = new Dependency(groupId, artifactId, version);
        }
        return dependency;
    }

}
