package com.medical.waste.http.retrofit;



import com.medical.waste.http.ApiService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pc on 2018/4/27.
 */
@Singleton
@Component(modules = HttpModule.class)
public interface HttpComponent {
    final class HttpInitialize{
        public static HttpComponent init(){return DaggerHttpComponent.builder().build();}
    }
    ApiService getApiService();
}
