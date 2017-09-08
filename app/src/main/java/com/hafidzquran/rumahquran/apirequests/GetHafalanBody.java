package com.hafidzquran.rumahquran.apirequests;

public class GetHafalanBody {
    private String usernya;
    private String tanggalnya;

    public GetHafalanBody(String usernya, String tanggalnya) {
        this.usernya = usernya;
        this.tanggalnya = tanggalnya;
    }
}
