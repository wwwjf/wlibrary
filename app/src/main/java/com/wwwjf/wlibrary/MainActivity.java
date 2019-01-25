package com.wwwjf.wlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wwwjf.wcommonlibrary.utils.KLog;
import com.wwwjf.wcommonlibrary.utils.StringUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KLog.e("======"+MainActivity.class.getSimpleName());
    }
}
