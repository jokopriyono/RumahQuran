package com.hafidzquran.myhafidzquran.apiservices;

import com.hafidzquran.myhafidzquran.apirequests.LoginRequestBody;
import com.hafidzquran.myhafidzquran.apiresults.LoginResult;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface LoginService {
    @POST("guru_login.php")
    Call<LoginResult> getRespond(@Body LoginRequestBody Body);

}
