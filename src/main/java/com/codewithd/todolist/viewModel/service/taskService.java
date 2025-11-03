package com.codewithd.todolist.viewModel.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.codewithd.todolist.model.task;
import com.codewithd.todolist.model.persistence.TaskDAO;

@Component
/**
 * class representing the service for task
 */
public class taskService {
    
    private final TaskDAO taskDAO;
    
    /**
     * constructor
     * @param taskDAO is instance of the taskDAO which is used as dependency injection 
     */
    public taskService(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }
    
    /**
     * method used to get all the tasks from the file
     * @return
     * @throws IOException
     */
    public task[] getTasks() throws IOException{
        return taskDAO.getTasks();
    }
    public task getTask(int id) throws IOException{
        return taskDAO.getTask(id);
    }
    public task[] findTasks(String name) throws IOException{
        return taskDAO.findTasks(name);
    }
    public task addTask(task task) throws IOException{
        return taskDAO.createTask(task);
        
    }
    public boolean deleteTask(int id) throws IOException{
        return taskDAO.deleteTask(id);
    }
    public task updateTask(task task) throws IOException{
        return taskDAO.updateTask(task);
    }
}
