package service.storage;


import model.Savable;
import model.SubTask;



import java.util.HashMap;


public class SubTasks implements Savable<SubTask> {
    private  HashMap<Integer, SubTask> allSubTasks = new HashMap<>();


    @Override
    public void save(SubTask subTask) {
        allSubTasks.put(subTask.getId(), subTask);
    }

    @Override
    public HashMap<Integer, SubTask> get() {
        return allSubTasks;
    }


}
