package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.HapusHafalanBody;
import com.hafidzquran.rumahquran.apiresults.HapusHafalanResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HapusHafalanService {
    @POST("hapus_hafalan.php")
    Call<HapusHafalanResult> getRespond(@Body HapusHafalanBody Body);

}
