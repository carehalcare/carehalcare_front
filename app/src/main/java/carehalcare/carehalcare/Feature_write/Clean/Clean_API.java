package carehalcare.carehalcare.Feature_write.Clean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Clean_API {

    @Headers("Content-Type: application/json")
    @POST("scleanliness")
    Call<Long> postDataClean(
            @Body Clean_ResponseDTO cleanResponseDTO
    );
    @GET("scleanliness/{id}")
    Call<Clean_ResponseDTO> getDataClean_2(@Path("id") Long id);

    @GET("scleanliness")
    Call<List<Clean_ResponseDTO>> getDataClean_3();

    @GET("scleanliness/list/{userId}/{puserId}")
    Call<List<Clean_ResponseDTO>> getDataClean(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("scleanliness/{id}")
    Call<Void> deleteDataClean(@Path("id") Long id);

    @PUT("scleanliness")
    Call<Long> putDataClean(
            @Body Clean_text_change cleanTextChange
    );
}