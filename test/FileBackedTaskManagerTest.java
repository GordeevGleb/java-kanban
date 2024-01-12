import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
    File file;
    FileBackedTasksManager fileBackedTasksManager;
    Epic epic;
    Task task;
    @BeforeEach
    public void create() {
        file = new File("test/storageTest/taskStorageTest.csv");
        fileBackedTasksManager = new FileBackedTasksManager(file);
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
        List<Task> savedHistoryList = fileBackedTasksManager.getHistoryManager().getHistory();
        assertEquals(1, savedHistoryList.size());
    }

}
