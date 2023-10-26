import model.Status;
import service.TaskManager;

import java.util.Scanner;                                           /* Ярослав, здравствуй!

                                                                     Эта итерация, скорее, для получения рекомендаций,
                                                                     нежели конечный вариант.

                                                                     */

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            getInfo();
            int command = scanner.nextInt();
            if (command == 1) {
                taskManager.createTask("fTask");
                taskManager.createEpic("sTask");
                taskManager.createEpic("tTask");
                taskManager.createSubTask("fSubTask", 2);
                taskManager.createSubTask("sSubTask", 2);
            } else if (command == 2) {
                System.out.println(taskManager.getAllTasks());
                System.out.println(taskManager.getAllEpic());
                System.out.println(taskManager.getAllSubTasks());
            } else if (command == 3) {
                taskManager.deleteAllTasks();
               // taskManager.deleteAllEpics();
                taskManager.deleteAllSubTasks();
            } else if (command == 4) {
                System.out.println(taskManager.getTaskById(1));
                System.out.println(taskManager.getTaskById(4));
                System.out.println(taskManager.getTaskById(1231));
            } else if (command == 5) {
                taskManager.refreshTask(4, "newSubTask", "123", Status.DONE);
                taskManager.refreshTask(1, "new Task", "3213", Status.IN_PROGRESS);
            } else if (command == 6) {
                System.out.println(taskManager.deleteTaskById(4));
                System.out.println(taskManager.deleteTaskById(3));
               // System.out.println(taskManager.deleteTaskById(2));
            } else if (command == 7) {
                System.out.println(taskManager.getSubTasksByEpic(2));
                System.out.println(taskManager.getSubTasksByEpic(3));
            } else if (command == 8) {
                System.out.println(taskManager.checkStatus(2));
            } else if (command == 0) {
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
        System.out.println("8 - Проверка статуса задачи(только для эпиков)");
        System.out.println("0 - Выход");
    }
}
