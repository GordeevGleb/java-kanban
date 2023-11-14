package service.storage;

import model.Epic;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EpicTasks {
    private static HashMap<Integer, Epic> allEpics = new HashMap<>();
    public static void add(Epic task) {
        allEpics.put(task.getId(),  task);
    }

    public static HashMap<Integer, Epic> getAllEpics(){
        return allEpics;
    }

}
