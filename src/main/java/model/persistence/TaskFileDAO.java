package model.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;


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
        //load();

        Log.info("Tasks file successfully loaded");

    }
    private synchronized static int nextId(){
        int id = nextId;
        ++nextId;
        return id;
    }
    private task[] getTaskArray(){
        return getTaskArray();
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

    @Override
    public task[] getTasks() throws IOException {
        return new task[0];
    }

    @Override
    public task[] findTasks(String containstext) throws IOException {
        return new task[0];
    }

    @Override
    public task getTask(int id) throws IOException {
        return null;
    }

    @Override
    public task findTaskByName(String name) throws IOException {
        return null;
    }

    @Override
    public task createTask(task task) throws IOException {
        return null;
    }

    @Override
    public task updateTask(task task) throws IOException {
        return null;
    }

    @Override
    public boolean deleteTask(int id) throws IOException {
        return false;
    }
}
