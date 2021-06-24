package com.example.userservice.Payload;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Long cash;


    public UserProfile(Long id, String username, String name, Long cash){
    this.id = id;
    this.username = username;
    this.name = name;
    this.cash = cash;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCash() {
        return cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

}