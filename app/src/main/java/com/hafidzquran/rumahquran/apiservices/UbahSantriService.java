package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.UbahSantriBody;
import com.hafidzquran.rumahquran.apiresults.UbahSantriResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UbahSantriService {
    @POST("ubah_santri.php")
    Call<UbahSantriResult> getRespond(@Body UbahSantriBody Body);

}
