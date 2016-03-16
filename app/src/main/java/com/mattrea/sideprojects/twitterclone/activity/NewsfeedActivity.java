package com.mattrea.sideprojects.twitterclone.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mattrea.sideprojects.twitterclone.R;
import com.mattrea.sideprojects.twitterclone.fragment.NewsfeedFragment;
import com.mattrea.sideprojects.twitterclone.model.User;

public class NewsfeedActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getExtras().getSerializable("User");

        Fragment fragment = NewsfeedFragment.newInstance(user);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_newsfeed, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_all) {
            return false;
        }else if (id == R.id.get_tweets){
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
}
