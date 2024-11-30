package com.example.greenpulse.apiInterfaces;

import android.content.Context;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.responses.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsApiUtil {

    public static NewsApi newsApi = RetrofitInstance.newsApi();
    private Context context;

    public NewsApiUtil(Context context) {
        this.context = context;
    }
    public NewsApiUtil() {}

    public void getNews(String query, NewsCallBack callBack)
    {
        Call<NewsResponse>newsCall = newsApi.getNews(context.getString(R.string.newApiKey),
                "google_news",query,"us","en");
        newsCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                callBack.onSuccess(response.body().news_results);
                Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable throwable) {
                callBack.onError(throwable.getLocalizedMessage());
            }
        });
    }

    public interface NewsCallBack{
        public void onSuccess(List<NewsResponse.NewsResult>newsList);
        public void onError(String errorMessage);
    }

}
