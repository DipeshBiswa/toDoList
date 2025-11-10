package com.codewithd.todolist.model.persistence;

import java.io.IOException;

import com.codewithd.todolist.model.user;

public interface userDAO {
    
    
    user getUser(int id) throws IOException;
    user findUserByName(String username) throws IOException;
    user createUser(user user) throws IOException;
    boolean updateUser(user user) throws IOException;
    boolean deleteUser(user user) throws IOException;
    user authenciate(String username, String password) throws IOException;
}
