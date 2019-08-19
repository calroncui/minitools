package com.cuiyf.minitools.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReqOauth implements Serializable {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}