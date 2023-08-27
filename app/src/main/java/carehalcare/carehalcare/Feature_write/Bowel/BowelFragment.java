package carehalcare.carehalcare.Feature_write.Bowel;

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

public class BowelFragment extends Fragment {
    String userid,puserid;

    Long ids;  //TODO ids는 삭제할 id값
    private ArrayList<Bowel_text> bowelArrayList;
    private Bowel_adapter bowelAdapter;
    public BowelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bowel_list,container,false);

        userid = this.getArguments().getString("userid");
        puserid = this.getArguments().getString("puserid");

        Bowel_API bowelApi = Retrofit_client.createService(Bowel_API.class, TokenUtils.getAccessToken("Access_Token"));

        //Bowel_API bowelApi = retrofit.create(Bowel_API.class);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_bowel_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        bowelArrayList = new ArrayList<>();
        bowelAdapter = new Bowel_adapter( bowelArrayList);
        mRecyclerView.setAdapter(bowelAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        bowelApi.getDataBowel(userid,puserid).enqueue(new Callback<List<Bowel_text>>() {
            @Override
            public void onResponse(Call<List<Bowel_text>> call, Response<List<Bowel_text>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Bowel_text> datas = response.body();
                        if (datas != null) {
                            bowelArrayList.clear();
                            if (datas.size()==0){
                                Toast.makeText(getActivity(), "배변 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < datas.size(); i++) {
                                Bowel_text dict_0 = new Bowel_text(response.body().get(i).getUserId(),
                                        response.body().get(i).getPuserId(),
                                        response.body().get(i).getId(),
                                        response.body().get(i).getCount(),response.body().get(i).getContent(),
                                        response.body().get(i).getCreatedDateTime());
                                bowelArrayList.add(dict_0);
                                bowelAdapter.notifyItemInserted(0);
                                Log.e("현재id : " + i, datas.get(i).getCount()+" "+datas.get(i).getId() + ""+"어댑터카운터"+bowelAdapter.getItemCount());
                            }
                            Log.e("getSleep success", "======================================");
                        }
                    }}
            }
            @Override
            public void onFailure(Call<List<Bowel_text>> call, Throwable t) {
                Log.e("getSleep fail", "======================================");
            }
        });
        Button buttonInsert = (Button)view.findViewById(R.id.btn_bowel_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext())
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

                        Long bowelcount;
                        String inputValue = et_bowelCount.getText().toString();

                        if (inputValue.isEmpty()) {
                            bowelcount = 0L;
                        } else {
                            bowelcount = Long.valueOf(inputValue);
                        }

                        String bowelForm = et_bowelForm.getText().toString();

                        if (bowelForm.length()==0){bowelForm = "-";};

                        Bowel_text dict = new Bowel_text(userid,puserid,bowelcount, bowelForm);
                        bowelArrayList.add(0, dict);
                        bowelAdapter.notifyItemInserted(0);
                        bowelAdapter.notifyDataSetChanged();
                        bowelApi.postDataBowel(dict).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                Log.e("보낼때bodyek ============",response.body()+"");

                                if (response.isSuccessful()) {

                                } else {
                                    //실패
                                    Log.e("BoWel", "stringToJson msg: 실패" + response.code());
                                }
                            }
                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {}
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

                bowelApi.getDataBowel(userid,puserid).enqueue(new Callback<List<Bowel_text>>() {
                    @Override
                    public void onResponse(Call<List<Bowel_text>> call, Response<List<Bowel_text>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                List<Bowel_text> datas = response.body();
                                if (datas != null) {
                                    ids = response.body().get(position).getId();
                                    Log.e("지금 position : ",position+"이고 DB ID는 : " + ids);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    View view = LayoutInflater.from(getContext())
                                            .inflate(R.layout.bowel_detail, null, false);
                                    builder.setView(view);
                                    final AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();

                                    final TextView boweldetail_count = dialog.findViewById(R.id.tv_boweldetail_count);
                                    final TextView boweldetail_et = dialog.findViewById(R.id.tv_boweldetail_et);

                                    bowelApi.getDataBowel_2(ids).enqueue(new Callback<Bowel_text>() {
                                        @Override
                                        public void onResponse(Call<Bowel_text> call, Response<Bowel_text> response) {
                                            if (response.isSuccessful()){
                                                if (response.body()!=null){
                                                    boweldetail_count.setText(String.valueOf(response.body().getCount()));
                                                    boweldetail_et.setText(response.body().getContent());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Bowel_text> call, Throwable t) {

                                        }
                                    });



                                    final Button btn_boweldetail = dialog.findViewById(R.id.btn_boweldetail);
                                    final Button btn_boweldelete = dialog.findViewById(R.id.btn_boweldetail_delete);
                                    final Button btn_off = dialog.findViewById(R.id.btn_off);
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                            View cview = LayoutInflater.from(getContext())
                                                    .inflate(R.layout.bowel_form_change, null, false);
                                            builder.setView(cview);
                                            final AlertDialog cdialog = builder.create();
                                            cdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            cdialog.show();

                                            EditText et_count = cdialog.findViewById(R.id.et_bowelCount);
                                            EditText et_form = cdialog.findViewById(R.id.et_bowelForm);
                                            Button btn_change = cdialog.findViewById(R.id.btn_bowel_change);
                                            Button btn_cancel = cdialog.findViewById(R.id.btn_cancel);

                                            bowelApi.getDataBowel_2(ids).enqueue(new Callback<Bowel_text>() {
                                                @Override
                                                public void onResponse(Call<Bowel_text> call, Response<Bowel_text> response) {
                                                    if (response.isSuccessful()){
                                                        if (response.body()!=null){
                                                            et_count.setText(String.valueOf(response.body().getCount()));

                                                            if (response.body().getContent().length() == 0) et_form.setText("");
                                                            else et_form.setText(response.body().getContent());
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Bowel_text> call, Throwable t) {

                                                }
                                            });



                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    cdialog.dismiss();
                                                }
                                            });

                                            btn_change.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    Long count;
                                                    String inputValue = et_count.getText().toString();

                                                    if (inputValue.isEmpty()) {
                                                        count = 0L;
                                                    } else {
                                                        count = Long.valueOf(inputValue);
                                                    }
                                                    String content = et_form.getText().toString();
                                                    if (content.length()==0){content = "-";};

                                                    Bowel_text_change update = new Bowel_text_change(ids, count, content);
                                                    bowelApi.putDataBowel(update).enqueue(new Callback<Long>() {
                                                        @Override
                                                        public void onResponse(Call<Long> call, Response<Long> response) {

                                                            if (response.isSuccessful()) {
                                                                Log.e("수정성공 ============",response.body()+"");
                                                                Toast.makeText(getContext(), "배변 기록이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                                                cdialog.dismiss();

                                                            } else {
                                                                //실패
                                                                Log.e("BoWel", "stringToJson msg: 실패" + response.code());
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(Call<Long> call, Throwable t) {}
                                                    });
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    });

                                    btn_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            }}
                    }
                    @Override
                    public void onFailure(Call<List<Bowel_text>> call, Throwable t) {
                    }
                });

            }
        });
        return view;
    }
}