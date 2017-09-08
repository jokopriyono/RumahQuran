package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.HapusSantriBody;
import com.hafidzquran.rumahquran.apiresults.HapusSantriResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HapusSantriService {
    @POST("hapus_santri.php")
    Call<HapusSantriResult> getRespond(@Body HapusSantriBody Body);

}
