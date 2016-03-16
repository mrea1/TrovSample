package com.mattrea.sideprojects.twitterclone.network;

import android.content.Context;
import android.provider.CallLog;

import com.google.gson.Gson;
import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.mattrea.sideprojects.twitterclone.model.TwitterResponse;
import com.mattrea.sideprojects.twitterclone.model.User;
import com.squareup.picasso.Downloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by h141764 on 3/8/16.
 */
public class ApiManager {

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    public interface TwitterCloneAPI {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("users/login")
        Call<User> logIn();

        @GET("tweets/getall")
        Call<List<Tweet>> getTweets();

        @POST("tweets/new")
        Call<TwitterResponse> postTweet(@Body Tweet tweet);
    }

    public static void initializeClient(Context context, boolean error){
        LocalResponseInterceptor interceptor = new LocalResponseInterceptor(context);
        interceptor.setError(error);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiService = retrofit.create(ApiManager.TwitterCloneAPI.class);
    }

    public static ApiManager.TwitterCloneAPI apiService;


    public static String BASE_URL = "http://api.twitter.com/";
}
