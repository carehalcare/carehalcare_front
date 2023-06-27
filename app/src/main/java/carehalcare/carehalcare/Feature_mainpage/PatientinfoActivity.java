package carehalcare.carehalcare.Feature_mainpage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

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

public class PatientinfoActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private TextView tv_info,tv_info_name;

    Call <PatientInfo> call;
    String userid, puserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinfo);

        btn_home = (ImageButton) findViewById(R.id.btn_homeinfo);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_info_name= (TextView)findViewById(R.id.tv_info_name);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        puserid = intent.getStringExtra("puserid");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        PInfoApi infoApi = retrofit.create(PInfoApi.class);
        infoApi = Retrofit_client.createService(PInfoApi.class, TokenUtils.getAccessToken("Access_Token"));


        call = infoApi.getDataInfo(TokenUtils.getPUser_Id("PUser_Id"));
        call.enqueue(new Callback<PatientInfo>() {
            @Override
            public void onResponse(Call<PatientInfo> call, Response<PatientInfo> response) {
                if (response.isSuccessful()) {
                    PatientInfo patientInfo = response.body();

                    String content = "";
//                    content += "이름: " + patientInfo.getPname() + "\n\n";
//
                    //생년월일 출력 방식 변경 0000-00-00 -> 0000년 00월 00일
                    String birthDate = patientInfo.getPbirthDate();
                    String year = birthDate.substring(0, 4);
                    String month = birthDate.substring(4, 6);
                    String day = birthDate.substring(6, 8);
                    String formattedBirthDate = year + "년 " + month + "월 " + day + "일";
                    content += "생년월일     " + formattedBirthDate + "\n\n";

                    //성별 출력 방식 변경 F, M -> 여성 남성
                    String gender = patientInfo.getPsex();
                    //String gender = "";
                    if(gender.equals("F")){
                        gender = "(여)";
                    } else if(gender.equals("M")){
                        gender = "(남)";
                    } else{
                        gender = "(성별 정보 없음)";
                    }
//                    content += "성별: " + gender + "\n\n";
                    content += "질       환     " + patientInfo.getDisease() + "\n\n";
                    content += "담당병원     " + patientInfo.getHospital() + "\n\n";
                    content += "투약정보     " + patientInfo.getMedicine() + "\n\n";
                    content += "성       격     " + patientInfo.getRemark() + "\n\n";
                    tv_info_name.setText(patientInfo.getPname()+gender);

                    tv_info.setText(content);
                    Log.d("연결 성공", "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PatientInfo> call, Throwable t) {
                Log.e("환자정보불러오기", "실패" );
                t.printStackTrace();
            }
        });


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientinfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}