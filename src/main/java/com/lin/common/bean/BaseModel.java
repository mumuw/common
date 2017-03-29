package com.lin.common.bean;

/**
 * Created by linweilin on 2017/3/10.
 */

public class BaseModel {

    private String reason = "";
    private String error_code = "";

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }


    @Override
    public String toString() {
        return "BaseModel{" +
                "reason='" + reason + '\'' +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}
