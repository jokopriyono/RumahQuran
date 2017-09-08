package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.TambahHafalanBody;
import com.hafidzquran.rumahquran.apiresults.TambahHafalanResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TambahHafalanService {
    @POST("tambah_hafalan.php")
    Call<TambahHafalanResult> getRespond(@Body TambahHafalanBody Body);
}
