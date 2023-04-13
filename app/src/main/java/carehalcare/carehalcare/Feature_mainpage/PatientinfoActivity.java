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

import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PatientinfoActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private TextView tv_info;

    Call <PatientInfo> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinfo);

        btn_home = (ImageButton) findViewById(R.id.btn_homeinfo);
        tv_info = (TextView) findViewById(R.id.tv_info);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.5.216:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        PInfoApi infoApi = retrofit.create(PInfoApi.class);

        call = infoApi.getDataInfo("userid1");
        call.enqueue(new Callback<PatientInfo>() {
            @Override
            public void onResponse(Call<PatientInfo> call, Response<PatientInfo> response) {
                if (response.isSuccessful()) {
                    PatientInfo patientInfo = response.body();
                    String content = "";
                    content += "이름: " + patientInfo.getPname() + "\n\n";
                    content += "생년월일: " + patientInfo.getPbirthDate() + "\n\n";
                    content += "성별: " + patientInfo.getPsex() + "\n\n";
                    content += "질환: " + patientInfo.getDisease() + "\n\n";
                    content += "담당병원: " + patientInfo.getHospital() + "\n\n";
                    content += "투약정보: " + patientInfo.getMedicine() + "\n\n";
                    content += "성격: " + patientInfo.getRemark() + "\n\n";

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