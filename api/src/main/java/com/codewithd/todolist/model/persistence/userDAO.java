package com.codewithd.todolist.model.persistence;

import java.io.IOException;

import com.codewithd.todolist.model.user;

public interface userDAO {
    
    user[] getUsers() throws IOException;
    user[] findUsers(String containsText) throws IOException;
    user getUser(int id) throws IOException;
    user findUserByName(String username) throws IOException;
    user createUser(user user) throws IOException;
    user updateUser(user user) throws IOException;
    boolean deleteUser(user user) throws IOException;
}
