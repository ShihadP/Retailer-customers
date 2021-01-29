package com.example.retailer_customers.api;



import com.example.retailer_customers.interfaces.ApplicationURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    //public Retrofit

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Interceptor networkInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Content-Type", "application/json");

                return chain.proceed(requestBuilder.build());
            }
        };
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(networkInterceptor)
               .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationURL.BaseURL+ ApplicationURL.api)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }

    public static Retrofit getClientSmp(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationURL.BaseURL+ ApplicationURL.api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }


    public static Retrofit getClientA() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient httpClient = new OkHttpClient();

        Interceptor networkInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();


                return chain.proceed(requestBuilder.build());
            }
        };
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addNetworkInterceptor(networkInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationURL.BaseURL+ ApplicationURL.api)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }



}
