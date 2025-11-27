package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.codewithd.todolist.model.user;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.basketService;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
public class userController {
    private static final Logger LOG = Logger.getLogger(userController.class.getName());
    private final authencationService authService;
    private final basketService basketService;

    public userController(authencationService authService, basketService basketService){
        this.authService = authService;
        this.basketService = basketService;
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
    @GetMapping("/me")
    public ResponseEntity<user> getCurrentUser(@RequestParam String sessionToken){
        LOG.log(Level.INFO, "Getting current user");
        
            try {
                user user = authService.getUser(sessionToken);
                if (user != null){
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    @PutMapping("/update")
    public ResponseEntity<user> updateUser(String sessionToken, @RequestParam String username, @RequestParam String password){
        LOG.info(String.format("updating user with session token: %s", sessionToken));

        try {
            boolean user = authService.updateUser(sessionToken, username, password);
            if(user){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestParam String username, @RequestParam String password){
        LOG.info(String.format("Creating user: %s", username));

       
        try {
            String token = authService.createUser(username, password);
            if(token != null){
                user user = authService.getUser(token);
                basketService.createBasket(user.getID());
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(String sessionToken){
        LOG.info(String.format("Attempting to delete user with session token: %s", sessionToken));
        try{
            if(authService.deleteUser(sessionToken)){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(String sessionToken){
        LOG.info(String.format("Attempting to logout with session token: %s", sessionToken));

    
            try {
                if(authService.logout(sessionToken)){
                    return new ResponseEntity<>(HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    
    }
    
}