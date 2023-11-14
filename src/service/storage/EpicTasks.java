package service.storage;

import model.Epic;
import model.Savable;
import model.Task;


import java.util.HashMap;


public class EpicTasks implements Savable<Epic> {
    private  HashMap<Integer, Epic> allEpics = new HashMap<>();



    @Override
    public void save(Epic epic) {
        allEpics.put(epic.getId(), epic);
    }

    @Override
    public HashMap<Integer, Epic> get() {
        return allEpics;
    }


}
