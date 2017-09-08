package com.hafidzquran.rumahquran.apiresults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HapusHafalanResult {
    @SerializedName("CODE")
    @Expose
    private String CODE;
    @SerializedName("DESC")
    @Expose
    private String DESC;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String cODE) {
        this.CODE = cODE;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String dESC) {
        this.DESC = dESC;
    }
}
