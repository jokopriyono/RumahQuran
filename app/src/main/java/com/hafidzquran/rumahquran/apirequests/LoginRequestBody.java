package com.hafidzquran.rumahquran.apirequests;

public class LoginRequestBody {
    private String usernya;
    private String passnya;

    public LoginRequestBody(String usernya, String passnya) {
        this.usernya = usernya;
        this.passnya = passnya;
    }
}
