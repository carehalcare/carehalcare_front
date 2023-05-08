package carehalcare.carehalcare.Feature_write.Bowel;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Bowel_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("bowelmovements")
    Call<List<Bowel_text>> postDataBowel(
            @Body Bowel_text bowel_text
    );
    @GET("bowelmovements/{id}")
    Call<List<Bowel_text>> getDataBowel_2(@Query("id") int id);

    @GET("bowelmovements")
    Call<List<Bowel_text>> getDataBowel_3();

    @GET("bowelmovements/list/{userId}/{puserId}")
    Call<List<Bowel_text>> getDataBowel(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("bowelmovements/{id}")
    Call<Void> deleteDataBowel(@Path("id") Long id);
}
