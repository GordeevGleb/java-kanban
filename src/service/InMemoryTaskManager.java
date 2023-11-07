package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class InMemoryTaskManager implements TaskManager {
    private  List<Task> taskHistory = new ArrayList<>();



    private HashMap<Integer, Task> allTasks = new HashMap<>();
    private HashMap<Integer, Epic> allEpicTasks = new HashMap<>();
    private HashMap<Integer, SubTask> allSubTasks = new HashMap<>();
    private int taskCount;
@Override
    public Task createTask(Task task) {
        task.setId(generateId());
        allTasks.put(task.getId(), task);
        return task;
    }
@Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        allEpicTasks.put(epic.getId(), epic);
        return epic;
    }
@Override
    public SubTask createSubTask(SubTask subTask) {
        if (allEpicTasks.containsKey(subTask.getMasterId())) {
            Epic epic = allEpicTasks.get(subTask.getMasterId());
            subTask.setId(generateId());
            epic.addSubtask(subTask);
            allSubTasks.put(subTask.getId(), subTask);
        }
        return subTask;
    }
@Override
    public HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }
@Override
    public HashMap<Integer, Epic> getAllEpic() {
        return allEpicTasks;
    }
@Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        return allSubTasks;
    }
@Override
    public HashMap<Integer, Task> deleteAllTasks() {
        allTasks.clear();
        return allTasks;
    }
@Override
    public HashMap<Integer, Epic> deleteAllEpics() {
        allSubTasks.clear();
        allEpicTasks.clear();
        return allEpicTasks;
    }
@Override
    public HashMap<Integer, SubTask> deleteAllSubTasks() {
        for (Epic epic : allEpicTasks.values()) {
            epic.removeAllSteps();
        }
        allSubTasks.clear();
        return allSubTasks;
    }
@Override
    public Task getTaskById(int id) {
        Task task = null;

            if (allTasks.containsKey(id))
                task = allTasks.get(id);
            if (allEpicTasks.containsKey(id))
                task = allEpicTasks.get(id);
            if (allSubTasks.containsKey(id))
                task = allSubTasks.get(id);

            taskHistory = fillHistory(task);


        return task;

    }
@Override
    public void deleteTaskById(int id) {
        if (allTasks.containsKey(id)) {
            allTasks.remove(id);

        } else if (allEpicTasks.containsKey(id)) {
            allEpicTasks.remove(id);
        } else if (allSubTasks.containsKey(id)) {
            int masterId = allSubTasks.get(id).getMasterId();
            allEpicTasks.get(masterId).removeStepById(id);
            allSubTasks.remove(id);
        }
    }
@Override
    public void refreshTask(Task task, int taskId) {
        if (allTasks.containsKey(taskId)) {
            createTask(new Task(task.getName(), task.getDescription(), task.getStatus()));
            allTasks.remove(taskId);
        }
    }
    @Override
    public void refreshEpic(Epic epic, int taskId) {
        if (allEpicTasks.containsKey(taskId)) {
            createEpic(new Epic(epic.getName(), epic.getDescription(), epic.getStatus()));
            allEpicTasks.remove(taskId);
            allSubTasks.values().removeIf(subTask -> subTask.getMasterId() == taskId);
        }
    }
    @Override
    public void refreshSubTask(SubTask subTask, int taskId) {
        if (allSubTasks.containsKey(taskId)) {
            int masterKey = allSubTasks.get(taskId).getMasterId();
            Epic epic = allEpicTasks.get(masterKey);
            epic.removeStepById(taskId);
            createSubTask(new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    subTask.getMasterId()));

            allSubTasks.remove(taskId);
        }
    }
    @Override
    public SubTask getSubTasksByEpic(int epicId) {
        SubTask resultTask = null;
        for (SubTask subTask : allSubTasks.values()) {
            if (subTask.getMasterId() == epicId)
                resultTask = subTask;
        }
        taskHistory = fillHistory(resultTask);
        return resultTask;
    }

private int generateId() {
        return taskCount++;
    }
@Override
    public  List<Task> getHistory() {
return taskHistory;
}
@Override
public  List<Task> fillHistory(Task task) {
    List<Task> taskLog = taskHistory;
    if (!task.equals(null)) {
        if (taskLog.size() < 10)
            taskLog.add(task);
        else {
            taskLog.remove(0);
            taskLog.add(task);
        }
    }
    return taskLog;
}
}