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
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import carehalcare.carehalcare.R;

public class EightMenuActivity extends AppCompatActivity implements Button.OnClickListener {
    private FrameLayout container;
    private TextView tv_medicine_check;
    private EditText et_medicine_check;
    private Button btnphoto;
    private ImageView gridview_meal;
    private static final int REQUEST_CODE = 1099;
    private Button gopic;
    Bitmap bitmap;
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    String mCurrentPhotoPath;

    Uri imageUri;


    Uri photoURI;

    private ImageButton btn_meal;
    private ImageButton btn_medicine;
    private ImageButton btn_active;
    private ImageButton btn_sleep;
    private ImageButton btn_toilet;
    private ImageButton btn_wash;
    private ImageButton btn_clean;
    private Button medicine_checkbtn;

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

    private int active_count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eightmenu_main);
        container = (FrameLayout) findViewById(R.id.container_menu);


        this.InitializeView();
        checkPermission();
    }

    public void InitializeView() {


        btn_active = (ImageButton) findViewById(R.id.btn_active);
        btn_meal = (ImageButton) findViewById(R.id.btn_meal);
        btn_medicine = (ImageButton) findViewById(R.id.btn_medicine);
        btn_sleep = (ImageButton) findViewById(R.id.btn_sleep);
        btn_toilet = (ImageButton) findViewById(R.id.btn_toilet);
        btn_wash = (ImageButton) findViewById(R.id.btn_wash);
        btn_clean = (ImageButton) findViewById(R.id.btn_clean);


    }
    // 각 버튼에 맞는 함수들
    public void onMeal(View view) {
        deleteview();

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
                Meal_text detail_active_text = mealArrayList.get(position);
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

                iv_meal_detail.setImageURI(detail_active_text.getPhotouri());
                tv_meal_detail.setText(detail_active_text.getContent());

                final Button btn_meal_detail = dialog.findViewById(R.id.btn_meal_detail);
                btn_meal_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    public void onWalk(View view){
        deleteview();

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

        Button buttonInsert = (Button)findViewById(R.id.btn_walk_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {captureCamera(200);}
        });
        walkAdapter.setOnItemClickListener(new Walk_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Walk_text detail_walk_text = walkArrayList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.walk_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final ImageView iv_walk_detail = dialog.findViewById(R.id.iv_walk_detail);
                final TextView tv_walk_detail = dialog.findViewById(R.id.tv_walk_detail);

                iv_walk_detail.setImageURI(detail_walk_text.getPhotouri_walk());
                tv_walk_detail.setText(detail_walk_text.getContent_walk());

                final Button btn_walk_detail = dialog.findViewById(R.id.btn_walk_detail);
                btn_walk_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    public void onMedicine(View view) {
        deleteview();

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
                final EditText et_medicineForm = dialog.findViewById(R.id.et_medicineForm);
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
                        String medicineTodayResult;


                        String medicine_name = et_medicine_name.getText().toString();
                        String medicineForm = et_medicineForm.getText().toString();

                        if (cb_morning.isChecked()){
                            morning = "아침 ";
                        }
                        if (cb_lunch.isChecked()){
                            lunch = "점심 ";
                        }if (cb_dinner.isChecked()){
                            dinner = "저녁";
                        }if (cb_empty.isChecked()){
                            empty = "공복 ";
                        }if (cb_before.isChecked()){
                            before = "식전 ";
                        }if (cb_after.isChecked()){
                            after = "식후";
                        }

                        medicine_time = morning + lunch + dinner;
                        medicine_state = empty + before + after;

                        if (medicineForm.length()==0){medicineForm = "-";};

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        medicineTodayResult = format.format(today_date)+" 기록확인하기";

                        //cleanTodayResult = changeSheet + changeCloth + ventilation + "특이사항" + cleanForm;
                        Medicine_text dict = new Medicine_text(medicine_time, medicine_state, medicine_name, medicineForm
                                ,medicineTodayResult);
                        medicineArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        medicineAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });

        medicineAdapter.setOnItemClickListener (new Medicine_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                //String vq = String.valueOf(position);
                Medicine_text detail_medicine_text = medicineArrayList.get(position);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
                AlertDialog.Builder builder = new AlertDialog.Builder(EightMenuActivity.this);

                View view = LayoutInflater.from(EightMenuActivity.this)
                        .inflate(R.layout.medicine_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                final TextView medicine_detail_timne = dialog.findViewById(R.id.tv_medicine_detail_timne);
                final TextView medicine_detail_state = dialog.findViewById(R.id.tv_medicine_detail_state);
                final TextView medicine_detail_name = dialog.findViewById(R.id.tv_medicine_detail_name);
                final TextView medicine_detail_et  = dialog.findViewById(R.id.tv_medicine_detail_et);

                medicine_detail_timne.setText(detail_medicine_text.getMedicine_time());
                medicine_detail_state.setText(detail_medicine_text.getMedicine_state());
                medicine_detail_name.setText(detail_medicine_text.getMedicine_name());
                medicine_detail_et.setText(detail_medicine_text.getEt_medicineForm());



                final Button btn_medicinedetail = dialog.findViewById(R.id.btn_medicine_detail);
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


                // 3. 다이얼로그에 있는 삽입 버튼을 클릭하면
                btn_save_active.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String jahal = "";
                        String bohang = "";
                        String change = "";
                        String activeTodayResult = " ";

                        // 4. 사용자가 입력한 내용을 가져와서
                        if(rb_jahalyes.isChecked()){
                            jahal = "재활치료 완료";
                        }else if(rb_jahalno.isChecked()){
                            jahal = "-";
                        }
                        //
                        if(rb_bohangyes.isChecked()){
                            bohang = "보행도움 완료";
                        }else if(rb_bohangno.isChecked()){
                            bohang = "-";
                        }
                        //
                        if(rb_changeyes.isChecked()){
                            change = "체위변경 완료";
                        }else if(rb_changeno.isChecked()){
                            change="-";
                        }

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        activeTodayResult = format.format(today_date)+" 기록확인하기";

                        // 5. ArrayList에 추가하고

                        Active_text dict = new Active_text(jahal,bohang,change,activeTodayResult);
                        activeArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨


                        // 6. 어댑터에서 RecyclerView에 반영하도록 합니다.

                        activeAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });
        activeAdapter.setOnItemClickListener (new Active_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                //String vq = String.valueOf(position);
                Active_text detail_active_text = activeArrayList.get(position);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
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

                activedetail_jahal.setText(detail_active_text.getJahal());
                activedetail_bohang.setText(detail_active_text.getBohang());
                activedetail_change.setText(detail_active_text.getChange());



                final Button btn_active_detail = dialog.findViewById(R.id.btn_active_detail);
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

                        //cleanTodayResult = changeSheet + changeCloth + ventilation + "특이사항" + cleanForm;
                        Sleep_text dict = new Sleep_text(sleepstate, sleepForm, sleepTodayResult);
                        sleepArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        sleepAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });

        sleepAdapter.setOnItemClickListener (new Sleep_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                //String vq = String.valueOf(position);
                Sleep_text detail_sleep_text = sleepArrayList.get(position);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
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

                tv_sleepdetail_state.setText(detail_sleep_text.getSleepstate());
                tv_sleepdetail_et.setText(detail_sleep_text.getEt_sleepForm());



                final Button btn_sleep_detail = dialog.findViewById(R.id.btn_sleep_detail);
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
                        String bowelTodayResult = " ";

                        String bowelcount = et_bowelCount.getText().toString();

                        String bowelForm = et_bowelForm.getText().toString();

                        if (bowelForm.length()==0){bowelForm = "-";};

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        bowelTodayResult = format.format(today_date)+" 기록확인하기";

                        //cleanTodayResult = changeSheet + changeCloth + ventilation + "특이사항" + cleanForm;
                        Bowel_text dict = new Bowel_text(bowelcount, bowelForm, bowelTodayResult);
                        bowelArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        bowelAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });

        bowelAdapter.setOnItemClickListener (new Bowel_adapter.OnItemClickListener () {
             @Override
             public void onItemClick(View v, int position) {
                 //String vq = String.valueOf(position);
                 Bowel_text detail_bowel_text = bowelArrayList.get(position);
                 //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                 //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                 //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
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


                 boweldetail_count.setText(detail_bowel_text.getBowelcount());
                 boweldetail_et.setText(detail_bowel_text.getEt_bowelForm());


                 final Button btn_boweldetail = dialog.findViewById(R.id.btn_boweldetail);
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

                        String washTodayResult;

                        String bodyscrub_point = et_bodyscrub.getText().toString();
                        String washForm = et_washForm.getText().toString();

                        if (cb_washface.isChecked()){
                            washface = "세안 완료 ";
                        } else if (cb_washface.isChecked() == false){
                            washface = "-";
                        }

                        if (cb_washmouth.isChecked()){
                            washmouth = "구강청결 완료 ";
                        } else if (cb_washmouth.isChecked() == false){
                            washmouth = "-";
                        }

                        if (cb_nailcare.isChecked()){
                            nailcare = "손발톱관리 완료 ";
                        }else  if (cb_nailcare.isChecked() == false){
                            nailcare = "-";
                        }

                        if (cb_haircare.isChecked()){
                            haircare = "세발간호 완료 ";
                        }else  if (cb_haircare.isChecked() == false){
                            haircare = "-";
                        }

                        if (cb_bodyscrub.isChecked()){
                            bodyscrub = "세신 완료 ";
                        }else  if (cb_bodyscrub.isChecked() == false){
                            bodyscrub = "-";
                        }

                        if (cb_shave.isChecked()){
                            shave = "면도 완료 ";
                        }else  if (cb_shave.isChecked() == false){
                            shave = "-";
                        }
                        if (bodyscrub_point.length()==0){bodyscrub_point = "-";};
                        if (washForm.length()==0){washForm = "-";};

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        washTodayResult = format.format(today_date)+" 기록확인하기";

                        //cleanTodayResult = changeSheet + changeCloth + ventilation + "특이사항" + cleanForm;
                        Wash_text dict = new Wash_text(washface,washmouth,nailcare,haircare,bodyscrub,bodyscrub_point,
                                shave,washForm,washTodayResult);
                        washArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        washAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });

        washAdapter.setOnItemClickListener (new Wash_adapter.OnItemClickListener () {
             @Override
             public void onItemClick(View v, int position) {
                 //String vq = String.valueOf(position);
                 Wash_text detail_clean_text = washArrayList.get(position);
                 //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                 //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                 //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
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

                 washdetail_face.setText(detail_clean_text.getWashface());
                 washdetail_mouth.setText(detail_clean_text.getWashmouth());
                 washdetail_nail.setText(detail_clean_text.getNailcare());
                 washdetail_hair.setText(detail_clean_text.getHaircare());
                 washdetail_scrub.setText(detail_clean_text.getBodyscrub());
                 washdetail_shave.setText(detail_clean_text.getShave());
                 washdetail_scrub_point.setText(detail_clean_text.getEt_bodyscrub());
                 washdetail_et.setText(detail_clean_text.getEt_washForm());


                 final Button btn_washdetail = dialog.findViewById(R.id.btn_wash_detail);
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
                        String cleanTodayResult = " ";
                        String changeSheet = " ";
                        String changeCloth = " ";
                        String ventilation = " ";
                        String cleanForm = et_cleanForm.getText().toString();

                        if (cb_changeSheet.isChecked()){
                            changeSheet += "시트 변경 완료 ";
                        } else if (cb_changeSheet.isChecked() == false){
                            changeSheet += "-";
                        }

                        if (cb_changeCloth.isChecked()){
                            changeCloth += "환의 교체 완료 ";
                        } else if (cb_changeCloth.isChecked() == false){
                            changeCloth += "-";
                        }

                        if (cb_ventilation.isChecked()){
                            ventilation += "환기 완료 ";
                        }else  if (cb_ventilation.isChecked() == false){
                            ventilation += "-";
                        }

                        if (cleanForm.length()==0){cleanForm = "-";};

                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        cleanTodayResult = format.format(today_date)+" 기록확인하기";

                        //cleanTodayResult = changeSheet + changeCloth + ventilation + "특이사항" + cleanForm;
                        Clean_text dict = new Clean_text(changeSheet,changeCloth,ventilation,cleanForm,cleanTodayResult);
                        cleanArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        cleanAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
            }
        });

        cleanAdapter.setOnItemClickListener (new
                Clean_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                //String vq = String.valueOf(position);
                Clean_text detail_clean_text = cleanArrayList.get(position);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",vq);
                //Log.v("$$******************************************************************^^^^^^^^^^^^^^^^*********88",iqq.cleanTodayResult);

                //Toast.makeText (getApplicationContext(), "ll", Toast.LENGTH_SHORT).show ();
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

                        // galleryAddPic();

                        Uri uris = result.getData().getParcelableExtra("uris");
                        String tsa = result.getData().getStringExtra("edit");

                        String walkTodayResult;
                        walkTodayResult = tsa;


                        //Meal_text dict = new Meal_text(imageUri, mealTodayResult, "uri");
                        Walk_text dict = new Walk_text(uris, walkTodayResult, "uri");

                        walkArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        walkAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();


                    }
                }
            });
    ActivityResultLauncher<Intent> activityResultDetail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==2888){

                        // galleryAddPic();

                        Uri uris = result.getData().getParcelableExtra("uris");
                        String tsa = result.getData().getStringExtra("edit");

                        String mealTodayResult;
                        mealTodayResult = tsa;


                        //Meal_text dict = new Meal_text(imageUri, mealTodayResult, "uri");
                        Meal_text dict = new Meal_text(uris, mealTodayResult, "uri");

                        mealArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        mealAdapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();


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

                    if (using==100){
                        Intent intent = new Intent(this, Meal_form.class);
                        intent.putExtra("uri", providerURI);
                        activityResultDetail.launch(intent);}
                    else if (using==200){
                        Intent intent = new Intent(this, Walk_form.class);
                        intent.putExtra("uri", providerURI);
                        walkResultDetail.launch(intent);
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

        return imageFile;
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA))) {
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
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(EightMenuActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }



    @Override
    public void onClick(View view) {

    }

}
