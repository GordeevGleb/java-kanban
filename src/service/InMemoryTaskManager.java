package service;

import model.Epic;
import model.SubTask;
import model.Task;


import java.util.HashMap;



public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private static int taskCount;
@Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }
@Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epicTasks.put(epic.getId(), epic);
        return epic;
    }
@Override
    public SubTask createSubTask(SubTask subTask) {
        if (epicTasks.containsKey(subTask.getMasterId())) {
            Epic epic = epicTasks.get(subTask.getMasterId());
            subTask.setId(generateId());
            epic.addSubtask(subTask);
            subTasks.put(subTask.getId(), subTask);
        }
        return subTask;
    }
@Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }
@Override
    public HashMap<Integer, Epic> getAllEpic() {
        return epicTasks;
    }
@Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subTasks;
    }
@Override
    public HashMap<Integer, Task> deleteAllTasks() {
       tasks.clear();
        return tasks;
    }
@Override
    public HashMap<Integer, Epic> deleteAllEpics() {
        subTasks.clear();
        epicTasks.clear();
        return epicTasks;
    }
@Override
    public HashMap<Integer, SubTask> deleteAllSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.removeAllSteps();
        }
        subTasks.clear();
        return subTasks;
    }
@Override
    public Task getTaskById(int id) {
        Task task = null;
        if (tasks.containsKey(id))
            task = tasks.get(id);
        if (epicTasks.containsKey(id))
            task = epicTasks.get(id);
        if (subTasks.containsKey(id))
            task = subTasks.get(id);
        if (task instanceof Task)
            historyManager.add(task);
        return task;

    }
@Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);

        } else if (subTasks.containsKey(id)) {
            int masterId = subTasks.get(id).getMasterId();
            epicTasks.get(masterId).removeStepById(id);
            subTasks.remove(id);
        }
    }
@Override
    public void refreshTask(Task task, int taskId) {
        if (tasks.containsKey(taskId)) {
            createTask(new Task(task.getName(), task.getDescription(), task.getStatus()));
            tasks.remove(taskId);
        }
    }
    @Override
    public void refreshEpic(Epic epic, int taskId) {
        if (epicTasks.containsKey(taskId)) {
            createEpic(new Epic(epic.getName(), epic.getDescription(), epic.getStatus()));
            epicTasks.remove(taskId);
            subTasks.values().removeIf(subTask -> subTask.getMasterId() == taskId);
        }
    }
    @Override
    public void refreshSubTask(SubTask subTask, int taskId) {
        if (subTasks.containsKey(taskId)) {
            int masterKey = subTasks.get(taskId).getMasterId();
            Epic epic = epicTasks.get(masterKey);
            epic.removeStepById(taskId);
            createSubTask(new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    subTask.getMasterId()));

            subTasks.remove(taskId);
        }
    }
    @Override
    public SubTask getSubTasksByEpic(int epicId) {
        SubTask resultTask = null;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getMasterId() == epicId)
                resultTask = subTask;
        }
        return resultTask;
    }

    private static int generateId() {
        return taskCount++;
    }

}
