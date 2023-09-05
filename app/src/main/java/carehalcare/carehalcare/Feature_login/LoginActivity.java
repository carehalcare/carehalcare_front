package carehalcare.carehalcare.Feature_login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_mainpage.CaregiverAPI;
import carehalcare.carehalcare.Feature_mainpage.FindPatientActivity;
import carehalcare.carehalcare.Feature_mainpage.MainActivity;
import carehalcare.carehalcare.Feature_mainpage.UserDTO;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    Button loginbtn,newuserbtn;
    private AlertDialog dialog;

    ImageButton kakaobtn;
    EditText login_id,login_pw;
    String UserEmail,UserPw,accessToken,refreshToken,grantType,puserid;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL.apiURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TokenUtils.init(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = (Button) findViewById(R.id.btn_login);
        newuserbtn = (Button) findViewById(R.id.btn_newuser);
        kakaobtn = (ImageButton) findViewById(R.id.btn_kakao);
        kakaobtn.setVisibility(View.INVISIBLE);


        login_id = (EditText)findViewById(R.id.loginidInput);
        login_pw = (EditText)findViewById(R.id.loginpw);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = login_id.getText().toString();
                String userPwd = login_pw.getText().toString();
                if (userEmail.equals("") || userPwd.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
                    SignupAPI signupAPI = retrofit.create(SignupAPI.class);
                    LoginDto loginDto = new LoginDto(userEmail,userPwd);
                    progressBar.setVisibility(View.VISIBLE);

                CaregiverAPI caregiverAPI = Retrofit_client.createService(CaregiverAPI.class,TokenUtils.getAccessToken("Access_Token"));


                signupAPI.postlogin(loginDto).enqueue(new Callback<TokenDto>() {
                        @Override
                        public void onResponse(Call<TokenDto> call, Response<TokenDto> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()){
                                if (response.body()!=null){
                                    TokenDto tokenDto = response.body();
                                    accessToken = tokenDto.getAccessToken();
                                    refreshToken = tokenDto.getRefreshToken();
                                    grantType = tokenDto.getGrantType();
                                    TokenUtils.setAccessToken(response.body().getAccessToken());
                                    TokenUtils.setRefreshToken(response.body().getRefreshToken());
                                    TokenUtils.setUser_Id(userEmail);
                                    caregiverAPI.getCaregiverInfo(TokenUtils.getUser_Id("User_Id")).enqueue(new Callback<UserDTO>() {
                                        @Override
                                        public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                                            if (response.isSuccessful()){
                                                if (response.body()!=null){
                                                    //Log.e("dto",response.body().getPuserId());
                                                    int code = response.body().getCode();
                                                    if (code != 0){
                                                        Toast.makeText(getApplicationContext(), String.format("보호자용 앱 ID입니다.\n보호자용앱에서" +
                                                                "로그인 해주세요."), Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    puserid = response.body().getPuserId();
                                                    if (puserid==null){
                                                        Intent intent = new Intent(LoginActivity.this, FindPatientActivity.class);
                                                        intent.putExtra("puserid",puserid);
                                                        Toast.makeText(getApplicationContext(), String.format("간병인님 환영합니다."), Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                        finish();
                                                        return;
                                                    }
                                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                                    SharedPreferences.Editor autoLoginEdit = auto.edit();
                                                    autoLoginEdit.putString("userId", userEmail);
                                                    autoLoginEdit.putString("passwordNo", userPwd);
                                                    autoLoginEdit.commit();

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    return;
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


                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "ID 혹은 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                        @Override
                        public void onFailure(Call<TokenDto> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "통신실패.", Toast.LENGTH_SHORT).show();
                            Log.e("통신실패",t.toString());
                            return;
                        }
                    });


            }
        });

        newuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        kakaobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LoginActivity.this, KakaologinActivity.class);
                //startActivity(intent);
            }
        });
    }
}