package carehalcare.carehalcare.Feature_mainpage;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PushmsgAPI {
    @POST("pushmsg")
    Call<Object> postMSG(
            @Query("userId") String userId,
            @Query("fcmToken") String fcmToken
    );

}
