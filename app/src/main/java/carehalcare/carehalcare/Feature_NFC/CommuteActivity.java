package carehalcare.carehalcare.Feature_NFC;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_mainpage.MainActivity;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommuteActivity extends AppCompatActivity {
    String userid, puserid;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    public MaterialCalendarView calendarView;
    public Button check_Btn;
    public TextView diaryTextView,tv_hello,tv_bye;
    Dialog dialog01, dialog_ornot;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);
        btn_home = (Button)findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_home);
                finish();
            }
        });
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        puserid = intent.getStringExtra("puserid");

        calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.tv_date);
        check_Btn=findViewById(R.id.btn_check);
        tv_hello=findViewById(R.id.tv_hello);
        tv_bye=findViewById(R.id.tv_bye);
        tv_hello.setVisibility(View.INVISIBLE);
        tv_bye.setVisibility(View.INVISIBLE);
        diaryTextView.setText("날짜를 선택하세요");
        diaryTextView.setTextSize(30);
        diaryTextView.setVisibility(View.VISIBLE);

        dialog01 = new Dialog(CommuteActivity.this);       // Dialog 초기화
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog01.setContentView(R.layout.dialog_nfc_newcustom);

        dialog_ornot = new Dialog(CommuteActivity.this);       // Dialog 초기화
        dialog_ornot.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog_ornot.setContentView(R.layout.dialog_nfc_ornot);


