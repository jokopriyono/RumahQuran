package com.hafidzquran.rumahquran.apiresults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataSantriResult {
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
        @SerializedName("ID")
        @Expose
        private String ID;
        public String getID() {
            return ID;
        }
        public void setID(String ID) {
            this.ID = ID;
        }

        @SerializedName("NAMA")
        @Expose
        private String NAMA;
        public String getNAMA() {
            return NAMA;
        }
        public void setNAMA(String NAMA) {
            this.NAMA = NAMA;
        }

        @SerializedName("TAHUN")
        @Expose
        private String TAHUN;
        public String getTAHUN() {
            return TAHUN;
        }
        public void setTAHUN(String TAHUN) {
            this.TAHUN = TAHUN;
        }
    }
}
