package carehalcare.carehalcare.Feature_write;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;

import carehalcare.carehalcare.R;

public class Active_adapter extends RecyclerView.Adapter<Active_adapter.CustomViewHolder> {
    private ArrayList<Active_text> mList;
    private Context mContext;

    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private Active_adapter.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(Active_adapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        protected TextView tv_todayActive;
        protected TextView tv_todayActiveResult;


        public CustomViewHolder(View view) {
            super(view);
            this.tv_todayActive = (TextView) view.findViewById(R.id.tv_todayActive);
            this.tv_todayActiveResult = (TextView) view.findViewById(R.id.tv_todayActiveResult);

            view.setOnCreateContextMenuListener(this);
            //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.

            view.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position);
                        }
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.

            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Delete.setOnMenuItemClickListener(onEditMenu);


        }
        // 4. 컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {



            @Override
            public boolean onMenuItemClick(MenuItem item) {


                switch (item.getItemId()) {
                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;

                }
                return true;
            }
        };

    }


    public Active_adapter(ArrayList<Active_text> list) {
        this.mList = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.active_onelist, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.tv_todayActive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewholder.tv_todayActiveResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewholder.tv_todayActive.setGravity(Gravity.CENTER);
        viewholder.tv_todayActiveResult.setGravity(Gravity.CENTER);


        viewholder.tv_todayActive.setText("오늘의 활동");
        viewholder.tv_todayActiveResult.setText(mList.get(position).getactiveTodayResult());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
