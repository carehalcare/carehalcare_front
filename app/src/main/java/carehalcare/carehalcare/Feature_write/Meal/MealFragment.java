package carehalcare.carehalcare.Feature_write.Meal;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_write.Active.Active_API;
import carehalcare.carehalcare.Feature_write.Active.Active_adapter;
import carehalcare.carehalcare.Feature_write.Active.Active_text;
import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Walk.Walk_form;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealFragment extends Fragment {
    String userid,puserid;
    Long ids;  //TODO ids는 삭제할 id값
    String mCurrentPhotoPath;
    Uri imageUri;
    private static final int REQUEST_CODE = 1099;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int MY_PERMISSION_CAMERA2 = 1112;
    private ArrayList<Meal_text> mealArrayList;
    private Meal_adapter mealAdapter;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public MealFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_list,container,false);

        //Meal_API mealapi = retrofit.create(Meal_API.class);
        Meal_API mealapi = Retrofit_client.createService(Meal_API.class, TokenUtils.getAccessToken("Access_Token"));

        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");


        RecyclerView mrecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_meal_list);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        mrecyclerView.setLayoutManager(mlayoutManager);

        mealArrayList = new ArrayList<>();
        mealAdapter = new Meal_adapter( mealArrayList);
        mrecyclerView.setAdapter(mealAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerView.getContext(),
//                mlayoutManager.getOrientation());
//        mrecyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mrecyclerView.addItemDecoration(dividerItemDecoration);

        getmeallsit();

        Button buttonInsert = (Button)view.findViewById(R.id.btn_meal_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureCamera(100);
            }
        });
        mealAdapter.setOnItemClickListener(new Meal_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Meal_text detail_meal_text = mealArrayList.get(position);
                //Meal_API meal_service = retrofit.create(Meal_API.class);
                Meal_API meal_service = Retrofit_client.createService(Meal_API.class, TokenUtils.getAccessToken("Access_Token"));

                meal_service.getDatameal(userid,puserid).enqueue(new Callback<List<Meal_ResponseDTO>>() {
                    @Override
                    public void onResponse(Call<List<Meal_ResponseDTO>> call, Response<List<Meal_ResponseDTO>> response) {
                        if (response.body() != null) {
                            List<Meal_ResponseDTO> datas = response.body();
                            if (datas != null) {
                                ids = datas.get(position).getId();
                                Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                            }
                        }}
                    @Override
                    public void onFailure(Call<List<Meal_ResponseDTO>> call, Throwable t) {
                        Log.e("통신에러","+"+t.toString());
                        Toast.makeText(getContext(), "통신에러", Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.meal_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final ImageView iv_meal_detail = dialog.findViewById(R.id.iv_meal_detail);
                final TextView tv_meal_detail = dialog.findViewById(R.id.tv_meal_detail);
                Log.e("이미지비트맵",""+detail_meal_text.getPhotobitmap());
                Log.e("이미지uri",""+detail_meal_text.getPhotouri());

                if (detail_meal_text.getPhotobitmap() == null){
                    if(detail_meal_text.getPhotouri()==null){
                        Glide.with(getContext()).load(detail_meal_text.getFilepath()).into(iv_meal_detail);
                    }
                    else{iv_meal_detail.setImageURI(detail_meal_text.getPhotouri());}
                } else{
                    iv_meal_detail.setImageBitmap(detail_meal_text.getPhotobitmap());
                }

                tv_meal_detail.setText(detail_meal_text.getContent());

                final Button btn_meal_detail = dialog.findViewById(R.id.btn_meal_detail);
                btn_meal_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final Button btn_meal_delete = dialog.findViewById(R.id.btn_meal_detail_delete);
                btn_meal_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mealapi.deleteData(ids).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (!response.isSuccessful()) {
                                                    return;
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Log.e("dummmm....................","뭐하며여?");
                                            }
                                        });
                                        mealArrayList.remove(position);
                                        mealAdapter.notifyItemRemoved(position);
                                        mealAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNeutralButton("취소", null)
                                .show();
                        dialog.dismiss();
                    }
                });

            }
        });
        return view;
    }
    public void getmeallsit(){
//        Meal_API meal_service = retrofit.create(Meal_API.class);
        Meal_API meal_service = Retrofit_client.createService(Meal_API.class, TokenUtils.getAccessToken("Access_Token"));

        meal_service.getDatameal(userid,puserid).enqueue(new Callback<List<Meal_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Meal_ResponseDTO>> call, Response<List<Meal_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Meal_ResponseDTO> datas = response.body();
                    String encodedString;
                    byte[] encodeByte;
                    Bitmap mealbitmap;
                    String filepath_;
                    String times;
                    for (int i = 0; i < datas.size(); i++) {

//                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();
//                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;
                        times = response.body().get(i).getCreatedDateTime();
                        filepath_ = response.body().get(i).getImages().get(0).getFilePath();
                        Meal_text dict_0 = new Meal_text(filepath_,
                                response.body().get(i).getContent(),response.body().get(i).getId(),times,"path");
//                        Meal_text dict_0 = new Meal_text(mealbitmap,
//                                response.body().get(i).getContent(),response.body().get(i).getId());

                        mealArrayList.add( dict_0);
                        mealAdapter.notifyItemInserted(0);
                        Log.e("음식리스트 날짜 출력", ""+response.body().get(i).getCreatedDateTime());
                    }
                    Log.e("getDatameal end", "======================================");

                }}
            @Override
            public void onFailure(Call<List<Meal_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getContext(), "통신에러", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "yeeun");
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.e("현재 절대경로는 : ",""+mCurrentPhotoPath);
        return imageFile;
    }
    Uri providerURI;
    private void captureCamera(int using){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    providerURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photoFile);
                    imageUri = providerURI;
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    if (using == 100) {

                    }
                    activityResultPicture.launch(takePictureIntent);

                }
            }
        } else {
            Toast.makeText(getContext(), "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if (result.getResultCode() == RESULT_OK){
                            Intent mealintent = new Intent(getActivity(), Meal_form.class);
                            mealintent.putExtra("uri", providerURI);
                            mealintent.putExtra("userid",userid);
                            mealintent.putExtra("puserid",puserid);
                            activityResultDetail.launch(mealintent);
                            Log.i("REQUEST_TAKE_PHOTO", "OK");
                            Log.i("카메라 Uri주소", imageUri.getPath());
                        }
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> activityResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){
                        Uri uris = result.getData().getParcelableExtra("uris");
                        String tsa = result.getData().getStringExtra("edit");

                        String mealTodayResult;
                        mealTodayResult = tsa;
                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.getDefault());
                        String seeText = format.format(today_date);

                        Meal_text dict = new Meal_text(uris, mealTodayResult, Long.valueOf(1), "uri",seeText,"uriru");

                        mealArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        mealAdapter.notifyItemInserted(0);
                        mealAdapter.notifyDataSetChanged();
                    }
                }
            });
}