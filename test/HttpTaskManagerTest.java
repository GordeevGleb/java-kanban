import com.google.gson.Gson;
import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import server.KVTaskClient;
import service.FileBackedTasksManager;
import service.HttpTaskManager;
import service.Managers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTest {
    HttpTaskManager httpTaskManager;
    FileBackedTasksManager fileBackedTasksManager;
    KVServer kvServer;
    KVTaskClient kvTaskClient;
    Managers managers;
    URL url;
    File file;
    Gson gson;
Task task;
Epic epic;
@BeforeEach
    public void create() throws IOException {
    kvServer = new KVServer();
    kvServer.start();
    managers = new Managers();
    file = new File("test/storageTest/taskStorageTest.csv");
    fileBackedTasksManager = new FileBackedTasksManager(managers.getDefaultHistoryManager(), file);
    url = new URL("http://localhost:8078");
    kvTaskClient = new KVTaskClient(url);
    gson = new Gson();
httpTaskManager = new HttpTaskManager(managers.getDefaultHistoryManager(), file, url);
task = new Task("test Task", "test Task desc.", Status.DONE);
epic = new Epic("test Epic", "test Epic desc.", Status.NEW);
}
@AfterEach
    public void close() {
    kvServer.stop();
}
@Test
    public void shouldCorrectlyReadFromServer() {
    httpTaskManager.createTask(task);
Task savedTask = httpTaskManager.getTaskById(task.getId());
assertEquals(task, savedTask);
}
}
