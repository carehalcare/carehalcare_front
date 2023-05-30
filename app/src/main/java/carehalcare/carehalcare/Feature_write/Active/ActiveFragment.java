package carehalcare.carehalcare.Feature_write.Active;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.API_URL;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_API;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_adapter;
import carehalcare.carehalcare.Feature_write.Bowel.Bowel_text;
import carehalcare.carehalcare.Feature_write.DividerItemDecorator;
import carehalcare.carehalcare.Feature_write.EightMenuActivity;
import carehalcare.carehalcare.Feature_write.Meal.Meal_API;
import carehalcare.carehalcare.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActiveFragment extends Fragment {
    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Active_text> activeArrayList;
    private Active_adapter activeAdapter;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_list,container,false);

        Active_API activeApi = retrofit.create(Active_API.class);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_active_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);



        activeArrayList = new ArrayList<>();

        activeAdapter = new Active_adapter( activeArrayList);
        mRecyclerView.setAdapter(activeAdapter);


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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
        return view;
    }
}