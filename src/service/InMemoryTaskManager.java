package service;

import model.Epic;
import model.SubTask;
import model.Task;
import service.storage.EpicTasks;
import service.storage.SubTasks;
import service.storage.Tasks;

import java.util.HashMap;


public class InMemoryTaskManager  implements TaskManager {
    private EpicTasks epicTasks = new EpicTasks();
    private Tasks tasks = new Tasks();
    private SubTasks subTasks = new SubTasks();
    private static int taskCount;
private HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager(HistoryManager inMemoryHistoryManager) {
        this.inMemoryHistoryManager = inMemoryHistoryManager;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
      tasks.save(task);
        return task;
    }
@Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
    epicTasks.save(epic);
        return epic;
    }
@Override
    public SubTask createSubTask(SubTask subTask) {
        if (epicTasks.get().containsKey(subTask.getMasterId())) {
            Epic epic = epicTasks.get().get(subTask.getMasterId());
            subTask.setId(generateId());
            epic.addSubtask(subTask);
       subTasks.save(subTask);
        }
        return subTask;
    }
@Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks.get();
    }
@Override
    public HashMap<Integer, Epic> getAllEpic() {
        return epicTasks.get();
    }
@Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subTasks.get();
    }
@Override
    public HashMap<Integer, Task> deleteAllTasks() {
        tasks.get().clear();
        return tasks.get();
    }
@Override
    public HashMap<Integer, Epic> deleteAllEpics() {
        epicTasks.get().clear();
        return epicTasks.get();
    }
@Override
    public HashMap<Integer, SubTask> deleteAllSubTasks() {
        for (Epic epic : epicTasks.get().values()) {
            epic.removeAllSteps();
        }
        subTasks.get().clear();
        return subTasks.get();
    }
@Override
    public Task getTaskById(int id) {
        Task task = null;
        if (tasks.get().containsKey(id))
            task = tasks.get().get(id);
        if (epicTasks.get().containsKey(id))
            task = epicTasks.get().get(id);
        if (subTasks.get().containsKey(id))
            task = subTasks.get().get(id);
        if (task instanceof Task)
            inMemoryHistoryManager.add(task);
        return task;

    }
@Override
    public void deleteTaskById(int id) {
        if (tasks.get().containsKey(id)) {
            tasks.get().remove(id);

        } else if (epicTasks.get().containsKey(id)) {
            epicTasks.get().remove(id);
        } else if (subTasks.get().containsKey(id)) {
            int masterId = subTasks.get().get(id).getMasterId();
            epicTasks.get().get(masterId).removeStepById(id);
            subTasks.get().remove(id);
        }
    }
@Override
    public void refreshTask(Task task, int taskId) {
        if (tasks.get().containsKey(taskId)) {
            createTask(new Task(task.getName(), task.getDescription(), task.getStatus()));
            tasks.get().remove(taskId);
        }
    }
    @Override
    public void refreshEpic(Epic epic, int taskId) {
        if (epicTasks.get().containsKey(taskId)) {
            createEpic(new Epic(epic.getName(), epic.getDescription(), epic.getStatus()));
            epicTasks.get().remove(taskId);
            subTasks.get().values().removeIf(subTask -> subTask.getMasterId() == taskId);
        }
    }
    @Override
    public void refreshSubTask(SubTask subTask, int taskId) {
        if (subTasks.get().containsKey(taskId)) {
            int masterKey = subTasks.get().get(taskId).getMasterId();
            Epic epic = epicTasks.get().get(masterKey);
            epic.removeStepById(taskId);
            createSubTask(new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    subTask.getMasterId()));

            subTasks.get().remove(taskId);
        }
    }
    @Override
    public SubTask getSubTasksByEpic(int epicId) {
        SubTask resultTask = null;
        for (SubTask subTask : subTasks.get().values()) {
            if (subTask.getMasterId() == epicId)
                resultTask = subTask;
        }
        return resultTask;
    }

    private static int generateId() {
        return taskCount++;
    }


}
