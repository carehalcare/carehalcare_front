package carehalcare.carehalcare.Feature_write.Wash;

import java.util.List;

import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Wash_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("pcleanliness")
    Call<List<Wash_ResponseDTO>> postDataWash(
            @Body Wash_ResponseDTO washResponseDTO
    );
    @GET("pcleanliness/{id}")
    Call<List<Wash_ResponseDTO>> getDataWash_2(@Query("id") int id);

    @GET("pcleanliness")
    Call<List<Wash_ResponseDTO>> getDataWash_3();

    @GET("pcleanliness/list/{userId}/{puserId}")
    Call<List<Wash_ResponseDTO>> getDataWash(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("pcleanliness/{id}")
    Call<Void> deleteDataWash(@Path("id") Long id);
}
