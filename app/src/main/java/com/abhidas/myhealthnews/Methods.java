package com.abhidas.myhealthnews;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Methods {

//    @GET("v2/top-headlines?country=in&category=health&apiKey=8c7cc42c761844ce9df12a37ede58939")
//    Call<Model> getAllData();


    @GET
    Call<Model> getAllData(@Url String url);

    //"v2/top-headlines?country=in&category=health&apiKey=8c7cc42c761844ce9df12a37ede58939"
}
