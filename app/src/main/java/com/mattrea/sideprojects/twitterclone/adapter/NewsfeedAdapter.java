package com.mattrea.sideprojects.twitterclone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattrea.sideprojects.twitterclone.R;
import com.mattrea.sideprojects.twitterclone.activity.LoginActivity;
import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h141764 on 3/6/16.
 */
public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.TweetViewHolder> {
    List<Tweet> list = null;
    Context context;

    public NewsfeedAdapter(List<Tweet> tweets){
        list =  tweets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Tweet> getItems(){
        return list;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_layout, parent, false);
        TweetViewHolder pvh = new TweetViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = list.get(position);

        holder.userName.setText(tweet.user);
        holder.body.setText(tweet.body);

        holder.timePosted.setText(tweet.getTimePosted());

        Picasso.with(context)
                .load(tweet.user_image_url)
                .placeholder(R.drawable.twitter_icon)
                .into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        if (list!=null)
            return list.size();
        else
            return 0;
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView timePosted;
        TextView body;
        ImageView userPhoto;

        TweetViewHolder(View itemView) {
            super(itemView);

            StringBuilder stringBuilder = new StringBuilder("hello");
            stringBuilder.toString();

            userName = (TextView)itemView.findViewById(R.id.user_name);
            timePosted = (TextView)itemView.findViewById(R.id.time_posted);
            body = (TextView)itemView.findViewById(R.id.body);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
        }
    }

}
