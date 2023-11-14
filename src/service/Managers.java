package service;

import model.Task;

import java.util.List;

public class Managers {

    public  HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
    public  TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

}
