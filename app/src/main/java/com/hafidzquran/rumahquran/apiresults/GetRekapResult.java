package com.hafidzquran.rumahquran.apiresults;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRekapResult {
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

        @SerializedName("TGL")
        @Expose
        private String TANGGAL;
        public String getTANGGAL() {
            return TANGGAL;
        }
        public void setTANGGAL(String TANGGAL) {
            this.TANGGAL = TANGGAL;
        }

        @SerializedName("JUZ")
        @Expose
        private String JUZ;
        public String getJUZ() {
            return JUZ;
        }
        public void setJUZ(String JUZ) {
            this.JUZ = JUZ;
        }

        @SerializedName("HAL")
        @Expose
        private String HALAMAN;
        public String getHALAMAN() {
            return HALAMAN;
        }
        public void setHALAMAN(String HALAMAN) {
            this.HALAMAN = HALAMAN;
        }

        @SerializedName("KEL")
        @Expose
        private String KELANCARAN;
        public String getKELANCARAN() {
            return KELANCARAN;
        }
        public void setKELANCARAN(String KELANCARAN) {
            this.KELANCARAN = KELANCARAN;
        }

        @SerializedName("MUR")
        @Expose
        private String MUROJAAH;
        public String getMUROJAAH() {
            return MUROJAAH;
        }
        public void setMUROJAAH(String MUROJAAH) {
            this.MUROJAAH = MUROJAAH;
        }

        @SerializedName("KET")
        @Expose
        private String KETERANGAN;
        public String getKETERANGAN() {
            return KETERANGAN;
        }
        public void setKETERANGAN(String KETERANGAN) {
            this.KETERANGAN = KETERANGAN;
        }

        @SerializedName("SAN")
        @Expose
        private String SANTRI;
        public String getSANTRI() {
            return SANTRI;
        }
        public void setSANTRI(String SANTRI) {
            this.SANTRI = SANTRI;
        }

        @SerializedName("IDSAN")
        @Expose
        private String IDSANTRI;
        public String getIDSANTRI() {
            return IDSANTRI;
        }
        public void setIDSANTRI(String IDSANTRI) {
            this.IDSANTRI = IDSANTRI;
        }
    }
}