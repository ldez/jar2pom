package org.maven.search.rest.support;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ludo.jar2pom.core.model.Dependency;
import org.apache.commons.collections4.CollectionUtils;
import org.maven.search.rest.model.Answer;
import org.maven.search.rest.model.Docs;

import java.util.List;

public class CentralAnswerTransformer implements Function<Answer, List<Dependency>> {

    private final Function<Docs, Dependency> docTransformer;

    public CentralAnswerTransformer(final Function<Docs, Dependency> docTransformer) {
        super();
        this.docTransformer = docTransformer;
    }

    @Override
    public final List<Dependency> apply(final Answer input) {
        List<Dependency> dependencies = null;
        if (input != null) {
            final List<Docs> docs = input.getResponse().getDocs();
            if (CollectionUtils.isNotEmpty(docs)) {
                dependencies = Lists.transform(docs, this.docTransformer);
            }
        }
        return dependencies;
    }

}
