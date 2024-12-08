package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.activities.requests.LogInRequest;
import com.example.greenpulse.activities.requests.RegisterRequest;
import com.example.greenpulse.responses.AllPostResponse;
import com.example.greenpulse.responses.CropResponse;
import com.example.greenpulse.responses.PostResponse;
import com.example.greenpulse.responses.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    @GET("admin/all")
    Call<AllPostResponse> getAllPosts();

    @GET("crop/get/name/{name}")
    Call<CropResponse>getCropByName(@Path("name") String name);
}
