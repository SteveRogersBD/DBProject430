package com.example.greenpulse.apiInterfaces;

import android.content.Context;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.responses.ImageResponse;
import com.example.greenpulse.responses.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsApiUtil {

    public static NewsApi newsApi = RetrofitInstance.newsApi();
    private Context context;
    public static final int NEWS = 1;
    public static final int IMAGES = 2;

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

    public <T> void getResources(String query, int tag, NewsCallBack2<T> newsCallBack2)
    {
        Call<T>call = null;
        if(tag==IMAGES)
        {
            call =(Call<T>) newsApi.getImages(context.getString(R.string.newApiKey),"google_images",query);
        }
        else if(tag==NEWS)
        {
            call =(Call<T>) newsApi.getNews(context.getString(R.string.newApiKey),
                    "google_news",query,"us","en");
        }
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    try{
                        if(tag==IMAGES)
                        {
                            ImageResponse imageResponse = (ImageResponse) response.body();
                            newsCallBack2.onSuccess((List<T>) imageResponse.images_results);
                        }
                        else if(tag==NEWS)
                        {
                            NewsResponse newsResponse = (NewsResponse) response.body();
                            newsCallBack2.onSuccess((List<T>) newsResponse.news_results);
                        }
                    }
                    catch(Exception e)
                    {
                        newsCallBack2.onError(e.getLocalizedMessage());
                    }
                }
                else{
                    newsCallBack2.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
                newsCallBack2.onError(throwable.getLocalizedMessage());
            }
        });
    }

    public interface NewsCallBack{
        public void onSuccess(List<NewsResponse.NewsResult>newsList);
        public void onError(String errorMessage);
    }

    public interface NewsCallBack2<T>{
        public void onSuccess(List<T>resourceList);
        public void onError(String errorMessage);
    }

}
