package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.GetRekapBody;
import com.hafidzquran.rumahquran.apiresults.GetRekapResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetRekapService {
    @POST("get_rekap.php")
    Call<GetRekapResult> getRespond(@Body GetRekapBody Body);

}
