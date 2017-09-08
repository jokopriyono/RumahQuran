package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.TambahSantriBody;
import com.hafidzquran.rumahquran.apiresults.TambahSantriResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TambahSantriService {
    @POST("tambah_santri.php")
    Call<TambahSantriResult> getRespond(@Body TambahSantriBody Body);

}
