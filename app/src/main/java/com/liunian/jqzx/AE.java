package com.liunian.jqzx;

public class AE {
    private String BZ;
    private String CE;
    private String CA;
    private String Q;
    private String MPCA="0";
    private String A_NO;
    private String TIME;


    public AE() {
    }

    public AE(String BZ, String CE, String CA, String q, String MPCA, String a_NO, String TIME) {
        this.BZ = BZ;
        this.CE = CE;
        this.CA = CA;
        Q = q;
        this.MPCA = MPCA;
        A_NO = a_NO;
        this.TIME = TIME;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public AE(String BZ, String CE, String CA, String q, String a_NO, String TIME) {
        this.BZ = BZ;
        this.CE = CE;
        this.CA = CA;
        Q = q;
        A_NO = a_NO;
        this.TIME = TIME;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getCE() {
        return CE;
    }

    public void setCE(String CE) {
        this.CE = CE;
    }

    public String getCA() {
        return CA;
    }

    public void setCA(String CA) {
        this.CA = CA;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public String getMPCA() {
        return MPCA;
    }

    public void setMPCA(String MPCA) {
        this.MPCA = MPCA;
    }

    public String getA_NO() {
        return A_NO;
    }

    public void setA_NO(String a_NO) {
        A_NO = a_NO;
    }

}