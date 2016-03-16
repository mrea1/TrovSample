package com.mattrea.sideprojects.twitterclone;

import android.content.Context;

import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.mattrea.sideprojects.twitterclone.model.TwitterResponse;
import com.mattrea.sideprojects.twitterclone.model.User;
import com.mattrea.sideprojects.twitterclone.network.ApiManager;
import com.mattrea.sideprojects.twitterclone.network.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by h141764 on 3/16/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class NetworkTest {

    private ShadowApplication shadowApplication;
    private ApiManager.TwitterCloneAPI apiService;
    private Context context;

    @Before
    public void setUp() throws Exception {
        shadowApplication = ShadowApplication.getInstance();
        context =  shadowApplication.getApplicationContext();
    }

    @Test
    public void testGetTweetsSuccessResponse() throws Exception{
        ApiManager.initializeClient(context, false);
        apiService = ApiManager.apiService;

        User user = new User();
        user.setUsername("Test");

        Call<List<Tweet>> call = apiService.getTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                assertNotNull(response.body());
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                ;
            }
        });

    }

    @Test
    public void testGetTweetsErrorResponse() throws Exception{
        ApiManager.initializeClient(context, true);
        apiService = ApiManager.apiService;

        User user = new User();
        user.setUsername("Test");

        Call<List<Tweet>> call = apiService.getTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                assert(response.body().toString().contains("Error"));
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                ;
            }
        });

    }



    @Test
    public void testPostTweetSuccessResponse() throws Exception{
        ApiManager.initializeClient(context, false);
        apiService = ApiManager.apiService;

        User user = new User();
        user.setUsername("Test");

        final Tweet tweet = new Tweet();
        tweet.body = "Example of a new tweet";
        tweet.timestamp = Utils.getCurrentDateTimeFormatted();
        tweet.user = user.getUsername();
        tweet.tweet_id = "001";
        tweet.user_image_url = "http://davidpapp.com/wp-content/uploads/2013/10/twitter.png";

        Call<TwitterResponse> call = apiService.postTweet(tweet);
        call.enqueue(new Callback<TwitterResponse>() {
            @Override
            public void onResponse(Call call, Response response) {
                assert(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<TwitterResponse> call, Throwable t) {
            }
        });
    }

    @Test
    public void testPostTweetErrorResponse() throws Exception{
        ApiManager.initializeClient(context, true);
        apiService = ApiManager.apiService;

        User user = new User();
        user.setUsername("Test");

        final Tweet tweet = new Tweet();
        tweet.body = "Example of a new tweet";
        tweet.timestamp = Utils.getCurrentDateTimeFormatted();
        tweet.user = user.getUsername();
        tweet.tweet_id = "001";
        tweet.user_image_url = "http://davidpapp.com/wp-content/uploads/2013/10/twitter.png";

        Call<TwitterResponse> call = apiService.postTweet(tweet);
        call.enqueue(new Callback<TwitterResponse>() {
            @Override
            public void onResponse(Call call, Response response) {
                assert(response.body().toString().contains("Error"));
            }

            @Override
            public void onFailure(Call<TwitterResponse> call, Throwable t) {
            }
        });
    }

}
