package com.demo.hungnguyendev.modules.users.resources;

public class UserResource {
    private final Long id;
    private final String email;
    private final String name; // Added property name

    public UserResource(Long id, String email, String name){ // Updated constructor
        this.id = id;
        this.email = email;
        this.name = name; // Initialize name
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() { // Added getter for name
        return name;
    }
}
