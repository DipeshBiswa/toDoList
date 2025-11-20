package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithd.todolist.model.user;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.userService;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
public class userController {
    private static final Logger LOG = Logger.getLogger(userController.class.getName());
    private final userService userService;
    private final authencationService authService;

    public userController(userService userService, authencationService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Void> authenticate(@RequestParam String username, @RequestParam String password){
        LOG.info(String.format("Authenticating user: %s", username));
        String token;
        try {
            token = authService.login(username, password);
            if (token != null){
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, token).build();
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }catch (IOException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    @GetMapping("/checkToken")
    public ResponseEntity<Void> checkToken(String sessionToken){
        LOG.info(String.format("Checking token: %s", sessionToken));

        try{if(authService.getUsername(sessionToken) != null){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    // @PostMapping
    // public ResponseEntity<user> createUser(@RequestParam String username, @RequestParam String password){
    //     LOG.log(Level.INFO, "POST /users{0}",username);

    //    try{
    //     String token = authService.createUser(username, password);
    //         if(token != null){

    //     }
    //    }
    // }
    
}