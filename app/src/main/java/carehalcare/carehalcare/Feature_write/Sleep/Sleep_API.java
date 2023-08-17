package carehalcare.carehalcare.Feature_write.Sleep;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Active.Active_text;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Sleep_API {
    String URL = "http://172.20.4.180:8080/";
    @Headers("Content-Type: application/json")
    @POST("sleepstates")
    Call<Long> postDataSleep(
            @Body Sleep_text sleep_text
    );
    @GET("sleepstates/{id}")
    Call<Sleep_text> getDataSleep_2(@Path("id") Long id);

    @GET("sleepstates")
    Call<List<Sleep_text>> getDataSleep_3();

    @GET("sleepstates/list/{uid}/{puid}")
    Call<List<Sleep_text>> getDataSleep(
            @Path("uid") String userId,
            @Path("puid") String puserId
    );

    @DELETE("sleepstates/{id}")
    Call<Void> deleteDataSleep(@Path("id") Long id);

    @PUT("sleepstates")
    Call<Long> putDataSleep(
            @Body Sleep_text_change sleep_text_change
    );
}
