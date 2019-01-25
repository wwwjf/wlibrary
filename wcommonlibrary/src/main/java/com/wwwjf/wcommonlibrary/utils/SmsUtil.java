package com.wwwjf.wcommonlibrary.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class SmsUtil {

    /**
     * 跳到短信应用发送短信
     *
     * @param context 上下文
     * @param phoneNumber 手机号码
     * @param message 短信内容
     */
    public static void sendInviteSms(Context context, String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
    }

    /**
     * 直接发送短信
     * @param phoneNum 手机号码
     * @param message 短信内容
     * @param sentIntent 发送方短信发送结果
     * @param deliveryIntent 接收方短信接收结果
     */
    public static void sendInviteSmsDirectly(String phoneNum, String message, PendingIntent sentIntent,PendingIntent deliveryIntent) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> list = smsManager.divideMessage(message);
        for (int i = 0; i < list.size(); i++) {
            smsManager.sendTextMessage(phoneNum, null, list.get(i), sentIntent, deliveryIntent);
        }
    }
}
