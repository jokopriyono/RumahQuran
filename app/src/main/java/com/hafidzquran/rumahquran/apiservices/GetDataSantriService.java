package com.hafidzquran.rumahquran.apiservices;

import com.hafidzquran.rumahquran.apirequests.GetDataSantriBody;
import com.hafidzquran.rumahquran.apiresults.GetDataSantriResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataSantriService {
    @POST("get_data_santri.php")
    Call<GetDataSantriResult> getRespond(@Body GetDataSantriBody Body);

}
