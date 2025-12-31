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

    public userFileDAO(@Value("${user.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
        log.info("users file successfully loaded");
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private user[] getUserArray() {
        return getUserArray(null);
    }

    private user[] getUserArray(String containstext) {
        ArrayList<user> userList = new ArrayList<user>();

        for (user user : users.values()) {
            // FIXED: Added null check for getUsername() to prevent NPE during search
            if (containstext == null || (user.getUsername() != null && user.getUsername().contains(containstext))) {
                userList.add(user);
            }
        }
        user[] userArray = new user[userList.size()];
        userList.toArray(userArray);
        return userArray;
    }

    private boolean save() throws IOException {
        user[] taskArray = getUserArray();
        objectMapper.writeValue(new File(filename), taskArray);
        return true;
    }

    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;
        File file = new File(filename);
        
        // Handle case where file doesn't exist yet
        if (!file.exists()) {
            return true;
        }

        user[] userArray = objectMapper.readValue(file, user[].class);

        for (user user : userArray) {
            users.put(user.getID(), user);
            if (user.getID() >= nextId)
                nextId = user.getID();
        }
        nextId++;
        return true;
    }

    @Override
    public user getUser(int id) throws IOException {
        synchronized (users) {
            return users.get(id);
        }
    }

    @Override
    public user findUserByName(String username) throws IOException {
        synchronized (users) {
            if (username == null) return null;
            for (user user : users.values()) {
                // FIXED: Null-safe comparison. Puts 'username' first to avoid NPE if user.getUsername() is null
                if (username.equals(user.getUsername())) {
                    return user;
                }
            }
            return null;
        }
    }

    @Override
    public user createUser(user user) throws IOException {
        synchronized (users) {
            // FIXED: Added safety check to prevent creating users with null names
            if (user.getUsername() == null) {
                log.warning("Attempted to create a user with a null username.");
                return null; 
            }
            user u = new user(nextId(), user.getUsername(), user.getPassword());
            users.put(u.getID(), u);
            save();
            return u;
        }
    }

    @Override
    public boolean updateUser(user user) throws IOException {
        synchronized (users) {
            if (!users.containsKey(user.getID())) {
                return false;
            }
            users.put(user.getID(), user);
            save();
            return true;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws IOException {
        synchronized (users) {
            if (!users.containsKey(userId)) {
                return false;
            }
            users.remove(userId);
            save();
            return true;
        }
    }

    @Override
    public user authenciate(String username, String password) throws IOException {
        if (username == null || password == null) return null;
        
        user user = findUserByName(username);
        // FIXED: Null-safe password check
        return (user != null && password.equals(user.getPassword())) ? user : null;
    }
}