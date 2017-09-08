package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.LoginRequestBody;
import com.hafidzquran.rumahquran.apiresults.LoginResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAdminService {
    @POST("admin_login.php")
    Call<LoginResult> getRespond(@Body LoginRequestBody Body);
}
