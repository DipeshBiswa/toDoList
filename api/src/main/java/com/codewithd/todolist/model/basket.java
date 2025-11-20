package com.codewithd.todolist.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class basket {
    @JsonProperty("id")
    private int id;
    @JsonProperty("tasks")
    private task[] tasks;
    @JsonProperty("userId")
    private int userId;

}
