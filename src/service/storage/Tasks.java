package service.storage;
import model.Task;
import java.util.HashMap;


public class Tasks{
    private static HashMap<Integer, Task> allTasks = new HashMap<>();
    public static void add(Task task){
allTasks.put(task.getId(), task);
    }

    public static HashMap<Integer, Task> getAllTasks(){
        return allTasks;
    }


}
