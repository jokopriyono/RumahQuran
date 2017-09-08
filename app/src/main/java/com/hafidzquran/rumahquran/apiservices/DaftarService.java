package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.DaftarRequestBody;
import com.hafidzquran.rumahquran.apiresults.DaftarResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DaftarService {
    @POST("guru_daftar.php")
    Call<DaftarResult> getRespond(@Body DaftarRequestBody Body);

}
