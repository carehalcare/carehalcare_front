package carehalcare.carehalcare;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Walk_adapter extends RecyclerView.Adapter<Walk_adapter.CustomViewHolder>{
    private ArrayList<Walk_text> mList;
    private Context mContext;

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private Walk_adapter.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(Walk_adapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        protected ImageView iv_walkpic;
        protected TextView tv_walkcontent;



        public CustomViewHolder(View view) {
            super(view);
            this.iv_walkpic = (ImageView) view.findViewById(R.id.iv_walkpic);
            this.tv_walkcontent = (TextView) view.findViewById(R.id.tv_walkcontent);

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
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.

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
    public Walk_adapter(ArrayList<Walk_text> list) {
        this.mList = list;
    }



    @Override
    public Walk_adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.walk_onelist, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull Walk_adapter.CustomViewHolder viewholder, int position) {

        viewholder.tv_walkcontent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        viewholder.tv_walkcontent.setGravity(Gravity.CENTER);



        //String seeText = mList.get(position).getContent_walk();
        Uri seePhoto = mList.get(position).getPhotouri_walk();

        Date today_date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 M월 dd일 : HH시 MM분", Locale.getDefault());
        String seeText = format.format(today_date);
        viewholder.tv_walkcontent.setText(seeText);        //      viewholder.iv_mealpic.setImageURI(seePhoto);
        Glide.with(viewholder.itemView).load(seePhoto).into(viewholder.iv_walkpic);



        Log.v("################################################################",seePhoto.getPath());




    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}