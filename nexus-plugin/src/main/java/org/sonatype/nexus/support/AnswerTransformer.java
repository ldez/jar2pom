package org.sonatype.nexus.support;

import org.sonatype.nexus.json.Answer;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;

public class AnswerTransformer implements Function<Answer, Dependency> {

    @Override
    public final Dependency apply(final Answer input) {
        Dependency dependency = null;
        if (input != null) {
            final String groupId = input.getGroupId();
            final String artifactId = input.getArtifactId();
            final String version = input.getVersion();
            final String type = input.getExtension();
            final String classifier = input.getClassifier();
            dependency = new Dependency(groupId, artifactId, version, type, classifier);
        }
        return dependency;
    }

}
