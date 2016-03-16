package com.mattrea.sideprojects.twitterclone.model;

import java.io.Serializable;

/**
 * Created by h141764 on 3/8/16.
 */

public class User implements Serializable {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
