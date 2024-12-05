package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.activities.requests.LogInRequest;
import com.example.greenpulse.activities.requests.RegisterRequest;
import com.example.greenpulse.responses.AllPostResponse;
import com.example.greenpulse.responses.PostResponse;
import com.example.greenpulse.responses.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GPApi {
    @POST("user/register")
    Call<UserResponse>registerUser(@Body RegisterRequest request);

    @POST("user/login")
    Call<UserResponse>logInUser(@Body LogInRequest request);

    @Multipart
    @POST("post/create/user/{userId}")
    Call<PostResponse> createPost(
            @Path("userId") Long userId,
            @Part("title") String title,
            @Part("content") String content,
            @Part MultipartBody.Part file // To handle file uploads
    );
    // Define the GET request for retrieving all posts
    @GET("all")
    Call<AllPostResponse> getAllPosts();
}
