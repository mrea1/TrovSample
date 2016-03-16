package com.mattrea.sideprojects.twitterclone;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.EditText;

import com.mattrea.sideprojects.twitterclone.activity.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

/**
 * Created by h141764 on 3/15/16.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class LoginActivityTest extends InstrumentationTestCase {

    Button loginButton;
    EditText emailView;
    EditText passwordView;

    private LoginActivity activity;
    private ShadowActivity shadowActivity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(LoginActivity.class).create().get();
        shadowActivity = Shadows.shadowOf(activity);

        loginButton = (Button) activity.findViewById(R.id.email_sign_in_button);
        emailView = (EditText) activity.findViewById(R.id.email);
        passwordView = (EditText) activity.findViewById(R.id.password);
    }

    @Test
    public void loginSuccess() {
        emailView.setText("foo@example.com");
        passwordView.setText("foofoo");
        loginButton.performClick();

        Intent intent = shadowActivity.peekNextStartedActivity();
        assertTrue(intent.hasExtra("User"));
        assertTrue(intent.getClass().equals("NewsfeedActivity"));
    }

    @Test
    public void loginWithEmptyUsernameAndPassword() {
        loginButton.performClick();

        assertNull(shadowActivity.peekNextStartedActivity());
        assertNotNull("Show error for Email field ", emailView.getError());
        assertNotNull("Show error for Password field ", passwordView.getError());
    }

    @Test
    public void loginFailure() {
        emailView.setText("invalid");
        passwordView.setText("inval");
        loginButton.performClick();

        assertNull(shadowActivity.peekNextStartedActivity());
        assertNotNull("Show error for Email field ", emailView.getError());
        assertNotNull("Show error for Password field ", passwordView.getError());
    }
}

