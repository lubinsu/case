package com.maiya.beans;

/**
 * Created by lubin 2016/11/7
 */
public class MemberContactsES {
    private String sguid;
    private String sdeviceno;
    private String suserid;
    private String suserno;
    private String sname;
    private String stelphone;
    private String sterminalchannel;
    private String idelflag;
    private String dadddate;

    public MemberContactsES(String sguid, String sdeviceno, String suserid, String suserno, String sname, String stelphone, String sterminalchannel, String idelflag, String dadddate) {
        this.sguid = sguid;
        this.sdeviceno = sdeviceno;
        this.suserid = suserid;
        this.suserno = suserno;
        this.sname = sname;
        this.stelphone = stelphone;
        this.sterminalchannel = sterminalchannel;
        this.idelflag = idelflag;
        this.dadddate = dadddate;
    }

    public String getSguid() {
        return sguid;
    }

    public MemberContactsES setSguid(String sguid) {
        this.sguid = sguid;
        return this;
    }

    public String getSdeviceno() {
        return sdeviceno;
    }

    public MemberContactsES setSdeviceno(String sdeviceno) {
        this.sdeviceno = sdeviceno;
        return this;
    }

    public String getSuserid() {
        return suserid;
    }

    public MemberContactsES setSuserid(String suserid) {
        this.suserid = suserid;
        return this;
    }

    public String getSuserno() {
        return suserno;
    }

    public MemberContactsES setSuserno(String suserno) {
        this.suserno = suserno;
        return this;
    }

    public String getSname() {
        return sname;
    }

    public MemberContactsES setSname(String sname) {
        this.sname = sname;
        return this;
    }

    public String getStelphone() {
        return stelphone;
    }

    public MemberContactsES setStelphone(String stelphone) {
        this.stelphone = stelphone;
        return this;
    }

    public String getSterminalchannel() {
        return sterminalchannel;
    }

    public MemberContactsES setSterminalchannel(String sterminalchannel) {
        this.sterminalchannel = sterminalchannel;
        return this;
    }

    public String getIdelflag() {
        return idelflag;
    }

    public MemberContactsES setIdelflag(String idelflag) {
        this.idelflag = idelflag;
        return this;
    }

    public String getDadddate() {
        return dadddate;
    }

    public MemberContactsES setDadddate(String dadddate) {
        this.dadddate = dadddate;
        return this;
    }
}
