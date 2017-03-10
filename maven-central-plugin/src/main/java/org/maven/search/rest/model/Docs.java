package org.maven.search.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Docs {

    private String id;
    private String g;
    private String a;
    private String v;
    private String p;
    private List<String> tags;
    private List<String> ec;

    public final String getId() {
        return this.id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getG() {
        return this.g;
    }

    public final void setG(final String g) {
        this.g = g;
    }

    public final String getA() {
        return this.a;
    }

    public final void setA(final String a) {
        this.a = a;
    }

    public final String getV() {
        return this.v;
    }

    public final void setV(final String v) {
        this.v = v;
    }

    public final String getP() {
        return this.p;
    }

    public final void setP(final String p) {
        this.p = p;
    }

    public final List<String> getTags() {
        return this.tags;
    }

    public final void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public final List<String> getEc() {
        return this.ec;
    }

    public final void setEc(final List<String> ec) {
        this.ec = ec;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
