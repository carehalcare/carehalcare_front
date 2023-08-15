package carehalcare.carehalcare.Feature_write.Medicine;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_write.Clean.Clean_API;
import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicineFragment extends Fragment {
    String userid,puserid;

    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Medicine_text> medicineArrayList;
    private Medicine_adapter medicineAdapter;

    public MedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medicine_list,container,false);
        Medicine_API medicineApi = Retrofit_client.createService(Medicine_API.class, TokenUtils.getAccessToken("Access_Token"));

        //Medicine_API medicineApi = retrofit.create(Medicine_API.class);

        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_medicine_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        medicineArrayList = new ArrayList<>();
        medicineAdapter = new Medicine_adapter( medicineArrayList);
        mRecyclerView.setAdapter(medicineAdapter);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
        medicineApi.getDatamedicine(userid,puserid).enqueue(new Callback<List<Medicine_text>>() {
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
                                        response.body().get(i).getPuserid(),response.body().get(i).getId(),
                                        response.body().get(i).getCreatedDateTime());
                                medicineArrayList.add(dict_0);
                                medicineAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getmedicine()+" "+datas.get(i).getId() + ""+"어댑터카운터"+medicineAdapter.getItemCount());

                            }
                            Log.e("getDatamedicine end", "======================================");
                        }
                    }
                  }
            }
            @Override
            public void onFailure(Call<List<Medicine_text>> call, Throwable t) {

            }
        });

        Button buttonInsert = (Button)view.findViewById(R.id.btn_medicine_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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

                        Medicine_text dict = new Medicine_text(medicine_time, medicine_state, medicine_name, userid
                                ,puserid);
                        medicineArrayList.add(0, dict); //첫번째 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        medicineAdapter.notifyItemInserted(0);
                        medicineAdapter.notifyDataSetChanged();

                        medicineApi.postDatamedicine(dict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                                if (response.isSuccessful()) {

                                } else {
                                    //실패
                                    Log.e("YMC", "stringToJson msg: 실패" + response.code());
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
        medicineAdapter.setOnItemClickListener (new Medicine_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Medicine_text detail_medicine_text = medicineArrayList.get(position);
                medicineApi.getDatamedicine(userid,puserid).enqueue(new Callback<List<Medicine_text>>() {
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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
        return view;
    }
}