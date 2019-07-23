package com.medical.waste.http.interceptor;

import com.medical.waste.common.AppConstant;
import com.medical.waste.utils.UserData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        //添加TOKEN
        requestBuilder.addHeader(AppConstant.TOKEN, UserData.getInstance().getToken());
        return chain.proceed(requestBuilder.build());
    }
}
