package com.codewithd.todolist.viewModel.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithd.todolist.model.task;
import com.codewithd.todolist.viewModel.service.taskService;


@RestController
@RequestMapping("tasks")
public class taskController {
    private static final Logger LOG = Logger.getLogger(taskController.class.getName());
    private final taskService taskService;

    public taskController(taskService service){
        this.taskService = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<task> getTask(@PathVariable int id){
        LOG.log(Level.INFO, "Get /tasks/{0}", id);
        try {
            task task = taskService.getTask(id);
            if (task != null){
                return new ResponseEntity<>(task, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    @GetMapping("")
    public ResponseEntity<task[]> getTasks(){
        LOG.log(Level.INFO, "Get /tasks");

        try {
            task[] tasks = taskService.getTasks();
            if (tasks != null){
                return new ResponseEntity<>(tasks,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/")
    public ResponseEntity<task[]> searchTask(@RequestParam String name){
        LOG.log(Level.INFO, "Get /tasks/?name={0}", name);
        try {
            task[] task = taskService.findTasks(name);
            if(task != null){
                return new ResponseEntity<>(task, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("")
    public ResponseEntity<task> createTask(@RequestBody task task){
        LOG.log(Level.INFO, "POST /tasks {0}", task);

        try {
            task t = taskService.addTask(task);
            if (t != null){
                return new ResponseEntity<>(t, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("")
    public ResponseEntity<task> updateTask(@RequestBody task task){
        LOG.log(Level.INFO, "Put /task{0}", task);
        
        try {
            task t = taskService.updateTask(task);
            if(t != null){
                return new ResponseEntity<>(t, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/id")
    public ResponseEntity<task> deleteTask(@PathVariable int id){
        LOG.log(Level.INFO, "Delete task/{id}", id);

        try {
            if(taskService.deleteTask(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        

    }



    
}
