package service;

import model.*;
import service.util.ManagerSaveException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static model.TaskType.*;


 public class FileBackedTasksManager extends InMemoryTaskManager {
     private File file;
     private String historyString;

     private static final String HEAD = "id,type,name,status,description,epic\n";

     public FileBackedTasksManager(File file) {
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
                 bufferedWriter.write(taskToString(epic));
                 bufferedWriter.newLine();
             }
             for (SubTask subTask : getAllSubTasks().values()) {
                 bufferedWriter.write(taskToString(subTask));
                 bufferedWriter.newLine();
             }
             bufferedWriter.newLine();
             bufferedWriter.write(historyToString(getHistoryManager()));


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
                 .append(task.getDescription()).append(",")
                 .append((task instanceof SubTask) ? ((SubTask) task).getMasterId() : "");
         String resultString = stringBuilder.toString();
         return resultString;
     }

     private Task taskFromString(String value) {
         Task task = null;
         String[] strings = value.split(",");
         if (!isHistoryString(value)) {
             int taskId = Integer.parseInt(strings[0]);
             TaskType taskType = TaskType.valueOf(strings[1]);
             String taskName = strings[2];
             Status taskStatus = Status.valueOf(strings[3]);
             String taskDescription = strings[4];
             switch (taskType) {
                 case EPIC:
                     task = new Epic(taskName, taskDescription, taskStatus);
                     task.setId(taskId);
                     generateId();
                     super.epicTasks.put(taskId, (Epic) task);
                     break;
                 case SUBTASK:
                     int masterId = Integer.parseInt(strings[5]);
                     task = new SubTask(taskName, taskDescription, taskStatus, masterId);
                     task.setId(taskId);
                     generateId();
                     super.subTasks.put(taskId, (SubTask) task);
                     break;
                 case TASK:
                     task = new Task(taskName, taskDescription, taskStatus);
                     task.setId(taskId);
                     generateId();
                     super.tasks.put(taskId, task);
             }
         }
         return task;
     }

     private static String historyToString(HistoryManager manager) {
         StringBuilder stringBuilder = new StringBuilder();
         for (Task task : manager.getHistory()) {
             stringBuilder.append(task.getId()).append(",");
         }
         String result = stringBuilder.toString();
         return result;
     }

     private static List<Integer> historyFromString(String value) {
         List<Integer> resultList = new ArrayList<>();
         String[] strings = value.split(",");
         for (String s : strings) {
             resultList.add(Integer.valueOf(s));
         }
         return resultList;
     }

     private static FileBackedTasksManager loadFromFile(File file) {
         FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
         try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
             bufferedReader.readLine();
             while (bufferedReader.ready()) {
                 String line = bufferedReader.readLine();
                 if (!line.trim().isEmpty()) {
                     fileBackedTasksManager.taskFromString(line);
                     if (isHistoryString(line)) {
                         historyFromString(line);
                     }
                 }
             }
         } catch (IOException e) {
             throw new ManagerSaveException("Ошибка загрузки данных из файла");
         }
         return fileBackedTasksManager;
     }

     public static boolean isHistoryString(String string) {
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
     public Task getTaskById(int id) {
         Task resultTask = super.getTaskById(id);
         save();
         return resultTask;
     }

     public static void main(String[] args) {
         FileBackedTasksManager fileBackedTasksManager
                 = FileBackedTasksManager.loadFromFile(new File("src/service/storage/taskStorage.csv"));
         fileBackedTasksManager.createTask(new Task("<>", "<>", Status.IN_PROGRESS));
         fileBackedTasksManager.getTaskById(0);
         fileBackedTasksManager.getTaskById(3);
         fileBackedTasksManager.getTaskById(5);
         fileBackedTasksManager.getTaskById(1);
     }
 }
