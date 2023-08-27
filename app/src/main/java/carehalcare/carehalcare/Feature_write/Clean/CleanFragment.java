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

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CleanFragment extends Fragment {
    String userid,puserid;

    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Clean_text> cleanArrayList;
    private Clean_adapter cleanAdapter;

    private CheckBox cb_changeSheet, cb_changeCloth, cb_ventilation ;
    private EditText et_cleanForm;
    private TextView detail_sheet, detail_cloth, detail_ventilation, et_detail_clean ;
    private Button btn_clean_active;

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

        Clean_API cleanApi = Retrofit_client.createService(Clean_API.class, TokenUtils.getAccessToken("Access_Token"));


        // Clean_API cleanApi = retrofit.create(Clean_API.class);
        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_clean_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        cleanArrayList = new ArrayList<>();
        cleanAdapter = new Clean_adapter( cleanArrayList);
        mRecyclerView.setAdapter(cleanAdapter);


        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        cleanApi.getDataClean(userid,puserid).enqueue(new Callback<List<Clean_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Clean_ResponseDTO>> call, Response<List<Clean_ResponseDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Clean_ResponseDTO> datas = response.body();
                        if (datas != null) {
                            cleanArrayList.clear();
                            if (datas.size()==0){
                                Toast.makeText(getActivity(), "주변청결 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < datas.size(); i++) {
                                String changesheet = "-";
                                String changecloths = "-";
                                String ventilation = "-";

                                String cleandata = datas.get(i).getCleanliness();
                                String[] values = cleandata.split(" ");
                                String sheet = cleandata.substring(0,1);
                                String cloth = cleandata.substring(1,2);
                                String ven = cleandata.substring(2);

                                if (sheet.equals("Y")){changesheet = "Y";}
                                if (cloth.equals("Y")){changecloths = "Y";}
                                if (ven.equals("Y")){ventilation = "Y";}

                                Clean_text dict_0 = new Clean_text(changesheet,changecloths,ventilation,
                                        response.body().get(i).getContent(),response.body().get(i).getCreatedDateTime());
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

                        if (cb_changeSheet.isChecked()){changeSheet = "Y";}

                        if (cb_changeCloth.isChecked()){changeCloth = "Y";}

                        if (cb_ventilation.isChecked()){ventilation = "Y";}

                        String cleaness = changeSheet+changeCloth+ventilation;

                        if (cleanForm.length()==0){cleanForm = "-";};

                        Clean_text dict = new Clean_text(changeSheet,changeCloth,ventilation,cleanForm,null);
                        cleanArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        cleanAdapter.notifyItemInserted(0);
                        cleanAdapter.notifyDataSetChanged();

                        Clean_ResponseDTO savedict = new Clean_ResponseDTO(userid,puserid,cleaness,cleanForm);
                        cleanApi.postDataClean(savedict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                                if (response.isSuccessful()) {

                                } else {
                                    //실패
                                    Log.e("clean", "stringToJson msg: 실패" + response.code());
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

        cleanAdapter.setOnItemClickListener (new Clean_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Clean_text detail_clean_text = cleanArrayList.get(position);
                cleanApi.getDataClean(userid,puserid).enqueue(new Callback<List<Clean_ResponseDTO>>() {
                    @Override
                    public void onResponse(Call<List<Clean_ResponseDTO>> call, Response<List<Clean_ResponseDTO>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Clean_ResponseDTO> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    View view = LayoutInflater.from(getContext())
                                            .inflate(R.layout.clean_detail, null, false);
                                    builder.setView(view);
                                    final AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();

                                    detail_sheet = dialog.findViewById(R.id.tv_cleandatail_sheet);
                                    detail_cloth = dialog.findViewById(R.id.tv_cleandatail_cloth);
                                    detail_ventilation = dialog.findViewById(R.id.tv_cleandatail_ventilation);
                                    et_detail_clean  = dialog.findViewById(R.id.tv_cleandetail_et);

                                    String sheet = detail_clean_text.getChangeSheet();
                                    String cloth = detail_clean_text.getChangeCloth();
                                    String ven = detail_clean_text.getVentilation();

                                    cleanApi.getDataClean_2(ids).enqueue(new Callback<Clean_ResponseDTO>() {
                                        @Override
                                        public void onResponse(Call<Clean_ResponseDTO> call, Response<Clean_ResponseDTO> response) {
                                            if (response.isSuccessful()){
                                                if (response.body()!=null){
                                                    Log.e("청소청소",response.body().getCleanliness().substring(0,1)+
                                                            response.body().getCleanliness().substring(1,2)+
                                                            response.body().getCleanliness().substring(2));
                                                    if(response.body().getCleanliness().substring(0,1).contains("Y")){
                                                        detail_sheet.setText("완료");
                                                    }
                                                    if(response.body().getCleanliness().substring(1,2).contains("Y")){
                                                        detail_cloth.setText("완료");
                                                    }
                                                    if(response.body().getCleanliness().substring(2).contains("Y")){
                                                        detail_ventilation.setText("완료");
                                                    }
//                                                    if (sheet.contains("Y")) detail_sheet.setText("완료");
//                                                    if (cloth.contains("Y")) detail_cloth.setText("완료");
//                                                    if (ven.contains("Y")) detail_ventilation.setText("완료");

                                                    et_detail_clean.setText(response.body().getContent());

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Clean_ResponseDTO> call, Throwable t) {

                                        }
                                    });


                                    final Button btn_cleandetail = dialog.findViewById(R.id.btn_detail_change);
                                    final Button btn_clean_delete = dialog.findViewById(R.id.btn_clean_delete_detail);
                                    final Button btn_off = dialog.findViewById(R.id.btn_off);

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

                                    btn_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    btn_cleandetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            View cview = LayoutInflater.from(getContext())
                                                    .inflate(R.layout.clean_form_change, null, false);
                                            builder.setView(cview);
                                            final AlertDialog cdialog = builder.create();
                                            cdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            cdialog.show();

                                            cb_changeSheet = cdialog.findViewById(R.id.cb_changeSheet);
                                            cb_changeCloth = cdialog.findViewById(R.id.cb_changeCloth);
                                            cb_ventilation = cdialog.findViewById(R.id.cb_ventilation);
                                            et_cleanForm = cdialog.findViewById(R.id.et_cleanForm);
                                            btn_clean_active = cdialog.findViewById(R.id.btn_clean_change);
                                            Button btn_cancel = cdialog.findViewById(R.id.btn_cancel);

                                            String sheet = detail_clean_text.getChangeSheet();
                                            String cloth = detail_clean_text.getChangeCloth();
                                            String ventil = detail_clean_text.getVentilation();

                                            if (detail_clean_text.getEt_cleanForm().equals("-"))
                                                et_cleanForm.setText("");
                                            else et_cleanForm.setText(detail_clean_text.getEt_cleanForm());

                                            if (sheet.contains("Y")) cb_changeSheet.setChecked(true);
                                            if (cloth.contains("Y")) cb_changeCloth.setChecked(true);
                                            if (ventil.contains("Y")) cb_ventilation.setChecked(true);

                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    cdialog.dismiss();
                                                }
                                            });

                                            btn_clean_active.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    String changeSheet = "-";
                                                    String changeCloth = "-";
                                                    String ventilation = "-";
                                                    String cleanForm = et_cleanForm.getText().toString();

                                                    if (cb_changeSheet.isChecked()){changeSheet = "Y";}

                                                    if (cb_changeCloth.isChecked()){changeCloth = "Y";}

                                                    if (cb_ventilation.isChecked()){ventilation = "Y";}

                                                    String cleaness = changeSheet+changeCloth+ventilation;

                                                    if (cleanForm.length()==0){cleanForm = "-";};

                                                    Clean_text_change update = new Clean_text_change(ids,cleaness,cleanForm);
                                                    cleanApi.putDataClean(update).enqueue(new Callback<Long>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                                                            if (response.isSuccessful()) {
                                                                Long body = response.body();
                                                                if (body != null) {
                                                                    cleanAdapter.notifyDataSetChanged();
                                                                    cdialog.dismiss();
                                                                }
                                                            } else {
                                                                //실패
                                                                Log.e("clean수정 PUT", "수정 msg: 실패" + response.code());
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
                    public void onFailure(Call<List<Clean_ResponseDTO>> call, Throwable t) {
                        Log.e("getSleep fail", "======================================");
                    }
                });

            }
        });
        return view;
    }
}