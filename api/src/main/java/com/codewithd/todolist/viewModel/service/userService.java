package com.codewithd.todolist.viewModel.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.user;
import com.codewithd.todolist.model.persistence.userDAO;

@Component
public class userService {

    private final userDAO userDAO;
    static final Random RNG = new Random();

    public userService(userDAO userDAO){
        this.userDAO = userDAO;      
    }

    public user getUser(int id) throws IOException {
        return this.userDAO.getUser(id); // Correct: Calling the DAO
    }
    public user findUserByName(String username) throws IOException{
        return this.userDAO.findUserByName(username);
    }
    public user createUser(String username, String password) throws IOException {
        // Pass -1 or 0 for the ID; the userFileDAO.createUser will overwrite it with nextId()
        user newU = new user(0, username, hashPassword(password));
        return userDAO.createUser(newU);
    }
    public boolean deleteUser(int userId) throws IOException{
        return this.userDAO.deleteUser(userId);
    }
    public user authenciate(String userName, String password) throws IOException{
        return this.userDAO.authenciate(userName, hashPassword(password));

    }
    public boolean updateUser(int id, String newUsername, String newPassword) throws IOException{
        user user = userDAO.getUser(id);

        if(user == null || newUsername == null || newUsername.isEmpty() || (newPassword != null && newPassword.isEmpty())){
            return false;
        }
        return userDAO.updateUser(new user(id, newUsername, newPassword == null ? user.getPassword(): hashPassword(newPassword)));
    }


    public String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for(byte b: hashBytes){
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }


    
}
