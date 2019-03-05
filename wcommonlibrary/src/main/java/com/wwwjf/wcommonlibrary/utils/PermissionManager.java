package com.wwwjf.wcommonlibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.wwwjf.wcommonlibrary.R;
import com.wwwjf.wcommonlibrary.common.WConstants;
import com.wwwjf.wcommonlibrary.widget.CustomDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

/**
 *
 */

public class PermissionManager {
    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Object mContext;

    private PermissionListener mListener;

    private String tag;

    private CustomDialog mCustomProgress;

    private boolean needCustomEnsure;

    public PermissionManager(@NonNull Object context) {
        this.mContext = context;
    }

    /**
     * 权限授权申请
     *
     * @param permissions 要申请的权限
     * @param listener    申请成功之后的callback
     */
    public void requestPermissions(@NonNull Activity context,
                                   @Nullable PermissionListener listener,
                                   @NonNull String[] permissions,
                                   String tag, boolean needCustomEnsure) {
        this.needCustomEnsure = needCustomEnsure;
        if (listener != null) {
            mListener = listener;
        }

        if (!TextUtils.isEmpty(tag)) {
            this.tag = tag;
        }

        //没全部权限
        //判断是否有权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasPermissions(context, permissions).length != 0) {
                executePermissionsRequest(hasPermissions(context, permissions));
            } else if (mListener != null) { //有全部权限
                mListener.doAfterGrand(permissions, tag);
            }
        } else {
            mListener.doAfterGrand(permissions, tag);
        }
    }

    /**
     * 权限授权申请
     *
     * @param permissions 要申请的权限
     * @param listener    申请成功之后的callback
     */
    public void requestPermissions(@NonNull Activity context,
                                   @Nullable PermissionListener listener,
                                   @NonNull String[] permissions,
                                   String tag) {
        requestPermissions(context, listener, permissions, tag, false);
    }

    /**
     * 处理onRequestPermissionsResult
     */
    public void handleRequestPermissionsResult(final Context context, int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                boolean allGranted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }

                    //检测小米手机
                    if (isMIUI()) {
                        if (!initMUIPermission(permissions[i])) {
                            executePermissionsRequest(new String[]{permissions[i]});
                        }
                    }
                }

                if (allGranted && mListener != null) {
                    mListener.doAfterGrand(permissions, tag);
                } else if (!allGranted && mListener != null) {
                    showMessageOKCancel(context, context.getString(R.string.common_no_permission_to_open), v -> {
                        destroy();
                        if (needCustomEnsure) {
                            //判断是否权限点击了不再询问
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                if (!shouldShowRequestPermissionRationale(permissions)) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    // 根据包名打开对应的设置界面
                                    if (mContext instanceof Activity) {
                                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                                        ((Activity) mContext).startActivityForResult(intent, WConstants.REQUEST_CODE);
                                    } else if (mContext instanceof Fragment) {
                                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                                        ((Fragment) mContext).startActivityForResult(intent, WConstants.REQUEST_CODE);
                                    }
                                    return;
                                }
                            }
                            mListener.doAfterDeniedEnsure(permissions, tag);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            // 根据包名打开对应的设置界面
                            if (mContext instanceof Activity) {
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                ((Activity) mContext).startActivityForResult(intent, WConstants.REQUEST_CODE);
                            } else if (mContext instanceof Fragment) {
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                ((Fragment) mContext).startActivityForResult(intent, WConstants.REQUEST_CODE);
                            }
                        }
                    }, v -> {
                        destroy();
                        mListener.doAfterDeniedCancel(permissions, tag);
                    });
                }
        }
    }

    /**
     * 判断小米MIUI系统中授权管理中对应的权限授取
     *
     * @return false 存在核心的未收取的权限   true 核心权限已经全部授权
     */
    private boolean initMUIPermission(String permission) {
        AppOpsManager appOpsManager = null;
        String MUIPermission = "";
        //解析权限对比
        //获取权限的最后一个点的数据
        //特殊权限判断
        if (permission != null) {
            if (permission.contains("ACCESS_COARSE_LOCATION")) {
                MUIPermission = AppOpsManager.OPSTR_COARSE_LOCATION;
            } else if (permission.contains("ACCESS_FINE_LOCATION")) {
                MUIPermission = AppOpsManager.OPSTR_FINE_LOCATION;
            } else {
                String substring = permission.substring(permission.lastIndexOf(".") + 1).toLowerCase();
                MUIPermission = "android:" + substring;
            }
        }


        int cameraOp = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !"".equals(MUIPermission)) {
            if (mContext instanceof Activity) {
                appOpsManager = (AppOpsManager) ((Activity) mContext).getSystemService(Context.APP_OPS_SERVICE);
                assert appOpsManager != null;
                cameraOp = appOpsManager.checkOp(MUIPermission, Binder.getCallingUid(), ((Activity) mContext).getPackageName());
            } else if (mContext instanceof Fragment) {
                FragmentActivity activity = ((Fragment) mContext).getActivity();
                if (activity != null)
                    appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
                assert appOpsManager != null;
                cameraOp = appOpsManager.checkOp(MUIPermission, Binder.getCallingUid(), activity.getPackageName());
            }
            if (cameraOp != -1) {
                return cameraOp != AppOpsManager.MODE_IGNORED;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查手机是否是miui系统
     */
    public boolean isMIUI() {
        String device = Build.MANUFACTURER;
        return device.equals("Xiaomi");
    }

    /**
     * 判断是否具有某权限
     */

    public String[] hasPermissions(@NonNull Activity context, String[] perms) {
        ArrayList<String> permissionList = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return permissionList.toArray(new String[permissionList.size()]);
        }
        for (String perm : perms) {
            //判断是activity还是fragment
            boolean hasPerm = context.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
            if (!hasPerm) {
                permissionList.add(perm);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }


    /**
     * 执行申请,兼容fragment
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void executePermissionsRequest(@NonNull String[] perms) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).requestPermissions(perms, PermissionManager.REQUEST_PERMISSION_CODE);
        } else if (mContext instanceof Fragment) {
            ((Fragment) mContext).requestPermissions(perms, PermissionManager.REQUEST_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean shouldShowRequestPermissionRationale(String[] perms) {
        if (mContext instanceof Activity) {
            for (String perm : perms) {
                if (!((Activity) mContext).shouldShowRequestPermissionRationale(perm)) {
                    return false;
                }
            }
            return true;
        } else if (mContext instanceof Fragment) {
            FragmentActivity activity = ((Fragment) mContext).getActivity();
            if (activity != null) {
                for (String perm : perms) {
                    if (!((Activity) mContext).shouldShowRequestPermissionRationale(perm)) {
                        return false;
                    }
                }
                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    public void showMessageOKCancel(Context context, String message, View.OnClickListener okListener, View.OnClickListener cancelListener) {
        if (mCustomProgress == null) {
            mCustomProgress = new CustomDialog(context);
        }
        mCustomProgress.show(message, "", cancelListener, okListener, false, null);
    }

    public boolean isShowing() {
        return mCustomProgress != null && mCustomProgress.isShowing();
    }

    public void destroy() {
        if (mCustomProgress != null) {
            mCustomProgress.dismiss();
        }
    }


    public interface PermissionListener {

        void doAfterGrand(String[] permission, String tag);

        void doAfterDeniedCancel(String[] permission, String tag);

        void doAfterDeniedEnsure(String[] permission, String tag);
    }

    public static abstract class SimplePermissionListener implements PermissionListener {
        @Override
        public void doAfterGrand(String[] permission, String tag) {

        }

        @Override
        public void doAfterDeniedCancel(String[] permission, String tag) {

        }

        @Override
        public void doAfterDeniedEnsure(String[] permission, String tag) {

        }
    }

}
