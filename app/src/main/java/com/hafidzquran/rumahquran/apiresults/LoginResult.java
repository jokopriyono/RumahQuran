package com.hafidzquran.rumahquran.apiresults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResult {
    @SerializedName("CODE")
    @Expose
    private String CODE;
    @SerializedName("DESC")
    @Expose
    private String DESC;
    @SerializedName("DATA")
    @Expose
    private List<isiData> DATA;

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

    public class isiData{
        @SerializedName("EMAIL")
        @Expose
        private String EMAIL;

        public String getEMAIL() {
            return EMAIL;
        }
        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }
    }

    public List<isiData> getDATA() {
        return DATA;
    }

    public void setDATA(List<isiData> DATA) {
        this.DATA = DATA;
    }
}
