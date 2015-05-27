package com.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JC87 on 15/09/2014.
 */
public class Configuration extends com.yammer.dropwizard.config.Configuration {

    @JsonProperty("couchbase.nodes")
    private String[] couchbaseNodes;

    @JsonProperty("couchbase.bucket")
    private String couchbaseBucket;

    @JsonProperty("couchbase.admin.username")
    private String couchbaseAdminUsername;

    @JsonProperty("couchbase.admin.password")
    private String couchbaseAdminPassword;

    @JsonProperty("couchbase.username")
    private String couchbaseUsername;

    @JsonProperty("couchbase.password")
    private String couchbasePassword;

    public Configuration() {}

    public String[] getCouchbaseNodes() {
        return couchbaseNodes;
    }

    public String getCouchbaseBucket() {
        return couchbaseBucket;
    }

    public String getCouchbaseUsername() {
        return couchbaseUsername;
    }

    public String getCouchbasePassword() {
        return couchbasePassword;
    }

    public String getCouchbaseAdminUsername() {
        return couchbaseAdminUsername;
    }

    public String getCouchbaseAdminPassword() {
        return couchbaseAdminPassword;
    }

    protected void setCouchbaseBucket(String couchbaseBucket){
        this.couchbaseBucket = couchbaseBucket;
    }
}
