package carehalcare.carehalcare.Feature_mainpage;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindPatientActivity extends AppCompatActivity {

//    public static final String BASEURL = "http://172.20.5.216:8080/";
    public static final String BASEURL = API_URL.URL;

    AppCompatButton btn_findId, btn_ok;
    ImageButton btn_home;
    TextView tv_result;
    EditText et_getid;
    private FindPatient pdatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpid);

        btn_findId = (AppCompatButton) findViewById(R.id.btn_idsearch);
        btn_ok = (AppCompatButton) findViewById(R.id.btn_ok);
        btn_home = (ImageButton) findViewById(R.id.btn_home);
        tv_result = (TextView) findViewById(R.id.tv_findresult);
        et_getid = (EditText) findViewById(R.id.et_getid);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        FindPatientApi findPatientApi= retrofit.create(FindPatientApi.class);


        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디찾기 버튼을 누르면
                String puserId = et_getid.getText().toString();

                Call<FindPatient> call = findPatientApi.getData(puserId);
                call.enqueue(new Callback<FindPatient>() {
                    @Override
                    public void onResponse(Call<FindPatient> call, Response<FindPatient> response) {
                        if (response.isSuccessful()) {
                            pdatas = response.body();
                            if (pdatas != null) {
                                String str = "";
                                String puserId = pdatas.getUserId();
                                if (pdatas.getUserId().equals(puserId)) {
                                    str = "아이디 조회 결과 " + "'" + pdatas.getUsername() + "'" + " 님이 맞습니까?";
                                } tv_result.setText(str);
                            }
                        }
                        else {
                            Log.d(TAG, "노데이터 : " + response.code());
                            tv_result.setText("일치하는 아이디가 없습니다.");
                        }
                        if (puserId == null || puserId.isEmpty()) {
                            tv_result.setText("아이디가 입력되지 않았습니다.");
                        }
                    }
                    @Override
                    public void onFailure(Call<FindPatient> call, Throwable t) {
                        tv_result.setText(t.getMessage());
                    }
                });

            }
        });

        //등록하기 버튼을 누르면 Post
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdatas == null) {
                    tv_result.setText("먼저 아이디 조회를 해주세요.");
                    return;
                }

                String newPid = "간병인ID";

                // 새로운 PID를 서버에 보내어 등록하기
                FindPatient findPatient = new FindPatient(newPid, pdatas.getUserId());
                Call<FindPatient> post = findPatientApi.addPID(findPatient);

                post.enqueue(new Callback<FindPatient>() {
                    @Override
                    public void onResponse(Call<FindPatient> post, Response<FindPatient> response) {
                        if (response.isSuccessful()) {
                            // TODO: 처리할 로직 작성
                            Log.e("보낸다 ========",response.body()+"");
                            FindPatient body = response.body();
                        } else {
                            Log.d(TAG, "오류 발생 : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<FindPatient> call, Throwable t) {
                        tv_result.setText(t.getMessage());
                    }
                });
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindPatientActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
