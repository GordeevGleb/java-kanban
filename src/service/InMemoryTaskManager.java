package service;

import model.Epic;
import model.SubTask;
import model.Task;


import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
     HashMap<Integer, Task> tasks = new HashMap<>();
     HashMap<Integer, Epic> epicTasks = new HashMap<>();
     HashMap<Integer, SubTask> subTasks = new HashMap<>();
     HistoryManager historyManager = Managers.getDefaultHistoryManager();
     static int taskCount;



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
            Epic epic = epicTasks.get(subTask.getMasterId());
            subTask.setId(generateId());
            epic.addSubtask(subTask);
            subTasks.put(subTask.getId(), subTask);
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
    public void deleteAllTasks() {
       tasks.clear();
       historyManager.remove("Task");
    }
@Override
    public void deleteAllEpics() {
        subTasks.clear();
        epicTasks.clear();
        historyManager.remove("Epic");
    }
@Override
    public void deleteAllSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.removeAllSteps();
        }
        subTasks.clear();
        historyManager.remove("SubTask");
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
        }

        else if (epicTasks.containsKey(id)) {
            subTasks.values().removeIf(subTask -> subTask.getMasterId() == id);
            epicTasks.get(id).removeAllSteps();
            epicTasks.remove(id);
        }

        else if (subTasks.containsKey(id)) {
            int masterId = subTasks.get(id).getMasterId();
            epicTasks.get(masterId).removeStepById(id);
            subTasks.remove(id);
        }
    historyManager.remove(id);
    }
@Override
    public void refreshTask(Task task, int taskId) {
      Task savedTask = tasks.get(taskId);
      savedTask.setName(task.getName());
      savedTask.setDescription(task.getDescription());
      savedTask.setStatus(task.getStatus());
    }
    @Override
    public void refreshEpic(Epic epic, int epicId) {
        Epic savedEpic = epicTasks.get(epicId);
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        savedEpic.setStatus(epic.getStatus());
    }
    @Override
    public void refreshSubTask(SubTask subTask, int subTaskId) {
    SubTask savedSubTask = subTasks.get(subTaskId);
    savedSubTask.setName(subTask.getName());
    savedSubTask.setDescription(subTask.getDescription());
    savedSubTask.setStatus(subTask.getStatus());
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


     static int generateId() {
        return taskCount++;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
