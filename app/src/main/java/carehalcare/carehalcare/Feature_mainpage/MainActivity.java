package carehalcare.carehalcare.Feature_mainpage;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_NFC.CommuteActivity;
import carehalcare.carehalcare.Feature_login.LoginDto;
import carehalcare.carehalcare.Feature_login.SignupAPI;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private Button commute, write, info, noti;
    private ImageButton btn_setting;
    private TextView tv_noti1, tv_noti2;
    String userid, puserid;
    TextView tv_welcommsg;
    String user_name="";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private String formatDate(String dateStr) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String newDate = "";
        try {
            Date date = originalFormat.parse(dateStr);
            newDate = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid = "userid1";
        puserid="puserid1";

        tv_welcommsg = (TextView)findViewById(R.id.tv_welcomemsg);
        CaregiverAPI caregiverAPI = Retrofit_client.createService(CaregiverAPI.class,TokenUtils.getAccessToken("Access_Token"));
        //CaregiverAPI caregiverAPI = retrofit.create(CaregiverAPI.class);
        Log.e("토큰 이름",TokenUtils.getUser_Id("User_Id"));
        Log.e("토큰 토큰",TokenUtils.getAccessToken("Access_Token"));

        caregiverAPI.getCaregiverInfo(TokenUtils.getUser_Id("User_Id")).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        user_name = response.body().getUsername();
                        Log.e("토큰받은 이름 ",user_name);
                        tv_welcommsg.setText(user_name+" 간병인님\n환영합니다.");

                    } else{
                        Log.e("토큰받아오기 실패 ","user_name");
                    }
                }else{
                    Log.e("실패" ,"연결이 안되었습니다");
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신실패.", Toast.LENGTH_SHORT).show();
                Log.e("통신실패",t.toString());
                return;
            }
        });

        tv_noti1 = (TextView)findViewById(R.id.tv_noti1);
        tv_noti2 = (TextView)findViewById(R.id.tv_noti2);
        commute = (Button) findViewById(R.id.menu1);
        write = (Button) findViewById(R.id.menu2);
        info = (Button) findViewById(R.id.menu3);
        noti = (Button) findViewById(R.id.menu4);
        btn_setting = (ImageButton) findViewById(R.id.btn_setting);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.URL)
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        NoticeApi noticeApi = retrofit.create(NoticeApi.class);

        Call<List<Notice>> call = noticeApi.getNotice("userid1");

        call.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful()) {

                    List<Notice> notices = response.body();
                    if (notices.size() > 0) {
                        tv_noti1.setText(formatDate(notices.get(0).getCreatedDate()) + "\n" + notices.get(0).getContent());
                    }
                    if (notices.size() > 1) {
                        tv_noti2.setText(formatDate(notices.get(1).getCreatedDate()) + "\n" + notices.get(1).getContent());
                    }
                }
                else {
                    Log.d(TAG, "연결 실패 : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
                tv_noti1.setText(t.getMessage());
            }
        });


        commute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommuteActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("puserid",puserid);
                startActivity(intent);
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EightMenuActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("puserid",puserid);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PatientinfoActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("puserid",puserid);
                startActivity(intent);
            }
        });

        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PNoticeActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("puserid",puserid);
                startActivity(intent);
            }
        });

        //일단 메인에서 설정 아이콘에서 연결시켜서 test, 로그인 api 완료시 로그인 id의 매칭되는 pid없을 경우 findpatient로 이동하게끔 변경
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindPatientActivity.class);
                startActivity(intent);
            }
        });
    }
}
