package carehalcare.carehalcare.Feature_write.Walk;

import java.util.List;
import java.util.Map;

import carehalcare.carehalcare.Feature_write.Meal.Meal_ResponseDTO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Walk_API {
    String URL = "http://172.20.20.168:8080/";

    @DELETE("walks/{id}")
    Call<Void> deleteDataWalk(@Path("id") Long id);

    @GET("walks/list/{userId}/{puserId}")
    Call<List<Walk_ResponseDTO>> getDataWalk(
            @Path("userId") String userId,
            @Path("puserId") String puserId);

    @Multipart
    @POST("walks") //식사 게시판 사진과 나머지 userid,puserid, content보내기
    Call<Long> postDataWalk (
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> images);

    @Multipart
    @POST("walks")
    Call<Long> addCustomer_Walk(
            //@Part("mealImageVO") Meal_DTO mealImageVO);
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> images);
}
