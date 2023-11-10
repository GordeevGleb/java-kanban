import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            getInfo();
            int command = scanner.nextInt();
            if (command == 1) {
                Managers.getDefault().createTask(new Task("fTask", "someDescription", Status.IN_PROGRESS));
                Managers.getDefault().createEpic(new Epic("sTask", "someEpicDescriptionn", Status.IN_PROGRESS));
                Managers.getDefault().createEpic(new Epic("tTask", "somesecondEpicDescription", Status.DONE));
                Managers.getDefault().createSubTask(new SubTask("subTask", "123", Status.DONE, 1));
                Managers.getDefault().createSubTask(new SubTask("sSubTask", "someSubTaskDesc.", Status.NEW, 1));
            } else if (command == 2) {
                System.out.println(Managers.getDefault().getAllTasks());
                System.out.println(Managers.getDefault().getAllEpic());
                System.out.println(Managers.getDefault().getAllSubTasks());
            } else if (command == 3) {
                Managers.getDefault().deleteAllTasks();
                Managers.getDefault().deleteAllEpics();
                Managers.getDefault().deleteAllSubTasks();
            } else if (command == 4) {
                System.out.println(Managers.getDefault().getTaskById(1));
                System.out.println(Managers.getDefault().getTaskById(4));
                System.out.println(Managers.getDefault().getTaskById(0));
                System.out.println(Managers.getDefault().getTaskById(2));
                System.out.println(Managers.getDefault().getTaskById(3));
                System.out.println(Managers.getDefault().getTaskById(1231));
                System.out.println(Managers.getDefault().getTaskById(6));
                System.out.println(Managers.getDefault().getTaskById(1));
                System.out.println(Managers.getDefault().getTaskById(4));
                System.out.println(Managers.getDefault().getTaskById(3));
                System.out.println(Managers.getDefault().getTaskById(2));
                System.out.println(Managers.getDefault().getTaskById(5));
                System.out.println(Managers.getDefault().getTaskById(0));
                System.out.println(Managers.getDefault().getTaskById(2));
                System.out.println(Managers.getDefault().getTaskById(0));
            } else if (command == 5) {
                Managers.getDefault().refreshTask(new Task("newName", "new description", Status.IN_PROGRESS),
                        0);
                Managers.getDefault().refreshSubTask(new SubTask("new subTaskName", "new description",
                        Status.DONE, 1),4);
                Managers.getDefault().refreshEpic(new Epic("newEpicName", "new epic description", Status.DONE), 1);
            } else if (command == 6) {
                Managers.getDefault().deleteTaskById(3);
                Managers.getDefault().deleteTaskById(3);
                Managers.getDefault().deleteTaskById(2);
            } else if (command == 7) {
                System.out.println(Managers.getDefault().getSubTasksByEpic(2));
                System.out.println(Managers.getDefault().getSubTasksByEpic(3));
            }  else if (command == 8) {
               Managers.getDefaultHistoryManager().getHistory().forEach(System.out::println);
            }
            else if (command == 0){
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
        System.out.println("8 - История просмотров");
    }
}
