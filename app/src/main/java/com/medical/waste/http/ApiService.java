package com.medical.waste.http;


import com.medical.waste.bean.Result;
import com.medical.waste.bean.StartCaptcha;
import com.medical.waste.bean.UserInfo;

import java.util.Map;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    @POST("/app/login/sendVertifyCode")
    Observable<Result<Boolean>> sendVertifyCode(@Query("mobileNo") String mobileNo);

    @GET("/geetest/getStartCaptcha")
    Observable<Result<StartCaptcha>> getStartCaptcha(@Query("clientType") String clientType);

    @POST("/geetest/verifyLogin")
    Observable<Result<String>> verifyLogin(@Body StartCaptcha startCaptcha);

    @POST("/app/login/checkVertifyCode")
    Observable<Result<UserInfo>> checkVertifyCode(@QueryMap Map<String, String> param);

    @GET("/app/login/auth")
    Observable<Result<UserInfo>> getUserInfo();

}
