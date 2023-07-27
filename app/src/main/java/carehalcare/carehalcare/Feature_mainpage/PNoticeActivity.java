package carehalcare.carehalcare.Feature_mainpage;

import static android.content.ContentValues.TAG;

import static carehalcare.carehalcare.DateUtils.formatDate;
import static carehalcare.carehalcare.DateUtils.formatDatestring;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PNoticeActivity extends AppCompatActivity {
    String userid, puserid;

    private NoticeViewAdapter noticeViewAdapter;
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
        noticeApi = Retrofit_client.createService(NoticeApi.class, TokenUtils.getAccessToken("Access_Token"));

        Call<List<Notice>> call = noticeApi.getNotice(TokenUtils.getPUser_Id("PUser_Id"));

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

        noticeViewAdapter.setOnItemClickListener(new NoticeViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 리스트 항목 클릭 시 동작할 코드 작성
                Notice notice = notiviewlist.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(PNoticeActivity.this);
                View view = LayoutInflater.from(PNoticeActivity.this)
                        .inflate(R.layout.notice_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                TextView notidetail = dialog.findViewById(R.id.tv_notidetail);
                TextView notidate = dialog.findViewById(R.id.tv_notidate);

                notidetail.setText(notice.getContent());
                notidate.setText(formatDatestring(notice.getModifiedDateTime()));

                Button btn_out = dialog.findViewById(R.id.btn_out);

                btn_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
