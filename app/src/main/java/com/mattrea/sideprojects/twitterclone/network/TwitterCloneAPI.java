package com.mattrea.sideprojects.twitterclone.network;

import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.mattrea.sideprojects.twitterclone.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by h141764 on 3/8/16.
 */
public interface TwitterCloneAPI {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("users/{username}")
    Call<User> logIn(@Path("username") String username);

    @GET("tweets/getAll")
    Call<List<Tweet>> getTweets(@Path("id") String username);

    @POST("tweets/new")
    Call<User> postTweet(@Body Tweet tweet);
}
