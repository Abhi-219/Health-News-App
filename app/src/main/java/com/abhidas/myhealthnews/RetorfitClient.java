package com.abhidas.myhealthnews;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetorfitClient {
    static Retrofit retrofit = null;
    static  String BASE_URL ="https://newsapi.org/";

    public  static  Retrofit getRetrofitInstances(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
