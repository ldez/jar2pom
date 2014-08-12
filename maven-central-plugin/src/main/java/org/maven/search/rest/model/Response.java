package org.maven.search.rest.model;

import java.util.List;

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
        final StringBuilder builder = new StringBuilder();
        builder.append("Response [numFound=");
        builder.append(this.numFound);
        builder.append(", start=");
        builder.append(this.start);
        builder.append(", docs=");
        builder.append(this.docs);
        builder.append("]");
        return builder.toString();
    }

}
