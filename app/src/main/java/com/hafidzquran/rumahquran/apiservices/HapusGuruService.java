package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.HapusGuruBody;
import com.hafidzquran.rumahquran.apiresults.HapusGuruResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HapusGuruService {
    @POST("hapus_guru.php")
    Call<HapusGuruResult> getRespond(@Body HapusGuruBody Body);

}
