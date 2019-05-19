package com.example.skarn.please;

import com.google.gson.annotations.SerializedName;

public class ResultObject {
    @SerializedName("data")
    public String success;
//    private String success;
    public ResultObject(String success) {
        this.success = success;
    }
    public String getSuccess() {
        return success;
    }
}