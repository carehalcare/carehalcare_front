package carehalcare.carehalcare.Feature_write.Active;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface Active_API {

    @Headers("Content-Type: application/json")
    @POST("activities")
    Call<Long> postDataActive(
            @Body Active_text active_text
    );
    @GET("activities/{id}")
    Call<Active_text> getDataActive_2(@Path("id") Long id);

    @GET("activities")
    Call<List<Active_text>> getDataActive_3();

    @GET("activities/list/{uid}/{puid}")
    Call<List<Active_text>> getDataActive(
            @Path("uid") String userId,
            @Path("puid") String puserId
    );

    @DELETE("activities/{id}")
    Call<Void> deleteDataActive(@Path("id") Long id);

    //수정
    @PUT("activities")
    Call<Long> putDataActive(
            @Body Active_text_change active_text_change
    );
}
