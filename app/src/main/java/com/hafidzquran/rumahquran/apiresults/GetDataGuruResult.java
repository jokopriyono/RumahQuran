package com.hafidzquran.rumahquran.apiresults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataGuruResult {
    @SerializedName("CODE")
    @Expose
    private String cODE;
    @SerializedName("DESC")
    @Expose
    private String dESC;
    @SerializedName("DATA")
    @Expose
    private List<isiData> dATA = null;

    public String getCODE() {
        return cODE;
    }

    public void setCODE(String cODE) {
        this.cODE = cODE;
    }

    public String getDESC() {
        return dESC;
    }

    public void setDESC(String dESC) {
        this.dESC = dESC;
    }

    public List<isiData> getDATA() {
        return dATA;
    }

    public class isiData{
        @SerializedName("USERNAME")
        @Expose
        private String USERNAME;
        @SerializedName("NAMA")
        @Expose
        private String NAMA;
        @SerializedName("EMAIL")
        @Expose
        private String EMAIL;

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getNAMA() {
            return NAMA;
        }

        public void setNAMA(String NAMA) {
            this.NAMA = NAMA;
        }

        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }
    }
}
