package com.example.skarn.please;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ResultObject {
    @SerializedName("result")
    public String success;
    @SerializedName("score")
    public ArrayList<String> prob;
//    private String success;
    public ResultObject(String success, ArrayList<String> prob) {
        this.success = success;
        this.prob = prob;
    }
    public String getSuccess() {
        return success;
    }
    public ArrayList<String> getProb() { return prob;}
}