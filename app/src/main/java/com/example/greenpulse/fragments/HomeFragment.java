package com.example.greenpulse.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.adapters.NewsAdapter;
import com.example.greenpulse.adapters.TextAdapter;
import com.example.greenpulse.adapters.VideoAdapter;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.databinding.FragmentHomeBinding;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.apiInterfaces.VideoApiUtil;
import com.example.greenpulse.responses.YouTubeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }
    FragmentHomeBinding binding;
    List<String>headers;
    TextAdapter headerAdapter;
    VideoAdapter videoAdapter;
    List<YouTubeResponse.Datum>videoList;
    VideoApiUtil videoApiUtil;
    NewsAdapter newsAdapter;
    List<NewsResponse.NewsResult>newsResults;
    NewsApiUtil newsApiUtil;
    VideoApi videoApi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //adds the headers on the top of the videos
        addTheHeaders();
        //initializes all the recyclerView
        initializeRecyclerViews();
        //adds the youtube videos
        //updateVideos("Farming & Agriculture");
        getVideos();
        updateNewsArticles("Farming & Agriculture");
        return binding.getRoot();
    }


    private void addTheHeaders()
    {
        headers = new ArrayList<>();
        headers.add("All");
        headers.add("Trending");
        headers.add("Farming");
        headers.add("Hacks");
        headers.add("Fertilizers");
        headers.add("Sustainability");
        headers.add("Agriculture");
        headers.add("Technology");
        headerAdapter = new TextAdapter(getContext(),headers);
        binding.textRecycler.setAdapter(headerAdapter);
        binding.textRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,
                false));
    }

    public void updateVideos(String query){
        videoApiUtil = new VideoApiUtil();

        videoApiUtil.fetchVideos(query,
                new VideoApiUtil.VideoCallBack() {
                    @Override
                    public void onSuccess(List<YouTubeResponse.Datum> videos) {
                        videoList.clear();
                        videoList.addAll(videos);
                        videoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void updateNewsArticles(String query)
    {   newsApiUtil = new NewsApiUtil(getContext());
        newsApiUtil.getNews(query, new NewsApiUtil.NewsCallBack() {
            @Override
            public void onSuccess(List<NewsResponse.NewsResult> newsList) {
                newsResults.clear();
                newsResults.addAll(newsList);
                newsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRecyclerViews(){
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(), videoList);
        binding.videoRecyclerHome.setAdapter(videoAdapter);
        binding.videoRecyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));
        newsResults = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(),newsResults);
        binding.newsRecyclerHome.setAdapter(newsAdapter);
        binding.newsRecyclerHome.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        return;
    }
    public void getVideos()
    {
        videoApi = RetrofitInstance.videoApi();
        videoApi.searchVideos("Agriculture & Farming").enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    try{
                        List<YouTubeResponse.Datum>contents = response.body().data;
                        List<YouTubeResponse.Datum>filtered = new ArrayList<>();
                        for(YouTubeResponse.Datum video:contents)
                        {
                            if(video.type.equals("video")) filtered.add(video);
                        }
                        videoList.addAll(filtered);
                        videoList.clear();
                        videoAdapter.notifyDataSetChanged();
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}