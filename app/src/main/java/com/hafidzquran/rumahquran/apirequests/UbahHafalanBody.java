package com.hafidzquran.rumahquran.apirequests;

public class UbahHafalanBody {
    private String idhafalannya;
    private String usernya;
    private String tanggalnya;
    private String santrinya;
    private String juznya;
    private String halamannya;
    private String kelancarannya;
    private String murojaahnya;
    private String keterangannya;

    public UbahHafalanBody(String idhafalannya, String usernya, String tanggalnya, String santrinya, String juznya, String halamannya,
                           String kelancarannya, String murojaahnya, String keterangannya) {
        this.idhafalannya = idhafalannya;
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
