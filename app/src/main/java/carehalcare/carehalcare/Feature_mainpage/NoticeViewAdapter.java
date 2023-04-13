package carehalcare.carehalcare.Feature_mainpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import carehalcare.carehalcare.R;

public class NoticeViewAdapter extends RecyclerView.Adapter<NoticeViewAdapter.ViewHolder>{

    private List<Notice> notices;
    //public NoticeViewAdapter(List<Notice> notices){ this.notices = notices; }
    public NoticeViewAdapter (List<Notice> notices){ this.notices = notices; };

    @NonNull
    @Override
    public NoticeViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list, parent, false);
        NoticeViewAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notice notice = notices.get(position);
        if (notices != null && holder.tv_notice != null) {
            holder.tv_notice.setText(notice.getContent() +"  " + notice.getCreatedDate());

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_notice;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_notice = (TextView) itemView.findViewById(R.id.tv_notice);
        }
    }


    @Override
    public int getItemCount() { return notices.size(); }
}
