import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
    File file;
    HistoryManager historyManager;
    FileBackedTasksManager fileBackedTasksManager;
    Epic epic;
    Task task;
    @BeforeEach
    public void create() {
        historyManager = new InMemoryHistoryManager();
        file = new File("test/storageTest/taskStorageTest.csv");
        fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);
        epic = new Epic("Test Epic Name", "Test Epic Description", Status.NEW);
        task = new Task("Test Task Name", "Test Task Description", Status.NEW);
    }
    @Test
    public void save() {
        assertNotNull(file);
        fileBackedTasksManager.createTask(task);
        int taskId = task.getId();
        Task savedTask = fileBackedTasksManager.getTaskById(taskId);
        assertEquals(task, savedTask);
        List<Task> savedHistoryList = fileBackedTasksManager.getHistory();
        assertEquals(1, savedHistoryList.size());
    }
@Test
    public void createFileManagerFromFile() {
        FileBackedTasksManager newFileBackedTaskManager = FileBackedTasksManager.loadFromFile(historyManager, file);
        assertEquals(fileBackedTasksManager, newFileBackedTaskManager,
                "Созданный из файла менеджер отличается от текущего");
}
}
