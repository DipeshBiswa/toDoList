package model.viewModel.service;

import org.springframework.stereotype.Component;

import model.persistence.TaskFileDAO;

@Component
public class taskService {
    private final TaskFileDAO taskDAO;
    
    public taskService(TaskFileDAO taskDAO){
        this.taskDAO = taskDAO;
    }
}
