package carehalcare.carehalcare.Feature_mainpage;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PNoticeActivity extends AppCompatActivity {

    NoticeViewAdapter noticeViewAdapter;
    private ImageButton btn_home;
    private RecyclerView notiview;
    private ArrayList<Notice> notiviewlist;
    private TextView tv_notiview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnotice);

        tv_notiview = (TextView) findViewById(R.id.tv_notiview);
        btn_home = (ImageButton) findViewById(R.id.btn_homenoti);
        notiview = (RecyclerView) findViewById(R.id.noti_view);

        //LayoutManager 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notiview.setLayoutManager(layoutManager);

        //어댑터 set
        notiviewlist = new ArrayList<>();
        noticeViewAdapter = new NoticeViewAdapter(notiviewlist);
        notiview.setAdapter(noticeViewAdapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.101.2.189:8080/") //연결된 네트워크 ip로 수정필요
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        NoticeApi noticeApi = retrofit.create(NoticeApi.class);

        Call<Notice> call = noticeApi.getNotice("userid1");

        call.enqueue(new Callback<Notice>() {
            @Override
            public void onResponse(Call<Notice> call, Response<Notice> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Notice notices = new Notice(response.body().getContent(),
                                response.body().getCreatedDate());
                        notiviewlist.add(0, notices);
                        Log.e("userid: ", response.body().getCreatedDate().toString());
                    }
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Notice> call, Throwable t) {
                tv_notiview.setText(t.getMessage());
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PNoticeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
