package com.medical.waste.common;

public class AppConstant {
    //应用名
    public static final String APP_NAME = "andall";
    //平台
    public static final String PLATFORM = "android";
    public static final boolean isDebug = true;
    public static final String DEIVCE_ID = "DEIVCE_ID";
    public static final String H5_CACHE_CONFIG = "H5_CACHE_CONFIG";
    public static final String USER_CACHE_CONFIG = "H5_CACHE_CONFIG";
    //渠道号  暂定TD_CHANNEL_ID
    // TODO: 2019-06-11 渠道KEY换成对应的统计平台
    public static final String CHANNEL_KEY = "TD_CHANNEL_ID";
    public static final String URL = "url";

    public static final String TOKEN = "token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_INFO = "USER_INFO";
    public static final String MOBILE_NO = "mobileNo";
    public static final String CODE = "code";
    public static final String DISABLE_SLIDE = "disableSlide";
    public static final String TITLE = "title";
    public static final String IS_SHOW_BACK_BTN = "isShowBackBtn";
    public static final String CAN_GOBACK = "canGoback";
    public static final String MOBILE = "mobile";
    public static final String QR_CODE = "qr_code";
    public static final String INPUT_QR_CODE = "INPUT_QR_CODE";

    //扫描code
    public static final int REQUEST_CODE_SCAN_CODE = 1000;
    //base url
    public static final String BASE_URL = "http://47.88.171.244:8080";
    public static final long CONNECT_TIME_OUT = 30000;
    public static final long READ_TIME_OUT = 30000;
    public static final String APP = "app";
    public static final String BT_DEVICE = "BT_DEVICE";
    public static final String SP_DEVICE_ADDRESS = "deviceAddress";
    public static final String SP_DEVICE_NAME = "deviceName";
    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String ADMINISTRATOR_ID = "administratorId";
    public static final String COMPANY_ID = "companyId";
    public static final String STATUS = "status";
    public static final String START = "start";
    public static final String END = "end";
    public static final String RUBBISH = "rubbish";

    public static final class RxEvent{
        public final static String  QR_CODE = "QR_CODE";

        public static final String FILTER = "filter";
    }
    //H5原生互调方法
    public static final class H5Method{
        //获取手机号码
        public static final String GET_MOBILE = "getMobile";
        public static final String RELOAD_WEBVIEW = "reloadWebview";
        //二维码、条码扫描
        public static final String SCAN_QR_CODE = "scanQRCode";
    }
}
