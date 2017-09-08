package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.UbahHafalanBody;
import com.hafidzquran.rumahquran.apiresults.UbahHafalanResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UbahHafalanService {
    @POST("ubah_hafalan.php")
    Call<UbahHafalanResult> getRespond(@Body UbahHafalanBody Body);
}
