package com.example.digo_ec.service.Api;


import com.example.digo_ec.service.model.ApiMaps.Example;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("json?")
    Call<Example> traerGeo(@Query("sensor") boolean sensor, @Query("latlng") String latlong, @Query("key") String Key);


    @POST("json?")
    Observable<Example> traerGeo2(@Query("sensor")boolean sensor, @Query("latlng") String latlong, @Query("key") String Key);

}
