package com.puskesmascilandak.e_jiwa.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dossy on 6/5/2018.
 */

public class ApiClient {


    public static final String BASE_URL="https://phccilandak.com/puskesmas/api/";
    private static Retrofit retrofit=null;


    public static Retrofit getClient(){

        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
