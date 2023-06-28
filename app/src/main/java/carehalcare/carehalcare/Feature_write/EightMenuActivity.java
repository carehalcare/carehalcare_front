package carehalcare.carehalcare.Feature_write;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.Feature_mainpage.MainActivity;
import carehalcare.carehalcare.Feature_write.Active.ActiveFragment;
import carehalcare.carehalcare.Feature_write.Active.Active_API;
import carehalcare.carehalcare.Feature_write.Allmenu.AllmenuFragment;
import carehalcare.carehalcare.Feature_write.Bowel.BowelFragment;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_API;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_adapter;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_text;
import carehalcare.carehalcare.Feature_write.Clean.CleanFragment;
import carehalcare.carehalcare.Feature_write.Clean.Clean_API;
import carehalcare.carehalcare.Feature_write.Clean.Clean_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Clean.Clean_adapter;
import carehalcare.carehalcare.Feature_write.Clean.Clean_text;
import carehalcare.carehalcare.Feature_write.Meal.MealFragment;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.Feature_write.Meal.Meal_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Meal.Meal_adapter;
import carehalcare.carehalcare.Feature_write.Meal.Meal_form;
import carehalcare.carehalcare.Feature_write.Meal.Meal_text;
import carehalcare.carehalcare.Feature_write.Medicine.MedicineFragment;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_adapter;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.Feature_write.Active.Active_adapter;
import carehalcare.carehalcare.Feature_write.Active.Active_text;
import carehalcare.carehalcare.Feature_write.Sleep.SleepFragment;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_API;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_adapter;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import carehalcare.carehalcare.Feature_write.Walk.WalkFragment;
import carehalcare.carehalcare.Feature_write.Walk.Walk_API;
import carehalcare.carehalcare.Feature_write.Walk.Walk_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Walk.Walk_adapter;
import carehalcare.carehalcare.Feature_write.Walk.Walk_form;
import carehalcare.carehalcare.Feature_write.Walk.Walk_text;
import carehalcare.carehalcare.Feature_write.Wash.WashFragment;
import carehalcare.carehalcare.Feature_write.Wash.Wash_API;
import carehalcare.carehalcare.Feature_write.Wash.Wash_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Wash.Wash_adapter;
import carehalcare.carehalcare.Feature_write.Wash.Wash_text;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EightMenuActivity extends AppCompatActivity implements Button.OnClickListener {
    String userid, puserid;
    Long ids;  //TODO ids는 삭제할 id값
    private FrameLayout container;
    private static final int REQUEST_CODE = 1099;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int MY_PERMISSION_CAMERA2 = 1112;
    String mCurrentPhotoPath;
    Uri imageUri;
    private ArrayList<Active_text> activeArrayList;
    private ArrayList<Clean_text> cleanArrayList;
    private ArrayList<Wash_text> washArrayList;
    private ArrayList<Sleep_text> sleepArrayList;
    private ArrayList<Bowel_text> bowelArrayList;
    private ArrayList<Medicine_text> medicineArrayList;
    private Medicine_adapter medicineAdapter;
    private Active_adapter activeAdapter;
    private Clean_adapter cleanAdapter;
    private Bowel_adapter bowelAdapter;
    private Wash_adapter washAdapter;
    private Sleep_adapter sleepAdapter;
    private ArrayList<Meal_text> mealArrayList;
    private Meal_adapter mealAdapter;
    private Walk_adapter walkAdapter;
    private ArrayList<Walk_text> walkArrayList;

    Button btn_home;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Meal_API.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eightmenu_main);
        container = (FrameLayout) findViewById(R.id.container_menu);
        if (Build.VERSION.SDK_INT < 23){}
        else {
            requestUserPermission();
        }

        Intent intent = getIntent();
//        userid = intent.getStringExtra("userid");
//        puserid = intent.getStringExtra("puserid");
        userid = TokenUtils.getUser_Id("User_Id");
        puserid = TokenUtils.getPUser_Id("PUser_Id");

        deleteview();
        AllmenuFragment allmenuFragment = new AllmenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);
        btn_home = (Button)findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_home);
                finish();
            }
        });
        allmenuFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, allmenuFragment);
        transaction.commit();

    }

    // 각 버튼에 맞는 함수들
    public void onall(View view) {
        deleteview();
        AllmenuFragment allmenuFragment = new AllmenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        allmenuFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, allmenuFragment);
        transaction.commit();

    }
    public void onMeal(View view) {
        deleteview();
        MealFragment mealFragment = new MealFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        mealFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, mealFragment);
        transaction.commit();

    }

    public void onWalk(View view){
        deleteview();
        WalkFragment walkFragment = new WalkFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        walkFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, walkFragment);
        transaction.commit();

    }

    public void onMedicine(View view) {
        deleteview();
        MedicineFragment medicineFragment = new MedicineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        medicineFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, medicineFragment);
        transaction.commit();
    }


    public void onActive(View view) {
        deleteview();
        ActiveFragment activeFragment = new ActiveFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        activeFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, activeFragment);
        transaction.commit();
    }

    public void onSleep(View view) {
        deleteview();
        SleepFragment sleepFragment = new SleepFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        sleepFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, sleepFragment);
        transaction.commit();

    }
    public void onToilet(View view) {
        deleteview();
        BowelFragment bowelFragment = new BowelFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        bowelFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, bowelFragment);
        transaction.commit();

    }
    public void onWash(View view) {
        deleteview();
        WashFragment washFragment = new WashFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        washFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, washFragment);
        transaction.commit();

    }
    public void onClean(View view) {
        deleteview();
        CleanFragment cleanFragment = new CleanFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        bundle.putString("puserid",puserid);

        cleanFragment.setArguments(bundle);
        transaction.replace(R.id.container_menu, cleanFragment);
        transaction.commit();
    }

    public void deleteview(){
        container.removeAllViews();
    }








    private void requestUserPermission(){
        try {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                if (ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EightMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
                            ,MY_PERMISSION_CAMERA);
                } else {
                }
            } else{
                if (ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(EightMenuActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EightMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},MY_PERMISSION_CAMERA2);
                } else {
                }
            }
        } catch (Exception e){
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != -1){
            for (int i = 0; i < grantResults.length; i++) {
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults[i] < 0) {
                    Log.e("거부된 권한",""+permissions[i]+"       "+i+"      "+grantResults[i]);
                    // Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }}
            switch (requestCode) {
                case MY_PERMISSION_CAMERA:
                    if (grantResults.length > 0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    } else{
                        new AlertDialog.Builder(this)
                                .setTitle("알림")
                                .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                    break;
                case MY_PERMISSION_CAMERA2:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    }else {
                        checkPermission();
                    } break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        boolean writeExternalStorageRationale = ActivityCompat.shouldShowRequestPermissionRationale(EightMenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(EightMenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED &&
                writeExternalStorageRationale){
            Toast.makeText(EightMenuActivity.this, "앱을 실행하려면 퍼미션을 허가하셔야 합니다.", Toast.LENGTH_SHORT).show();
        } else if(hasWriteExternalStoragePermission == PackageManager.PERMISSION_DENIED
                && !writeExternalStorageRationale){
            new AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                    .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else if(hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED){}
    }



    @Override
    public void onClick(View view) {
    }

}
