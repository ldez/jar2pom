package org.maven.search.rest.model;

public class Params {

    private String fl;
    private String sort;
    private String indent;
    private String q;
    private String wt;
    private String rows;
    private String version;

    public final String getFl() {
        return this.fl;
    }

    public final void setFl(final String fl) {
        this.fl = fl;
    }

    public final String getSort() {
        return this.sort;
    }

    public final void setSort(final String sort) {
        this.sort = sort;
    }

    public final String getIndent() {
        return this.indent;
    }

    public final void setIndent(final String indent) {
        this.indent = indent;
    }

    public final String getQ() {
        return this.q;
    }

    public final void setQ(final String q) {
        this.q = q;
    }

    public final String getWt() {
        return this.wt;
    }

    public final void setWt(final String wt) {
        this.wt = wt;
    }

    public final String getRows() {
        return this.rows;
    }

    public final void setRows(final String rows) {
        this.rows = rows;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Params [fl=");
        builder.append(this.fl);
        builder.append(", sort=");
        builder.append(this.sort);
        builder.append(", indent=");
        builder.append(this.indent);
        builder.append(", q=");
        builder.append(this.q);
        builder.append(", wt=");
        builder.append(this.wt);
        builder.append(", rows=");
        builder.append(this.rows);
        builder.append(", version=");
        builder.append(this.version);
        builder.append("]");
        return builder.toString();
    }

}
