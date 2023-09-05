package carehalcare.carehalcare.Feature_login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class SplashActivity extends AppCompatActivity {
    String userId, passwordNo;
    String accessToken,refreshToken,grantType,puserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TokenUtils.init(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.apiURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // 자동로그인 처리
        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        userId = auto.getString("userId", null);
        passwordNo = auto.getString("passwordNo", null);

        SignupAPI signupAPI = retrofit.create(SignupAPI.class);
        CaregiverAPI caregiverAPI = Retrofit_client.createService(CaregiverAPI.class, TokenUtils.getAccessToken("Access_Token"));


        LoginDto loginDto = new LoginDto(userId,passwordNo);





        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userId != null && passwordNo != null){
                    signupAPI.postlogin(loginDto).enqueue(new Callback<TokenDto>() {
                        @Override
                        public void onResponse(Call<TokenDto> call, Response<TokenDto> response) {
                            if (response.isSuccessful()){
                                if (response.body()!=null){
                                    TokenDto tokenDto = response.body();
                                    accessToken = tokenDto.getAccessToken();
                                    refreshToken = tokenDto.getRefreshToken();
                                    grantType = tokenDto.getGrantType();
                                    TokenUtils.setAccessToken(response.body().getAccessToken());
                                    TokenUtils.setRefreshToken(response.body().getRefreshToken());
                                    TokenUtils.setUser_Id(userId);
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
                                                        Intent intent = new Intent(SplashActivity.this, FindPatientActivity.class);
                                                        intent.putExtra("puserid",puserid);
                                                        Toast.makeText(getApplicationContext(), String.format("간병인님 환영합니다."), Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                        finish();
                                                        return;
                                                    }

                                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
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
                            Toast.makeText(getApplicationContext(), "통신실패.", Toast.LENGTH_SHORT).show();
                            Log.e("통신실패",t.toString());
                            return;
                        }
                    });
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }
            }
        }, 2000);
    }
}
