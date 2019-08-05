package com.medical.waste.http;


import com.medical.waste.bean.Department;
import com.medical.waste.bean.LoginData;
import com.medical.waste.bean.RecycleCompany;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.bean.UploadData;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    @GET("/api/login")
    Observable<Result<LoginData>> login(@Query("id") String cardId);

    @GET("/api/getTypes")
    Observable<Result<List<RubbishType>>> getTypes();

    @POST("/api/addRubbish")
    Observable<Result> addRubbish(@Body List<UploadData> uploadDatas);

    @GET("/api/getRubbish")
    Observable<Result<Rubbish>> getRubbish(@QueryMap Map<String, String> params);

    @PUT("/api/store")
    Observable<Result> store(@QueryMap Map<String, String> params);

    @PUT("/api/recycle")
    Observable<Result> recycle(@QueryMap Map<String, String> params);

    @GET("/api/getCompanies")
    Observable<Result<RecycleCompany>> getCompanies(@QueryMap Map<String, String> params);

    @GET("/api/getOneRubbish")
    Observable<Result<Rubbish>> getOneRubbish(@Query("id") String id);

    @GET("/api/getDepartmentById")
    Observable<Result<Department>> getDepartmentById(@Query("id") String id);
}
