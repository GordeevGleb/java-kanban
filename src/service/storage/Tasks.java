package service.storage;
import model.Savable;
import model.Task;
import java.util.HashMap;


public class Tasks implements Savable<Task> {
    private HashMap<Integer, Task> allTasks = new HashMap<>();


    @Override
    public void save(Task task) {
        allTasks.put(task.getId(), task);
    }

    @Override
    public HashMap<Integer, Task> get() {
        return allTasks;
    }


}
