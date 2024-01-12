import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;
import service.util.ManagerSaveException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    TaskManager taskManager;
    Task task;
    Task task1;
    Epic epic;
    SubTask subTask;
    SubTask subTask1;

    @BeforeEach
    public void create() {
        taskManager = new InMemoryTaskManager();
    }


    @Override
    @Test
    void createTask() {
        task = new Task(
                "Test Task Name", "Test Task Description",
                Status.DONE, LocalTime.of(9, 10), 10);
        taskManager.createTask(task);
        int taskId = task.getId();
        Task createdTask = taskManager.getTaskById(taskId);
        assertNotNull(createdTask, "Задача не создана");
        assertEquals(task, createdTask, "Задачи не совпадают");
        HashMap<Integer, Task> savedTasks = taskManager.getAllTasks();
        assertNotNull(savedTasks, "Список задач отображается некорректно");
        assertEquals(1, savedTasks.size(), "Некорректное поведение при добавлении задачи в список");
        assertEquals(task, savedTasks.get(0), "Задачи не совпадают");
        TreeSet<Task> savedSortedTasks = InMemoryTaskManager.getPrioritizedTasks();
        assertEquals(1,savedSortedTasks.size(),
                "Некорректное поведение при добавлении в сортированный по LocalTime список задач");
        task1 = new Task("Another Test Task Name", "Another Test Task Descripton",
                Status.NEW, LocalTime.of(9,15), 30);
        assertThrows(ManagerSaveException.class,
                () -> taskManager.createTask(task1));
        taskManager.deleteTaskById(taskId);
        savedTasks = taskManager.getAllTasks();
        assertEquals(0, savedTasks.size(), "Метод для удаления всех задач работает некорректно");
    }

    @Override
    @Test
    void createEpic() {
epic = new Epic("Test Epic Name", "Test Epic Description", Status.NEW);
taskManager.createEpic(epic);
int epicId = epic.getId();
Epic createdEpic =(Epic) taskManager.getTaskById(epicId);
assertNotNull(createdEpic, "Эпик не создан");
assertEquals(epic, createdEpic, "Эпики не совпадают");
HashMap<Integer, Epic> savedEpics = taskManager.getAllEpic();
assertNotNull(savedEpics, "Список эпиков отображается некорректно");
assertEquals(1, savedEpics.size(), "Некорректное поведение при добавлении эпика в список");
assertEquals(epic, createdEpic, "Эпики не совпадают");
subTask = new SubTask("Test Subtask for Epic Test", "Some Description",
        Status.IN_PROGRESS, LocalTime.of(14,0), 20, epicId);
taskManager.createSubTask(subTask);
        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus,
                "Некорректное поведение статуса эпика при добавлении сабтаска");
assertEquals(1, epic.getEpicSteps().size(), "Некорректное добавление задачи в список эпика");
epic.setStartTime();
epic.setDuration();
epic.setEndTime();
LocalTime epicStartTime = epic.getStartTime();
assertEquals(LocalTime.of(14, 0), epicStartTime,
        "Некорректный подсчёт времени начала выполнения эпика");
int epicDuration = epic.getDuration();
assertEquals(20, epicDuration, "Некорректный подсчёт времени выполнения эпика");
LocalTime epicEndTime = epic.getEndTime();
assertEquals(LocalTime.of(14, 20), epicEndTime,
        "Некорректный подсчёт времени окончания выполения эпика");
        taskManager.deleteTaskById(epicId);
        savedEpics = taskManager.getAllEpic();
        assertEquals(0, savedEpics.size(), "Метод для удаления всех эпиков работает некорректно");
    }

    @Override
    @Test
    void createSubTask() {
epic = new Epic("Test Epic Name", "Test Epic Description", Status.NEW);
subTask = new SubTask("Test Subtask", "Some Description",
        Status.IN_PROGRESS, LocalTime.of(14,0), 20, 314);
assertThrows(NullPointerException.class,
        ()-> taskManager.createSubTask(subTask),
        "Некорректное поведение при создание сабтаска с несуществующим мастер-эпиком");
taskManager.createEpic(epic);
int epicId = epic.getId();
subTask1 = new SubTask("Test Subtask1", "Some Description",
        Status.IN_PROGRESS, LocalTime.of(14,0), 20, epicId);
taskManager.createSubTask(subTask1);
int subTask1Id = subTask1.getId();
SubTask savedSubTask =(SubTask) taskManager.getTaskById(subTask1Id);
assertNotNull(savedSubTask, "Сабтаск не создан");
assertEquals(subTask1, savedSubTask, "Сабтаски не совпадают");
        HashMap<Integer, SubTask> savedSubTasks = taskManager.getAllSubTasks();
        assertNotNull(savedSubTasks, "Список сабтасков отображается некорректно");
        assertEquals(1, savedSubTasks.size(), "Некорректное поведение при добавлении сабтаска в список");
        assertEquals(subTask1, savedSubTasks.get(subTask1Id), "Сабтаски не совпадают");
        TreeSet<Task> savedSortedTasks = InMemoryTaskManager.getPrioritizedTasks();
        assertEquals(1,savedSortedTasks.size(),
                "Некорректное поведение при добавлении в сортированный по LocalTime список задач");
        subTask = new SubTask("Another Test SubTask Name", "Another Test SubTask Descripton",
                Status.NEW, LocalTime.of(14,15), 30, epicId);
        assertThrows(ManagerSaveException.class,
                () -> taskManager.createTask(subTask));
        taskManager.deleteTaskById(subTask1Id);
        savedSubTasks = taskManager.getAllSubTasks();
        assertEquals(0, savedSubTasks.size(), "Метод для удаления всех эпиков работает некорректно");
    }

    @Override
    @Test
    void getSubTasksByEpic() {
        epic = new Epic("Test Epic Name", "Test Epic Description", Status.NEW);
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        subTask = new SubTask("Test Subtask", "Some Description",
                Status.IN_PROGRESS, epicId);
        taskManager.createSubTask(subTask);
        subTask1 = new SubTask("Test Subtask1", "Some Description",
                Status.IN_PROGRESS, epicId);
        taskManager.createSubTask(subTask1);
        HashMap<Integer, SubTask> savedEpicSteps = epic.getEpicSteps();
        assertNotNull(savedEpicSteps, "Сабтаски некорректно сохранились в качестве шагов эпика");
assertEquals(2, savedEpicSteps.size(), "Количество сабтасков в эпике некорректно выводится");
int subtaskId = subTask.getId();
        SubTask savedSubTask = epic.getEpicSteps().get(subtaskId);
        assertEquals(subTask, savedSubTask,"Сабтаски не идентичны");
        epic.removeStepById(subtaskId);
        savedEpicSteps = epic.getEpicSteps();
        assertEquals(1, savedEpicSteps.size(), "Некорректное поведение при удалении одного сабтаска");
    }
}
