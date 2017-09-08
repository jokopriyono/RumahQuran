package com.hafidzquran.rumahquran.apirequests;

public class GetRekapBody {
    private String darinya;
    private String sampainya;
    private String santrinya;
    private String usernya;

    public GetRekapBody(String darinya, String sampainya, String santrinya, String usernya) {
        this.darinya = darinya;
        this.sampainya = sampainya;
        this.santrinya = santrinya;
        this.usernya = usernya;
    }
}
