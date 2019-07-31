package com.cuiyf.minitools.result;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AbstractRestResponse<T> {
    private String resCode;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("obj")
    private T restObject;

    /**
     * @Return the String resCode
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * @Param String resCode to set
     */
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    /**
     * @Return the T restObject
     */
    public T getRestObject() {
        return restObject;
    }

    /**
     * @Param T restObject to set
     */
    public void setRestObject(T restObject) {
        this.restObject = restObject;
    }

    /**
     * @Return the String message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @Param String message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String AbstractRestResponseStr(AbstractRestResponse rest){
        String result=null;
        result= JSONObject.toJSONString(rest);
        return result;
    }
}
