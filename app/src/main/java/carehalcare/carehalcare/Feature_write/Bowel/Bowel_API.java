package carehalcare.carehalcare.Feature_write.Bowel;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Bowel_API {

    @Headers("Content-Type: application/json")
    @POST("bowelmovements")
    Call<Long> postDataBowel(
            @Body Bowel_text bowel_text
    );
    @GET("bowelmovements/{id}")
    Call<Bowel_text> getDataBowel_2(@Path("id") Long id);

    @GET("bowelmovements")
    Call<List<Bowel_text>> getDataBowel_3();

    @GET("bowelmovements/list/{uid}/{puid}")
    Call<List<Bowel_text>> getDataBowel(
            @Path("uid") String userId,
            @Path("puid") String puserId
    );

    @DELETE("bowelmovements/{id}")
    Call<Void> deleteDataBowel(@Path("id") Long id);

    @PUT("bowelmovements")
    Call<Long> putDataBowel(
            @Body Bowel_text_change bowel_text_change
    );

}
