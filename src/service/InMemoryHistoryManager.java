package service;

import model.Task;
import service.storage.ViewList;

import java.util.ArrayList;
import java.util.List;



public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> taskHistory = ViewList.getViewList();
    private static final int MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        if (taskHistory.size() < MAX_SIZE)
            taskHistory.add(task);
        else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }


@Override
    public  List<Task> getHistory() {
        return taskHistory;
    }
}
