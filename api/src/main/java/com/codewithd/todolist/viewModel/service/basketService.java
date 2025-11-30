package com.codewithd.todolist.viewModel.service;

import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;
import com.codewithd.todolist.model.persistence.basketDAO;

@Component
public class basketService {
    private final basketDAO basketDAO;
    private final Random RNG = new Random();

    public basketService(basketDAO basketDAO){
        this.basketDAO = basketDAO;
    }
    
    public basket getBasket(int userId) throws IOException{
        return basketDAO.getBasket(userId);
    }
    public basket createBasket(int userId) throws IOException{
        return basketDAO.createBasket(userId);
    }
    public basket updateBasket(basket basket) throws IOException{
        return basketDAO.updateBasket(basket);
    }
    public boolean deleteBasket(int userId) throws IOException{
        return basketDAO.deleteBasket(userId);
    }
    public boolean addToBasket(int userId, String name, String description) throws IOException{
        task task = new task(name, RNG.nextInt(), description, false);
        return basketDAO.addToBasket(userId, task);
    }
    public boolean removeFromBasket(int userId, int taskId) throws IOException{
        return basketDAO.removeFromBasket(userId, taskId);
    }
    public basket clearBasket(int userId) throws IOException{
       return basketDAO.clearBasket(userId);
    }

    
}
