package com.victor.bean;

public class OnlineInfo {
    String esn;

    public String getEsn() {
        return esn;
    }

    public void setEsn(String esn) {
        this.esn = esn;
    }

    @Override
    public String toString() {
        return "OnlineInfo{" +
                "esn='" + esn + '\'' +
                '}';
    }
}
