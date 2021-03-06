package com.example.crudcustomer.network.service;

import com.example.crudcustomer.model.Data;
import com.example.crudcustomer.network.EndPoint;
import com.example.crudcustomer.network.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {
    @FormUrlEncoded
    @POST(EndPoint.API_CREATE)
    Call<BaseResponse> apiCreate(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password);

    @GET(EndPoint.API_READ)
    Call<BaseResponse<List<Data>>> apiRead();

    @FormUrlEncoded
    @PUT(EndPoint.API_UPDATE+"{id}")
    Call<BaseResponse> apiUpdate(
            @Path("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @DELETE(EndPoint.API_DELETE+"{id}")
    Call<BaseResponse> apiDelete(@Path("id") String id);
}
