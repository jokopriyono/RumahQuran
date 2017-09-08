package com.hafidzquran.rumahquran.apirequests;

public class DaftarRequestBody {
    private String usernya;
    private String namanya;
    private String emailnya;
    private String passnya;
    private String kodenya;

    public DaftarRequestBody(String usernya, String namanya, String emailnya, String passnya, String kodenya) {
        this.usernya = usernya;
        this.namanya = namanya;
        this.emailnya = emailnya;
        this.passnya = passnya;
        this.kodenya = kodenya;
    }
}
