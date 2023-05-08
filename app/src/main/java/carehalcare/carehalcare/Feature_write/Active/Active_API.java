package carehalcare.carehalcare.Feature_write.Active;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.Feature_write.Wash.Wash_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Active_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("activities")
    Call<List<Active_text>> postDataActive(
            @Body Active_text active_text
    );
    @GET("activities/{id}")
    Call<List<Active_text>> getDataActive_2(@Query("id") int id);

    @GET("activities")
    Call<List<Active_text>> getDataActive_3();

    @GET("activities/list/{userId}/{puserId}")
    Call<List<Active_text>> getDataActive(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("activities/{id}")
    Call<Void> deleteDataActive(@Path("id") Long id);
}
