package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.basketService;

@RestController
@RequestMapping("basket")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class basketController {
    private static final Logger LOG = Logger.getLogger(basketController.class.getName());
    private final basketService basketService;
    private final authencationService authService;

    public basketController(basketService basketService, authencationService authService){
        this.basketService = basketService;
        this.authService = authService;
    }
    
    private Integer getUserId(String sessionToken) throws IOException{
        String userName = authService.getUsername(sessionToken);
        if(userName == null){
            return null;
        }
        return authService.getUser(sessionToken).getID();
    }
    @GetMapping("")
    public ResponseEntity<basket> getBasket(String sessionToken){
        LOG.info(String.format("getting basket from user: %s", sessionToken));

        try {
            int userId = getUserId(sessionToken);
            basket basket = basketService.getBasket(userId);
            if(basket != null){
                return new ResponseEntity<>(basket, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("")
    public ResponseEntity<Void> createBasket(String sessionToken){
        LOG.info("Creating a new user");
        try {
            int userId = getUserId(sessionToken);
            basket basket =basketService.createBasket(userId);
            if(basket != null){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("")
    public ResponseEntity<Void> deleteBasket(String sessionToken){
        LOG.info("deleting user basket");

        try {
            int userId = getUserId(sessionToken);
            boolean deleted = basketService.deleteBasket(userId);
            if(deleted){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    @PostMapping("/items")
    public ResponseEntity<Void> addToBasket(String sessionToken, @RequestParam String name, @RequestParam String description){
        LOG.info("add task to basket");
        try{
        int userId = getUserId(sessionToken);
        boolean basket = basketService.addToBasket(userId, name, description);
        if(basket){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/items/{taskId}")
    public ResponseEntity<Void> deleteTaskFromBasket(String sessionToken, @RequestParam int taskId){
        LOG.info("Deleting task from basket");
        
        try {
            int userId = getUserId(sessionToken);
            boolean deleted = basketService.removeFromBasket(userId, taskId);
            if(deleted){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/items")
    public ResponseEntity<basket> clearBasket(String sessionToken){
        LOG.info("clearing user basket");

        try {
            int userId = getUserId(sessionToken);
            basket basket = basketService.clearBasket(userId);
            if(basket != null){
                return new ResponseEntity<>(basket, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        

    }
}
