package com.codewithd.todolist.model.persistence;

import java.io.IOException;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;

public interface basketDAO {
    
    /**
     * gets basket of certain user
     * @param userId id of the certain user 
     * @return basket of the certain user
     */
    basket getBasket(int userId)  throws IOException;

    /**
     * creates a new basket for user
     * @param userId if of the user
     * @return created basket
     */
    basket createBasket(int userId) throws IOException;

    /**
     * updates a users basket
     * @param userId id of the user
     * @return updated basket
     */
    basket updateBasket(basket basket) throws IOException;

    /**
     * deletes the users basket
     * @param userId id of the user
     * @return true if deleted, false otherwise
     */
    boolean deleteBasket(int userId)throws IOException;

    /**
     * adds a task to basket
     * @param task task being aded
     * @return true if added, false otherwise
     */
    boolean addToBasket(int userId,task task)throws IOException;

    /**
     * removes a task from basket
     * @param taskId id of the task
     * @return true if removed, false otherwise
     */
    boolean removeFromBasket(int userId, int taskId) throws IOException;

    /**
     * clears all task from basket
     * @param userId id of the user
     */
    void clearBasket(int userId) throws IOException;
}
