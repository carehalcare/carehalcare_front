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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PNoticeActivity extends AppCompatActivity {
    String userid, puserid;

    NoticeViewAdapter noticeViewAdapter;
    private ImageButton btn_home;
    private RecyclerView notiview;
    private ArrayList<Notice> notiviewlist;
    private TextView tv_notiview;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnotice);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        puserid = intent.getStringExtra("puserid");

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
                .baseUrl(API_URL.URL) //연결된 네트워크 ip로 수정필요
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        NoticeApi noticeApi = retrofit.create(NoticeApi.class);

        Call<List<Notice>> call = noticeApi.getNotice("puserid1");

        call.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Notice> notices = response.body();

                    for (Notice notice : notices) {
                        notiviewlist.add(notice);

                    }
                    Log.d("연결 성공", "Status Code : " + response.code());
                    noticeViewAdapter.notifyDataSetChanged();

                } else {
                    Log.d("연결 실패", "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
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
