package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.GetDataGuruBody;
import com.hafidzquran.rumahquran.apiresults.GetDataGuruResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataGuruService {
    @POST("get_data_guru.php")
    Call<GetDataGuruResult> getRespond(@Body GetDataGuruBody Body);

}
