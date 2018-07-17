package com.maiya.beans;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by lubin 2016/11/7
 */
public class MemberContacts {
    @Field
    private String id;
    @Field("stelphone")
    private String stelphone;

    public MemberContacts() {
    }

    public MemberContacts(String id, String stelphone) {
        this.id = id;
        this.stelphone = stelphone;
    }

    public String getId() {
        return id;
    }

    public String getStelphone() {
        return stelphone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStelphone(String stelphone) {
        this.stelphone = stelphone;
    }
}
