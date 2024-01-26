import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private Managers managers;
    private File file;
    private FileBackedTasksManager fileBackedTasksManager;
    private Gson gson;
    private Task task;
    @BeforeEach
    public void start() throws IOException {
        managers = new Managers();
        file = new File("test/storageTest/taskStorageTest.csv");
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(managers.getDefaultHistoryManager(), file);
        gson = new Gson();
        task = new Task("New Task", "New Task Desc.", Status.DONE);
       // fileBackedTasksManager.createTask(task);
        httpTaskServer = new HttpTaskServer(fileBackedTasksManager);
        httpTaskServer.start();
    }
    @AfterEach
    public void stop() {
        httpTaskServer.stop();
    }
    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
Task task1 = fileBackedTasksManager.getTaskById(1);
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(httpResponse.body(), taskType);
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals(task1, actual.get(0));
    }
    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Epic> actual = gson.fromJson(httpResponse.body(), taskType);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }
    @Test
    public void shouldGetSubTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<SubTask> actual = gson.fromJson(httpResponse.body(), taskType);
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }
    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());

        Task actual = gson.fromJson(httpResponse.body(), Task.class);
        System.out.println(actual);

    }
    @Test
    public void shouldGetTimeSortedTaskList() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());
    }
@Test
    public void shouldGetHistory() throws IOException, InterruptedException {
    HttpClient httpClient = HttpClient.newHttpClient();
    URI uri = URI.create("http://localhost:8080/tasks/history/");
    HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, httpResponse.statusCode());
}
@Test
    public void shouldAddNewTask() throws IOException, InterruptedException {
    HttpClient httpClient = HttpClient.newHttpClient();
    URI uri = URI.create("http://localhost:8080/tasks/task/");
    String actual = gson.toJson(task);
    HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(actual)).build();
    HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, httpResponse.statusCode());
}
}
