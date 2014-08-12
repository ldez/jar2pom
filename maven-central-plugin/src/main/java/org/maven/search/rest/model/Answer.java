package org.maven.search.rest.model;

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
        final StringBuilder builder = new StringBuilder();
        builder.append("Answer [responseHeader=");
        builder.append(this.responseHeader);
        builder.append(", response=");
        builder.append(this.response);
        builder.append("]");
        return builder.toString();
    }

}
