package com.wwwjf.wlibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.wwwjf.stepprogresslibrary.StepProgressView;
import com.wwwjf.wcommonlibrary.utils.KLog;

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
    }
}
