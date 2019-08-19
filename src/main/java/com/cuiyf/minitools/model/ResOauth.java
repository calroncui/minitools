package com.cuiyf.minitools.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResOauth implements Serializable {
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}