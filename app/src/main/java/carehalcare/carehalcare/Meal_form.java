package carehalcare.carehalcare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Meal_form extends AppCompatActivity {
    ImageView imgtest;
    Button btn_save, btn_nono;

    EditText et_mealdetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_form);

        imgtest = (ImageView)findViewById(R.id.iv_meal_form);
        btn_save = (Button) findViewById(R.id.btn_save_mealdetail);
        btn_nono = (Button) findViewById(R.id.btn_nono_mealdetail);
        et_mealdetail = (EditText)findViewById(R.id.et_meal_form);

        Uri output = getIntent().getParcelableExtra("uri");
        Bitmap bitmaps = UriBitmap_meal(output);
        //MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmaps, "wow","");

        imgtest.setImageBitmap(bitmaps);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uris = getImageUri(Meal_form.this,bitmaps);
                String wow = et_mealdetail.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("edit",wow);
                intent.putExtra("uris",uris);
                setResult(2888, intent);
                finish();
            }
        });
        btn_nono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                setResult(3000, intent);
                finish();
            }
        });

    }

    public Bitmap UriBitmap_meal(Uri imageuris) {
        Bitmap bm = null;
        String dateText = "";
        String timeText = "";
        Canvas cs;
        Paint pt = new Paint();
        float height = pt.measureText("yY");

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(200);

        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setTextSize(200);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(14);

//        String strTime, strCovdate, strCovTime;
//        strTime = PSH.utc2kst(String.valueOf(GPSpos.time));
//        strCovdate = "20"+GPSpos.utcDate.substring(4,6)+"-"+
//                GPSpos.utcDate.substring(2,4)+"-"+GPSpos.utcDate.substring(0,2);
//        strCovdate = strTime.substring(0,2)+":"+strTime.substring(2,4)+":"+strTime.substring(4,6);

        Date today_date = Calendar.getInstance().getTime();
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy년 M월 dd일", Locale.KOREAN);
        SimpleDateFormat tformat = new SimpleDateFormat("HH시 MM분 SS초", Locale.KOREAN);

        dateText = dformat.format(today_date);
        timeText = tformat.format(today_date);
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageuris)).copy(Bitmap.Config.ARGB_8888, true);

            } else {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuris).copy(Bitmap.Config.ARGB_8888, true);
            }
            cs = new Canvas(bm);
            cs.drawText(dateText, 20f, height+300f, strokePaint);
            cs.drawText(dateText, 20f, height+300f, textPaint);
            cs.drawText(timeText, 20f, height+500f, strokePaint);
            cs.drawText(timeText, 20f, height+500f, textPaint);
            return bm;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;



    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
