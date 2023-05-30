package carehalcare.carehalcare.Feature_mainpage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PInfoApi {

    @GET("patients/info/{puid}")
    Call<PatientInfo> getDataInfo(@Path("puid") String puid);
}