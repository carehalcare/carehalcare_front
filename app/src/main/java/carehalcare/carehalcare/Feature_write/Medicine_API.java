package carehalcare.carehalcare.Feature_write;

import java.util.HashMap;
import java.util.List;

import kotlin.jvm.JvmMultifileClass;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Medicine_API {
    String URL = "http://192.168.0.18:8080/";
    @Headers("Content-Type: application/json")
    @POST("administrations")
    Call<List<Medicine_text>> postDatamedicine(
            @Body Medicine_text medicine_text
//            @FieldMap HashMap<String,Object> param
//            @Field("userid") String userid,
//            @Field("puserid") String puserid,
//            @Field("medicine") String medicine,
//            @Field("time") String time,
//            @Field("mealStatus") String mealStatus
    );
    @GET("administrations/{id}")
    Call<List<Medicine_text>> getDatamedicine_2(@Query("id") int id);

    @GET("administrations")
    Call<List<Medicine_text>> getDatamedicine_3();

    @GET("administrations/list/{userId}/{puserId}")
    Call<List<Medicine_text>> getDatamedicine(
            @Path("userId") String userId,
            @Path("puserId") String puserId
    );

    @DELETE("/administrations/{id}")
    Call<Void> deleteData(@Path("id") Long id);
}
