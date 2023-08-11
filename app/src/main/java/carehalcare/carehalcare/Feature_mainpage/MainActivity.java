package carehalcare.carehalcare.Feature_mainpage;


import static android.content.ContentValues.TAG;

import static carehalcare.carehalcare.DateUtils.formatDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
import carehalcare.carehalcare.Feature_mypage.MypageActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userid = TokenUtils.getUser_Id("User_Id");
        puserid= TokenUtils.getPUser_Id("PUser_Id");



        tv_welcommsg = (TextView)findViewById(R.id.tv_welcomemsg);
        CaregiverAPI caregiverAPI = Retrofit_client.createService(CaregiverAPI.class,TokenUtils.getAccessToken("Access_Token"));
        Log.e("토큰 이름",TokenUtils.getUser_Id("User_Id"));
        Log.e("토큰 토큰",TokenUtils.getAccessToken("Access_Token"));
        PushmsgAPI pushmsgAPI = Retrofit_client.createService(PushmsgAPI.class,TokenUtils.getAccessToken("Token_Access"));

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d(" fcm token is : ", token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        pushmsgAPI.postMSG(userid, token).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if (response.isSuccessful()){
                                    Log.e("msg fcm token 연결 성공", "Status Code : " + response.code());
                                    //Log.e("msg 연결 성공", "Status Code : " + response.body().toString());
                                } else{
                                    Log.e("msg 연결 실패", "Status Code : " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Log.e("통신오류","");
                            }
                        });
                    }
                });



        caregiverAPI.getCaregiverInfo(TokenUtils.getUser_Id("User_Id")).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        user_name = response.body().getUsername();
                        Log.e("토큰받은 이름 ",user_name);
                        tv_welcommsg.setText(user_name+" 간병인님\n환영합니다.");
                        if (response.body()!=null) {
                            TokenUtils.setPUser_Id(response.body().getPuserId());
                        }
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
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.URL)
                .addConverterFactory(GsonConverterFactory.create()) //파싱등록
                .build();

        NoticeApi noticeApi = Retrofit_client.createService(NoticeApi.class,TokenUtils.getAccessToken("Access_Token"));

        Call<List<Notice>> call = noticeApi.getNotice(puserid);

        call.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if (response.isSuccessful()) {

                    List<Notice> notices = response.body();
                    for(int i =0;i<response.body().size();i++){
                        Log.d(TAG, i+"      공지사항최근 : " + response.body().get(i).getContent());
                    }

                    if (notices.size() == 1 ) {
                        tv_noti1.setText(formatDate(notices.get(0).getcreatedDateTime()) + "\n" + notices.get(0).getContent());
                        tv_noti2.setVisibility(View.INVISIBLE);
                    }
                    else if (notices.size() > 1) {
                        tv_noti1.setText(formatDate(notices.get(0).getcreatedDateTime()) + "\n" + notices.get(0).getContent());
                        tv_noti2.setText(formatDate(notices.get(1).getcreatedDateTime()) + "\n" + notices.get(1).getContent());
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

    }
    //뒤로가기 두번 클릭 시 앱 종료
    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
            finish();
        }    }

}
