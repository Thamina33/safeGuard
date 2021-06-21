package com.bu.safeguard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResult {
    @Expose
    @SerializedName("MSG")
    private String msg;

    public UploadResult() {
    }

    public UploadResult(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
