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

public class ListPatientInfoAdapter extends BaseAdapter {

    private ArrayList<ListPatientInfo> listPatientInfos = new ArrayList<ListPatientInfo>() ;
    public ListPatientInfoAdapter() {

    }

    @Override
    public int getCount() {
        return listPatientInfos.size() ;
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

        TextView p_name = (TextView) convertView.findViewById(R.id.patient_name) ;
       /* TextView p_birth = (TextView) convertView.findViewById(R.id.patient_birth) ;
        TextView p_sick = (TextView) convertView.findViewById(R.id.patient_sick) ;
        TextView p_hos = (TextView) convertView.findViewById(R.id.patient_hospital) ;
        TextView p_medi = (TextView) convertView.findViewById(R.id.patient_medi) ;
        TextView p_personal = (TextView) convertView.findViewById(R.id.patient_personal) ;
        */

        ListPatientInfo listViewItem = listPatientInfos.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        p_name.setText(listViewItem.getContext());

        /*
        p_birth.setText(listViewItem.getContext());
        p_sick.setText(listViewItem.getContext());
        p_hos.setText(listViewItem.getContext());
        p_medi.setText(listViewItem.getContext());
        p_personal.setText(listViewItem.getContext());
         */

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
        return listPatientInfos.get(position) ;
    }

    // 데이터값 넣어줌
    public void addInfo(String text) {
        ListPatientInfo item = new ListPatientInfo();

        item.setContext(text);

        listPatientInfos.add(item);
    }
}
