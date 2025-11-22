package com.codewithd.todolist.model.persistence;

import com.codewithd.todolist.model.basket;
import com.codewithd.todolist.model.task;

public interface basketDAO {
    
    /**
     * gets basket of certain user
     * @param userId id of the certain user 
     * @return basket of the certain user
     */
    basket getBasket(int userId);

    /**
     * creates a new basket for user
     * @param userId if of the user
     * @return created basket
     */
    basket createBasket(int userId);

    /**
     * updates a users basket
     * @param userId id of the user
     * @return updated basket
     */
    basket updateBasket(int userId);

    /**
     * deletes the users basket
     * @param userId id of the user
     * @return true if deleted, false otherwise
     */
    boolean deleteBasket(int userId);

    /**
     * adds a task to basket
     * @param task task being aded
     * @return true if added, false otherwise
     */
    boolean addToBasket(task task);

    /**
     * removes a task from basket
     * @param taskId id of the task
     * @return true if removed, false otherwise
     */
    boolean removeFromBasket(int taskId);

    /**
     * clears all task from basket
     * @param userId id of the user
     */
    void clearBasket(int userId);
}
