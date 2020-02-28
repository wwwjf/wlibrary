package com.wwwjf.wlibrary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.wwwjf.pickerlibrary.DatePicker;
import com.wwwjf.stepprogresslibrary.StepProgressView;
import com.wwwjf.wcommonlibrary.utils.KLog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KLog.e("======" + MainActivity.class.getSimpleName());
        /*CustomDialog customDialog = new CustomDialog(this);
        customDialog.show("显示", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgress(50);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("12312");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

        ImageView btnAvatar = findViewById(R.id.btn_avatar);
        btnAvatar.setOnClickListener(v -> {
            Intent intentPicture = new Intent(MainActivity.this,PhotoActivity.class);

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                    v,getString(R.string.cover_scene_transition));

            startActivity(intentPicture,optionsCompat.toBundle());
        });

        StepProgressView stepProgressView = findViewById(R.id.stepView_content);
//        stepProgressView.setProgressText(5,"选择提现币种");

        showPicker();
    }

    private void showPicker() {

        String[] nowDate = "2020-02-28".split("-");
        String[] ymdInit = "2020-02-02".split("-");
        DatePicker picker = new DatePicker(this);
        picker.setCancelTextColor(Color.parseColor("#17364e"));
        picker.setCancelTextSize(17);
        picker.setSubmitTextColor(Color.parseColor("#17364e"));
        picker.setSubmitTextSize(17);
        picker.setTopLineVisible(false);
        picker.setOffset(2);
        picker.setDividerVisible(false);
        picker.setTextSize(20);
        picker.setTopPadding(54);
        picker.setLineSpaceMultiplier(2);
        picker.setBackgroundColor(Color.WHITE);
        picker.setPressedTextColor(Color.parseColor("#b3ffffff"));
        picker.setRangeStart(1949, 1, 1);
        picker.setTopBackgroundColor(Color.WHITE);
        picker.setTextPadding(0);
        picker.setTextColor(Color.parseColor("#17364e"),
                Color.parseColor("#17364e"));
        picker.setLabelTextColor(Color.parseColor("#33ffffff"));
        picker.setLabel("年","月","日");
        picker.setShadowColor(Color.parseColor("#33ffffff"));
        picker.setRangeEnd(Integer.valueOf(nowDate[0]), Integer.valueOf(nowDate[1]), Integer.valueOf(nowDate[2]));

        if (ymdInit.length == 3) {
            picker.setSelectedItem(Integer.valueOf(ymdInit[0]), Integer.valueOf(ymdInit[1]), Integer.valueOf(ymdInit[2]));
        } else {
            picker.setSelectedItem(1985, 1, 1);
        }
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
            String date = year + "-" + month + "-" + day;
//            showToast(date);
            KLog.e("-------date:" + date);
        });
        picker.show();
    }
}
