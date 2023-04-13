package carehalcare.carehalcare.Feature_mainpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import carehalcare.carehalcare.R;

public class NoticeViewAdapter extends RecyclerView.Adapter<NoticeViewAdapter.ViewHolder>{

    private ArrayList<Notice> notices;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_notice;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_notice = (TextView) itemView.findViewById(R.id.tv_noti);
        }
    }

    public NoticeViewAdapter (ArrayList<Notice> Pnoti){ this.notices = Pnoti; };

    @Override
    public NoticeViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list, parent, false);
        NoticeViewAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter.ViewHolder holder, int position) {
        holder.tv_notice.setText(notices.get(position).getContent());
        holder.tv_notice.setText(notices.get(position).getCreatedDate());
    }


    @Override
    public int getItemCount() { return notices.size(); }
}
