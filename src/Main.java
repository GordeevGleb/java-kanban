import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.Scanner;                                           /* Ярослав, здравствуй!

                                                                     Эта итерация, по ощущениям, может претендовать на
                                                                     итоговый вариант. Ошибки были глупыми, но они дали
                                                                     ценный опыт :)
                                                                     */

public class Main {
    public static void main(String[] args) {
       TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            getInfo();
            int command = scanner.nextInt();
            if (command == 1) {
                taskManager.createTask(new Task("fTask", "someDescription", Status.IN_PROGRESS));
                taskManager.createEpic(new Epic("sTask", "someEpicDescriptionn", Status.IN_PROGRESS));
                taskManager.createEpic(new Epic("tTask", "somesecondEpicDescription", Status.DONE));
                taskManager.createSubTask(new SubTask("subTask", "123", Status.DONE, 1));
                taskManager.createSubTask(new SubTask("sSubTask", "someSubTaskDesc."
                        , Status.NEW, 1));
            } else if (command == 2) {
                System.out.println(taskManager.getAllTasks());
                System.out.println(taskManager.getAllEpic());
                System.out.println(taskManager.getAllSubTasks());
            } else if (command == 3) {
                taskManager.deleteAllTasks();
                taskManager.deleteAllEpics();
                taskManager.deleteAllSubTasks();
            } else if (command == 4) {
                System.out.println(taskManager.getTaskById(1));
                System.out.println(taskManager.getTaskById(4));
                System.out.println(taskManager.getTaskById(1231));
            } else if (command == 5) {
                taskManager.refreshTask(new Task("newName", "new description", Status.IN_PROGRESS),
                        0);
                taskManager.refreshSubTask(new SubTask("new subTaskName", "new description",
                        Status.DONE, 1),4);
              //  taskManager.refreshEpic(new Epic("newEpicName", "new epic description", Status.DONE), 1);
            } else if (command == 6) {
                taskManager.deleteTaskById(3);
              //  taskManager.deleteTaskById(3);
             //  taskManager.deleteTaskById(2);
            } else if (command == 7) {
                System.out.println(taskManager.getSubTasksByEpic(2));
                System.out.println(taskManager.getSubTasksByEpic(3));
            }  else if (command == 0) {
                break;
            }
        }
    }
    public static void getInfo () {
        System.out.println("1 - Создание и сохранение задачи");
        System.out.println("2 - Сгруппировать список задач по классам");
        System.out.println("3 - Удалить задачи по классам");
        System.out.println("4 - Поиск задачи по id");
        System.out.println("5 - Обновление задачи");
        System.out.println("6 - Удаление задачи по id");
        System.out.println("7 - Получить список всех задач Эпика");
        System.out.println("0 - Выход");
    }
}
