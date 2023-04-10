package carehalcare.carehalcare.Feature_mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import carehalcare.carehalcare.R;

public class PatientinfoActivity extends AppCompatActivity {

    private ImageButton btn_home;
    private ListView listinfo;
    private ListPatientInfoAdapter adapter;
    private String[] context = {"이름","생년월일","질환", "담당병원", "복용 약", "성격"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinfo);

        btn_home = (ImageButton) findViewById(R.id.btn_homeinfo);
        adapter = new ListPatientInfoAdapter();
        listinfo = (ListView) findViewById(R.id.list_view);

        listinfo.setAdapter(adapter);

        for(int i=0; i<context.length; i++){
            adapter.addInfo(context[i]);
        }

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientinfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}