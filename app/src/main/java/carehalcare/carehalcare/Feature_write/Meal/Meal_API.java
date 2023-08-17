package carehalcare.carehalcare.Feature_write.Meal;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Meal_API {
    String URL = "http://192.168.0.18:8080/";

    @GET("meals/{id}")
    Call<Meal_ResponseDTO> getdatameal2(
            @Path("id") Long id
    );

    @DELETE("meals/{id}")
    Call<Void> deleteData(@Path("id") Long id);
    @GET("meals/list/{userId}/{puserId}")
    Call<List<Meal_ResponseDTO>> getDatameal(
            @Path("userId") String userId,
            @Path("puserId") String puserId);

    @Multipart
    @POST("meals") //식사 게시판 사진과 나머지 userid,puserid, content보내기
    Call<Long> postDatameal (
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> images);

    @Multipart
    @POST("meals")
    Call<Long> addCustomer(
            //@Part("mealImageVO") Meal_DTO mealImageVO);
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> images);

    @PUT("meals")
    Call<Long> updateMeal(
            @Body Meal_text_change mealTextChange);

}
