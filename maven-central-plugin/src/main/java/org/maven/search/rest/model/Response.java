package org.maven.search.rest.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Response {

    private Long numFound;

    private Long start;

    private List<Docs> docs;

    public final Long getNumFound() {
        return this.numFound;
    }

    public final void setNumFound(final Long numFound) {
        this.numFound = numFound;
    }

    public final Long getStart() {
        return this.start;
    }

    public final void setStart(final Long start) {
        this.start = start;
    }

    public final List<Docs> getDocs() {
        return this.docs;
    }

    public final void setDocs(final List<Docs> docs) {
        this.docs = docs;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
