package com.rit.user;

public class User {
    private int id;
    private String username;
    private String authCookie;
    
    public User(int id, String username, String authCookie) {
        this.id = id;
        this.username = username;
        this.authCookie = authCookie;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthCookie() {
        return authCookie;
    }

    public void setAuthCookie(String authCookie) {
        this.authCookie = authCookie;
    }
}
