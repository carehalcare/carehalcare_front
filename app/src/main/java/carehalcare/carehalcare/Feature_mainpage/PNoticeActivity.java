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

import java.util.List;

import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PNoticeActivity extends AppCompatActivity {

    ImageButton btn_home;
    private ListView listnoti;
    private ListNoticeAdapter notiadapter;
    //private String[] context = {"공지", "공지공지"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workernoti);

         TextView p_notice = (TextView) findViewById(R.id.p_notice);
        btn_home = (ImageButton) findViewById(R.id.btn_homenoti);
        notiadapter = new ListNoticeAdapter();
        listnoti = (ListView) findViewById(R.id.list_notice);

        listnoti.setAdapter(notiadapter);
/*
        for(int i=0; i < context.length; i++){
            notiadapter.addInfo(context[i]);
        }
 */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.5.58:8080/") //연결된 네트워크 ip로 수정필요
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        NoticeApi noticeApi = retrofit.create(NoticeApi.class);

        Call<List<Notice>> call = noticeApi.getPosts();

        call.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful()) {
                    List<Notice> notices = response.body();
                    String content = "";

                    for (Notice notice : notices) {
                        content += "내용: " + notice.getContent() + "  ";
                        content += "작성날짜: " + notice.getCreatedDate() + "\n";
                        p_notice.append(content);
                        notiadapter.addInfo(content);
                        //list로 어떻게 받아야할 지 수정이 필요함
                    }
                }
                else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
                p_notice.setText(t.getMessage());
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
