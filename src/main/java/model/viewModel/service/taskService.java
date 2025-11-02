package model.viewModel.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import model.persistence.TaskFileDAO;
import model.persistence.task;

@Component
public class taskService {
    private final TaskFileDAO taskDAO;
    
    public taskService(TaskFileDAO taskDAO){
        this.taskDAO = taskDAO;
    }

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
