package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithd.todolist.model.user;
import com.codewithd.todolist.viewModel.service.userService;

@RestController
@RequestMapping("users")
public class userController {
    private static final Logger LOG = Logger.getLogger(userController.class.getName());
    private final userService userService;

    public userController(userService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<user> getUser(@PathVariable int id){
        LOG.log(Level.INFO, "Get /users/{0}", id);
        
            user user = userService.getUser(id);
            if(user != null){
                return new ResponseEntity<>(user,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    @GetMapping
    public ResponseEntity<user> getUserByName(@RequestParam String userName){
        LOG.log(Level.INFO, "Get /users?user={username}",userName);

        try {
            user user = userService.findUserByName(userName);
            if(user != null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping
    public ResponseEntity<user> createUser(@RequestBody user user){
        LOG.log(Level.INFO, "POST /users{0}",user);

        try {
            user createdUser = userService.createUser(user.getUsername(), user.getPassword());
            if(createdUser != null){
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
           LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}