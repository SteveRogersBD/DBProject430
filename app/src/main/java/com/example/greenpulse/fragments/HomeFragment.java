package com.example.greenpulse.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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
import com.example.greenpulse.activities.SearchActivity;
import com.example.greenpulse.adapters.NewsAdapter;
import com.example.greenpulse.adapters.TextAdapter;
import com.example.greenpulse.adapters.WeatherAdapter;
import com.example.greenpulse.adapters.YoutubeAdapter;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.apiInterfaces.NewsApi;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.apiInterfaces.WeatherApi;
import com.example.greenpulse.apiInterfaces.WeatherApiUtil;
import com.example.greenpulse.databinding.FragmentHomeBinding;
import com.example.greenpulse.responses.CropResponse;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.apiInterfaces.VideoApiUtil;
import com.example.greenpulse.responses.WeatherResponse;
import com.example.greenpulse.responses.YouTubeResponse;
import com.example.greenpulse.responses.YoutubeVideo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private List<String> headers;
    private TextAdapter headerAdapter;
    private YoutubeAdapter videoAdapter;
    private List<YoutubeVideo.VideoResult> videoList = new ArrayList<>();
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
        binding.searchViewHome.setOnClickListener((v)->{
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        //getVideos();
        newsApiUtil = new NewsApiUtil(requireContext());
        //updateNewsArticles("Farming & Agriculture");
        //updateVideos("Farming & Agriculture");
        //getYTVideos();
        getWeatherInfo();
        return binding.getRoot();
    }

    private void getVideos() {
        VideoApi videoApi1 = RetrofitInstance.videoApi();
        Call call = videoApi1.searchVideos("Trending Videos");
        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                YouTubeResponse youTubeResponse = response.body();

                for (YouTubeResponse.Datum datum : youTubeResponse.data) {
                    if ("video".equals(datum.type)) {
                        // Process standard videos
                        List<YouTubeResponse.Thumbnail> thumbnails = datum.thumbnail; // ArrayList<Thumbnail>
                        for (YouTubeResponse.Thumbnail thumbnail : thumbnails) {
                            Log.d("VideoThumbnail", thumbnail.url);
                        }
                    } else {
                        // Skip or handle other types like shorts
                        Log.d("OtherType", "Skipping type: " + datum.type);
                    }
                }
                Toast.makeText(getContext(), "Success video!!!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call call, Throwable throwable) {
                binding.errorTv.setText(throwable.getLocalizedMessage());
            }
        });
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

        newsApiUtil.getResources(query, 3, new NewsApiUtil.NewsCallBack2<YoutubeVideo.VideoResult>() {
            @Override
            public void onSuccess(List<YoutubeVideo.VideoResult> resourceList) {
                Toast.makeText(getContext(), "Video Success!!!", Toast.LENGTH_SHORT).show();
                videoList.clear();
                videoList.addAll(resourceList);
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                binding.errorTv.setText(errorMessage);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getYTVideos()
    {
        NewsApi newsApi = RetrofitInstance.newsApi();
        Call call = newsApi.
                getVideos(getString(R.string.newApiKey),"youtube_video","Farming Videos");
        call.enqueue(new Callback<YoutubeVideo>() {
            @Override
            public void onResponse(Call<YoutubeVideo> call, Response<YoutubeVideo> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    try{

                    }
                    catch (Exception e)
                    {
                        binding.errorTv.setText(e.getLocalizedMessage());
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    binding.errorTv.setText(response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call call, Throwable throwable) {
                binding.errorTv.setText(throwable.getLocalizedMessage());
                Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNewsArticles(String query) {

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
        videoAdapter = new YoutubeAdapter(requireContext(), videoList);
        binding.videoRecyclerHome.setAdapter(videoAdapter);
        binding.videoRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext()));

        newsAdapter = new NewsAdapter(requireContext(), newsResults);
        binding.newsRecyclerHome.setAdapter(newsAdapter);
        binding.newsRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
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
                binding.errorTv.setText(throwable.getLocalizedMessage());
                //Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

