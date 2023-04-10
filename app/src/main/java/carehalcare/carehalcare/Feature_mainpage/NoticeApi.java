package carehalcare.carehalcare.Feature_mainpage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NoticeApi {

    @GET("/notices/list/userid1")
    Call<List<Notice>> getPosts();
}