package carehalcare.carehalcare.Feature_mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import carehalcare.carehalcare.R;

public class ListNoticeAdapter extends BaseAdapter {

    private ArrayList<ListPatientInfo> listnoti = new ArrayList<ListPatientInfo>() ;
    public ListNoticeAdapter() {

    }

    @Override
    public int getCount() {
        return listnoti.size() ;
    }

    // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_listinfo, parent, false);
        }

        TextView p_notice = (TextView) convertView.findViewById(R.id.p_notice) ;
        ListPatientInfo listViewItem = listnoti.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        p_notice.setText(listViewItem.getContext());


        //리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, (pos+1)+"번째 리스트가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listnoti.get(position) ;
    }

    // 데이터값 넣어줌
    public void addInfo(String text) {
        ListPatientInfo item = new ListPatientInfo();
        item.setContext(text);
        listnoti.add(item);
    }
}
