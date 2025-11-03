package com.codewithd.todolist.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class user {
    
    public static final String STRING_FORMAT = "user [id=%d, username=%s, password=[HIDDEN]]";
    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public user(@JsonProperty("id") int id, 
                @JsonProperty("username") String username,
                @JsonProperty("password") String password){
                    this.id = id;
                    this.username = username;
                    this.password = password;
                }

    
    public int getID(){
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    
    @Override
    public String toString(){
        return String.format(STRING_FORMAT, id,username);
    }

}
