package carehalcare.carehalcare.Feature_mainpage;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_login.LoginActivity;
import carehalcare.carehalcare.Feature_login.SignupActivity;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindPatientActivity extends AppCompatActivity {
    AppCompatButton btn_findId, btn_ok;
    TextView tv_result,tv_findpid_welcomemsg;
    EditText et_getid;
    String puserId;
    private AlertDialog dialog;

    private boolean validate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpid);

        btn_findId = (AppCompatButton) findViewById(R.id.btn_idsearch);
        btn_ok = (AppCompatButton) findViewById(R.id.btn_ok);
        tv_result = (TextView) findViewById(R.id.tv_findresult);
        et_getid = (EditText) findViewById(R.id.et_getid);
        tv_findpid_welcomemsg = (TextView)findViewById(R.id.tv_findpid_welcomemsg);

        CaregiverAPI caregiverAPI = Retrofit_client.createService(CaregiverAPI.class,TokenUtils.getAccessToken("Access_Token"));

        caregiverAPI.getCaregiverInfo(TokenUtils.getUser_Id("User_Id")).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        String user_name = response.body().getUsername();
                        Log.e("토큰받은 이름 ",user_name);
                        tv_findpid_welcomemsg.setText(user_name+" 간병인님\n환영합니다.");}
                    else{Log.e("토큰받아오기 실패 ","user_name");}}
                else{Log.e("실패" ,"연결이 안되었습니다");}}
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신실패.", Toast.LENGTH_SHORT).show();
                Log.e("통신실패",t.toString());
                return;}});
        FindPatientApi findPatientApi = Retrofit_client.createService(FindPatientApi.class, TokenUtils.getAccessToken("Access_Token"));

        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디찾기 버튼을 누르면
                puserId = et_getid.getText().toString();

                if (validate) {
                    return; //검증 완료
                }
                if (puserId.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                    dialog = builder.setMessage("보호자 아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Call<UserDTO> call = findPatientApi.getData(puserId);
                call.enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if (response.isSuccessful()) {
                            UserDTO pdatas = response.body();
                            if (pdatas != null) {
                                String str = "";
                                String puserId = pdatas.getUserId();
                                if (pdatas.getUserId().equals(puserId)) {
                                    str = "아이디 조회 결과 " + "'" + pdatas.getUsername() + "'" + " 님이 맞습니까?";

                                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                                    dialog = builder
                                            .setMessage(str)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    
                                                    et_getid.setEnabled(false);
                                                    validate = true;
                                                    btn_findId.setEnabled(false);
                                                    btn_findId.setBackgroundColor(Color.parseColor("#808080"));
                                                }
                                            })
                                            .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    et_getid.setEnabled(true);
                                                    validate = false;
                                                    btn_findId.setEnabled(true);

                                                }
                                            })
                                            .create();
                                    dialog.show();

                                }
                            }
                        }
                        else {
                            Log.d(TAG, "노데이터 : " + response.code());
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                            dialog = builder.setMessage("일치하는 아이디가 없습니다.").setNegativeButton("확인", null).create();
                            dialog.show();
                        }
                        if (puserId == null || puserId.isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                            dialog = builder.setMessage("아이디가 입력되지 않았습니다.").setNegativeButton("확인", null).create();
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        tv_result.setText(t.getMessage());
                    }
                });

            }
        });

        //등록하기 버튼을 누르면 Post
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디 중복체크 했는지 확인
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                    dialog = builder.setMessage("보호자 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //한 칸이라도 입력 안했을 경우
                if (puserId.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPatientActivity.this);
                    dialog = builder.setMessage("보호자 아이디를 입력해주세요").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                // 새로운 PID를 서버에 보내어 등록하기
                FindPatient findPatient = new FindPatient(TokenUtils.getUser_Id("User_Id"), puserId);
                Call<Long> post = findPatientApi.addPID(findPatient);

                post.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> post, Response<Long> response) {
                        if (response.isSuccessful()) {
                            // TODO: 처리할 로직 작성
                            Log.e("보낸다 ========",response.body()+"");
                            TokenUtils.setPUser_Id(puserId);
                            Toast.makeText(getApplicationContext(), String.format("보호자 등록이 완료되었습니다"),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FindPatientActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, "오류 발생 : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        tv_result.setText(t.getMessage());
                    }
                });
            }
        });

    }
}
