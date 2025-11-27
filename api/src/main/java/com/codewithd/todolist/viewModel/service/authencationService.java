package com.codewithd.todolist.viewModel.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.user;

@Component
public class authencationService {
    static final Logger LOG = Logger.getLogger(authencationService.class.getName());
    public static final Random RNG = new Random();

    private final userService userService;
    private HashMap<String, Integer> sessionStorage;
    private HashMap<Integer, String> reverseSessionStorage;

    public authencationService(userService userService){
        this.userService = userService;
        this.sessionStorage = new HashMap<>();
        this.reverseSessionStorage = new HashMap<>();
    }

    public String createUser(String userName, String password) throws IOException{
        user user = this.userService.createUser(userName, password);
        if(user != null){
            LOG.log(Level.INFO, "user created Successfully");
            return login(userName, password);
        }else{
            LOG.log(Level.INFO, "Unable to create user.");
            return null;
        }
        
    }
    public boolean updateUser(String sessionToken, String userName, String password) throws IOException{
        Integer userId = sessionStorage.get(sessionToken);

        if(userId == null){
            LOG.log(Level.WARNING, "Attempted to update user with invalid session token: {0}", sessionToken);
            return false;
        }
        user user = userService.getUser(userId);

        if(user == null){
            LOG.log(Level.WARNING, "No user found for session token: {0}", sessionToken);
            logout(sessionToken);
            return false;
        }
        String newName = (userName != null && !userName.isEmpty()) ? userName : user.getUsername();
        String newPassword = (password != null && !password.isEmpty()) ? password : null;

        if(userService.updateUser(userId, newName, newPassword)){
            LOG.log(Level.INFO, "User {0} updated successfully", userId);
            return true;
        }else{
            LOG.log(Level.INFO, "Failed to update user {0}", userId);
            return false;
        }


    }
    public String login(String userName, String password) throws IOException{
        user user = userService.authenciate(userName, password);
        
        if(user != null){
            String sessionToken = generateSessionKey();
            LOG.log(Level.INFO, "User {0} authenciated successfully", userName);
            if(reverseSessionStorage.containsKey(user.getID())){
                String oldToken = reverseSessionStorage.get(user.getID());
                sessionStorage.remove(oldToken);
            }
            sessionStorage.put(sessionToken, user.getID());
            reverseSessionStorage.put(user.getID(), sessionToken);
            return sessionToken;
        }
        return null;
    }
    public boolean logout(String sessionToken) throws IOException{
        if(sessionStorage.containsKey(sessionToken)){
            int userId = sessionStorage.get(sessionToken);

            sessionStorage.remove(sessionToken);
            reverseSessionStorage.remove(userId);
            LOG.log(Level.INFO, "Session {0} logged off successfully", sessionToken);
            return true;
        }
        LOG.log(Level.WARNING, "Attempted to logout with invalid session token: {0}", sessionToken);
        return false;
    }
    public boolean deleteUser(String sessionToken) throws IOException{
        if(sessionStorage.containsKey(sessionToken)){
            int userId = sessionStorage.get(sessionToken);
            sessionStorage.remove(sessionToken);
            reverseSessionStorage.remove(userId);
            userService.deleteUser(userId);
            LOG.log(Level.INFO, "User {0} deleted successfully", sessionToken);
            return true;
        }
        LOG.log(Level.WARNING, "user {0} failed to delete", sessionToken);
        return false;
    }
    public String getUsername(String sessionToken) throws IOException{
        if(sessionStorage.containsKey(sessionToken)){
            int userId = sessionStorage.get(sessionToken);
            user user = userService.getUser(userId);
            return user.getUsername();
        }
        return null;
    }
    public user getUser(String sessionToken)throws IOException{
        if(sessionStorage.containsKey(sessionToken)){
            int userId = sessionStorage.get(sessionToken);
            user user = userService.getUser(userId);
            LOG.log(Level.INFO, "Session {0} user returned", sessionToken);
            return user;
        }
        LOG.log(Level.WARNING, "Session {0} user not found", sessionToken);
        return null;
    }
    public String generateSessionKey(){
        byte[] randomBytes = new byte[16];
        StringBuilder sb = new StringBuilder();

        RNG.nextBytes(randomBytes);

        for(byte b : randomBytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    
}
