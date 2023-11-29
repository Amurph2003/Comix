package com.rit.comix.controllers;

import com.rit.user.User;

public class UserService {
    //For the purposes of time, im hardcoding this
    private static User[] users = {
        new User(0, "testUser1", ""),
        new User(1, "testUser2", "")
    };

    public static User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }
}
