package com.mattrea.sideprojects.twitterclone.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mattrea.sideprojects.twitterclone.R;
import com.mattrea.sideprojects.twitterclone.adapter.NewsfeedAdapter;
import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.mattrea.sideprojects.twitterclone.model.TwitterResponse;
import com.mattrea.sideprojects.twitterclone.model.User;
import com.mattrea.sideprojects.twitterclone.network.ApiManager;
import com.mattrea.sideprojects.twitterclone.network.Utils;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsfeedFragment extends Fragment {

    User user = null;
    NewsfeedAdapter adapter = null;
    ArrayList<Tweet> tweets = null;

    @Bind(R.id.root_view)
    View mRoot;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView = null;

    private ApiManager.TwitterCloneAPI apiService;

    public void onCreate(Bundle onSavedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(onSavedInstanceState);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            case R.id.get_tweets:
                getTweets(user);
                return true;
            default:
                break;
        }
        return false;
    }

    public void deleteAll(){
        tweets.clear();
        adapter.notifyDataSetChanged();
        Tweet.deleteAll(Tweet.class);
        SugarRecord.delete(Tweet.class);
    }

    public static Fragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("User",user);

        NewsfeedFragment fragment = new NewsfeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        user = (User) getArguments().getSerializable("User");
        apiService = ApiManager.apiService;

        tweets = new ArrayList<Tweet>();
        //Get items from API
        getTweets(user);

        //Just to test the database
        List<Tweet> queryResults = Tweet.listAll(Tweet.class);
        for (int i = 0 ; i < queryResults.size() ; i++)
            Log.d("Fragment", "Tweets: " + queryResults.get(i));

        adapter = new NewsfeedAdapter(tweets);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void getTweets(User user){
        List<Tweet> queryResults = Tweet.listAll(Tweet.class);

        if (queryResults!=null && queryResults.isEmpty()) {
            Call<List<Tweet>> call = apiService.getTweets();
            call.enqueue(new Callback<List<Tweet>>() {
                @Override
                public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                    tweets.clear();
                    tweets.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    //Add to Database
                    SugarRecord.saveInTx(tweets);
                }

                @Override
                public void onFailure(Call<List<Tweet>> call, Throwable t) {
                    Snackbar snackbar = Snackbar.make(mRoot, getString(R.string.get_tweets_failure), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }else{
            //load all from database
            tweets.addAll(Tweet.listAll(Tweet.class));
        }
    }

    //Add a tweet to the recyclerview
    //Also add to database
    //Show snackbar
    @OnClick(R.id.fab)
    public void postTweet(){
        final Tweet tweet = new Tweet();
        tweet.body = "Example of a new tweet";
        tweet.timestamp = Utils.getCurrentDateTimeFormatted();
        tweet.user = user.getUsername();

        if (Tweet.last(Tweet.class)!=null)
            tweet.tweet_id = Integer.toString(Integer.valueOf(Tweet.last(Tweet.class).tweet_id) + 1);
        else
            tweet.tweet_id = "001";

        tweet.user_image_url = "http://davidpapp.com/wp-content/uploads/2013/10/twitter.png";
        tweet.save();

        tweets.add(tweet);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(tweets.size() - 1);

        Call<TwitterResponse> call = apiService.postTweet(tweet);
        call.enqueue(new Callback<TwitterResponse>() {
            @Override
            public void onResponse(Call call, Response response) {
                int statusCode = response.code();

                if (response.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(mRoot, getString(R.string.tweet_posted), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(mRoot, getString(R.string.tweet_post_failed), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<TwitterResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(mRoot, getString(R.string.tweet_post_failed), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
