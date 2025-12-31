package com.codewithd.todolist.viewModel.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;
import com.codewithd.todolist.model.persistence.TaskDAO;
import com.codewithd.todolist.model.persistence.basketDAO;

@Component
public class basketService {
    private final basketDAO basketDAO;
    private final TaskDAO taskDAO;
    private final Random RNG = new Random();

    public basketService(basketDAO basketDAO, TaskDAO taskDAO){
        this.basketDAO = basketDAO;
        this.taskDAO = taskDAO;
    }
    
    /**
     * Creates a new basket for a user.
     * This is called by userController during registration.
     */
    public basket createBasket(int userId) throws IOException {
        return basketDAO.createBasket(userId);
    }

    /**
     * Retrieves the basket for a specific user.
     */
    public basket getBasket(int userId) throws IOException {
        return basketDAO.getBasket(userId);
    }

    /**
     * Adds a task to the basket. 
     * If the basket doesn't exist, it creates it first (self-healing).
     */
    public boolean addToBasket(int userId, String name, String description) throws IOException {
        // Auto-create if registration somehow missed it
        if (basketDAO.getBasket(userId) == null) {
            basketDAO.createBasket(userId);
        }

        int taskId = Math.abs(RNG.nextInt()); 
        task newTask = new task(name, taskId, description, false);
        
        taskDAO.createTask(newTask); 
        return basketDAO.addToBasket(userId, newTask);
    }

    /**
     * Returns a list of tasks for the user.
     */
    public List<task> getBasketTasks(int userId) throws IOException {
        basket basket = basketDAO.getBasket(userId);

        if (basket == null) {
            return new ArrayList<>(); 
        }

        // Returns values from the HashMap as a List for Angular
        return new ArrayList<>(basket.getTasks().values());
    }

    public boolean removeFromBasket(int userId, int taskId) throws IOException {
        return basketDAO.removeFromBasket(userId, taskId);
    }

    public basket updateBasket(basket basket) throws IOException {
        return basketDAO.updateBasket(basket);
    }

    public boolean deleteBasket(int userId) throws IOException {
        return basketDAO.deleteBasket(userId);
    }

    public basket clearBasket(int userId) throws IOException {
       return basketDAO.clearBasket(userId);
    }
}