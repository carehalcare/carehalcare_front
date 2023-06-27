package carehalcare.carehalcare.Feature_write.Allmenu;

import java.util.List;
import java.util.Objects;

import carehalcare.carehalcare.Feature_write.Active.Active_text;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Allmenu_API {
    @GET("boards/{uid}/{puid}")
    Call<List<BoardResponseDto>> getallmenu(
            @Header("Authorization") String header,
            @Path("uid") String userId,
            @Path("puid") String puserId);

}
