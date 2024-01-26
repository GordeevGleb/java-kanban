package service;

import java.io.File;

public class Managers {
private File file = new File("src/service/storage/taskStorage.csv");
    public HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
    public TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }
    public FileBackedTasksManager getManagerFromFileContent() {
        return new FileBackedTasksManager(getDefaultHistoryManager(), file);
    }

}
