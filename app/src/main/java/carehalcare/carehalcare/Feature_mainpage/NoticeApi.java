package carehalcare.carehalcare.Feature_mainpage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NoticeApi {

    @GET("notices/list/{puid}")
    Call<List<Notice>> getNotice(@Path("puid") String puid);
}