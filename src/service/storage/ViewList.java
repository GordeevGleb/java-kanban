package service.storage;


import model.Task;
import service.HistoryManager;

import java.util.ArrayList;

import java.util.List;

public class ViewList implements HistoryManager {
    private  List<Task> viewList = new ArrayList<>();


    @Override
    public void add(Task task) {
        viewList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewList;
    }
}
