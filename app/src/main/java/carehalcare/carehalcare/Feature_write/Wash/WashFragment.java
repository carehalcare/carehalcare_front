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
import carehalcare.carehalcare.Feature_write.Sleep.Sleep_API;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WashFragment extends Fragment {
    String userid,puserid;

    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Wash_text> washArrayList;
    private Wash_adapter washAdapter;
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

        Wash_API washApi = Retrofit_client.createService(Wash_API.class, TokenUtils.getAccessToken("Access_Token"));


        //Wash_API washApi = retrofit.create(Wash_API.class);
        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_wash_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        washArrayList = new ArrayList<>();
        washAdapter = new Wash_adapter( washArrayList);
        mRecyclerView.setAdapter(washAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        washApi.getDataWash(userid,puserid).enqueue(new Callback<List<Wash_ResponseDTO>>() {
            @Override
            public void onResponse(Call<List<Wash_ResponseDTO>> call, Response<List<Wash_ResponseDTO>> response) {
                if(response.isSuccessful()){
                    if (response.body() != null) {
                        List<Wash_ResponseDTO> datas = response.body();
                        if (datas != null) {
                            if (datas.size()==0){
                                Toast.makeText(getActivity(), "환자청결 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < datas.size(); i++) {
                                String washface = "-";
                                String washmouth = "-";
                                String nailcare = "-";
                                String haircare = "-"; //세발간호
                                String bodyscrub = "-"; //세신
                                String shave = "-"; //면도

                                String[] values = datas.get(i).getCleanliness().split("");

                                if (values[0].equals("Y")){washface = "Y";}
                                if (values[1].equals("Y")){washmouth = "Y";}
                                if (values[2].equals("Y")){nailcare = "Y";}
                                if (values[3].equals("Y")){haircare = "Y";}
                                if (values[4].equals("Y")){bodyscrub = "Y";}
                                if (values[5].equals("Y")){shave = "Y";}
                                Wash_text dict_0 = new Wash_text(washface,washmouth,nailcare,haircare,
                                        bodyscrub, response.body().get(i).getPart(), shave, response.body().get(i).getContent());
                                washArrayList.add(dict_0);
                                washAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, response.body().get(i).getCreatedDateTime() + ""+"어댑터카운터"+washAdapter.getItemCount());
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
                        String washface = "-";
                        String washmouth = "-";
                        String nailcare = "-";
                        String haircare = "-";
                        String bodyscrub = "-";
                        String shave = "-";

                        String bodyscrub_point = et_bodyscrub.getText().toString();
                        String washForm = et_washForm.getText().toString();

                        if (cb_washface.isChecked()){washface = "Y";}
                        else if (cb_washface.isChecked() == false){washface = "-";}

                        if (cb_washmouth.isChecked()){washmouth = "Y";}
                        else if (cb_washmouth.isChecked() == false){washmouth = "-";}

                        if (cb_nailcare.isChecked()){nailcare = "Y";}
                        else  if (cb_nailcare.isChecked() == false){nailcare = "-";}

                        if (cb_haircare.isChecked()){haircare = "Y";}
                        else  if (cb_haircare.isChecked() == false){haircare = "-";}

                        if (cb_bodyscrub.isChecked()){bodyscrub = "Y";}
                        else  if (cb_bodyscrub.isChecked() == false){bodyscrub = "-";}

                        if (cb_shave.isChecked()){shave = "Y";}
                        else  if (cb_shave.isChecked() == false){shave = "-";}

                        if (bodyscrub_point.length()==0){bodyscrub_point = "-";};
                        if (washForm.length()==0){washForm = "-";};

                        String cleaness = washface+ washmouth+nailcare+haircare+bodyscrub+shave;
                        Wash_text dict = new Wash_text(washface,washmouth,nailcare,haircare,bodyscrub,
                                shave,bodyscrub_point,washForm);
                        washArrayList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        washAdapter.notifyItemInserted(0);
                        washAdapter.notifyDataSetChanged();

                        Wash_ResponseDTO savedict = new Wash_ResponseDTO(userid,puserid,cleaness,bodyscrub_point,washForm);
                        washApi.postDataWash(savedict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");
                                if (response.isSuccessful()) {
                                    Long body = response.body();
                                    if (body != null) {
                                    }
                                } else {
                                    //실패
                                    Log.e("wash", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
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
                washApi.getDataWash(userid,puserid).enqueue(new Callback<List<Wash_ResponseDTO>>() {
                    @Override
                    public void onResponse(Call<List<Wash_ResponseDTO>> call, Response<List<Wash_ResponseDTO>> response) {
                        if(response.isSuccessful()){
                            if (response.body() != null) {
                                List<Wash_ResponseDTO> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);
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

                                    washApi.getDataWash_2(ids).enqueue(new Callback<Wash_ResponseDTO>() {
                                        @Override
                                        public void onResponse(Call<Wash_ResponseDTO> call, Response<Wash_ResponseDTO> response) {
                                            if (response.isSuccessful()){
                                                if (response.body()!=null){
                                                    String[] values = response.body().getCleanliness().split("");


                                                    if(values[0].equals("Y")) washdetail_face.setText("완료");
                                                    if (values[1].equals("Y")) washdetail_mouth.setText("완료");
                                                    if (values[2].equals("Y")) washdetail_nail.setText("완료");
                                                    if (values[3].equals("Y")) washdetail_hair.setText("완료");
                                                    if (values[4].equals("Y")) washdetail_scrub.setText("완료");
                                                    if (values[5].equals("Y"))  washdetail_shave.setText("완료");


                                                    washdetail_scrub_point.setText(response.body().getPart());
                                                    washdetail_et.setText(response.body().getContent());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Wash_ResponseDTO> call, Throwable t) {

                                        }
                                    });




                                    final Button btn_washdetail = dialog.findViewById(R.id.btn_wash_detail);
                                    final Button btn_wash_delete = dialog.findViewById(R.id.btn_wash_detail_delete);
                                    final Button btn_off = dialog.findViewById(R.id.btn_off);
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
                                    btn_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    btn_washdetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            View cview = LayoutInflater.from(getContext())
                                                    .inflate(R.layout.wash_form_change, null, false);
                                            builder.setView(cview);
                                            final AlertDialog cdialog = builder.create();
                                            cdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            cdialog.show();

                                            final CheckBox cb_washface = cdialog.findViewById(R.id.cb_washface);
                                            final CheckBox cb_washmouth = cdialog.findViewById(R.id.cb_washmouth);
                                            final CheckBox cb_nailcare = cdialog.findViewById(R.id.cb_nailcare);
                                            final CheckBox cb_haircare = cdialog.findViewById(R.id.cb_haircare);
                                            final CheckBox cb_bodyscrub = cdialog.findViewById(R.id.cb_bodyscrub);
                                            final CheckBox cb_shave = cdialog.findViewById(R.id.cb_shave);

                                            final EditText et_bodyscrub = cdialog.findViewById(R.id.et_bodyscrub);
                                            final EditText et_washForm = cdialog.findViewById(R.id.et_washForm);
                                            final Button btn_wash_active = cdialog.findViewById(R.id.btn_wash_change);
                                            final Button btn_cancel = cdialog.findViewById(R.id.btn_cancel);

                                            String form = detail_wash_text.getEt_washForm();
                                            String pointform = detail_wash_text.getEt_bodyscrub();
                                            if(form.equals("-"))    et_washForm.setText("");
                                            else et_washForm.setText(form);

                                            if (et_bodyscrub.equals("-"))    et_bodyscrub.setText("");
                                            else et_bodyscrub.setText(pointform);

                                            String face = detail_wash_text.getWashface();
                                            String mouth = detail_wash_text.getWashmouth();
                                            String nail = detail_wash_text.getNailcare();
                                            String hair = detail_wash_text.getHaircare();
                                            String body = detail_wash_text.getBodyscrub();
                                            String shave = detail_wash_text.getShave();

                                            if(face.contains("Y")) cb_washface.setChecked(true);
                                            if(mouth.contains("Y")) cb_washmouth.setChecked(true);
                                            if(nail.contains("Y")) cb_nailcare.setChecked(true);
                                            if(hair.contains("Y")) cb_haircare.setChecked(true);
                                            if(body.contains("Y")) cb_bodyscrub.setChecked(true);
                                            if(shave.contains("Y")) cb_shave.setChecked(true);

                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    cdialog.dismiss();
                                                }
                                            });
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

                                                    if (cb_washface.isChecked()){washface = "Y";}
                                                    else if (cb_washface.isChecked() == false){washface = "-";}

                                                    if (cb_washmouth.isChecked()){washmouth = "Y";}
                                                    else if (cb_washmouth.isChecked() == false){washmouth = "-";}

                                                    if (cb_nailcare.isChecked()){nailcare = "Y";}
                                                    else  if (cb_nailcare.isChecked() == false){nailcare = "-";}

                                                    if (cb_haircare.isChecked()){haircare = "Y";}
                                                    else  if (cb_haircare.isChecked() == false){haircare = "-";}

                                                    if (cb_bodyscrub.isChecked()){bodyscrub = "Y";}
                                                    else  if (cb_bodyscrub.isChecked() == false){bodyscrub = "-";}

                                                    if (cb_shave.isChecked()){shave = "Y";}
                                                    else  if (cb_shave.isChecked() == false){shave = "-";}

                                                    if (bodyscrub_point.length()==0){bodyscrub_point = "-";};
                                                    if (washForm.length()==0){washForm = "-";};

                                                    String cleaness = washface+washmouth+nailcare+haircare+bodyscrub+shave;

                                                    Wash_text_change update = new Wash_text_change(ids,cleaness,bodyscrub_point,washForm);
                                                    washApi.putDataWash(update).enqueue(new Callback<Long>() {
                                                        @Override
                                                        public void onResponse(Call<Long> call, Response<Long> response) {
                                                            if (response.isSuccessful()) {
                                                                Long body = response.body();
                                                                if (body != null) {
                                                                    Log.e("clean수정 PUT", "ok---------" + response.code());
                                                                    cdialog.dismiss();
                                                                }
                                                            } else {
                                                                //실패
                                                                Log.e("Pclean수정 PUT", "수정 msg: 실패" + response.code());
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(Call<Long> call, Throwable t) {
                                                        }
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
                    public void onFailure(Call<List<Wash_ResponseDTO>> call, Throwable t) {
                    }
                });

            }
        });
        return view;
    }
}