package service;

import model.Epic;
import model.SubTask;
import model.Task;
import service.util.ManagerSaveException;


import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epicTasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HistoryManager historyManager;
    static int taskMaxId;
      TreeSet<Task> timeSortedSet = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Task createTask(Task task) {
        if (isTimeCross(task)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        timeSortedSet.add(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (isTimeCross(epic)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        epic.setId(generateId());
        epicTasks.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Epic epic = epicTasks.get(subTask.getMasterId());
        if (isTimeCross(subTask)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        subTask.setId(generateId());
        epic.addSubtask(subTask);
        subTasks.put(subTask.getId(), subTask);
        timeSortedSet.add(subTask);
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
        historyManager.removeByName("Task");
        timeSortedSet.removeIf(task -> task.getClass().equals(Task.class));
    }

    @Override
    public void deleteAllEpics() {
        subTasks.clear();
        epicTasks.clear();
        historyManager.removeByName("Epic");
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.removeAllSteps();
        }
        subTasks.clear();
        historyManager.removeByName("SubTask");
        timeSortedSet.removeIf(task -> task.getClass().equals(SubTask.class));
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
            timeSortedSet.removeIf(task -> task.getId() == id);
        } else if (epicTasks.containsKey(id)) {
            subTasks.values().removeIf(subTask -> subTask.getMasterId() == id);
            epicTasks.get(id).removeAllSteps();
            epicTasks.remove(id);
        } else if (subTasks.containsKey(id)) {
            int masterId = subTasks.get(id).getMasterId();
            Epic epic = epicTasks.get(masterId);
            epic.removeStepById(id);
            subTasks.remove(id);
            timeSortedSet.removeIf(task -> task.getId() == id);
        }
        for (Task task : historyManager.getHistory()) {
            if (task.getId() == id) {
                historyManager.remove(task.getId());
            }
        }
    }

    @Override
    public void refreshTask(Task task, int taskId) {
        if (isTimeCross(task)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        Task savedTask = tasks.get(taskId);
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());

    }

    @Override
    public void refreshEpic(Epic epic, int epicId) {
        if (isTimeCross(epic)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        Epic savedEpic = epicTasks.get(epicId);
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        savedEpic.setStatus(epic.getStatus());

    }

    @Override
    public void refreshSubTask(SubTask subTask, int subTaskId) {
        if (isTimeCross(subTask)) {
            throw new ManagerSaveException("Время выполнения пересекается с уже существуеющей задачей");
        }
        SubTask savedSubTask = subTasks.get(subTaskId);
        savedSubTask.setName(subTask.getName());
        savedSubTask.setDescription(subTask.getDescription());
        savedSubTask.setStatus(subTask.getStatus());
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasksByEpic(int epicId) {
        HashMap<Integer, SubTask> resultList = null;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getMasterId() == epicId)
                resultList.put(subTask.getId(), subTask);
        }
        return resultList;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return timeSortedSet;
    }

    public void fillTimeSortedSet(Task task) {
        timeSortedSet.add(task);
    }

    private boolean isTimeCross(Task task) {
        boolean isCrossed = false;
        for (Task sortedTask : timeSortedSet) {
            if (Optional.ofNullable(sortedTask.getStartTime()).isEmpty() ||
                    Optional.ofNullable(task.getStartTime()).isEmpty()) {
                continue;
            } else if (task.getStartTime().isAfter(sortedTask.getStartTime())
                    && (task.getStartTime().isBefore(sortedTask.getEndTime()))) {
                isCrossed = true;
            }
            else if (task.getEndTime().isAfter(sortedTask.getStartTime())
            && (task.getEndTime().isBefore(sortedTask.getEndTime()))) {
                isCrossed = true;
            }
            else if (task.getStartTime().isAfter(sortedTask.getStartTime())
            && (task.getEndTime().isBefore(sortedTask.getEndTime()))) {
                isCrossed = true;
            }
        }
        return isCrossed;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    static int generateId() {
        return taskMaxId++;
    }

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
