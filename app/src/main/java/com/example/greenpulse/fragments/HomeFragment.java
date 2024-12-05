package com.example.greenpulse.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.adapters.NewsAdapter;
import com.example.greenpulse.adapters.TextAdapter;
import com.example.greenpulse.adapters.VideoAdapter;
import com.example.greenpulse.adapters.WeatherAdapter;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.apiInterfaces.WeatherApi;
import com.example.greenpulse.apiInterfaces.WeatherApiUtil;
import com.example.greenpulse.databinding.FragmentHomeBinding;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.apiInterfaces.VideoApiUtil;
import com.example.greenpulse.responses.WeatherResponse;
import com.example.greenpulse.responses.YouTubeResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private List<String> headers;
    private TextAdapter headerAdapter;
    private VideoAdapter videoAdapter;
    private List<YouTubeResponse.Datum> videoList = new ArrayList<>();
    private VideoApiUtil videoApiUtil;
    private NewsAdapter newsAdapter;
    private List<NewsResponse.NewsResult> newsResults = new ArrayList<>();
    private NewsApiUtil newsApiUtil;
    private VideoApi videoApi;
    private WeatherApiUtil weatherApiUtil;
    private WeatherAdapter weatherAdapter;
    private FusedLocationProviderClient locationProviderClient;
    private final int LOCATION_REQUEST_CODE = 100;
    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sp = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        askLocationPermission();
        addTheHeaders();
        initializeRecyclerViews();
        getVideos();
        updateNewsArticles("Farming & Agriculture");
        return binding.getRoot();
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            getAndStoreLocation();
            getWeatherInfo();
        }
    }

    private void addTheHeaders() {
        headers = new ArrayList<>();
        headers.add("All");
        headers.add("Trending");
        headers.add("Farming");
        headers.add("Hacks");
        headers.add("Fertilizers");
        headers.add("Sustainability");
        headers.add("Agriculture");
        headers.add("Technology");
        headerAdapter = new TextAdapter(requireContext(), headers);
        binding.textRecycler.setAdapter(headerAdapter);
        binding.textRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void updateVideos(String query) {
        videoApiUtil = new VideoApiUtil();
        videoApiUtil.fetchVideos(query, new VideoApiUtil.VideoCallBack() {
            @Override
            public void onSuccess(List<YouTubeResponse.Datum> videos) {
                videoList.clear();
                videoList.addAll(videos);
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to load videos: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateNewsArticles(String query) {
        newsApiUtil = new NewsApiUtil(requireContext());
        newsApiUtil.getNews(query, new NewsApiUtil.NewsCallBack() {
            @Override
            public void onSuccess(List<NewsResponse.NewsResult> newsList) {
                newsResults.clear();
                newsResults.addAll(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to load news: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRecyclerViews() {
        videoAdapter = new VideoAdapter(requireContext(), videoList);
        binding.videoRecyclerHome.setAdapter(videoAdapter);
        binding.videoRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext()));

        newsAdapter = new NewsAdapter(requireContext(), newsResults);
        binding.newsRecyclerHome.setAdapter(newsAdapter);
        binding.newsRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void getVideos() {
        videoApi = RetrofitInstance.videoApi();
        videoApi.searchVideos("Agriculture & Farming").enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoList.clear();
                    for (YouTubeResponse.Datum video : response.body().data) {
                        if ("video".equals(video.type)) {
                            videoList.add(video);
                        }
                    }
                    videoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to load videos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Failed to load videos: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeatherInfo() {
//        String[] location = new String[2];
//        retrieveLocation(location);
//
//        if (location[0] == null || location[1] == null || location[0].isEmpty() || location[1].isEmpty()) {
//            Log.e("getWeatherInfo", "Location data is invalid or null.");
//            return;
//        }
//
//        try {
//            double lat = Double.parseDouble(location[0]);
//            double lon = Double.parseDouble(location[1]);
//
//            weatherApiUtil = new WeatherApiUtil(requireContext());
//            weatherApiUtil.getWeather(49.76, 99.67, WeatherApiUtil.DAILY, new WeatherApiUtil.WeatherCallBack<WeatherResponse.Datum>() {
//                @Override
//                public void onSuccess(List<WeatherResponse.Datum> resultList) {
//                    Toast.makeText(getContext(), "Success!!!", Toast.LENGTH_LONG).show();
//                    weatherAdapter = new WeatherAdapter(getContext(), resultList);
//                    binding.weatherRecyclerHome.setAdapter(weatherAdapter);
//                    binding.weatherRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//                }
//
//                @Override
//                public void onError(String errorMessage) {
//                    //Toast.makeText(requireContext(), "Failed to load weather data: " + errorMessage, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_LONG).show();
//                }
//            });
//        } catch (NumberFormatException e) {
//            Log.e("getWeatherInfo", "Invalid location format", e);
//        }
        WeatherApi weatherApi = RetrofitInstance.weatherApi();
        weatherApi.getWeather(34.67,99.89,getString(R.string.weatherKey)).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    weatherAdapter = new WeatherAdapter(getContext(),response.body().data);
                    binding.weatherRecyclerHome.setAdapter(weatherAdapter);
                    binding.weatherRecyclerHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable throwable) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getAndStoreLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("lat", String.valueOf(lat));
                editor.putString("lon", String.valueOf(lon));
                editor.apply();
            }
        });
    }

    private void retrieveLocation(String[] location) {
        location[0] = sp.getString("lat", null);
        location[1] = sp.getString("lon", null);
    }
}

