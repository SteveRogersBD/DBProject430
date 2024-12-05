package com.example.greenpulse.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.activities.CreatePostActivity;
import com.example.greenpulse.adapters.PostAdapter;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.FragmentMediaBinding;
import com.example.greenpulse.responses.AllPostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaFragment extends Fragment {
    public MediaFragment() {
        // Required empty public constructor
    }
    FragmentMediaBinding binding;
    GPApi gpApi;
    PostAdapter postAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMediaBinding.inflate(inflater, container, false);
        //gpApi = RetrofitInstance.gpApi();
        getPosts();
        binding.questionTv.setOnClickListener((v)->{
            getContext().startActivity(new Intent(getContext(), CreatePostActivity.class));
        });

        return binding.getRoot();
    }
    private void getPosts()
    {
        gpApi = RetrofitInstance.gpApi();
        gpApi.getAllPosts().enqueue(new Callback<AllPostResponse>() {
            @Override
            public void onResponse(Call<AllPostResponse> call, Response<AllPostResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    postAdapter = new PostAdapter(getContext(),response.body().data);
                    binding.postRecycler.setAdapter(postAdapter);
                    binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    Toast.makeText(getContext(), "Success from media", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllPostResponse> call, Throwable throwable) {
                //Toast.makeText(getContext(), "Error from media",
                        //Toast.LENGTH_LONG).show();
            }
        });
    }
}