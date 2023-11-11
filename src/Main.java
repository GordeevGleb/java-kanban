import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Managers managers = new Managers();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            getInfo();
            int command = scanner.nextInt();
            if (command == 1) {
                managers.getDefault().createTask(new Task("fTask", "someDescription",
                        Status.IN_PROGRESS));
                managers.getDefault().createEpic(new Epic("sTask", "someEpicDescriptionn",
                        Status.IN_PROGRESS));
                managers.getDefault().createEpic(new Epic("tTask", "somesecondEpicDescription",
                        Status.DONE));
                managers.getDefault().createSubTask(new SubTask("subTask", "123", Status.DONE,
                        1));
                managers.getDefault().createSubTask(new SubTask("sSubTask", "someSubTaskDesc.",
                        Status.NEW, 1));
            } else if (command == 2) {
                System.out.println(managers.getDefault().getAllTasks());
                System.out.println(managers.getDefault().getAllEpic());
                System.out.println(managers.getDefault().getAllSubTasks());
            } else if (command == 3) {
                managers.getDefault().deleteAllTasks();
                managers.getDefault().deleteAllEpics();
                managers.getDefault().deleteAllSubTasks();
            } else if (command == 4) {
                System.out.println(managers.getDefault().getTaskById(1));
                System.out.println(managers.getDefault().getTaskById(4));
                System.out.println(managers.getDefault().getTaskById(0));
                System.out.println(managers.getDefault().getTaskById(2));
                System.out.println(managers.getDefault().getTaskById(3));
                System.out.println(managers.getDefault().getTaskById(1231));
                System.out.println(managers.getDefault().getTaskById(6));
                System.out.println(managers.getDefault().getTaskById(1));
                System.out.println(managers.getDefault().getTaskById(4));
                System.out.println(managers.getDefault().getTaskById(3));
                System.out.println(managers.getDefault().getTaskById(2));
                System.out.println(managers.getDefault().getTaskById(5));
                System.out.println(managers.getDefault().getTaskById(0));
                System.out.println(managers.getDefault().getTaskById(2));
                System.out.println(managers.getDefault().getTaskById(0));
            } else if (command == 5) {
                managers.getDefault().refreshTask(new Task("newName", "new description",
                                Status.IN_PROGRESS),
                        0);
                managers.getDefault().refreshSubTask(new SubTask("new subTaskName", "new description",
                        Status.DONE, 1),4);
                managers.getDefault().refreshEpic(new Epic("newEpicName", "new epic description",
                        Status.DONE), 1);
            } else if (command == 6) {
                managers.getDefault().deleteTaskById(3);
                managers.getDefault().deleteTaskById(3);
                managers.getDefault().deleteTaskById(2);
            } else if (command == 7) {
                System.out.println(managers.getDefault().getSubTasksByEpic(2));
                System.out.println(managers.getDefault().getSubTasksByEpic(3));
            }  else if (command == 8) {
               managers.getDefaultHistoryManager().getHistory().forEach(System.out::println);
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
