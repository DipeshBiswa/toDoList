package model.persistence;


import java.io.IOException;

/**
 * Defines the interface for task object persistence
 * @author Dipesh
 */
public interface TaskDAO {

    /**
     *
     * @return an array of all {@link task}s
     * @throws IOException if underlying storage cannot be accessed
     */
    task[] getTasks() throws IOException;


    task[] findTasks(String containstext) throws IOException;
    /**
     *
     * @param id used to find the specific {@link task}
     * @return a {@link task} with matching id
     * @throws IOException if underlying storage cannot be accessed
     */

    task getTask(int id) throws IOException;


    /**
     *
     * @param name used to find specific {@link task}
     * @return a {@link task} by name
     * @throws IOException if underlying storage cannot be accessed
     */
    task findTaskByName(String name)throws IOException;

    /**
     *
     * @param task {@link task} object to be created
     * @return new {@link task} if successful
     * @throws IOException if underlying storage cannot be accessed
     */
    task createTask(task task) throws IOException;

    /**
     *
     * @param task {@link task} object ot be updated
     * @return {@link task} that has been updated
     * @throws IOException if storage cannot be accessed
     */
    task updateTask(task task)throws IOException;

    /**
     *
     * @param id used to delete {@link task}
     * @return true if deleted, false otherwise
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteTask(int id)throws IOException;
}
