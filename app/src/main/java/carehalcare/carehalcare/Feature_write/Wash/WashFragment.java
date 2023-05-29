package carehalcare.carehalcare.Feature_write.Wash;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_API;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_adapter;
import carehalcare.carehalcare.Feature_write.Medicine.Medicine_text;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WashFragment extends Fragment {
    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Wash_text> washArrayList;
    private Wash_adapter washAdapter;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Meal_API.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public WashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wash_list,container,false);
        Wash_API washApi = retrofit.create(Wash_API.class);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_wash_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        washArrayList = new ArrayList<>();
        washAdapter = new Wash_adapter( washArrayList);
        mRecyclerView.setAdapter(washAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
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
                    }}
            }
            @Override
            public void onFailure(Call<List<Wash_ResponseDTO>> call, Throwable t) {
            }
        });

        Button buttonInsert = (Button)view.findViewById(R.id.btn_wash_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = LayoutInflater.from(getContext())
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
        return view;
    }
}