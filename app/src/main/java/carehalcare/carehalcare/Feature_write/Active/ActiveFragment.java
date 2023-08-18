package carehalcare.carehalcare.Feature_write.Active;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.R;
import carehalcare.carehalcare.Retrofit_client;
import carehalcare.carehalcare.TokenUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActiveFragment extends Fragment {
    String userid,puserid;
    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Active_text> activeArrayList;
    private Active_adapter activeAdapter;

    private RadioButton rb_jahalyes , rb_jahalno, rb_bohangyes, rb_bohangno, rb_changeyes, rb_changeno;
    private Button btn_save_active, btn_cancel, btn_change;

    private TextView activedetail_jahal,activedetail_bohang, activedetail_change;
    public ActiveFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_list,container,false);

        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        Active_API activeApi = Retrofit_client.createService(Active_API.class, TokenUtils.getAccessToken("Access_Token"));

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_active_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        activeArrayList = new ArrayList<>();

        activeAdapter = new Active_adapter( activeArrayList);
        mRecyclerView.setAdapter(activeAdapter);

        activeApi.getDataActive(userid,puserid).enqueue(new Callback<List<Active_text>>() {
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
                                        response.body().get(i).getPosition(),response.body().get(i).getCreatedDateTime());
                                activeArrayList.add(dict_0);
                                activeAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getPosition()+" "+datas.get(i).getId() + ""+"어댑터카운터"+activeAdapter.getItemCount());
                            }
                            Log.e("getActive success", "======================================");
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Active_text>> call, Throwable t) {
            }
        });

        Button buttonInsert = (Button) view.findViewById(R.id.btn_active_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.active_form, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                rb_jahalyes = dialog.findViewById(R.id.rb_jahalyes);
                rb_jahalno = dialog.findViewById(R.id.rb_jahalno);
                rb_bohangyes = dialog.findViewById(R.id.rb_bohangyes);
                rb_bohangno = dialog.findViewById(R.id.rb_bohangno);
                rb_changeyes =dialog.findViewById(R.id.rb_changeyes);
                rb_changeno = dialog.findViewById(R.id.rb_changeno);

                btn_save_active = dialog.findViewById(R.id.btn_active_save);
                btn_save_active.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String jahal = rb_jahalyes.isChecked() ? "Y" : "N";
                        String bohang = rb_bohangyes.isChecked() ? "Y" : "N";
                        String change = rb_changeyes.isChecked() ? "Y" : "N";


                        Active_text dict = new Active_text(userid,puserid,jahal,bohang,change);
                        Long id = Long.valueOf(1);
                        if (activeAdapter.getItemCount()!=0)
                            id = Long.valueOf(activeArrayList.get(0).getId()+1);

                        Active_text dict_0 = new Active_text(id,
                                userid,puserid,jahal,bohang,change,null);
                        activeArrayList.add(0, dict_0); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨

                        // 어댑터에서 RecyclerView에 반영하도록 합니다.
                        activeAdapter.notifyItemInserted(0);
                        activeAdapter.notifyDataSetChanged();
                        activeApi.postDataActive(dict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                Log.e("######################################################","뭬야");
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {
                                    Log.e("등록 성공 ============",response.body()+"");
                                    Log.e("재활보행체위 값------------", jahal+bohang+change);
                                    Toast.makeText(getContext(), "활동 기록이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    //실패
                                    Log.e("활동", "stringToJson msg: 실패" + response.code());
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
        activeAdapter.setOnItemClickListener (new Active_adapter.OnItemClickListener () {
            @Override
            public void onItemClick(View v, int position) {
                Active_text detail_active_text = activeArrayList.get(position);
                activeApi.getDataActive(userid,puserid).enqueue(new Callback<List<Active_text>>() {
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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
                        .inflate(R.layout.active_detail, null, false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

                activedetail_jahal = dialog.findViewById(R.id.tv_activedetail_jahal);
                activedetail_bohang = dialog.findViewById(R.id.tv_activedetail_bohang);
                activedetail_change = dialog.findViewById(R.id.tv_activedetail_change);

                String getjahal = detail_active_text.getRehabilitation();
                String getbohang = detail_active_text.getWalkingAssistance();
                String getchange = detail_active_text.getPosition();

                if(getjahal.equals("Y"))
                    activedetail_jahal.setText("완료");
                else activedetail_jahal.setText("-");

                if(getbohang.equals("Y"))
                    activedetail_bohang.setText("완료");
                else activedetail_bohang.setText("-");

                if (getchange.equals("Y"))
                    activedetail_change.setText("완료");
                else activedetail_change.setText("-");

                final Button btn_active_detail = dialog.findViewById(R.id.btn_active_detail);
                final Button btn_active_delete = dialog.findViewById(R.id.btn_active_detail_delete);
                final Button btn_active_off = dialog.findViewById(R.id.btn_off);

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        View cview = LayoutInflater.from(getContext())
                                .inflate(R.layout.active_form_change, null, false);
                        builder.setView(cview);
                        final AlertDialog changedialog = builder.create();
                        changedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        changedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        changedialog.show();

                        rb_jahalyes = changedialog.findViewById(R.id.rb_jahalyes);
                        rb_jahalno = changedialog.findViewById(R.id.rb_jahalno);
                        rb_bohangyes = changedialog.findViewById(R.id.rb_bohangyes);
                        rb_bohangno = changedialog.findViewById(R.id.rb_bohangno);
                        rb_changeyes = changedialog.findViewById(R.id.rb_changeyes);
                        rb_changeno = changedialog.findViewById(R.id.rb_changeno);
                        btn_change = changedialog.findViewById(R.id.btn_active_change);
                        btn_cancel = changedialog.findViewById(R.id.btn_cancel);

                        String cjahal = detail_active_text.getRehabilitation();
                        String cbohang = detail_active_text.getWalkingAssistance();
                        String cchange = detail_active_text.getPosition();

                        // 활동 상태 값에 따라 라디오 버튼 선택
                        if (cjahal.equals("Y")) {
                            rb_jahalyes.setChecked(true);
                            rb_jahalno.setChecked(false);
                        } else if (cjahal.equals("N")) {
                            rb_jahalyes.setChecked(false);
                            rb_jahalno.setChecked(true);
                        }
                        if (cbohang.equals("Y")) {
                            rb_bohangyes.setChecked(true);
                            rb_bohangno.setChecked(false);
                        } else if (cbohang.equals("N")) {
                            rb_bohangyes.setChecked(false);
                            rb_bohangno.setChecked(true);
                        }
                        if (cchange.equals("Y")) {
                            rb_changeyes.setChecked(true);
                            rb_changeno.setChecked(false);
                        } else if (cchange.equals("N")) {
                            rb_changeyes.setChecked(false);
                            rb_changeno.setChecked(true);
                        }
                        else {
                            rb_jahalno.setChecked(true);
                            rb_bohangno.setChecked(true);
                            rb_changeno.setChecked(true);
                        }

                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                changedialog.dismiss();
                            }
                        });


                        btn_change.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                String cjahal = rb_jahalyes.isChecked() ? "Y" : "N";
                                String cbohang = rb_bohangyes.isChecked() ? "Y" : "N";
                                String cchange = rb_changeyes.isChecked() ? "Y" : "N";
                                Log.e("수정된 재활보행체위 값------------", cjahal+cbohang+cchange);

                                Active_text_change updatedActive = new Active_text_change(ids, cjahal, cbohang, cchange);

                                activeApi.putDataActive(updatedActive).enqueue(new Callback<Long>() {
                                    @Override
                                    public void onResponse(Call<Long> call, Response<Long> response) {

                                        if (response.isSuccessful()) {
                                            Log.e("수정성공 ============",response.body()+"");
                                            Toast.makeText(getContext(), "활동 기록이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                            activeAdapter.notifyItemChanged(position);
                                            dialog.dismiss();
                                        } else {
                                            //실패
                                            Log.e("활동", "stringToJson msg: 실패" + response.code() + response.body());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Long> call, Throwable t) {
                                        Log.e("활동", "onFailure: 수정 실패", t);
                                        dialog.dismiss();
                                    }
                                });
                                changedialog.dismiss();
                            }
                        });
                    }
                });

                btn_active_off.setOnClickListener(new View.OnClickListener() {
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