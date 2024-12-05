package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.activities.requests.LogInRequest;
import com.example.greenpulse.activities.requests.RegisterRequest;
import com.example.greenpulse.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GPApi {
    @POST("/user/register")
    Call<UserResponse>registerUser(@Body RegisterRequest request);

    @POST("/user/login")
    Call<UserResponse>logInUser(@Body LogInRequest request);
}
