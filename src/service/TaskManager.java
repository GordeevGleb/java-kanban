package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {
    private int taskCount;
    private HashMap<Integer, Task> allTasks = new HashMap<>();

    public Task createTask(String name) {
        Task task = new Task(name);
        task.setId(++taskCount);
        allTasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name) {
        Epic epic = new Epic(name);
        epic.setId(++taskCount);
        allTasks.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubTask(String name, int masterId) {
        SubTask subTask = null;
        Epic epic;
        if (allTasks.containsKey(masterId) && allTasks.get(masterId).getClass().equals(Epic.class)) {
            epic = (Epic) allTasks.get(masterId);
            subTask = new SubTask(name);
            subTask.setId(++taskCount);
            subTask.setMasterId(masterId);
            epic.addStep(subTask);
        }
        return subTask;
    }

    public HashMap<Integer, Task> getAllTasks() {
        HashMap<Integer, Task> allUsualTasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.getClass().equals(Task.class)) {
                allUsualTasks.put(task.getId(), task);
            }
        }
        return allUsualTasks;
    }

    public HashMap<Integer, Task> getAllEpic() {
        HashMap<Integer, Task> allEpics = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.getClass().equals(Epic.class)) {
                allEpics.put(task.getId(), task);
            }
        }
        return allEpics;
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        HashMap<Integer, SubTask> allSubTasks = new HashMap<>();
        Epic epic;
        for (Task task : allTasks.values()) {
            if (task.getClass().equals(Epic.class)) {
                epic = (Epic) task;
                for (SubTask subTask : epic.getEpicSteps().values())
                    allSubTasks.put(subTask.getId(), subTask);
            }
        }
        return allSubTasks;
    }

    public HashMap<Integer, Task> deleteAllTasks() {
        HashMap<Integer, Task> resultMap = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (!task.getClass().equals(Task.class)) {
                resultMap.put(task.getId(), task);
            }
        }
        allTasks = resultMap;
        return allTasks;
    }

    public HashMap<Integer, Task> deleteAllEpics() {
        HashMap<Integer, Task> resultMap = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (!task.getClass().equals(Epic.class)) {
                resultMap.put(task.getId(), task);
            }
        }
        allTasks = resultMap;
        return allTasks;
    }

    public HashMap<Integer, Task> deleteAllSubTasks() {
        Epic epic;
        for (Task task : allTasks.values()) {
            if (task.getClass().equals(Epic.class)) {
                epic = (Epic) task;
                epic.removeAllSteps();
            }
        }
        return allTasks;
    }

    public Task getTaskById(int id) {
        Task task = null;
        if (allTasks.containsKey(id))
            task = allTasks.get(id);
        if (getAllSubTasks().containsKey(id))
            task = getAllSubTasks().get(id);
        return task;

    }

    public HashMap<Integer, Task> deleteTaskById(int id) {
        HashMap<Integer, Task> resultMap = new HashMap<>();
        if (allTasks.containsKey(id)) {
            for (Integer key : allTasks.keySet()) {
                if (!(key == id)) {
                    resultMap.put(key, allTasks.get(key));
                }
            }
            allTasks = resultMap;
        } else if (getAllSubTasks().containsKey(id)) {
            SubTask subTask = getAllSubTasks().get(id);
            int masterId = subTask.getMasterId();
            Epic epic = (Epic) allTasks.get(masterId);
            epic.removeStepById(id);
            epic.checkStatus();
        } else {
            return allTasks;
        }
        return allTasks;
    }

    public void refreshTask(int taskId, String name, String description, Status status) {
        if (allTasks.containsKey(taskId) && allTasks.get(taskId).getClass().equals(Task.class)) {
            Task task = createTask(name);
            task.setDescription(description);
            task.setStatus(status);
            allTasks.remove(taskId);
        }
        if (allTasks.containsKey(taskId) && (allTasks.get(taskId).getClass().equals(Epic.class))) {
            Epic epic = createEpic(name);
            epic.setDescription(description);
            epic.setStatus(status);

            allTasks.remove(taskId);
        }
        if (getAllSubTasks().containsKey(taskId)) {
            int masterKey = getAllSubTasks().get(taskId).getMasterId();
            Epic epic = (Epic) allTasks.get(masterKey);
            epic.removeStepById(taskId);
            SubTask subTask = createSubTask(name, masterKey);
            subTask.setDescription(description);
            subTask.setStatus(status);
            epic.checkStatus();
            getAllSubTasks().remove(taskId);
        }
    }
    public HashMap <Integer, SubTask> getSubTasksByEpic(int epicId) {
        Epic epic = null;
        if (allTasks.containsKey(epicId) && allTasks.get(epicId).getClass().equals(Epic.class)) {
            epic = (Epic) allTasks.get(epicId);
        }
        return epic.getEpicSteps();
    }
    public Status checkStatus(int id) {
        Status status = Status.NEW;
        if (allTasks.containsKey(id) && allTasks.get(id).getClass().equals(Epic.class)) {
            Epic epic = (Epic) allTasks.get(id);
            epic.checkStatus();
            status = epic.getStatus();
        }
        return status;
    }
}







