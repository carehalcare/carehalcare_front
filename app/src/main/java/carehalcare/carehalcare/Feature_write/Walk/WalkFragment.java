package carehalcare.carehalcare.Feature_write.Walk;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.Feature_write.Meal.Meal_form;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_API;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_adapter;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalkFragment extends Fragment {
    String userid,puserid;
    private ArrayList<Walk_text> walkArrayList;
    String mCurrentPhotoPath;
    Uri imageUri;
    Long ids;  //TODO ids는 삭제할 id값
    private Walk_adapter walkAdapter;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public WalkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walk_list,container,false);

        Walk_API walkApi = retrofit.create(Walk_API.class);
        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        RecyclerView mrecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_walk_list);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        mrecyclerView.setLayoutManager(mlayoutManager);

        walkArrayList = new ArrayList<>();
        walkAdapter = new Walk_adapter( walkArrayList);
        mrecyclerView.setAdapter(walkAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerView.getContext(),
//                mlayoutManager.getOrientation());
//        mrecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mrecyclerView.addItemDecoration(dividerItemDecoration);

        getwalklist();

        Button buttonInsert = (Button)view.findViewById(R.id.btn_walk_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {captureCamera(200);}
        });
        walkAdapter.setOnItemClickListener(new Walk_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Walk_text detail_walk_text = walkArrayList.get(position);
                Walk_API walk_service = retrofit.create(Walk_API.class);
                walk_service.getDataWalk(userid,puserid).enqueue(new Callback<List<Walk_ResponseDTO>>() {
                    @Override
                    public void onResponse(Call<List<Walk_ResponseDTO>> call, Response<List<Walk_ResponseDTO>> response) {
                        if (response.body() != null) {
                            List<Walk_ResponseDTO> datas = response.body();
                            if (datas != null) {
                                ids = datas.get(position).getId();
                                Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                            }
                        }}
                    @Override
                    public void onFailure(Call<List<Walk_ResponseDTO>> call, Throwable t) {
                        Log.e("통신에러","+"+t.toString());
                        Toast.makeText(getContext(), "통신에러", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.walk_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final ImageView iv_walk_detail = dialog.findViewById(R.id.iv_walk_detail);
                if (detail_walk_text.getPhotobitmap() == null){
                    if(detail_walk_text.getPhotouri()==null){
                        Glide.with(getContext()).load(detail_walk_text.getFilepath()).into(iv_walk_detail);
                    }
                    else{iv_walk_detail.setImageURI(detail_walk_text.getPhotouri());}
                } else{
                    iv_walk_detail.setImageBitmap(detail_walk_text.getPhotobitmap());
                }

//                if (detail_walk_text.getPhotobitmap() == null){
//                    iv_walk_detail.setImageURI(detail_walk_text.getPhotouri());
//                } else{
//                    iv_walk_detail.setImageBitmap(detail_walk_text.getPhotobitmap());
//                }
                //iv_walk_detail.setImageURI(detail_walk_text.getPhotouri());

                final Button btn_walk_detail = dialog.findViewById(R.id.btn_walk_detail);
                btn_walk_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final  Button btn_walk_delete = dialog.findViewById(R.id.btn_walk_detail_delete);
                btn_walk_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        walkApi.deleteDataWalk(detail_walk_text.getId()).enqueue(new Callback<Void>() {
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
                                        walkArrayList.remove(position);
                                        walkAdapter.notifyItemRemoved(position);
                                        walkAdapter.notifyDataSetChanged();
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
    public void getwalklist(){
        Walk_API walk_service = retrofit.create(Walk_API.class);
        walk_service.getDataWalk(userid,puserid).enqueue(new Callback<List<Walk_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Walk_ResponseDTO>> call, Response<List<Walk_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Walk_ResponseDTO> datas = response.body();
//                    String encodedString;
//                    byte[] encodeByte;
//                    Bitmap mealbitmap;
                    String filepath_;
                    String times;

                    for (int i = 0; i < datas.size(); i++) {
                        times = response.body().get(i).getCreatedDateTime();
                        filepath_ = response.body().get(i).getImages().get(0).getFilePath();

//                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();
//                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;

                        Walk_text dict_0 = new Walk_text(filepath_,
                                response.body().get(i).getId(),times,"path");

                        walkArrayList.add( dict_0);
                        walkAdapter.notifyItemInserted(0);
                        Log.e("산책리스트 출력", "********1*************1*********!");
                    }
                    Log.e("getDatawalk end", "======================================");

                }}
            @Override
            public void onFailure(Call<List<Walk_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getContext(), "통신에러", Toast.LENGTH_SHORT).show();
            }
        });
    }
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
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                    Uri providerURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                   if (using==200){
                        Intent walkintent = new Intent(getActivity(), Walk_form.class);
                        walkintent.putExtra("uri", providerURI);
                        walkintent.putExtra("userid",userid);
                        walkintent.putExtra("puserid",puserid);
                        walkResultDetail.launch(walkintent);
                    }
                    activityResultPicture.launch(takePictureIntent);                }
            }
        } else {
            Toast.makeText(getContext(), "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
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
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if (result.getResultCode() == RESULT_OK){
                            Log.i("REQUEST_TAKE_PHOTO", "OK");
                            Log.i("카메라 Uri주소", imageUri.getPath());
                        }
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> walkResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){
                        Uri uris = result.getData().getParcelableExtra("uris");

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일 : HH시 MM분", Locale.getDefault());
                        String seeText = format.format(today_date);

                        Walk_text dict = new Walk_text(uris, Long.valueOf(1), "uri",seeText,"uriuri");

                        walkArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        walkAdapter.notifyItemInserted(0);
                        walkAdapter.notifyDataSetChanged();
                    }
                }
            });
}