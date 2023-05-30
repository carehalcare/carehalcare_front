package carehalcare.carehalcare.Feature_write.Clean;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Bowel.Bowel_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Clean_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("scleanliness")
    Call<List<Clean_ResponseDTO>> postDataClean(
            @Body Clean_ResponseDTO cleanResponseDTO
    );
    @GET("scleanliness/{id}")
    Call<List<Clean_ResponseDTO>> getDataClean_2(@Query("id") int id);

    @GET("scleanliness")
    Call<List<Clean_ResponseDTO>> getDataClean_3();

    @GET("scleanliness/list/{userId}/{puserId}")
    Call<List<Clean_ResponseDTO>> getDataClean(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("scleanliness/{id}")
    Call<Void> deleteDataClean(@Path("id") Long id);
}
