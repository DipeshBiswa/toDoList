package com.codewithd.todolist.model.persistence;

import com.codewithd.todolist.model.task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


@Component
public class TaskFileDAO implements TaskDAO {
    private static final Logger Log = Logger.getLogger(TaskFileDAO.class.getName());
    Map<Integer, task> tasks;
    private ObjectMapper objectMapper;

    private static int nextId;
    private String filename;

    public TaskFileDAO(@Value("${tasks.file}") String filename,ObjectMapper objectMapper)throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();

        Log.info("Tasks file successfully loaded");

    }
    private synchronized static int nextId(){
        int id = nextId;
        ++nextId;
        return id;
    }
    private task[] getTaskArray(){
        return getTaskArray(null);
    }

    private task[] getTaskArray(String containstext){
        ArrayList<task> tasklist = new ArrayList<task>();

        for(task task: tasks.values()){
            if(containstext == null || task.getName().contains(containstext)){
                tasklist.add(task);
            }
        }
        task[] taskarray = new task[tasklist.size()];
        tasklist.toArray(taskarray);
        return taskarray;
    }

    private boolean save() throws IOException{
        task[] taskarray = getTaskArray();
        objectMapper.writeValue(new File(filename), taskarray);
        return true;
    }
    private boolean load() throws IOException{
        tasks = new TreeMap<>();
        nextId = 0;

        task[] taskArray = objectMapper.readValue(new File(filename), task[].class);

        for(task task: taskArray){
            tasks.put(task.getId(), task);
            if(task.getId() > nextId)
                nextId = task.getId();
        }
        ++nextId;
        return true;
    }

    @Override
    public task[] getTasks() throws IOException {
        synchronized(tasks){
            return getTaskArray();
        }

    }

    @Override
    public task[] findTasks(String containstext) throws IOException {
        synchronized(tasks){
            return getTaskArray(containstext);
        }
    }

    @Override
    public task getTask(int id) throws IOException {
        synchronized(tasks){
            if(tasks.containsKey(id)){
                return tasks.get(id);
            }else{
                return null;
            }
    }
}

    @Override
    public task findTaskByName(String name) throws IOException {
        synchronized(tasks){
            for(task task: tasks.values()){
                if(task.getName().equals(name))
                    return task;
            }
        }
        return null;
    }

    @Override
    public task createTask(task task) throws IOException {
        synchronized(tasks){
            task newTask = new task(task.getName(), nextId(), task.getDescription(), task.getStatus());
            tasks.put(task.getId(),newTask);
            save();
            return newTask;
        }     
        
    }

    @Override
    public task updateTask(task task) throws IOException {
        synchronized(tasks){
            if(tasks.containsKey(task.getId()) == false){
                return null;
            }
            tasks.put(task.getId(), task);
            save();
            return task;
        }
       
    }

    @Override
    public boolean deleteTask(int id) throws IOException {
        synchronized(tasks){
           
            if(tasks.containsKey(id)){
                tasks.remove(id);
                save();
                return true;

            }return false;
            
        }
    }
}
