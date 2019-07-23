package com.medical.waste.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.medical.waste.app.App;
import com.medical.waste.common.AppConstant;
import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okio.Buffer;

/**
 * Created by jiajia on 2016/5/9.
 */
public class Utils {

    /**
     * 检查手机号码格式
     * 判断是否为11位数字和开头是否为"13 14 15 18 "
     *
     * @param _mobile
     * @return
     */
    public static boolean isValidMobile(String _mobile) {
        if (_mobile != null && _mobile.length() == 11
                && (_mobile.startsWith("13") || _mobile.startsWith("15")
                || _mobile.startsWith("18") || _mobile
                .startsWith("14") || _mobile
                .startsWith("17"))) {
            return true;
        } else {
            return false;
        }
    }

    // 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否含有中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 产生UUID 等图片验证码使用
     *
     * @return
     */
    public static String getUUid() {
        return UUID.randomUUID().toString();
    }

    // IMEI 设备号
    private static String getPhoneId() {
        String deviceId = null;
        try {
            PackageManager pm = App.getContext().getPackageManager();
            if (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.READ_PHONE_STATE", AppUtils.getAppPackageName())) {
                TelephonyManager tm = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
                KLog.d("deviceId：" + deviceId);
            }
        } catch (Exception e) {

        }
        return deviceId;
    }

    public static String getDeviceId() {
        String deviceId = SpUtil.readString(AppConstant.DEIVCE_ID);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        deviceId = getPhoneId();
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        deviceId = getMD5(getUUid(), false);
        SpUtil.writeString(AppConstant.DEIVCE_ID, deviceId);
        return deviceId;
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }


    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }


    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 获取渠道名
     *
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @param channelKey 渠道对应Key TalkingData对应的为TD_CHANNEL_ID
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context ctx,String channelKey) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString(channelKey);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(channelName)) {
            channelName = "default";
        }
        return channelName;
    }


    //获取tint图片 填充颜色
    public static Drawable getTintDrawable(Context context, int drawableRes, int colorRes) {
        //利用ContextCompat工具类获取drawable图片资源
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        //简单的使用tint改变drawable颜色
        return tintDrawable(drawable, ContextCompat.getColor(context, colorRes));
    }

    public static Drawable tintDrawable(Drawable drawable, int colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static boolean checkResult(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                // Permission Denied
                return false;
            }
        }
        return true;
    }

    public static String getJsonFromAssert(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }


    // 处理名字，将第二个字隐藏
    public static String replaceRealnameStarToString(String str1) {
        // 当前时间
        if (TextUtils.isEmpty(str1)) {
            return str1;
        }
        String formatString = null;

        try {
            formatString = str1.substring(0, 1) + "*" + str1.substring(2, str1.length());
//            System.currentTimeMillis()
        } catch (Exception e) {
        }
        return formatString;
    }

    // 处理银行卡号
    public static String replaceCardIdStarToString(String str1) {
        // 当前时间
        if (TextUtils.isEmpty(str1)) {
            return str1;
        }
        String formatString = null;

        try {
            formatString = str1.substring(0, 3) + "**********" + str1.substring(str1.length() - 3, str1.length());
//            System.currentTimeMillis()
        } catch (Exception e) {
        }
        return formatString;
    }

    // 处理手机号码
    public static String replacePhoneStarToString(String str1) {
        // 当前时间
        if (TextUtils.isEmpty(str1)) {
            return str1;
        }
        String formatString = null;

        try {
            formatString = str1.substring(0, 3) + "*****" + str1.substring(str1.length() - 3, str1.length());
//            System.currentTimeMillis()
        } catch (Exception e) {
        }
        return formatString;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */

    public static boolean checkNameChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 二代身份证号码有效性校验
     *
     * @param idNo
     * @return
     */
    public static boolean isValidIdNo(String idNo) {
        return isIdNoPattern(idNo) && isValidProvinceId(idNo.substring(0, 2))
                && isValidDate(idNo.substring(6, 14)) && checkIdNoLastNum(idNo);
    }

    /**
     * 二代身份证正则表达式
     *
     * @param idNo
     * @return
     */
    private static boolean isIdNoPattern(String idNo) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idNo);
    }


    //省(直辖市)码表
    private static String provinceCode[] = {"11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91"};

    /**
     * 检查身份证的省份信息是否正确
     *
     * @param provinceId
     * @return
     */
    public static boolean isValidProvinceId(String provinceId) {
        for (String id : provinceCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断日期是否有效
     *
     * @param inDate
     * @return
     */
    public static boolean isValidDate(String inDate) {
        if (inDate == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (inDate.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }
        dateFormat.setLenient(false);//执行严格的日期匹配
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    //身份证前17位每位加权因子
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    //身份证第18位校检码
    private static String[] refNumber = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * 计算身份证的第十八位校验码
     *
     * @param cardIdArray
     * @return
     */
    public static String sumPower(int[] cardIdArray) {
        int result = 0;
        for (int i = 0; i < power.length; i++) {
            result += power[i] * cardIdArray[i];
        }
        return refNumber[(result % 11)];
    }

    /**
     * 校验身份证第18位是否正确(只适合18位身份证)
     *
     * @param idNo
     * @return
     */
    public static boolean checkIdNoLastNum(String idNo) {
        if (idNo.length() != 18) {
            return false;
        }
        char[] tmp = idNo.toCharArray();
        int[] cardidArray = new int[tmp.length - 1];
        int i = 0;
        for (i = 0; i < tmp.length - 1; i++) {
            cardidArray[i] = Integer.parseInt(tmp[i] + "");
        }
        String checkCode = sumPower(cardidArray);
        String lastNum = tmp[tmp.length - 1] + "";
        if (lastNum.equals("x")) {
            lastNum = lastNum.toUpperCase();
        }
        if (!checkCode.equals(lastNum)) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled() {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) App.getContext().getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = App.getContext().getApplicationInfo();
        String pkg = App.getContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //进入设置界面
    public static void gotoSettingPage() {
        Context context = App.getContext();
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
    //webview userAgent
    public static String getUserAgent(String userAgentDefault) {
        //  andall/android/设备名称/设备id/大版本号/小版本号/系统版本/应用包名/渠道号/浏览器默认UserAgent
        String divier = File.separator;
        String userAgent =
                //应用名
                AppConstant.APP_NAME + divier +
                        //平台
                        AppConstant.PLATFORM + divier +
                        //设备唯一id
                        Utils.getDeviceId() + divier +
                        //大版本号
                        AppUtils.getAppVersionName() + divier +
                        //小版本号
                        AppUtils.getAppVersionCode() + divier +
                        //手机系统版本
                        DeviceUtils.getSDKVersionName() + divier +
                        //应用包名
                        AppUtils.getAppPackageName() + divier +
                        //渠道号
                        Utils.getChannelName(App.getContext(), AppConstant.CHANNEL_KEY) + divier +
                        //浏览器默认UserAgent
                        userAgentDefault;
        KLog.d("userAgent：" + userAgent);
        return userAgentDefault;
    }



    public static Map<String,String> getDefaultParams(){
        return new HashMap<>();
    }

}
