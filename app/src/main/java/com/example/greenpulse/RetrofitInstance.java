package com.example.greenpulse;

import com.example.greenpulse.apiInterfaces.NewsApi;
import com.example.greenpulse.apiInterfaces.VideoApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static final Retrofit VideoFit = new Retrofit.Builder().
            baseUrl("https://yt-api.p.rapidapi.com/").
            addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final Retrofit NewsFit = new Retrofit.Builder().
            baseUrl("https://serpapi.com/").
            addConverterFactory(GsonConverterFactory.create())
            .build();

    public static VideoApi videoApi(){
        return VideoFit.create(VideoApi.class);
    }
    public static NewsApi newsApi(){return NewsFit.create(NewsApi.class);}
}