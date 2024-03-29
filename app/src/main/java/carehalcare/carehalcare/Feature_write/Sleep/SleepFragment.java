package carehalcare.carehalcare.Feature_write.Sleep;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_adapter;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SleepFragment extends Fragment {
    String userid,puserid;

    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Sleep_text> sleepArrayList;
    private Sleep_adapter sleepAdapter;

    private TextView tv_sleepdetail_state, tv_sleepdetail_et;
    private Button btn_sleep_detail, btn_sleep_delete, btn_off;
    private RadioButton rb_goodsleep, rb_sososleep, rb_badsleep;
    private EditText et_sleepForm;

    public SleepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_list,container,false);

        Sleep_API sleepApi = Retrofit_client.createService(Sleep_API.class, TokenUtils.getAccessToken("Access_Token"));

        //Sleep_API sleepApi = retrofit.create(Sleep_API.class);

        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_sleep_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        sleepArrayList = new ArrayList<>();
        sleepAdapter = new Sleep_adapter( sleepArrayList);
        mRecyclerView.setAdapter(sleepAdapter);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        sleepApi.getDataSleep(userid,puserid).enqueue(new Callback<List<Sleep_text>>() {
            @Override
            public void onResponse(Call<List<Sleep_text>> call, Response<List<Sleep_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Sleep_text> datas = response.body();
                        if (datas != null) {
                            sleepArrayList.clear();
                            if (datas.size()==0){
                                Toast.makeText(getActivity(), "수면 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < datas.size(); i++) {
                                Sleep_text dict_0 = new Sleep_text(response.body().get(i).getUserId(),
                                        response.body().get(i).getPuserId(), response.body().get(i).getState(),response.body().get(i).getContent(),
                                        response.body().get(i).getId(),
                                        response.body().get(i).getCreatedDateTime());
                                sleepArrayList.add(dict_0);
                                sleepAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getState()+" "+datas.get(i).getId() + ""+"어댑터카운터"+sleepAdapter.getItemCount());
                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Sleep_text>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
            }


        });

        Button buttonInsert = (Button)view.findViewById(R.id.btn_sleep_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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
                            sleepstate = "좋음";
                        } else if (rb_sososleep.isChecked()){
                            sleepstate = "보통";
                        } else if (rb_badsleep.isChecked()){
                            sleepstate = "나쁨";
                        }
                        if (sleepForm.length()==0){sleepForm = "-";};
                        Date today_date = Calendar.getInstance().getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일", Locale.getDefault());
                        sleepTodayResult = format.format(today_date)+" 기록확인하기";

                        Sleep_text dict = new Sleep_text(userid,puserid,
                                sleepstate, sleepForm);
                        sleepArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        sleepAdapter.notifyItemInserted(0);
                        sleepAdapter.notifyDataSetChanged();
                        sleepApi.postDataSleep(dict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {

                                } else {
                                    //실패
                                    Log.e("WAHTSLEEP", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {}
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

                sleepApi.getDataSleep(userid,puserid).enqueue(new Callback<List<Sleep_text>>() {
                    @Override
                    public void onResponse(Call<List<Sleep_text>> call, Response<List<Sleep_text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Sleep_text> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    View view = LayoutInflater.from(getContext())
                                            .inflate(R.layout.sleep_detail, null, false);
                                    builder.setView(view);
                                    final AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();

                                    tv_sleepdetail_state = dialog.findViewById(R.id.tv_sleepdetail_state);
                                    tv_sleepdetail_et = dialog.findViewById(R.id.tv_sleepdetail_et);

                                    sleepApi.getDataSleep_2(ids).enqueue(new Callback<Sleep_text>() {
                                        @Override
                                        public void onResponse(Call<Sleep_text> call, Response<Sleep_text> response) {
                                            if (response.isSuccessful()){
                                                if (response.body()!=null){
                                                    tv_sleepdetail_state.setText(response.body().getState());
                                                    tv_sleepdetail_et.setText(response.body().getContent());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Sleep_text> call, Throwable t) {

                                        }
                                    });



                                    btn_sleep_detail = dialog.findViewById(R.id.btn_sleep_detail);
                                    btn_sleep_delete = dialog.findViewById(R.id.btn_sleep_delete_detail);
                                    btn_off = dialog.findViewById(R.id.btn_off);
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
                                    btn_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    btn_sleep_detail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            View cview = LayoutInflater.from(getContext())
                                                    .inflate(R.layout.sleep_form_change, null, false);
                                            builder.setView(cview);
                                            final AlertDialog cdialog = builder.create();
                                            cdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            cdialog.show();

                                            rb_goodsleep = cdialog.findViewById(R.id.rb_goodsleep);
                                            rb_sososleep = cdialog.findViewById(R.id.rb_sososleep);
                                            rb_badsleep = cdialog.findViewById(R.id.rb_badsleep);
                                            et_sleepForm = cdialog.findViewById(R.id.et_sleepForm);

                                            final Button btn_change = cdialog.findViewById(R.id.btn_sleep_change);
                                            final Button btn_cancel = cdialog.findViewById(R.id.btn_cancel);

                                            String state = detail_sleep_text.getState();
                                            String form = detail_sleep_text.getContent();

                                            if (state.contains("좋음")) rb_goodsleep.setChecked(true);
                                            if (state.contains("보통")) rb_sososleep.setChecked(true);
                                            if (state.contains("나쁨")) rb_badsleep.setChecked(true);
                                            et_sleepForm.setText(form);

                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    cdialog.dismiss();
                                                }
                                            });
                                            btn_change.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    String sstate = "";
                                                    String scontent = et_sleepForm.getText().toString();

                                                    if (rb_goodsleep.isChecked()){
                                                        sstate = "좋음";
                                                    } else if (rb_sososleep.isChecked()){
                                                        sstate = "보통";
                                                    } else if (rb_badsleep.isChecked()){
                                                        sstate = "나쁨";
                                                    }
                                                    if (scontent.length()==0){scontent = "-";};

                                                    Sleep_text_change update = new Sleep_text_change(ids, sstate, scontent);
                                                    sleepApi.putDataSleep(update).enqueue(new Callback<Long>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                                                            if (response.isSuccessful()) {
                                                                Log.e("sleep 수정 ok", response.body() +"");
                                                                sleepAdapter.notifyDataSetChanged();
                                                                cdialog.dismiss();
                                                            } else {
                                                                //실패
                                                                Log.e("sleep 수정 실패", "stringToJson msg: 실패" + response.code());
                                                                Toast.makeText(getContext(), "통신 실패",  Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {}
                                                    });

                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    });
                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Sleep_text>> call, Throwable t) {
                        Log.e("getSleep fail", "======================================");
                    }
                });

            }
        });
        return view;
    }
}