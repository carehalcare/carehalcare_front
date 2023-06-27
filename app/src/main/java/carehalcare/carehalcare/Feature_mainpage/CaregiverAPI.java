package carehalcare.carehalcare.Feature_mainpage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CaregiverAPI {
    @GET("caregivers")
    Call<UserDTO> getCaregiverInfo(@Query("userId") String userId);
}
