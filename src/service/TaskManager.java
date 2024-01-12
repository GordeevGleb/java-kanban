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

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    Task getTaskById(int id);

    void deleteTaskById(int id);

    void refreshTask(Task task, int taskId);

    void refreshEpic(Epic epic, int epicId);

    void refreshSubTask(SubTask subTask, int subTaskId);

    HashMap<Integer, SubTask> getSubTasksByEpic(int epicId);
}










