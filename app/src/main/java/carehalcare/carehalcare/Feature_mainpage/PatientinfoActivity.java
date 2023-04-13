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
    private ArrayList<PatientInfo> pinfolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinfo);

        btn_home = (ImageButton) findViewById(R.id.btn_homeinfo);
        tv_info = (TextView) findViewById(R.id.tv_info);

        pinfolist = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.101.2.189:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        PInfoApi infoApi = retrofit.create(PInfoApi.class);

        Call<PatientInfo> call = infoApi.getDataInfo("userid1");
        call.enqueue(new Callback<PatientInfo>() {
             @Override
             public void onResponse(Call<PatientInfo> call, Response<PatientInfo> response) {

                 if (response.isSuccessful()) {
                     if (response.body() != null) {
                         PatientInfo patientInfo = new PatientInfo(response.body().getPname(),
                                 response.body().getPsex(), response.body().getPbirthDate(),
                                 response.body().getDisease(), response.body().getHospital(),
                                 response.body().getMedicine(), response.body().getRemark());
                         pinfolist.add(0, patientInfo);
                         tv_info.append((CharSequence) pinfolist);
                                 Log.e("userid : ", response.body().getHospital().toString());
                             }

                 }
                 else { Log.d(TAG, "Status Code : " + response.code());}
             }
             @Override
             public void onFailure(Call<PatientInfo> call, Throwable t) {
                 tv_info.setText(t.getMessage());
                 Log.v("ㅂㅂ", "에러야?");

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