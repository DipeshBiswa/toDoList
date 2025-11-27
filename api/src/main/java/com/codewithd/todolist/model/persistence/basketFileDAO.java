package com.codewithd.todolist.model.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class basketFileDAO implements basketDAO{
    private static final Logger log = Logger.getLogger(basketFileDAO.class.getName());
    private final ObjectMapper objectMapper;
    private final String filename;

    private Map<Integer, basket> baskets; 
    public basketFileDAO(@Value("${basket.file}") String filename, ObjectMapper obj) throws IOException{
        this.filename = filename;
        this.objectMapper = obj;
        this.baskets = new TreeMap<>();
        load();

        log.info("basket file successfully loaded");
    }
    private boolean save() throws IOException{
        basket[] basketArray = baskets.values().toArray(basket[]::new);
        objectMapper.writeValue(new File(filename), basketArray);
        return true;
    }
    private boolean load() throws IOException{
        baskets.clear();
        basket[] basketArray = objectMapper.readValue(new File(filename), basket[].class);

        for(basket basket:basketArray){
            baskets.put(basket.getUserId(), basket);

        }
        return true;
    }
    
    @Override
    public basket getBasket(int userId) {
        synchronized(baskets){
            if(baskets.containsKey(userId)){
                return baskets.get(userId);
            }else{
                return null;
            }
        }
    }

    @Override
    public basket createBasket(int userId) throws IOException {
        synchronized(baskets){basket basket = new basket(userId);
        if(!baskets.containsKey(userId)){
            baskets.put(userId, basket);
            save();
            return basket;
        }else{
            return null;
        }
    }
    }

    @Override
    public basket updateBasket(basket basket) throws IOException {
        synchronized(baskets){
            if(!baskets.containsKey(basket.getUserId())){
                return null;
            }
            baskets.put(basket.getUserId(), basket);
            save();
            return basket;
        }
    }

    @Override
    public boolean deleteBasket(int userId) throws IOException {
        synchronized(baskets){
            if(baskets.containsKey(userId)){
                baskets.remove(userId);
                save();
                return true;
                
            }else{
                return false;
            }

        }
    }

    @Override
    public boolean addToBasket(int userId, task task) throws IOException {
       synchronized(baskets){
        if(baskets.containsKey(userId)){
            basket basket = baskets.get(userId);
            basket.addTaskToBasket(task);
            save();
            return true;
        }else{
            return false;
        }
       }
    }

    @Override
    public boolean removeFromBasket(int userId, int taskId) throws IOException {
        synchronized(baskets){
            if(baskets.containsKey(userId)){
                basket basket = baskets.get(userId);
                basket.removeTaskFromBasket(taskId);
                save();
                return true;
            }else{
                return false;
            }
           }
    }

    @Override
    public void clearBasket(int userId) throws IOException {
       synchronized(baskets){
        if(baskets.containsKey(userId)){
            basket basket = baskets.get(userId);
            basket.clearTasks();
            save();
        }
       }
    }
    
}
