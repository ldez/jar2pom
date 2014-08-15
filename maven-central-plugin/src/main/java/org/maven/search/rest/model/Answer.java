package org.maven.search.rest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Answer {

    private ResponseHeader responseHeader;

    private Response response;

    public final ResponseHeader getResponseHeader() {
        return this.responseHeader;
    }

    public final void setResponseHeader(final ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public final Response getResponse() {
        return this.response;
    }

    public final void setResponse(final Response response) {
        this.response = response;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
