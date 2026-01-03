package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.basketService;

@RestController
@RequestMapping("basket")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class basketController {
    private final basketService basketService;
    private final authencationService authService;

    public basketController(basketService basketService, authencationService authService){
        this.basketService = basketService;
        this.authService = authService;
    }
    
    private Integer getUserId(String sessionToken) throws IOException {
        if (sessionToken == null) return null;
        var user = authService.getUser(sessionToken);
        return (user != null) ? user.getID() : null;
    }

    @GetMapping("/items")
    public ResponseEntity<List<task>> getTaskFromBasket(@CookieValue(name = "sessionToken", required = false) String sessionToken){
        try {
            Integer userId = getUserId(sessionToken);
            if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(basketService.getBasketTasks(userId), HttpStatus.OK);
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addToBasket(
            @CookieValue(name = "sessionToken", required = false) String sessionToken, 
            @RequestParam String name, 
            @RequestParam String description){
        try {
            Integer userId = getUserId(sessionToken);
            if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            boolean success = basketService.addToBasket(userId, name, description);
            return success ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/items/{taskId}")
    public ResponseEntity<Void> deleteTaskFromBasket(
            @CookieValue(name = "sessionToken", required = false) String sessionToken, 
            @PathVariable int taskId){
        try {
            Integer userId = getUserId(sessionToken);
            if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            boolean deleted = basketService.removeFromBasket(userId, taskId);
            return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}