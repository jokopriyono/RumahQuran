package com.hafidzquran.rumahquran.apirequests;

public class TambahHafalanBody {
    private String usernya;
    private String tanggalnya;
    private String santrinya;
    private String juznya;
    private String halamannya;
    private String kelancarannya;
    private String murojaahnya;
    private String keterangannya;

    public TambahHafalanBody(String usernya, String tanggalnya, String santrinya, String juznya, String halamannya,
                             String kelancarannya, String murojaahnya, String keterangannya) {
        this.usernya = usernya;
        this.tanggalnya = tanggalnya;
        this.santrinya = santrinya;
        this.juznya = juznya;
        this.halamannya = halamannya;
        this.kelancarannya = kelancarannya;
        this.murojaahnya = murojaahnya;
        this.keterangannya = keterangannya;
    }
}
