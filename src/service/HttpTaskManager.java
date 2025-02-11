package service;

import com.google.gson.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import server.KVServer;
import server.KVTaskClient;
import service.util.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HttpTaskManager extends FileBackedTasksManager {
    private URL url;
    private static Gson gson = new GsonBuilder().serializeNulls().create();
    private static Managers managers = new Managers();
    private static KVTaskClient kvTaskClient;

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
        saveToServer();
        return task1;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epic1 = super.createEpic(epic);
        String key = String.valueOf(epic1.getId());
        String value = gson.toJson(epic1);
       saveToServer();
        return epic1;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTask1 = super.createSubTask(subTask);
        String key = String.valueOf(subTask1.getId());
        String value = gson.toJson(subTask1);
        saveToServer();
        return subTask1;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveToServer();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        saveToServer();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        saveToServer();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveToServer();
    }
    private void saveToServer() {
        FileBackedTasksManager fileBackedTasksManager =
                FileBackedTasksManager.loadFromFile(managers.getDefaultHistoryManager() ,managers.getFile());
        List<Task> tasks =  new ArrayList<>(fileBackedTasksManager.getAllTasks().values());
        for (Task task : tasks) {
            String id = String.valueOf(task.getId());
            String body = gson.toJson(task);
            kvTaskClient.put(id, body);
        }
        List<SubTask> subtasks = new ArrayList<>(fileBackedTasksManager.getAllSubTasks().values());
        for (SubTask subTask : subtasks) {
            String id = String.valueOf(subTask.getId());
            String body = gson.toJson(subTask);
            kvTaskClient.put(id, body);
        }
List<Epic> epics = new ArrayList<>(fileBackedTasksManager.getAllEpic().values());
        for (Epic epic : epics) {
            String id = String.valueOf(epic.getId());
            String body = gson.toJson(epic);
            kvTaskClient.put(id, body);
        }
    }

    public  void loadFromServer() {
        try {
            JsonElement element = JsonParser.parseString(kvTaskClient.load("all"));
            JsonArray jsonElements = element.getAsJsonArray();
            int maxId = -1;
            for (JsonElement jsonElement : jsonElements) {
                String taskString = jsonElement.getAsString();
                if (taskString.contains("EPIC")) {
                    Epic epic = gson.fromJson(taskString, Epic.class);
                    if (!epic.equals(null)) {
                        int id = epic.getId();
                        if (maxId < id) {
                            maxId = id;
                        }
                        super.epicTasks.put(id, epic);
                    }
                } else if (taskString.contains("SUBTASK")) {
                    SubTask subTask = gson.fromJson(taskString, SubTask.class);
                    if (!subTask.equals(null)) {
                        int id = subTask.getId();
                        if (maxId < id) {
                            maxId = id;
                        }
                        super.subTasks.put(id, subTask);
                        fillTimeSortedSet(subTask);
                    }
                } else {
                    Task task = gson.fromJson(taskString, Task.class);
                    if (!task.equals(null)) {
                        int id = task.getId();
                        if (maxId < id) {
                            maxId = id;
                        }
                        super.tasks.put(id, task);
                        fillTimeSortedSet(task);
                    }
                }
            }
            taskMaxId = maxId + 1;
        }
         catch (Exception e) {
            throw new ManagerSaveException("Ошибка при загрузке данных с сервера");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HttpTaskManager that = (HttpTaskManager) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url);
    }


    public static void main(String[] args) {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            HttpTaskManager httpTaskManager = new HttpTaskManager(managers.getDefaultHistoryManager(),
                    managers.getFile(), new URL("http://localhost:8078"));
            httpTaskManager.saveToServer();
            httpTaskManager.loadFromServer();
        }
        catch (IOException e) {
throw new ManagerSaveException("Ошибка при работе http-менеджера");
        }
    }
}
