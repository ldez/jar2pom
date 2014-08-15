package org.maven.search.rest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeader {

    private Long status;

    // ,"QTime":1

    private Params params;

    public final Long getStatus() {
        return this.status;
    }

    public final void setStatus(final Long status) {
        this.status = status;
    }

    public final Params getParams() {
        return this.params;
    }

    public final void setParams(final Params params) {
        this.params = params;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
