package service;

public class Managers {
    private  InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public  TaskManager getDefault() {
        return inMemoryTaskManager;
    }
    public static HistoryManager getDefaultHistoryManager() {
        return inMemoryHistoryManager;
    }
}
