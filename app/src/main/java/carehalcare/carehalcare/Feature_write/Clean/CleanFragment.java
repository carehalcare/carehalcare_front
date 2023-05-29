package carehalcare.carehalcare.Feature_write.Clean;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
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

public class CleanFragment extends Fragment {
    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Clean_text> cleanArrayList;
    private Clean_adapter cleanAdapter;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Meal_API.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public CleanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clean_list,container,false);
        Clean_API cleanApi = retrofit.create(Clean_API.class);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_clean_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        cleanArrayList = new ArrayList<>();
        cleanAdapter = new Clean_adapter( cleanArrayList);
        mRecyclerView.setAdapter(cleanAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
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
                    }}
            }

            @Override
            public void onFailure(Call<List<Clean_ResponseDTO>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
            }
        });

        Button buttonInsert = (Button)view.findViewById(R.id.btn_clean_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = LayoutInflater.from(getContext())
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
        return view;
    }
}