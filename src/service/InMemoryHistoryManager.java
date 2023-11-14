package service;

import model.Task;
import service.storage.ViewList;

import java.util.ArrayList;
import java.util.List;



public class InMemoryHistoryManager implements HistoryManager {
    private ViewList viewList = new ViewList();
    private static final int MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        if (viewList.getViewList().size() < MAX_SIZE)
            viewList.add(task);
        else {
            viewList.getViewList().remove(0);
            viewList.add(task);
        }
    }


@Override
    public  List<Task> getHistory() {
        return viewList.getViewList();
    }
}
