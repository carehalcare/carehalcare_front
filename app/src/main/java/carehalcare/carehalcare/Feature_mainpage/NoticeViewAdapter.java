package carehalcare.carehalcare.Feature_mainpage;

import static carehalcare.carehalcare.DateUtils.formatDate;

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
    private OnItemClickListener onItemClickListener;

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
        if (notices != null) {
            //createdDate를 변경하여 저장
            String newDate = formatDate(notice.getModifiedDateTime());
            holder.tv_notidate.setText(newDate);
            holder.tv_content.setText(notice.getContent());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_notidate, tv_content;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_notidate = (TextView) itemView.findViewById(R.id.tv_notidate);
            tv_content = (TextView) itemView.findViewById(R.id.tv_notice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                onItemClickListener.onItemClick(position); // 클릭 리스너 콜백 호출

            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() { return notices.size(); }
}
