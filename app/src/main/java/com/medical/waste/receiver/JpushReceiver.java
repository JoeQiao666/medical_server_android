package com.medical.waste.receiver;

import android.content.Context;
import android.text.TextUtils;


import com.medical.waste.common.AppConstant;
import com.medical.waste.utils.SpUtil;
import com.socks.library.KLog;

import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JpushReceiver extends JPushMessageReceiver {
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
//        try {
            //打开自定义的Activity
//            if (TextUtils.isEmpty(message.notificationExtras)) {
//                return;
//            }
//            JpushData jpushData = App.gson.fromJson(message.notificationExtras, JpushData.class);
//            if (jpushData != null && !TextUtils.isEmpty(jpushData.getAction()) && jpushData.getAction().startsWith("andall://andall.com")) {
//                SchemeUtil.INSTANCE.jumpToMain(Uri.parse(jpushData.getAction()));
//            }
//        } catch (Throwable throwable) {
//        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
    }

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        //获取到jpush registerid
//        CacheMap.put(AppCacheKey.Companion.getJPUSH_ID(), s);
        KLog.e("jpushID:" + s);
        SpUtil.writeString(AppConstant.JPUSH_ID, s);
    }
}
