package org.sonatype.nexus.support;

import com.google.common.base.Function;
import com.ludo.jar2pom.core.model.Dependency;
import org.sonatype.nexus.json.Answer;

import java.util.Optional;

/**
 * The Class AnswerTransformer.
 */
public class AnswerTransformer implements Function<Answer, Dependency> {

    /*
     * (non-Javadoc)
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */
    @Override
    public final Dependency apply(final Answer answer) {
        return Optional.ofNullable(answer)
                .map(input -> new Dependency(
                                input.getGroupId(),
                                input.getArtifactId(),
                                input.getVersion(),
                                input.getExtension(),
                                input.getClassifier()
                        )
                )
                .orElse(null);
    }

}
