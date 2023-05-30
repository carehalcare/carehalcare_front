package carehalcare.carehalcare.Feature_mainpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import carehalcare.carehalcare.R;

public class NoticeViewAdapter extends RecyclerView.Adapter<NoticeViewAdapter.ViewHolder>{

    private List<Notice> notices;

    public NoticeViewAdapter (List<Notice> notices){ this.notices = notices; };

    private String formatDate(String dateStr) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String newDate = "";
        try {
            Date date = originalFormat.parse(dateStr);
            newDate = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

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
            //createdDate를 변경하여 저장
            String newDate = formatDate(notice.getCreatedDate());

            holder.tv_notice.setText(notice.setCreatedDate(newDate) + "\n" + notice.getContent());
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
