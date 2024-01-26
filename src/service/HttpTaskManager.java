package service;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;
import server.KVTaskClient;
import service.FileBackedTasksManager;
import service.HistoryManager;

import java.io.File;
import java.net.URL;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager {
    private URL url;
    private KVTaskClient kvTaskClient;
    private Gson gson = new Gson();
    public HttpTaskManager(HistoryManager historyManager, File file, URL url) {
        super(historyManager, file);
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public Task createTask(Task task) {
        Task task1 = super.createTask(task);
        String key = String.valueOf(task1.getId());
        String value = gson.toJson(task1);
        kvTaskClient.put(key, value);
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        String key = String.valueOf(epic1.getId());
        String value = gson.toJson(epic1);
        kvTaskClient.put(key, value);
        return epic1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTask1 = super.createSubTask(subTask);
        String key = String.valueOf(subTask1.getId());
        String value = gson.toJson(subTask1);
        kvTaskClient.put(key, value);
        return subTask1;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
}
