package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codewithd.todolist.model.user;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.basketService;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
public class userController {
    private static final Logger LOG = Logger.getLogger(userController.class.getName());
    private final authencationService authService;
    private final basketService basketService;

    public userController(authencationService authService, basketService basketService){
        this.authService = authService;
        this.basketService = basketService;
    }

    /**
     * CREATE USER (Registration)
     * Fixed: Now automatically creates a basket and logs the user in.
     */
    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestParam String username, @RequestParam String password){
        LOG.info(String.format("Creating user: %s", username));
        try {
            String token = authService.createUser(username, password);
            if(token != null){
                user newUser = authService.getUser(token);
                
                if (newUser != null) {
                    // 1. Initialize the user's basket storage
                    basketService.createBasket(newUser.getID());
                    
                    // 2. Return 201 Created and set the session cookie 
                    // This allows the user to start using the app without logging in manually
                    return ResponseEntity.status(HttpStatus.CREATED)
                        .header(HttpHeaders.SET_COOKIE, "sessionToken=" + token + "; Path=/; HttpOnly; SameSite=Lax")
                        .build();
                }
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error creating user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * AUTHENTICATE (Login)
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticate(@RequestParam String username, @RequestParam String password){
        LOG.info(String.format("Authenticating user: %s", username));
        try {
            String token = authService.login(username, password);
            if (token != null){
                return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, "sessionToken=" + token + "; Path=/; HttpOnly; SameSite=Lax")
                    .build();
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * CHECK TOKEN
     */
    @GetMapping("/checkToken")
    public ResponseEntity<Void> checkToken(@CookieValue(name = "sessionToken", required = false) String sessionToken){
        try {
            if(sessionToken != null && authService.getUsername(sessionToken) != null){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET CURRENT USER
     */
    @GetMapping("/me")
    public ResponseEntity<user> getCurrentUser(@CookieValue(name = "sessionToken", required = false) String sessionToken){
        try {
            user user = authService.getUser(sessionToken);
            if (user != null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * UPDATE USER
     */
    @PutMapping("/update")
    public ResponseEntity<user> updateUser(
            @CookieValue(name = "sessionToken", required = false) String sessionToken, 
            @RequestParam String username, 
            @RequestParam String password){
        try {
            boolean success = authService.updateUser(sessionToken, username, password);
            return success ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE USER
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@CookieValue(name = "sessionToken", required = false) String sessionToken){
        try {
            return authService.deleteUser(sessionToken) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * LOGOUT
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "sessionToken", required = false) String sessionToken){
        try {
            if(sessionToken != null && authService.logout(sessionToken)){
                // Clear the cookie by setting Max-Age to 0
                return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, "sessionToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax")
                    .build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}