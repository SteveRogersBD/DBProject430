package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.responses.CropResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SearchView searchView = findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GPApi cropApi = RetrofitInstance.gpApi();
                cropApi.getCropByName(query).enqueue(new Callback<CropResponse>() {
                    @Override
                    public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                        if(response.isSuccessful() && response.body()!=null)
                        {
                            Toast.makeText(SearchActivity.this, response.body().data.cropName,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CropResponse> call, Throwable throwable) {
                        Toast.makeText(SearchActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}