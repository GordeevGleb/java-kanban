package service;

import server.KVTaskClient;
import service.FileBackedTasksManager;
import service.HistoryManager;

import java.io.File;
import java.net.URL;

public class HttpTaskManager extends FileBackedTasksManager {
    private URL url;
    private KVTaskClient kvTaskClient;
    public HttpTaskManager(HistoryManager historyManager, File file, URL url) {
        super(historyManager, file);
        this.url = url;
    }

}
