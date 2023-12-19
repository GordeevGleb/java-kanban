package service;

import model.*;
import service.util.ManagerSaveException;

import java.io.*;

import java.util.ArrayList;
import java.util.List;


import static java.nio.file.Files.createFile;
import static model.TaskType.*;
                                                                         /*Ярослав, здравствуй.
                                                                         Заранее извиняюсь за то, что отправляю
                                                                         на ревью заведомо нерабочий вариант, однако,
                                                                         руководствуясь принципом "за спрос не бьют",
                                                                         решил таким образом спросить твоего совета.


                                                                          */

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    private final String HEAD = "id,type,name,status,description,epic\n";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(HEAD);
            for (Task task : getAllTasks().values()) {
                bufferedWriter.write(taskToString(task));
                bufferedWriter.newLine();
            }
            for (Epic epic : getAllEpic().values()) {
                bufferedWriter.write(taskToString(epic));
                bufferedWriter.newLine();
            }
            for (SubTask subTask : getAllSubTasks().values()) {
                bufferedWriter.write(taskToString(subTask));
                bufferedWriter.newLine();
            }
            for (Task task : getHistoryManager().getHistory()) {
                historyToString(getHistoryManager());
            }
        }
        catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл");
        }
    }
    public String taskToString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(",");
        if (task instanceof Epic) {
            stringBuilder.append(EPIC).append(",");
        }
        else if (task instanceof SubTask) {
            stringBuilder.append(SUBTASK).append(",");
        }
        else {
            stringBuilder.append(TASK).append(",");
        }
        stringBuilder.append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append((task instanceof SubTask)? ((SubTask) task).getMasterId() :"");
        String resultString = stringBuilder.toString();
        return resultString;
    }
    public Task taskFromString(String value) {
        Task task = null;
        String[] strings = value.split(",");
        TaskType taskType = TaskType.valueOf(strings[1]);
        switch (taskType) {
            case EPIC:
                task = createEpic(new Epic(strings[2], strings[4], Status.valueOf(strings[3])));
                break;
            case SUBTASK :
                task = createSubTask(new SubTask(strings[2], strings[4],
                    Status.valueOf(strings[3]), Integer.valueOf(strings[5])));
                break;
            case TASK:
                task = createTask(new Task(strings[2], strings[4], Status.valueOf(strings[3])));

        }
        return task;
    }
    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringBuilder.append(task.getId()).append(",");
        }
        String result = stringBuilder.toString();
        return result;
    }
    public static List<Integer> historyFromString(String value) {
        List<Integer> resultList = new ArrayList<>();
        String[] strings = value.split(",");
        for (String s : strings) {
            resultList.add(Integer.valueOf(s));
        }
        return resultList;
    }
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                fileBackedTasksManager.taskFromString(line);
            }
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.isEmpty()) {
                    historyFromString(line);
                }
            }
        }
        catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла");
        }
        return fileBackedTasksManager;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void refreshTask(Task task, int taskId) {
        super.refreshTask(task, taskId);
        save();
    }

    @Override
    public void refreshEpic(Epic epic, int epicId) {
        super.refreshEpic(epic, epicId);
        save();
    }

    @Override
    public void refreshSubTask(SubTask subTask, int subTaskId) {
        super.refreshSubTask(subTask, subTaskId);
        save();
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager
                = FileBackedTasksManager.loadFromFile(new File("src/service/storage/taskStorage.csv"));
        fileBackedTasksManager.createTask(new Task("Вырастить дерево", "123", Status.NEW));
        fileBackedTasksManager.createEpic(new Epic("Воспитать сына", "|", Status.IN_PROGRESS));
        fileBackedTasksManager.createSubTask(new SubTask("Почистить зубы", "123123", Status.DONE, 1));
    }
}
