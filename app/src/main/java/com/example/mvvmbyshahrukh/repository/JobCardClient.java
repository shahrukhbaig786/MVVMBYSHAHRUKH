package com.example.mvvmbyshahrukh.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JobCardClient {

    @GET("report/get-material-stock/{search_string}")
    Call<JsonObject> getMaterialStockList(@Header("udid") String udid, @Header("authorization") String authorization, @Path("search_string") String search_string, @Query("filter_type") int filter_type, @Query("pageno") Long pageno, @Query("datetime") Long datetime);

}


