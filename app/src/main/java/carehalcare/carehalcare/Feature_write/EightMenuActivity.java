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

import carehalcare.carehalcare.Feature_write.Active.Active_API;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_API;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_adapter;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_text;
import carehalcare.carehalcare.Feature_write.Clean.Clean_API;
import carehalcare.carehalcare.Feature_write.Clean.Clean_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Clean.Clean_adapter;
import carehalcare.carehalcare.Feature_write.Clean.Clean_text;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.Feature_write.Meal.Meal_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Meal.Meal_adapter;
import carehalcare.carehalcare.Feature_write.Meal.Meal_form;
import carehalcare.carehalcare.Feature_write.Meal.Meal_text;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_adapter;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.Feature_write.Active.Active_adapter;
import carehalcare.carehalcare.Feature_write.Active.Active_text;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_API;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_adapter;
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_text;
import carehalcare.carehalcare.Feature_write.Walk.Walk_API;
import carehalcare.carehalcare.Feature_write.Walk.Walk_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Walk.Walk_adapter;
import carehalcare.carehalcare.Feature_write.Walk.Walk_form;
import carehalcare.carehalcare.Feature_write.Walk.Walk_text;
import carehalcare.carehalcare.Feature_write.Wash.Wash_API;
import carehalcare.carehalcare.Feature_write.Wash.Wash_ResponseDTO;
import carehalcare.carehalcare.Feature_write.Wash.Wash_adapter;
import carehalcare.carehalcare.Feature_write.Wash.Wash_text;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EightMenuActivity extends AppCompatActivity implements Button.OnClickListener {
    Long ids;  //TODO ids는 삭제할 id값
    LoadingDialog loadingDialog;
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
        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
    }

    // 각 버튼에 맞는 함수들
    public void onMeal(View view) {
        deleteview();
        Meal_API mealapi = retrofit.create(Meal_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.meal_list,container,true);

        RecyclerView mrecyclerView= (RecyclerView) findViewById(R.id.recyclerview_meal_list);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mlayoutManager);

        mealArrayList = new ArrayList<>();
        mealAdapter = new Meal_adapter( mealArrayList);
        mrecyclerView.setAdapter(mealAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerView.getContext(),
                mlayoutManager.getOrientation());
        mrecyclerView.addItemDecoration(dividerItemDecoration);

        getmeallsit();

        Button buttonInsert = (Button)findViewById(R.id.btn_meal_insert);
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
                Meal_API meal_service = retrofit.create(Meal_API.class);
                meal_service.getDatameal("userId1","puserId1").enqueue(new Callback<List<Meal_ResponseDTO>>() {
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
                        Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
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
                    iv_meal_detail.setImageURI(detail_meal_text.getPhotouri());
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

    }

    public void onWalk(View view){
        deleteview();
        Walk_API walkApi = retrofit.create(Walk_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.walk_list,container,true);


        RecyclerView mrecyclerView= (RecyclerView) findViewById(R.id.recyclerview_walk_list);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mlayoutManager);

        walkArrayList = new ArrayList<>();
        walkAdapter = new Walk_adapter( walkArrayList);
        mrecyclerView.setAdapter(walkAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerView.getContext(),
                mlayoutManager.getOrientation());
        mrecyclerView.addItemDecoration(dividerItemDecoration);

        getwalklist();

        Button buttonInsert = (Button)findViewById(R.id.btn_walk_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {captureCamera(200);}
        });
        walkAdapter.setOnItemClickListener(new Walk_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Walk_text detail_walk_text = walkArrayList.get(position);
                Walk_API walk_service = retrofit.create(Walk_API.class);
                walk_service.getDataWalk("userId1","puserId1").enqueue(new Callback<List<Walk_ResponseDTO>>() {
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
                        Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.walk_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final ImageView iv_walk_detail = dialog.findViewById(R.id.iv_walk_detail);
                if (detail_walk_text.getPhotobitmap() == null){
                    iv_walk_detail.setImageURI(detail_walk_text.getPhotouri());
                } else{
                    iv_walk_detail.setImageBitmap(detail_walk_text.getPhotobitmap());
                }
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

    }

    public void onMedicine(View view) {
        deleteview();
        loadingDialog.show();
        Medicine_API medicineApi = retrofit.create(Medicine_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.medicine_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_medicine_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        medicineArrayList = new ArrayList<>();
        medicineAdapter = new Medicine_adapter( medicineArrayList);
        mRecyclerView.setAdapter(medicineAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        medicineApi.getDatamedicine("userId1","puserId1").enqueue(new Callback<List<Medicine_text>>() {
            @Override
            public void onResponse(Call<List<Medicine_text>> call, Response<List<Medicine_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                    List<Medicine_text> datas = response.body();
                    if (datas != null) {
                        medicineArrayList.clear();
                        for (int i = 0; i < datas.size(); i++) {
                            Medicine_text dict_0 = new Medicine_text(response.body().get(i).gettime(),
                                    response.body().get(i).getmealStatus(), response.body().get(i).getmedicine(),
                                    response.body().get(i).getUserid(),
                                    response.body().get(i).getPuserid(),response.body().get(i).getId());
                            medicineArrayList.add(dict_0);
                            medicineAdapter.notifyItemInserted(0);
                            Log.e("현재id : " + i, datas.get(i).getmedicine()+" "+datas.get(i).getId() + ""+"어댑터카운터"+medicineAdapter.getItemCount());

                        }
                        Log.e("getDatamedicine end", "======================================");
                    }
                }
                loadingDialog.dismiss();}
            }
            @Override
            public void onFailure(Call<List<Medicine_text>> call, Throwable t) {
                loadingDialog.dismiss();
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.btn_medicine_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.medicine_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final CheckBox cb_morning = dialog.findViewById(R.id.cb_morning);
                final CheckBox cb_lunch = dialog.findViewById(R.id.cb_lunch);
                final CheckBox cb_dinner = dialog.findViewById(R.id.cb_dinner);
                final CheckBox cb_empty = dialog.findViewById(R.id.cb_empty);
                final CheckBox cb_before = dialog.findViewById(R.id.cb_before);
                final CheckBox cb_after = dialog.findViewById(R.id.cb_after);

                final EditText et_medicine_name = dialog.findViewById(R.id.et_medicine_name);
                final Button btn_medicine_save = dialog.findViewById(R.id.btn_medicine_save);
                btn_medicine_save.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String morning = "";
                        String lunch = "";
                        String dinner = "";
                        String empty = "";
                        String before = "";
                        String after = "";

                        String medicine_time;
                        String medicine_state;
                        String medicine_name = et_medicine_name.getText().toString();
                        if (cb_morning.isChecked()){
                            morning = "아침 ";
                        }if (cb_lunch.isChecked()){
                            lunch = "점심 ";
                        }if (cb_dinner.isChecked()){
                            dinner = "저녁";
                        }if (cb_empty.isChecked()){
                            empty = "공복 ";
                        }if (cb_before.isChecked()){
                            before = "식전 ";
                        }if (cb_after.isChecked()){
                            after = "식후 ";
                        }
                        medicine_time = morning + lunch + dinner;
                        medicine_state = empty + before + after;

                        Medicine_text dict = new Medicine_text(medicine_time, medicine_state, medicine_name, "userId1"
                                ,"puserId1");
                        medicineArrayList.add(0, dict); //첫번째 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        medicineAdapter.notifyItemInserted(0);
                        medicineAdapter.notifyDataSetChanged();

                        medicineApi.postDatamedicine(dict).enqueue(new Callback<List<Medicine_text>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Medicine_text>> call, @NonNull Response<List<Medicine_text>> response) {
                                if (response.isSuccessful()) {
                                    List<Medicine_text> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("YMC", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Medicine_text>> call, @NonNull Throwable t) {}
                        });

                        dialog.dismiss();
                    }
                });
            }
        });
        medicineAdapter.setOnItemClickListener (new Medicine_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Medicine_text detail_medicine_text = medicineArrayList.get(position);
                medicineApi.getDatamedicine("userId1","puserId1").enqueue(new Callback<List<Medicine_text>>() {
                    @Override
                    public void onResponse(Call<List<Medicine_text>> call, Response<List<Medicine_text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Medicine_text> datas = response.body();
                                if (datas != null) {
                                    ids = (response.body().get(position).getId());
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Medicine_text>> call, Throwable t) {
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.medicine_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final TextView medicine_detail_date = dialog.findViewById(R.id.tv_medicine_detail_date);
                final TextView medicine_detail_timne = dialog.findViewById(R.id.tv_medicine_detail_timne);
                final TextView medicine_detail_state = dialog.findViewById(R.id.tv_medicine_detail_state);
                final TextView medicine_detail_name = dialog.findViewById(R.id.tv_medicine_detail_name);

                Date today_date = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                medicine_detail_date.setText(format.format(today_date));
                medicine_detail_timne.setText(detail_medicine_text.gettime());
                medicine_detail_state.setText(detail_medicine_text.getmealStatus());
                medicine_detail_name.setText(detail_medicine_text.getmedicine());

                final Button btn_medicinedetail = dialog.findViewById(R.id.btn_medicine_detail);
                final Button btn_delete_medicine = dialog.findViewById(R.id.btn_medicine__delete_detail);
                btn_delete_medicine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        medicineApi.deleteDataMedicine(ids).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (!response.isSuccessful()) {
                                                    Log.e("#지금 position : ",position+"이고 DB ID는 : " + ids);
                                                    return;
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });
                                        medicineArrayList.remove(position);
                                        medicineAdapter.notifyItemRemoved(position);
                                        medicineAdapter.notifyDataSetChanged();

                                    }
                                })
                                .setNeutralButton("취소", null)
                                .show();
                                dialog.dismiss();
                    }
                });
                btn_medicinedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }

        });

    }


    public void onActive(View view) {
        deleteview();
        loadingDialog.show();
        Active_API activeApi = retrofit.create(Active_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.active_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_active_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        activeArrayList = new ArrayList<>();

        activeAdapter = new Active_adapter( activeArrayList);
        mRecyclerView.setAdapter(activeAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        activeApi.getDataActive("userId1","puserId1").enqueue(new Callback<List<Active_text>>() {
            @Override
            public void onResponse(Call<List<Active_text>> call, Response<List<Active_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Active_text> datas = response.body();
                        if (datas != null) {
                            activeArrayList.clear();
                            for (int i = 0; i < datas.size(); i++) {
                                Active_text dict_0 = new Active_text(response.body().get(i).getId(),response.body().get(i).getUserId(),
                                        response.body().get(i).getPuserId(),response.body().get(i).getRehabilitation(),response.body().get(i).getWalkingAssistance(),
                                        response.body().get(i).getPosition());
                                activeArrayList.add(dict_0);
                                activeAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getPosition()+" "+datas.get(i).getId() + ""+"어댑터카운터"+activeAdapter.getItemCount());
                            }
                            Log.e("getActive success", "======================================");
                        }
                    }
                loadingDialog.dismiss();}
            }
            @Override
            public void onFailure(Call<List<Active_text>> call, Throwable t) {
                loadingDialog.dismiss();
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.btn_active_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.active_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final RadioButton rb_jahalyes = dialog.findViewById(R.id.rb_jahalyes);
                final RadioButton rb_jahalno = dialog.findViewById(R.id.rb_jahalno);
                final RadioButton rb_bohangyes = dialog.findViewById(R.id.rb_bohangyes);
                final RadioButton rb_bohangno = dialog.findViewById(R.id.rb_bohangno);
                final RadioButton rb_changeyes =dialog.findViewById(R.id.rb_changeyes);
                final RadioButton rb_changeno = dialog.findViewById(R.id.rb_changeno);

                final Button btn_save_active = dialog.findViewById(R.id.btn_active_save);
                btn_save_active.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String jahal = "";
                        String bohang = "";
                        String change = "";

                        if(rb_jahalyes.isChecked()){
                            jahal = "재활치료 완료";
                        }else if(rb_jahalno.isChecked()){
                            jahal = "-";
                        }

                        if(rb_bohangyes.isChecked()){
                            bohang = "보행도움 완료";
                        }else if(rb_bohangno.isChecked()){
                            bohang = "-";
                        }

                        if(rb_changeyes.isChecked()){
                            change = "체위변경 완료";
                        }else if(rb_changeno.isChecked()){
                            change="-";
                        }
                        Active_text dict = new Active_text("userId1","puserId1",jahal,bohang,change);
                        Long id = Long.valueOf(1);
                        if (activeAdapter.getItemCount()!=0)
                        {id = Long.valueOf(activeArrayList.get(0).getId()+1);}
                        Active_text dict_0 = new Active_text(id,
                                "userId1","puserId1",jahal,bohang,change);
                        activeArrayList.add(0, dict_0); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        activeAdapter.notifyItemInserted(0);
                        activeAdapter.notifyDataSetChanged();
                        activeApi.postDataActive(dict).enqueue(new Callback<List<Active_text>>() {
                            @Override
                            public void onResponse(Call<List<Active_text>> call, Response<List<Active_text>> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {
                                    List<Active_text> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("활동", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Active_text>> call, Throwable t) {
                            }
                        });
                        dialog.dismiss();
                    }
                });
            }
        });
        activeAdapter.setOnItemClickListener (new Active_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Active_text detail_active_text = activeArrayList.get(position);
                activeApi.getDataActive("userId1","puserId1").enqueue(new Callback<List<Active_text>>() {
                    @Override
                    public void onResponse(Call<List<Active_text>> call, Response<List<Active_text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Active_text> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Active_text>> call, Throwable t) {
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.active_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final TextView activedetail_jahal = dialog.findViewById(R.id.tv_activedetail_jahal);
                final TextView activedetail_bohang = dialog.findViewById(R.id.tv_activedetail_bohang);
                final TextView activedetail_change = dialog.findViewById(R.id.tv_activedetail_change);

                activedetail_jahal.setText(detail_active_text.getRehabilitation());
                activedetail_bohang.setText(detail_active_text.getWalkingAssistance());
                activedetail_change.setText(detail_active_text.getPosition());

                final Button btn_active_detail = dialog.findViewById(R.id.btn_active_detail);
                final Button btn_active_delete = dialog.findViewById(R.id.btn_active_detail_delete);
                btn_active_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        activeApi.deleteDataActive(ids).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (!response.isSuccessful()) {
                                                    return;
                                                }}
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });
                                        activeArrayList.remove(position);
                                        activeAdapter.notifyItemRemoved(position);
                                        activeAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNeutralButton("취소", null)
                                .show();
                        dialog.dismiss();
                    }
                });
                btn_active_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onSleep(View view) {
        deleteview();
        loadingDialog.show();

        Sleep_API sleepApi = retrofit.create(Sleep_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sleep_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_sleep_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        sleepArrayList = new ArrayList<>();
        sleepAdapter = new Sleep_adapter( sleepArrayList);
        mRecyclerView.setAdapter(sleepAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        sleepApi.getDataSleep("userId1","puserId1").enqueue(new Callback<List<Sleep_text>>() {
            @Override
            public void onResponse(Call<List<Sleep_text>> call, Response<List<Sleep_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Sleep_text> datas = response.body();
                        if (datas != null) {
                            sleepArrayList.clear();
                            for (int i = 0; i < datas.size(); i++) {
                                Sleep_text dict_0 = new Sleep_text(response.body().get(i).getUserId(),
                                        response.body().get(i).getPuserId(), response.body().get(i).getState(),response.body().get(i).getContent(),
                                        response.body().get(i).getId());
                                sleepArrayList.add(dict_0);
                                sleepAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getState()+" "+datas.get(i).getId() + ""+"어댑터카운터"+sleepAdapter.getItemCount());
                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }
                    loadingDialog.dismiss();}
            }
            @Override
            public void onFailure(Call<List<Sleep_text>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
                loadingDialog.dismiss();
            }


        });

        Button buttonInsert = (Button)findViewById(R.id.btn_sleep_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.sleep_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final RadioButton rb_goodsleep = dialog.findViewById(R.id.rb_goodsleep);
                final RadioButton rb_sososleep = dialog.findViewById(R.id.rb_sososleep);
                final RadioButton rb_badsleep = dialog.findViewById(R.id.rb_badsleep);

                final EditText et_sleepForm = dialog.findViewById(R.id.et_sleepForm);

                final Button btn_sleep_save = dialog.findViewById(R.id.btn_sleep_save);
                btn_sleep_save.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String sleepstate = "";
                        String sleepForm = et_sleepForm.getText().toString();
                        String sleepTodayResult = "";

                        if (rb_goodsleep.isChecked()){
                            sleepstate = "잘주무심";
                        } else if (rb_sososleep.isChecked()){
                            sleepstate = "보통";
                        } else if (rb_badsleep.isChecked()){
                            sleepstate = "잘못주무심";
                        }
                        if (sleepForm.length()==0){sleepForm = "-";};
                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        sleepTodayResult = format.format(today_date)+" 기록확인하기";

                        Sleep_text dict = new Sleep_text("userId1","puserId1",
                                sleepstate, sleepForm);
                        sleepArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        sleepAdapter.notifyItemInserted(0);
                        sleepAdapter.notifyDataSetChanged();
                        sleepApi.postDataSleep(dict).enqueue(new Callback<List<Sleep_text>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Sleep_text>> call, @NonNull Response<List<Sleep_text>> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {
                                    List<Sleep_text> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("WAHTSLEEP", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Sleep_text>> call, @NonNull Throwable t) {}
                        });

                        dialog.dismiss();
                    }
                });
            }
        });

        sleepAdapter.setOnItemClickListener (new Sleep_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Sleep_text detail_sleep_text = sleepArrayList.get(position);

                sleepApi.getDataSleep("userId1","puserId1").enqueue(new Callback<List<Sleep_text>>() {
                    @Override
                    public void onResponse(Call<List<Sleep_text>> call, Response<List<Sleep_text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Sleep_text> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Sleep_text>> call, Throwable t) {
                        Log.e("getSleep fail", "======================================");
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.sleep_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final TextView tv_sleepdetail_state = dialog.findViewById(R.id.tv_sleepdetail_state);
                final TextView tv_sleepdetail_et = dialog.findViewById(R.id.tv_sleepdetail_et);

                tv_sleepdetail_state.setText(detail_sleep_text.getState());
                tv_sleepdetail_et.setText(detail_sleep_text.getContent());

                final Button btn_sleep_detail = dialog.findViewById(R.id.btn_sleep_detail);
                final Button btn_sleep_delete = dialog.findViewById(R.id.btn_sleep_delete_detail);
                btn_sleep_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sleepApi.deleteDataSleep(ids).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (!response.isSuccessful()) {return;}
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {

                                            }
                                        });
                                        sleepArrayList.remove(position);
                                        sleepAdapter.notifyItemRemoved(position);
                                        sleepAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNeutralButton("취소", null)
                                .show();
                        dialog.dismiss();
                    }
                });
                btn_sleep_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
    public void onToilet(View view) {
        deleteview();
        loadingDialog.show();

        Bowel_API bowelApi = retrofit.create(Bowel_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bowel_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_bowel_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        bowelArrayList = new ArrayList<>();
        bowelAdapter = new Bowel_adapter( bowelArrayList);
        mRecyclerView.setAdapter(bowelAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        bowelApi.getDataBowel("userId1","puserId1").enqueue(new Callback<List<Bowel_text>>() {
            @Override
            public void onResponse(Call<List<Bowel_text>> call, Response<List<Bowel_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Bowel_text> datas = response.body();
                        if (datas != null) {
                            bowelArrayList.clear();
                            for (int i = 0; i < datas.size(); i++) {
                                Bowel_text dict_0 = new Bowel_text(response.body().get(i).getUserId(),
                                        response.body().get(i).getPuserId(),
                                        response.body().get(i).getId(),
                                        response.body().get(i).getCount(),response.body().get(i).getContent());
                                bowelArrayList.add(dict_0);
                                bowelAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getCount()+" "+datas.get(i).getId() + ""+"어댑터카운터"+bowelAdapter.getItemCount());
                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }loadingDialog.dismiss();}
            }
            @Override
            public void onFailure(Call<List<Bowel_text>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
                loadingDialog.dismiss();
            }
        });
        Button buttonInsert = (Button)findViewById(R.id.btn_bowel_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.bowel_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final EditText et_bowelCount = dialog.findViewById(R.id.et_bowelCount);
                final EditText et_bowelForm = dialog.findViewById(R.id.et_bowelForm);

                final Button btn_bowel_save = dialog.findViewById(R.id.btn_bowel_save);
                btn_bowel_save.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Long bowelcount = Long.valueOf(et_bowelCount.getText().toString());

                        String bowelForm = et_bowelForm.getText().toString();

                        if (bowelForm.length()==0){bowelForm = "-";};

                        Bowel_text dict = new Bowel_text("userId1","puserId1",bowelcount, bowelForm);
                        bowelArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        bowelAdapter.notifyItemInserted(0);
                        bowelAdapter.notifyDataSetChanged();
                        bowelApi.postDataBowel(dict).enqueue(new Callback<List<Bowel_text>>() {
                            @Override
                            public void onResponse(Call<List<Bowel_text>> call, Response<List<Bowel_text>> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {
                                    List<Bowel_text> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("BoWel", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Bowel_text>> call, Throwable t) {}
                        });
                        dialog.dismiss();

                    }
                });
            }
        });

        bowelAdapter.setOnItemClickListener (new Bowel_adapter.OnItemClickListener () {
             @Override
             public void onItemClick(View v, int position) {
                 Bowel_text detail_bowel_text = bowelArrayList.get(position);

                 bowelApi.getDataBowel("userId1","puserId1").enqueue(new Callback<List<Bowel_text>>() {
                     @Override
                     public void onResponse(Call<List<Bowel_text>> call, Response<List<Bowel_text>> response) {
                         if (response.isSuccessful()) {
                             if (response.body() != null) {
                                 List<Bowel_text> datas = response.body();
                                 if (datas != null) {
                                     ids = response.body().get(position).getId();
                                     Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                 }
                             }}
                     }
                     @Override
                     public void onFailure(Call<List<Bowel_text>> call, Throwable t) {
                     }
                 });
                 AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                 View view = LayoutInflater.from(EightMenuActivity.this)
                         .inflate(R.layout.bowel_detail, null, false);
                 builder.setView(view);
                 final AlertDialog dialog = builder.create();
                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                 dialog.show();

                 final TextView boweldetail_count = dialog.findViewById(R.id.tv_boweldetail_count);
                 final TextView boweldetail_et = dialog.findViewById(R.id.tv_boweldetail_et);

                 boweldetail_count.setText(String.valueOf(detail_bowel_text.getCount()));
                 boweldetail_et.setText(detail_bowel_text.getContent());

                 final Button btn_boweldetail = dialog.findViewById(R.id.btn_boweldetail);
                 final Button btn_boweldelete = dialog.findViewById(R.id.btn_boweldetail_delete);
                 btn_boweldelete.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                         builder.setTitle("삭제하기")
                                 .setMessage("삭제하시겠습니까?")
                                 .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         bowelApi.deleteDataBowel(ids).enqueue(new Callback<Void>() {
                                             @Override
                                             public void onResponse(Call<Void> call, Response<Void> response) {
                                                 if (!response.isSuccessful()) {
                                                     return;
                                                 }}
                                             @Override
                                             public void onFailure(Call<Void> call, Throwable t) {
                                             }
                                         });
                                         bowelArrayList.remove(position);
                                         bowelAdapter.notifyItemRemoved(position);
                                         bowelAdapter.notifyDataSetChanged();
                                     }
                                 })
                                 .setNeutralButton("취소", null)
                                 .show();
                         dialog.dismiss();
                     }
                 });
                 btn_boweldetail.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         dialog.dismiss();
                     }
                 });
             }
         });

    }
    public void onWash(View view) {
        deleteview();
        loadingDialog.show();
        Wash_API washApi = retrofit.create(Wash_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wash_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_wash_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        washArrayList = new ArrayList<>();
        washAdapter = new Wash_adapter( washArrayList);
        mRecyclerView.setAdapter(washAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        washApi.getDataWash("userId1","puserId1").enqueue(new Callback<List<Wash_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Wash_ResponseDTO>> call, Response<List<Wash_ResponseDTO>> response) {
                if(response.isSuccessful()){
                    if (response.body() != null) {
                        List<Wash_ResponseDTO> datas = response.body();
                        if (datas != null) {
                            for (int i = 0; i < datas.size(); i++) {
                                String washface = "-";
                                String washmouth = "-";
                                String nailcare = "-";
                                String haircare = "-"; //세발간호
                                String bodyscrub = "-"; //세신
                                String shave = "-"; //면도

                                if ((response.body().get(i).getCleanliness()).contains("세안")){washface = "세안 완료";}
                                if ((response.body().get(i).getCleanliness()).contains("구강")){washmouth = "구강청결 완료";}
                                if ((response.body().get(i).getCleanliness()).contains("손발톱")){nailcare = "손발톱관리 완료";}
                                if ((response.body().get(i).getCleanliness()).contains("세발간호")){haircare = "세발간호 완료";}
                                if ((response.body().get(i).getCleanliness()).contains("세신")){bodyscrub = "세신 완료";}
                                if ((response.body().get(i).getCleanliness()).contains("면도")){shave = "면도 완료";}
                                Wash_text dict_0 = new Wash_text(washface,washmouth,nailcare,haircare,
                                        response.body().get(i).getPart(),bodyscrub, shave,response.body().get(i).getContent());
                                washArrayList.add(dict_0);
                                washAdapter.notifyItemInserted(0);
                                //Log.e("userid : " + i, datas.get(i).getUserid() + "");
                                Log.e("현재id : " + i, datas.get(i).getCleanliness()+" "+datas.get(i).getId() + ""+"어댑터카운터"+washAdapter.getItemCount());
                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }loadingDialog.dismiss();}
            }
            @Override
            public void onFailure(Call<List<Wash_ResponseDTO>> call, Throwable t) {
                loadingDialog.dismiss();
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.btn_wash_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.wash_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final CheckBox cb_washface = dialog.findViewById(R.id.cb_washface);
                final CheckBox cb_washmouth = dialog.findViewById(R.id.cb_washmouth);
                final CheckBox cb_nailcare = dialog.findViewById(R.id.cb_nailcare);
                final CheckBox cb_haircare = dialog.findViewById(R.id.cb_haircare);
                final CheckBox cb_bodyscrub = dialog.findViewById(R.id.cb_bodyscrub);
                final CheckBox cb_shave = dialog.findViewById(R.id.cb_shave);

                final EditText et_bodyscrub = dialog.findViewById(R.id.et_bodyscrub);
                final EditText et_washForm = dialog.findViewById(R.id.et_washForm);
                final Button btn_wash_active = dialog.findViewById(R.id.btn_wash_save);
                btn_wash_active.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String washface = "";
                        String washmouth = "";
                        String nailcare = "";
                        String haircare = "";
                        String bodyscrub = "";
                        String shave = "";

                        String bodyscrub_point = et_bodyscrub.getText().toString();
                        String washForm = et_washForm.getText().toString();

                        if (cb_washface.isChecked()){washface = "세안 완료 ";}
                        else if (cb_washface.isChecked() == false){washface = "-";}

                        if (cb_washmouth.isChecked()){washmouth = "구강청결 완료 ";}
                        else if (cb_washmouth.isChecked() == false){washmouth = "-";}

                        if (cb_nailcare.isChecked()){nailcare = "손발톱관리 완료 ";}
                        else  if (cb_nailcare.isChecked() == false){nailcare = "-";}

                        if (cb_haircare.isChecked()){haircare = "세발간호 완료 ";}
                        else  if (cb_haircare.isChecked() == false){haircare = "-";}

                        if (cb_bodyscrub.isChecked()){bodyscrub = "세신 완료 ";}
                        else  if (cb_bodyscrub.isChecked() == false){bodyscrub = "-";}

                        if (cb_shave.isChecked()){shave = "면도 완료 ";}
                        else  if (cb_shave.isChecked() == false){shave = "-";}

                        if (bodyscrub_point.length()==0){bodyscrub_point = "-";};
                        if (washForm.length()==0){washForm = "-";};

                        String cleaness = washface+washmouth+nailcare+haircare+bodyscrub+shave;
                        Wash_text dict = new Wash_text(washface,washmouth,nailcare,haircare,bodyscrub,bodyscrub_point,
                                shave,washForm);
                        washArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        washAdapter.notifyItemInserted(0);
                        washAdapter.notifyDataSetChanged();

                        Wash_ResponseDTO savedict = new Wash_ResponseDTO("userId1","puserId1",cleaness,bodyscrub_point,washForm);
                        washApi.postDataWash(savedict).enqueue(new Callback<List<Wash_ResponseDTO>>() {
                            @Override
                            public void onResponse(Call<List<Wash_ResponseDTO>> call, Response<List<Wash_ResponseDTO>> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");
                                if (response.isSuccessful()) {
                                    List<Wash_ResponseDTO> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("wash", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Wash_ResponseDTO>> call, Throwable t) {
                            }
                        });

                        dialog.dismiss();
                    }
                });
            }
        });

        washAdapter.setOnItemClickListener (new Wash_adapter.OnItemClickListener () {
             @Override
             public void onItemClick(View v, int position) {
                 Wash_text detail_wash_text = washArrayList.get(position);
                 washApi.getDataWash("userId1","puserId1").enqueue(new Callback<List<Wash_ResponseDTO>>() {
                     @Override
                     public void onResponse(Call<List<Wash_ResponseDTO>> call, Response<List<Wash_ResponseDTO>> response) {
                         if(response.isSuccessful()){
                             if (response.body() != null) {
                                 List<Wash_ResponseDTO> datas = response.body();
                                 if (datas != null) {
                                     ids = response.body().get(position).getId();
                                     Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                 }
                             }}
                     }
                     @Override
                     public void onFailure(Call<List<Wash_ResponseDTO>> call, Throwable t) {
                     }
                 });
                 AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                 View view = LayoutInflater.from(EightMenuActivity.this)
                         .inflate(R.layout.wash_detail, null, false);
                 builder.setView(view);
                 final AlertDialog dialog = builder.create();
                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                 dialog.show();

                 final TextView washdetail_face = dialog.findViewById(R.id.tv_washdetail_face);
                 final TextView washdetail_mouth = dialog.findViewById(R.id.tv_washdetail_mouth);
                 final TextView washdetail_nail = dialog.findViewById(R.id.tv_washdetail_nail);
                 final TextView washdetail_hair  = dialog.findViewById(R.id.tv_washdetail_hair);
                 final TextView washdetail_scrub  = dialog.findViewById(R.id.tv_washdetail_scrub);
                 final TextView washdetail_shave  = dialog.findViewById(R.id.tv_washdetail_shave);
                 final TextView washdetail_scrub_point  = dialog.findViewById(R.id.tv_washdetail_scrub_point);
                 final TextView washdetail_et  = dialog.findViewById(R.id.tv_washdetail_et);

                 washdetail_face.setText(detail_wash_text.getWashface());
                 washdetail_mouth.setText(detail_wash_text.getWashmouth());
                 washdetail_nail.setText(detail_wash_text.getNailcare());
                 washdetail_hair.setText(detail_wash_text.getHaircare());
                 washdetail_scrub.setText(detail_wash_text.getBodyscrub());
                 washdetail_shave.setText(detail_wash_text.getShave());
                 washdetail_scrub_point.setText(detail_wash_text.getEt_bodyscrub());
                 washdetail_et.setText(detail_wash_text.getEt_washForm());

                 final Button btn_washdetail = dialog.findViewById(R.id.btn_wash_detail);
                 final Button btn_wash_delete = dialog.findViewById(R.id.btn_wash_detail_delete);
                 btn_wash_delete.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                         builder.setTitle("삭제하기")
                                 .setMessage("삭제하시겠습니까?")
                                 .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         washApi.deleteDataWash(ids).enqueue(new Callback<Void>() {
                                             @Override
                                             public void onResponse(Call<Void> call, Response<Void> response) {
                                                 if (!response.isSuccessful()) {
                                                     return;
                                                 }
                                             }
                                             @Override
                                             public void onFailure(Call<Void> call, Throwable t) {
                                             }
                                         });
                                         washArrayList.remove(position);
                                         washAdapter.notifyItemRemoved(position);
                                         washAdapter.notifyDataSetChanged();
                                     }
                                 })
                                 .setNeutralButton("취소", null)
                                 .show();
                         dialog.dismiss();
                     }
                 });
                 btn_washdetail.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         dialog.dismiss();
                     }
                 });
             }
         });
    }
    public void onClean(View view) {
        deleteview();
        loadingDialog.show();
        Clean_API cleanApi = retrofit.create(Clean_API.class);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clean_list,container,true);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_clean_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        cleanArrayList = new ArrayList<>();
        cleanAdapter = new Clean_adapter( cleanArrayList);
        mRecyclerView.setAdapter(cleanAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        cleanApi.getDataClean("userId1","puserId1").enqueue(new Callback<List<Clean_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Clean_ResponseDTO>> call, Response<List<Clean_ResponseDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Clean_ResponseDTO> datas = response.body();
                        if (datas != null) {
                            cleanArrayList.clear();
                            for (int i = 0; i < datas.size(); i++) {
                                String changesheet = "-";
                                String changecloths = "-";
                                String ventilation = "-";

                                if ((response.body().get(i).getCleanliness()).contains("시트변경완료")){changesheet = "시트변경완료";}
                                if ((response.body().get(i).getCleanliness()).contains("환의교체완료")){changecloths = "환의교체완료";}
                                if ((response.body().get(i).getCleanliness()).contains("환기완료")){ventilation = "환기완료";}
                                Clean_text dict_0 = new Clean_text(changesheet,changecloths,ventilation,
                                        response.body().get(i).getContent()
                                       );
                                cleanArrayList.add(dict_0);
                                cleanAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getCleanliness()+" "+datas.get(i).getId() + ""+"어댑터카운터"+cleanAdapter.getItemCount());

                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }loadingDialog.dismiss();}
            }

            @Override
            public void onFailure(Call<List<Clean_ResponseDTO>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
                loadingDialog.dismiss();
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.btn_clean_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);
                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.clean_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final CheckBox cb_changeSheet = dialog.findViewById(R.id.cb_changeSheet);
                final CheckBox cb_changeCloth = dialog.findViewById(R.id.cb_changeCloth);
                final CheckBox cb_ventilation = dialog.findViewById(R.id.cb_ventilation);
                final EditText et_cleanForm = dialog.findViewById(R.id.et_cleanForm);
                final Button btn_clean_active = dialog.findViewById(R.id.btn_clean_save);
                btn_clean_active.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String changeSheet = "-";
                        String changeCloth = "-";
                        String ventilation = "-";
                        String cleanForm = et_cleanForm.getText().toString();

                        if (cb_changeSheet.isChecked()){changeSheet = "시트변경완료 ";}

                        if (cb_changeCloth.isChecked()){changeCloth = "환의교체완료 ";}

                        if (cb_ventilation.isChecked()){ventilation = "환기완료 ";}

                        String cleaness = changeSheet+" "+changeCloth+" "+ventilation;

                        if (cleanForm.length()==0){cleanForm = "-";};

                        Clean_text dict = new Clean_text(changeSheet,changeCloth,ventilation,cleanForm);
                        cleanArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        cleanAdapter.notifyItemInserted(0);
                        cleanAdapter.notifyDataSetChanged();

                        Clean_ResponseDTO savedict = new Clean_ResponseDTO("userId1","puserId1",cleaness,cleanForm);
                        cleanApi.postDataClean(savedict).enqueue(new Callback<List<Clean_ResponseDTO>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Clean_ResponseDTO>> call, @NonNull Response<List<Clean_ResponseDTO>> response) {
                                if (response.isSuccessful()) {
                                    List<Clean_ResponseDTO> body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("clean", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Clean_ResponseDTO>> call, @NonNull Throwable t) {}
                        });
                        dialog.dismiss();
                    }
                });
            }
        });

        cleanAdapter.setOnItemClickListener (new Clean_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Clean_text detail_clean_text = cleanArrayList.get(position);
                cleanApi.getDataClean("userId1","puserId1").enqueue(new Callback<List<Clean_ResponseDTO>>() {
                    @Override
                    public void onResponse(Call<List<Clean_ResponseDTO>> call, Response<List<Clean_ResponseDTO>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Clean_ResponseDTO> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Clean_ResponseDTO>> call, Throwable t) {
                        Log.e("getSleep fail", "======================================");
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.clean_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final TextView detail_sheet = dialog.findViewById(R.id.tv_cleandatail_sheet);
                final TextView detail_cloth = dialog.findViewById(R.id.tv_cleandatail_cloth);
                final TextView detail_ventilationt = dialog.findViewById(R.id.tv_cleandatail_ventilation);
                final TextView et_detail_clean  = dialog.findViewById(R.id.tv_cleandetail_et);

                detail_sheet.setText(detail_clean_text.getChangeSheet());
                detail_cloth.setText(detail_clean_text.getChangeCloth());
                detail_ventilationt.setText(detail_clean_text.getVentilation());
                et_detail_clean.setText(detail_clean_text.getEt_cleanForm());

                final Button btn_cleandetail = dialog.findViewById(R.id.btn_cleandtail);
                final Button btn_clean_delete = dialog.findViewById(R.id.btn_clean_delete_detail);
                btn_clean_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("삭제하기")
                                .setMessage("삭제하시겠습니까?")
                                .setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        cleanApi.deleteDataClean(ids).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (!response.isSuccessful()) {
                                                    return;
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });
                                        cleanArrayList.remove(position);
                                        cleanAdapter.notifyItemRemoved(position);
                                        cleanAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNeutralButton("취소", null)
                                .show();
                        dialog.dismiss();
                    }
                });
                btn_cleandetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void deleteview(){
        container.removeAllViews();
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

                        Walk_text dict = new Walk_text(uris, Long.valueOf(1), "uri");

                        walkArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        walkAdapter.notifyItemInserted(0);
                        walkAdapter.notifyDataSetChanged();
                    }
                }
            });
    ActivityResultLauncher<Intent> activityResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){
                        Uri uris = result.getData().getParcelableExtra("uris");
                        String tsa = result.getData().getStringExtra("edit");
                        Log.e("왜 안돼는건데ㄹㄹㄹㄹㄹㄹㄹ",""+uris.getPath());

                        String mealTodayResult;
                        mealTodayResult = tsa;

                        Meal_text dict = new Meal_text(uris, mealTodayResult, Long.valueOf(1), "uri");

                        mealArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        mealAdapter.notifyItemInserted(0);
                        mealAdapter.notifyDataSetChanged();
                    }
                }
            });

    private void captureCamera(int using){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    if (using == 100) {
                        Log.e("dfdjfdkfjdskfjksdjflsjfk", "whwhwhwhwhwhwhwhw");
                        Intent mealintent = new Intent(this, Meal_form.class);
                        mealintent.putExtra("uri", providerURI);
                        Log.e("whwhwhwhwhwhwhwh", "whwhwhwhwhwhwhwhw");
                        activityResultDetail.launch(mealintent);
                    }

                    else if (using==200){
                        Intent walkintent = new Intent(this, Walk_form.class);
                        walkintent.putExtra("uri", providerURI);
                        walkResultDetail.launch(walkintent);
                    }
                    activityResultPicture.launch(takePictureIntent);                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
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


    public void getmeallsit(){
        Meal_API meal_service = retrofit.create(Meal_API.class);
        loadingDialog.show();
        meal_service.getDatameal("userId1","puserId1").enqueue(new Callback<List<Meal_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Meal_ResponseDTO>> call, Response<List<Meal_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Meal_ResponseDTO> datas = response.body();
                    String encodedString;
                    byte[] encodeByte;
                    Bitmap mealbitmap;
                    for (int i = 0; i < datas.size(); i++) {

                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();

                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;

                        Meal_text dict_0 = new Meal_text(mealbitmap,
                                response.body().get(i).getContent(),response.body().get(i).getId());

                        mealArrayList.add( dict_0);
                        mealAdapter.notifyItemInserted(0);
                        Log.e("음식리스트 출력", "********1*************1*********!");
                    }
                    Log.e("getDatameal end", "======================================");

                    loadingDialog.dismiss();
                }}
            @Override
            public void onFailure(Call<List<Meal_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
    public void getwalklist(){
        Walk_API walk_service = retrofit.create(Walk_API.class);
        loadingDialog.show();
        walk_service.getDataWalk("userId1","puserId1").enqueue(new Callback<List<Walk_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Walk_ResponseDTO>> call, Response<List<Walk_ResponseDTO>> response) {
                if (response.body() != null) {
                    List<Walk_ResponseDTO> datas = response.body();
                    String encodedString;
                    byte[] encodeByte;
                    Bitmap mealbitmap;
                    for (int i = 0; i < datas.size(); i++) {
                        encodedString = response.body().get(i).getImages().get(0).getEncodedString();
                        encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                        mealbitmap = BitmapFactory.decodeByteArray( encodeByte, 0, encodeByte.length ) ;

                        Walk_text dict_0 = new Walk_text(mealbitmap,
                                response.body().get(i).getId());

                        walkArrayList.add( dict_0);
                        walkAdapter.notifyItemInserted(0);
                        Log.e("산책리스트 출력", "********1*************1*********!");
                    }
                    Log.e("getDatawalk end", "======================================");

                    loadingDialog.dismiss();
                }}
            @Override
            public void onFailure(Call<List<Walk_ResponseDTO>> call, Throwable t) {
                Log.e("통신에러","+"+t.toString());
                Toast.makeText(getApplicationContext(), "통신에러", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

}
