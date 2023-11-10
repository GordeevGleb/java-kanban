package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;



public class InMemoryHistoryManager implements HistoryManager {
    private static List<Task> taskHistory = new ArrayList<>();

    @Override
    public List<Task> add(Task task) {
        if (taskHistory.size() < 10)
            taskHistory.add(task);
        else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
        return taskHistory;
    }


@Override
    public  List<Task> getHistory() {
        return taskHistory;
    }
}
