package com.medical.waste.http.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.medical.waste.common.AppConstant;
import com.medical.waste.http.ApiService;
import com.medical.waste.http.interceptor.HeaderInterceptor;
import com.medical.waste.http.interceptor.LogInterceptor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class HttpModule {
    @Singleton
    @Provides
    ApiService getApiService(ScalarsConverterFactory scalarsConverterFactory, RxJava2CallAdapterFactory callAdapterFactory, GsonConverterFactory gsonConverterFactory, OkHttpClient client){
        return new Retrofit.Builder().baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(scalarsConverterFactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(client)
                .build().create(ApiService.class);
    }

    @Singleton
    @Provides
    ScalarsConverterFactory getScalarsConverterFactory() {
        return ScalarsConverterFactory.create();
    }

    @Singleton
    @Provides
    RxJava2CallAdapterFactory getCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    GsonConverterFactory getGsonConvertFactory() {
        return GsonConverterFactory.create();
    }


    @Singleton
    @Provides
    OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                //添加默认header信息
                .addInterceptor(new HeaderInterceptor())
                //打印日志
                .addInterceptor(new LogInterceptor())
                .connectTimeout(AppConstant.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConstant.READ_TIME_OUT, TimeUnit.MILLISECONDS).build();
    }
}
