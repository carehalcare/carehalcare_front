package carehalcare.carehalcare.Feature_write.Medicine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Medicine_API {
    String URL = "http://172.20.20.77:8080/";
    @Headers("Content-Type: application/json")
    @POST("administrations")
    Call<Long> postDatamedicine(
            @Body Medicine_text medicine_text
    );
    @GET("administrations/{id}")
    Call<Medicine_text> getDatamedicine_2(@Path("id") Long id);

    @GET("administrations")
    Call<List<Medicine_text>> getDatamedicine_3();

    @GET("administrations/list/{userId}/{puserId}")
    Call<List<Medicine_text>> getDatamedicine(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("/administrations/{id}")
    Call<Void> deleteDataMedicine(@Path("id") Long id);

    @POST("administrations")
    Call<Long> putDatamedicine(
            @Body Medicine_text_change medicine_text_change
    );
}
