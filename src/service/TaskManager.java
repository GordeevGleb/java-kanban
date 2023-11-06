package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {



     Task createTask(Task task);

     Epic createEpic(Epic epic);

     SubTask createSubTask(SubTask subTask);

     HashMap<Integer, Task> getAllTasks();

     HashMap<Integer, Epic> getAllEpic();

     HashMap<Integer, SubTask> getAllSubTasks();

     HashMap<Integer, Task> deleteAllTasks();

     HashMap<Integer, Epic> deleteAllEpics();

     HashMap<Integer, SubTask> deleteAllSubTasks();

     Task getTaskById(int id);

     void deleteTaskById(int id);

     void refreshTask(Task task, int taskId);
     void refreshEpic(Epic epic, int taskId);
     void refreshSubTask(SubTask subTask, int taskId);
     SubTask getSubTasksByEpic(int epicId);

     int generateId();

    static List<Task> getHistory() {
        return null;
    }
}







