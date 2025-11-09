package com.codewithd.todolist.model.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;

import com.codewithd.todolist.model.user;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class userFileDAO implements userDAO {
    private static final Logger log = Logger.getLogger(userFileDAO.class.getName());

    Map<Integer, user> users;
    private ObjectMapper objectMapper;
    private static int nextId;
    private String filename;

    public userFileDAO(@Value("${tasks.file}") String filename, ObjectMapper objectMapper) throws IOException{
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();

        log.info("users file successfully loaded");
    }

    private synchronized static int nextId(){
        int id = nextId;
        ++nextId;
        return id;
    }

    private user[] getUserArray(){
        return getUserArray(null);
    }
    private user[] getUserArray(String containstext){
        ArrayList<user> userList = new ArrayList<user>();

        for(user user:users.values()){
            if(containstext == null || user.getUsername().contains(containstext)){
                userList.add(user);
            }
        }
        user[] userArray = new user[userList.size()];

        userList.toArray(userArray);
        return userArray;
    }

    private boolean save() throws IOException{
        user[] taskArray = getUserArray();
        objectMapper.writeValue(new File(filename), taskArray);
        return true;
    }
    private boolean load() throws IOException{
        users = new TreeMap<>();
        nextId = 0;
        user[] userArray = objectMapper.readValue(new File(filename), user[].class);

        for(user user:userArray){
            users.put(user.getID(),user);
            if(user.getID()>nextId)
                nextId = user.getID();
        }
        ++nextId;
        return true;
    }

    @Override
    public user[] getUsers() throws IOException {
        synchronized(users){
        return getUserArray();
        }
    }

    @Override
    public user[] findUsers(String containsText) throws IOException {
        synchronized(users){
        return getUserArray(containsText);
        }
    }

    @Override
    public user getUser(int id) throws IOException {
        synchronized(users){
            if(users.containsKey(id)){
                return users.get(id);
            }else{
                return null;
            }
        }
    }

    @Override
    public user findUserByName(String username) throws IOException {
        synchronized(users){
            for(user user: users.values()){
                if(user.getUsername().equals(username)){
                    return user;
                }
            }
            return null;
        }
    }

    @Override
    public user createUser(user user) throws IOException {
        synchronized(users){
            user u = new user(nextId(), user.getUsername(), user.getPassword());
            users.put(u.getID(),u);
            save();
            return u;
        }
    }

    @Override
    public user updateUser(user user) throws IOException {
        synchronized(users){
            if(users.containsKey(user.getID()) == false){
                return null;
            }
            users.put(user.getID(),user);
            save();
            return user;

        }
    }

    @Override
    public boolean deleteUser(user user) throws IOException {
        synchronized(users){
            if(users.containsKey(user.getID()) == false){
                return false;
            }
            users.remove(user.getID());
            save();
            return true;
        }
    }
    
}
