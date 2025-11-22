package com.codewithd.todolist.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * class that represents a basket that holds all of the users taks 
 */
public class basket {
    /**
     * collection of tasks
     */
    @JsonProperty("tasks")
    private HashMap<Integer, task> tasks;
    /**
     * id of the user 
     */
    @JsonProperty("userId")
    private int userId;

    /**
     * constructor for the basket 
     * @param userId id of the user
     * the hashMap for the tasks is initialized 
     */
    public basket(@JsonProperty("userID") int userId){
        this.userId = userId;
        this.tasks = new HashMap<>();
    }
    /**
     * getting the users id
     * @return the id of the user 
     */
    public int getUserId(){
        return userId;
    }
    /**
     * getting all of the tasks in the basket
     * @return HashMap 
     */
    public HashMap<Integer, task> getTasks(){
        return tasks;
    }
    /**
     * adds a task to the basket 
     * @param task being added
     * @return true is successfully added, false otherwise
     */
    public boolean addTaskToBasket(task task){
        if(!tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
            return true;
        }return false;
    }
    /**
     * removes a task from the basket
     * @param task being removed
     * @return true if removed successfully, false otherwise
     */
    public boolean removeTaskFromBasket(int taskId){
        if(tasks.containsKey(taskId)){
            tasks.remove(taskId);
            return true;
        }return false;
    }
    /**
     * removes all task from basket
     */
    public void clearTasks(){
        this.tasks.clear();
    }


}
