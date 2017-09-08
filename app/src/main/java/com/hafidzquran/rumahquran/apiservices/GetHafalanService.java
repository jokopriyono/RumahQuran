package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.GetHafalanBody;
import com.hafidzquran.rumahquran.apiresults.GetHafalanResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetHafalanService {
    @POST("get_hafalan.php")
    Call<GetHafalanResult> getRespond(@Body GetHafalanBody Body);

}
