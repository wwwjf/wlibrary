package com.wwwjf.wlibrary.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wwwjf.mvplibrary.view.LifeCycleMvpActivity;
import com.wwwjf.wcommonlibrary.utils.KLog;
import com.wwwjf.wcommonlibrary.utils.KeyboardUtils;
import com.wwwjf.wcommonlibrary.utils.PermissionManager;
import com.wwwjf.wcommonlibrary.widget.CustomDialog;
import com.wwwjf.wlibrary.R;

import butterknife.ButterKnife;


public abstract class BaseActivity extends LifeCycleMvpActivity {

    protected CustomDialog mCustomProgress;
    protected PermissionManager mPermissionManager;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInject annotation = this.getClass().getAnnotation(ViewInject.class);
        int contentViewId;
        if (annotation != null) {
            contentViewId = annotation.contentViewId();
        } else {
            contentViewId = getLayoutId();
        }
        if (contentViewId <= 0) {
            throw new RuntimeException("contentViewId 为 0");
        }

        if (!isNeedRefresh()) {//无刷新

            if (isNeedToolbar()) {
                setContentView(R.layout.activity_base_no_refresh_toolbar);
                if (isTitleDarkBackground()) {
                    setToolbarDarkBackground();
                    setStatusbarDarkBackground();
                } else {
                    setToolbarLightBackground();
                    setStatusbarLightBackground();
                }
            } else {
                setContentView(R.layout.activity_base_no_refresh_no_toolbar);
                if (isTitleDarkBackground()) {
                    setStatusbarDarkBackground();
                } else {
                    setStatusbarLightBackground();
                }
            }
        } else {//有刷新

            if (isNeedToolbar()) {
                setContentView(R.layout.activity_base_refresh_toolbar);
                if (isTitleDarkBackground()) {
                    setToolbarDarkBackground();
                    setStatusbarDarkBackground();
                } else {
                    setToolbarLightBackground();
                    setStatusbarLightBackground();
                }
            } else {
                setContentView(R.layout.activity_base_refresh_no_toolbar);
                if (isTitleDarkBackground()) {
                    setStatusbarDarkBackground();
                } else {
                    setStatusbarLightBackground();
                }
            }
            SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeRefresh_base);
            refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
            refreshLayout.setProgressViewEndTarget(true, 200);
        }


        FrameLayout content = findViewById(R.id.fl_base_content);
        LayoutInflater.from(this).inflate(contentViewId, content);

        bindView();


        if (isNeedRxBus()) {

        }

        mCustomProgress = new CustomDialog(this);
        mPermissionManager = new PermissionManager(this);

        showLoading();
        onCreateBaseActivity(savedInstanceState);
    }

    /**
     * 头部背景颜色是否深色
     *
     * @return 默认false
     */
    public boolean isTitleDarkBackground() {
        return false;
    }

    /**
     * 白色底深色文字
     */
    private void setToolbarLightBackground() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_center);
        TextView tvToolbarMore = findViewById(R.id.tv_toolbar_right);
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.color_text_main_17364E));
        tvToolbarMore.setTextColor(ContextCompat.getColor(this, R.color.color_text_main_17364E));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_back_17364e);

    }

    /**
     * 设置状态栏 默认白底黑字
     */
    private void setStatusbarLightBackground() {

    }

    /**
     * 深色底白色文字
     */
    private void setToolbarDarkBackground() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_center);
        TextView tvToolbarMore = findViewById(R.id.tv_toolbar_right);
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvToolbarMore.setTextColor(ContextCompat.getColor(this, R.color.color_text_main_17364E));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_3B55E6));
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
    }

    /**
     * 设置状态栏 深色底白色字
     */
    private void setStatusbarDarkBackground() {

    }

    public @LayoutRes
    int getLayoutId() {
        return -1;
    }


    public void showLoading() {

        if (!mCustomProgress.isShowing()) {
            mCustomProgress.show("loading", true, null);
        }
    }

    public void disMissLoading() {
        if (mCustomProgress.isShowing()) {
            mCustomProgress.cancel();
        }
    }

    public void showToast(String msg) {

    }

    public void showToast(String msg, int duration) {

    }

    @Override
    public void onBackPressed() {
        if (mCustomProgress.isShowing()) {
            mCustomProgress.cancel();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isUserLogin() {

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionManager.handleRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCustomProgress != null) {
            if (mCustomProgress.isShowing()) {
                mCustomProgress.cancel();
            }
            mCustomProgress = null;
        }
        if (mPermissionManager != null) {
            mPermissionManager = null;
        }
        if (isNeedRxBus()) {

        }
    }

    //-------------------------------点击空白区域，收起键盘 start-------------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && v != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (isClickBlankClearFocus())
                        v.clearFocus();
                }*/
                KeyboardUtils.hideSoftInput(v);
                if (v != null) {
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 点击空白区域清除EditText焦点
     * 需要根布局设置属性
     * android:focusable="true"
     * android:focusableInTouchMode="true"
     * 配合使用
     */
    public boolean isClickBlankClearFocus() {
        return false;
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];

            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom &&
                    v.isFocusable()) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //-------------------------------点击空白区域，收起键盘 end-------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        initText();
    }

    /**
     * 初始化界面
     * 涉及到语言文字的控件需要在此方法中获取对应语言的文字
     */
    protected abstract void initText();

    protected boolean isNeedToolbar() {
        return true;
    }

    protected boolean isNeedRefresh() {
        return false;
    }

    protected boolean isNeedRxBus() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                requestCode == 2) {
            loginRefreshData();
        }
    }

    /**
     * 登录成功后刷新数据
     */
    public void loginRefreshData() {
        KLog.e("===loginRefreshData===");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        KLog.e("--------");
        if (newConfig.fontScale != 1.0f) {
            //系统字体放大了，重置为1.0f
            getResources();
        }
        /*String language = LanguageUtil.getLastSettingLanguage(this);
        KLog.e("+++++++======++++++language:"+language);
        if (language != null) {
            if ("zh".equals(language)) {
                LanguageUtil.changeLanguage(this, Locale.SIMPLIFIED_CHINESE);
            } else {
                LanguageUtil.changeLanguage(this, Locale.ENGLISH);
            }
        } else {
            // 使用系统默认语言
        }*/
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public Resources getResources() {

        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) {
            Configuration configuration = new Configuration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    /**
     * ButterKnife绑定
     */
    private void bindView() {
        ButterKnife.bind(this);
    }

    //模板方法设计模式
    public abstract void onCreateBaseActivity(@Nullable Bundle savedInstanceState);
}