//        CommuteAPI commuteAPI = retrofit.create(CommuteAPI.class);
        CommuteAPI commuteAPI = Retrofit_client.createService(CommuteAPI.class, TokenUtils.getAccessToken("Access_Token"));
        for (int iday = 1; iday < 31;iday++){
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");

            int years = CalendarDay.today().getYear();
            int months = CalendarDay.today().getMonth();
            int days = iday;
            String dtext = years+"-"+months+"-"+iday;

            commuteAPI.getDataCommute(dtext,userid,puserid).enqueue(new Callback<List<CommuteResponseDto>>() {
                @Override
                public void onResponse(Call<List<CommuteResponseDto>> call, Response<List<CommuteResponseDto>> response) {
                    if(response.isSuccessful()){
                        if(response.body()!=null){
                            for (int i = 0; i < response.body().size(); i++) {
                                if(response.body().get(response.body().size()-1).getCategory().equals("1")){
                                    calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singletonList(
                                            CalendarDay.from(years,months,days))));}
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<CommuteResponseDto>> call, Throwable t) {
                    Log.e("실패",t.toString());
                }
            });
        }

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                for (int iday = 1; iday < 32;iday++){
                    long now = System.currentTimeMillis();

                    int years = date.getYear();
                    int months = date.getMonth();
                    int days = iday;
                    String dtext = years+"-"+months+"-"+iday;

                    commuteAPI.getDataCommute(dtext,userid,puserid).enqueue(new Callback<List<CommuteResponseDto>>() {
                        @Override
                        public void onResponse(Call<List<CommuteResponseDto>> call, Response<List<CommuteResponseDto>> response) {
                            if(response.isSuccessful()){
                                if(response.body()!=null){
                                    for (int i = 0; i < response.body().size(); i++) {
                                        if(response.body().get(response.body().size()-1).getCategory().equals("1")){
                                            calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singletonList(
                                                CalendarDay.from(years,months,days))));
                                    }

                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<List<CommuteResponseDto>> call, Throwable t) {
                            Log.e("실패",t.toString());
                        }
                    });
                }
            }
        });

        calendarView.setSelectedDate(CalendarDay.today());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tv_hello.setVisibility(View.VISIBLE);
                tv_bye.setVisibility(View.VISIBLE);
                check_Btn.setVisibility(View.VISIBLE);
                check_Btn.setBackgroundResource(R.drawable.nfc_dialog_radius);

                int year = date.getYear();
                int month = date.getMonth();
                int dayOfMonth = date.getDay();
                Log.e("출퇴근여부 : ","+"+month);

                diaryTextView.setText(String.format("%d년 %d월 %d일",year,month,dayOfMonth));
                tv_hello.setText("출근기록이 없습니다");
                tv_bye.setText("퇴근기록이 없습니다");
                check_Btn.setEnabled(true);
                check_Btn.setText("출근하기");

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");
                SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd");

                int years = Integer.parseInt(simpleDateFormatYear.format(mDate));
                int months = Integer.parseInt(simpleDateFormatMonth.format(mDate));
                int days = Integer.parseInt(simpleDateFormatDay.format(mDate));


                check_Btn.setEnabled(false);
                check_Btn.setText("기록할 수 없습니다");
                check_Btn.setBackgroundResource(R.drawable.nfc_enable_design);

                if(year==years && month==months && dayOfMonth==days){
                    check_Btn.setEnabled(true);
                    check_Btn.setText("출근하기");
                    check_Btn.setBackgroundResource(R.drawable.nfc_dialog_radius);}

                String dtext = year+"-"+month+"-"+dayOfMonth;
                Log.e("달력날짜",dtext);
                Log.e("달력날짜 변동",years+"-"+months+"-"+days);

                commuteAPI.getDataCommute(dtext,userid,puserid).enqueue(new Callback<List<CommuteResponseDto>>() {
                    @Override
                    public void onResponse(Call<List<CommuteResponseDto>> call, Response<List<CommuteResponseDto>> response) {
                        if(response.isSuccessful()){
                            if(response.body()!=null){
                                for (int i = 0; i < response.body().size(); i++) {
                                    if (response.body().get(i).getCategory().equals("0")){
                                        tv_hello.setText("출근시간 : "+response.body().get(i).getDate()+" "+response.body().get(i).getTime());
                                        check_Btn.setText("퇴근하기");

                                    } else{
                                        tv_bye.setText("퇴근시간 : "+response.body().get(i).getDate()+" "+response.body().get(i).getTime());
                                        check_Btn.setText("기록할 수 없습니다");
                                        check_Btn.setBackgroundResource(R.drawable.nfc_enable_design);
                                    }
                                    Log.e("출퇴근여부 : " + i, response.body().get(i).getCategory()+" "+response.body().get(i).getDate() +
                                            " "+response.body().get(i).getTime());
                                    if(year!=years || month!=months || dayOfMonth!=days){
                                        Log.e("달력날짜",month+"월"+dayOfMonth+"일");
                                        Log.e("달력날짜",months+"월"+days+"일");
                                        Log.e("달력날짜","ㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁ");
                                        check_Btn.setEnabled(false);
                                        check_Btn.setText("기록할 수 없습니다");
                                        check_Btn.setBackgroundResource(R.drawable.nfc_enable_design);}
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CommuteResponseDto>> call, Throwable t) {
                        Log.e("실패",t.toString());
                    }
                });
                checkDay(year,month,dayOfMonth,userid);

            }
        });

        check_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_Btn.getText().toString().equals("출근하기")) {
                    // Viewdialog alert = new Viewdialog();
                    //alert.showDialog(CommuteActivity.this);
                    showDialog01();
                } else if (check_Btn.getText().toString().equals("퇴근하기")) {
                    showDialog02();
                }
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);    // 객체 얻기
        readfromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this,0,
                new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);
    }
    public void readfromIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_TECH_DISCOVERED .equals(action)
                ||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs!=null){
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs){
        if (msgs == null || msgs.length == 0) return;
        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = "UTF-8";
        int languageCodeLegnth = payload[0] & 0063;
        try{
            text = new String(payload, languageCodeLegnth+1,
                    payload.length-languageCodeLegnth-1,textEncoding);
        } catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.toString());
        }
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getdates = sdf_date.format(date);
        String gettimes = sdf_time.format(date);
        String getTime = sdf.format(date);

        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd");

        int years = Integer.parseInt(simpleDateFormatYear.format(date));
        int months = Integer.parseInt(simpleDateFormatMonth.format(date));
        int days = Integer.parseInt(simpleDateFormatDay.format(date));
        String dtext = years+"-"+months+"-"+days;


        Log.e("nfc리더 읽기 값 : ",text);
        if(check_Btn.getText().equals("출근하기")){
            tv_hello.setText(getTime);
//            CommuteAPI commuteAPI = retrofit.create(CommuteAPI.class);
            CommuteAPI commuteAPI = Retrofit_client.createService(CommuteAPI.class, TokenUtils.getAccessToken("Access_Token"));

            CommuteSaveRequestDto commuteSaveRequestDto = new CommuteSaveRequestDto(userid,puserid,"0",
                    dtext,gettimes);
            commuteAPI.postDataCommute(commuteSaveRequestDto).enqueue(new Callback<List<CommuteResponseDto>>() {
                @Override
                public void onResponse(Call<List<CommuteResponseDto>> call, Response<List<CommuteResponseDto>> response) {
                    if(response.isSuccessful()){
                        if (response.body()!=null){
                            CommuteResponseDto datas = response.body().get(0);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<CommuteResponseDto>> call, Throwable t) {
                    Log.e("실패:",t.toString());
                }
            });
        }
        else if (check_Btn.getText().equals("퇴근하기")) {
            tv_bye.setText(getTime);
            CommuteAPI commuteAPI = Retrofit_client.createService(CommuteAPI.class, TokenUtils.getAccessToken("Access_Token"));
//            CommuteAPI commuteAPI = retrofit.create(CommuteAPI.class);
            CommuteSaveRequestDto commuteSaveRequestDto = new CommuteSaveRequestDto(userid,puserid,"1",
                    dtext,gettimes);
            commuteAPI.postDataCommute(commuteSaveRequestDto).enqueue(new Callback<List<CommuteResponseDto>>() {
                @Override
                public void onResponse(Call<List<CommuteResponseDto>> call, Response<List<CommuteResponseDto>> response) {
                    if(response.isSuccessful()){
                        if (response.body()!=null){
                            CommuteResponseDto datas = response.body().get(0);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<CommuteResponseDto>> call, Throwable t) {
                    Log.e("실패:",t.toString());
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DismissDialog01();
        if(check_Btn.getText().equals("출근하기")){
        ornotshow(intent);}
        else if (check_Btn.getText().equals("퇴근하기")) {
            ornotshow02(intent);
        }

        //setIntent(intent);
        //readfromIntent(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        WriteModeOff();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WriteModeOff();
    }
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent,writingTagFilters,null);
    }
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
    public void  checkDay(int cYear,int cMonth,int cDay,String userID){

        try{
            if(tv_hello.getText()=="출근시간"){
                diaryTextView.setVisibility(View.VISIBLE);
                tv_hello.setText("출근시간");
                tv_bye.setText("퇴근시간");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showDialog01(){
        WriteModeOn();
        dialog01.show(); // 다이얼로그 띄우기
        dialog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
        // 아니오 버튼

        Button noBtn = dialog01.findViewById(R.id.frmNo);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                dialog01.dismiss(); // 다이얼로그 닫기
            }
        });
    }
    public void showDialog02(){
        WriteModeOn();
        dialog01.show(); // 다이얼로그 띄우기
        dialog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
        // 아니오 버튼
        TextView tv_title = dialog01.findViewById(R.id.tv_title);
        tv_title.setText("퇴근을 시작합니다");

        Button noBtn = dialog01.findViewById(R.id.frmNo);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                dialog01.dismiss(); // 다이얼로그 닫기
            }
        });
    }
    public void DismissDialog01(){
        dialog01.dismiss(); // 다이얼로그 띄우기
    }
    public void ornotshow(Intent intent){
        dialog_ornot.show(); // 다이얼로그 띄우기
        dialog_ornot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
        // 아니오 버튼

        Button noBtn = dialog_ornot.findViewById(R.id.ornot_No);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                dialog_ornot.dismiss(); // 다이얼로그 닫기
            }
        });

        Button yesBtn = dialog_ornot.findViewById(R.id.ornot_yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                setIntent(intent);
                readfromIntent(intent);

                WriteModeOff();
                dialog_ornot.dismiss(); // 다이얼로그 닫기
                check_Btn.setText("퇴근하기");
            }
        });
    }
    public void ornotshow02(Intent intent){
        dialog_ornot.show(); // 다이얼로그 띄우기
        dialog_ornot.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
        // 아니오 버튼

        Button noBtn = dialog_ornot.findViewById(R.id.ornot_No);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                dialog_ornot.dismiss(); // 다이얼로그 닫기
            }
        });

        Button yesBtn = dialog_ornot.findViewById(R.id.ornot_yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                WriteModeOff();
                setIntent(intent);
                readfromIntent(intent);
                WriteModeOff();
                dialog_ornot.dismiss(); // 다이얼로그 닫기
                check_Btn.setText("출퇴근완료");
                check_Btn.setEnabled(false);
            }
        });
    }
}