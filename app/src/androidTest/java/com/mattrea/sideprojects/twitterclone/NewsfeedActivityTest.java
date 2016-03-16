package com.mattrea.sideprojects.twitterclone;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.mattrea.sideprojects.twitterclone.activity.NewsfeedActivity;
import com.mattrea.sideprojects.twitterclone.adapter.NewsfeedAdapter;
import com.mattrea.sideprojects.twitterclone.fragment.NewsfeedFragment;
import com.mattrea.sideprojects.twitterclone.model.Tweet;
import com.mattrea.sideprojects.twitterclone.model.User;
import com.mattrea.sideprojects.twitterclone.network.ApiManager;
import com.orm.SugarRecord;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class NewsfeedActivityTest{

    private NewsfeedFragment myFragment;
    private NewsfeedActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(NewsfeedActivity.class).create().get();
        myFragment = new NewsfeedFragment();
        startFragment(myFragment);
        // SugarORM need some time to start up, else tests will fail
        Thread.sleep(3000);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void checkFragmentNotBeNull() throws Exception{
        assertNotNull(myFragment);
    }

    @Test
    public void testDatabaseFull() throws Exception{
        myFragment.deleteAll();
        User user = new User();
        user.setUsername("Test");
        myFragment.getTweets(user);
        List<Tweet> tweetList = Tweet.listAll(Tweet.class);

        assertEquals(tweetList.size(), 16);
    }

    @Test
    public void testDatabaseEmpty() throws Exception{
        SugarRecord.delete(Tweet.class);
        assertFalse(SugarRecord.isSugarEntity(Tweet.class));
    }


    @Test
    public void testDeleteAll() throws Exception{
        myFragment.deleteAll();
        RecyclerView recyclerView = (RecyclerView) myFragment.getView().findViewById(R.id.recyclerView);
        NewsfeedAdapter adapter = (NewsfeedAdapter) recyclerView.getAdapter();

        Assert.assertEquals(adapter.getItemCount(), 0);
    }


    @Test
    public void testTweetsAreShown() throws Exception {
        RecyclerView recyclerView = (RecyclerView) myFragment.getView().findViewById(R.id.recyclerView);
        NewsfeedAdapter adapter = (NewsfeedAdapter) recyclerView.getAdapter();

        Assert.assertEquals(adapter.getItemCount(), 16);

        //Test that all 16 Tweets are displayed with the correct text
        for (int i=0;i<16;i++){
            assertEquals(adapter.getItems().get(i).body,"Example Tweet "+(i+1));
        }
    }

    @Test
    public void testPostNewTweet() throws Exception {
        Button button = (Button) activity.findViewById(R.id.fab);
        button.performClick();

        //Test that the recyclerview contains a new item at the correct position with the correct text
        RecyclerView recyclerView = (RecyclerView) myFragment.getView().findViewById(R.id.recyclerView);
        NewsfeedAdapter adapter = (NewsfeedAdapter) recyclerView.getAdapter();
        int lastItem = adapter.getItems().size()-1;
        Tweet lastTweet = adapter.getItems().get(lastItem);

        assertEquals("Example of a new tweet", lastTweet.body);
    }

    public static void startFragment( Fragment fragment )
    {
        FragmentManager fragmentManager = new FragmentActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
    }

}