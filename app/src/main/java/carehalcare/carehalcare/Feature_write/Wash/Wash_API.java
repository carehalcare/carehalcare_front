package carehalcare.carehalcare.Feature_write.Wash;

import java.util.List;

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

public interface Wash_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("pcleanliness")
    Call<Long> postDataWash(
            @Body Wash_ResponseDTO washResponseDTO
    );
    @GET("pcleanliness/{id}")
    Call<Wash_ResponseDTO> getDataWash_2(@Path("id") Long id);

    @GET("pcleanliness")
    Call<List<Wash_ResponseDTO>> getDataWash_3();

    @GET("pcleanliness/list/{userId}/{puserId}")
    Call<List<Wash_ResponseDTO>> getDataWash(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("pcleanliness/{id}")
    Call<Void> deleteDataWash(@Path("id") Long id);

    @PUT("pcleanliness")
    Call<Long> putDataWash(
            @Body Wash_text_change washTextChange
    );
}
