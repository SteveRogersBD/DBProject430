package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.responses.AllPostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaActivity extends AppCompatActivity {

    GPApi gpApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        gpApi = RetrofitInstance.gpApi();
        gpApi.getAllPosts().enqueue(new Callback<AllPostResponse>() {
            @Override
            public void onResponse(Call<AllPostResponse> call, Response<AllPostResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    try{
                        Toast.makeText(MediaActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Toast.makeText(MediaActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllPostResponse> call, Throwable throwable) {
                Toast.makeText(MediaActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}