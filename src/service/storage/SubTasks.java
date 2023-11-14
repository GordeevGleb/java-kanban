package service.storage;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubTasks{
    private static HashMap<Integer, SubTask> allSubTasks = new HashMap<>();

    public static HashMap<Integer, SubTask> getAllSubTasks(){
        return allSubTasks;
    }
    public static void add(SubTask task){
allSubTasks.put(task.getId(),  task);
    }

}
