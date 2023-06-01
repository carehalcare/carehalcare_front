package carehalcare.carehalcare.Feature_mainpage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FindPatientApi {

    @GET("/patients")
    Call<FindPatient> getData(@Query("userId") String userId);

    @POST("/patients")
    Call<Long> addPID(@Body FindPatient findPatient);
}