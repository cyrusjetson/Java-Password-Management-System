package com.passwordmanager;

public class User {
    private String username;
    private String name;
    private String password;

    // ---- Getters and Setters ----
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void deleteAll(){
        this.name = "";
        this.username = "";
        this.password = "";
    }

}
