package service;

import model.*;
import service.util.ManagerSaveException;
import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static model.TaskType.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static List<Integer> historyFromFile;

    private static final String HEAD = "id,type,name,status,description,epic,startTime,endTime\n";

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(HEAD);
            for (Task task : getAllTasks().values()) {
                bufferedWriter.write(taskToString(task));
                bufferedWriter.newLine();
            }
            for (Epic epic : getAllEpic().values()) {
                epic.refreshTime();
                bufferedWriter.write(taskToString(epic));
                bufferedWriter.newLine();
            }
            for (SubTask subTask : getAllSubTasks().values()) {
                bufferedWriter.write(taskToString(subTask));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл");
        }
    }

    private String taskToString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(",");
        if (task instanceof Epic) {
            stringBuilder.append(EPIC).append(",");
        } else if (task instanceof SubTask) {
            stringBuilder.append(SUBTASK).append(",");
        } else {
            stringBuilder.append(TASK).append(",");
        }
        stringBuilder.append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());
        if (task instanceof SubTask) {
            stringBuilder.append(",").append(((SubTask) task).getMasterId());
        } else {
            stringBuilder.append(",").append("-");
        }
        stringBuilder.append(",").append(task.getStartTime()).append(",").append(task.getEndTime());
        String resultString = stringBuilder.toString();
        return resultString;
    }

    private Task taskFromString(String value) {
        Task task = null;
        if (!isHistoryString(value)) {
            String[] strings = value.split(",");
            int taskId = Integer.parseInt(strings[0]);
            TaskType taskType = TaskType.valueOf(strings[1]);
            String taskName = strings[2];
            Status taskStatus = Status.valueOf(strings[3]);
            String taskDescription = strings[4];
            LocalTime startTime = parseTime(strings[6]);
            LocalTime endTime = parseTime(strings[7]);
            long duration = getDuration(startTime, endTime);
            switch (taskType) {
                case EPIC:
                    task = new Epic(taskName, taskDescription, taskStatus, startTime, duration);
                    task.setId(taskId);
                    super.epicTasks.put(taskId, (Epic) task);
                    break;
                case SUBTASK:
                    int masterId = Integer.parseInt(strings[5]);
                    task = new SubTask(taskName, taskDescription, taskStatus, startTime, duration, masterId);
                    task.setId(taskId);
                    Epic epic = epicTasks.get(masterId);
                    epic.addSubtask((SubTask) task);
                    super.subTasks.put(taskId, (SubTask) task);
                    timeSortedSet.add(task);
                    break;
                case TASK:
                    task = new Task(taskName, taskDescription, taskStatus, startTime, duration);
                    task.setId(taskId);
                    super.tasks.put(taskId, task);
                    timeSortedSet.add(task);
            }
        }
        return task;
    }

    private LocalTime parseTime(String s) {
        try {
            return LocalTime.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private long getDuration(LocalTime startTime, LocalTime endTime) {
        if (startTime == null) {
            return 0;
        }
        else {
            return Duration.between(startTime, endTime).toMinutes();
        }
    }

    private static String historyToString(List<Task> history) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Task task : history) {
            stringJoiner.add(String.valueOf(task.getId()));
        }
        return stringJoiner.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> resultList = new ArrayList<>();
        String[] strings = value.split(",");
        for (String s : strings) {
            resultList.add(Integer.valueOf(s));
        }
        return resultList;
    }

    public static FileBackedTasksManager loadFromFile(HistoryManager historyManager, File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager ,file);
        int maxId = -1;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.trim().isEmpty()) {
                    if (isHistoryString(line)) {
                        historyFromFile = historyFromString(line);
                    } else {
                        Task task = fileBackedTasksManager.taskFromString(line);
                        if (!task.equals(null)) {
                            int id = task.getId();
                            if (maxId < id) {
                                maxId = id;
                            }
                        }
                    }
                }
            }
            taskMaxId = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла");
        }
        return fileBackedTasksManager;
    }

    private static boolean isHistoryString(String string) {
        try {
            Integer.parseInt(string.replaceAll(",", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Task createTask(Task task) {
        Task resultTask = super.createTask(task);
        save();
        return resultTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic resultEpic = super.createEpic(epic);
        save();
        return resultEpic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask resultSubTask = super.createSubTask(subTask);
        save();
        return resultSubTask;
    }

    @Override
    public void deleteAllTasks() {
        try {
            super.deleteAllTasks();
            save();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("Задачи уже удалены, либо не найдены.");
        }
    }

    @Override
    public void deleteAllEpics() {
        try {
            super.deleteAllEpics();
            save();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("Задачи уже удалены, либо не найдены.");
        }
    }

    @Override
    public void deleteAllSubTasks() {
        try {
            super.deleteAllSubTasks();
            save();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("Задачи уже удалены, либо не найдены.");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        try {
            super.deleteTaskById(id);
            save();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("Задачи с таким id нет в файле");
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task resultTask = super.getTaskById(id);
        save();
        return resultTask;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBackedTasksManager that = (FileBackedTasksManager) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
    public List<Integer> getHistoryFromFile() {
        return historyFromFile;
    }

}
