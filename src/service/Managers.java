package service;

public class Managers {


    public  HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
    public  TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }
}
