package com.demo.hungnguyendev.modules.users.resources;

public class LoginResource {
    private String token;
    private UserResource user;

    public LoginResource(String token, UserResource user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResource getUser() {
        return user;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }
}
