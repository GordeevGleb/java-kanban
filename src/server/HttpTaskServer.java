package server;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import service.FileBackedTasksManager;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer httpServer;
    private Gson gson = new Gson();
    private FileBackedTasksManager fileBackedTasksManager;



    public HttpTaskServer(FileBackedTasksManager fileBackedTasksManager) throws IOException {
        this.fileBackedTasksManager = fileBackedTasksManager;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTask);
    }

    private void handleTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath().trim();
            String requestMethod = httpExchange.getRequestMethod();
            Optional<String> optQuery = Optional.ofNullable(httpExchange.getRequestURI().getQuery());
            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (optQuery.isEmpty()) {
                            List<Task> taskList = new ArrayList<>(fileBackedTasksManager.getAllTasks().values());
                            String response = gson.toJson(taskList);
                            sendText(httpExchange, response);
                        } else {
                            String query = optQuery.get().replaceAll("id=", "");
                            int id = parseId(query);
                            if (id != -1) {
                                Task task = fileBackedTasksManager.getTaskById(id);
                                String responce = gson.toJson(task);
                                sendText(httpExchange, responce);
                            }
                        }
                        break;
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        List<Epic> epicList = new ArrayList<>(fileBackedTasksManager.getAllEpic().values());
                        String response = gson.toJson(epicList);
                        sendText(httpExchange, response);
                        break;
                    } else if (Pattern.matches("^/tasks/subtask/$", path)) {
                        List<SubTask> subTaskList = new ArrayList<>(fileBackedTasksManager.getAllSubTasks().values());
                        String response = gson.toJson(subTaskList);
                        sendText(httpExchange, response);
                        break;
                    } else if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                        String s = optQuery.get().replaceAll("id=", "");
                        int id = parseId(s);
                        if (id != -1) {
                            Epic epic = (Epic) fileBackedTasksManager.getTaskById(id);
                            List<SubTask> subTaskList = new ArrayList<>(epic.getEpicSteps().values());
                            String response = gson.toJson(subTaskList);
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получили некорректный id :" + id);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    } else if (Pattern.matches("^/tasks$", path)) {
                        List<Task> timeSortedTasks = new ArrayList<>(fileBackedTasksManager.getPrioritizedTasks());
                        String response = gson.toJson(timeSortedTasks);
                        sendText(httpExchange, response);
                        break;
                    } else if (Pattern.matches("^/tasks/history/", path)) {
                        List<Task> history = new ArrayList<>();
                        for (Integer i : fileBackedTasksManager.getHistoryFromFile()) {
                            history.add(fileBackedTasksManager.getTaskById(i));
                        }
                        String response = gson.toJson(history);
                        sendText(httpExchange, response);
                        break;
                    }
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/tasks/$", path)) {
                        fileBackedTasksManager.deleteAllTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    } else if (Pattern.matches("^/tasks/task/epic/$", path)) {
                        fileBackedTasksManager.deleteAllEpics();
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    } else if (Pattern.matches("^/tasks/task/subtasks/$", path)) {
                        fileBackedTasksManager.deleteAllSubTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    } else if (Pattern.matches("^/tasks/task/", path)) {
                        String query = optQuery.get().replaceAll("id=", "");
                        int id = parseId(query);
                        if (id != -1) {
                            fileBackedTasksManager.deleteTaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Получили некорректный id :" + id);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                case "POST":
                    if (Pattern.matches("^/tasks/task/", path)) {
                        String actual = readText(httpExchange);
                        Task task = gson.fromJson(actual, Task.class);
                        fileBackedTasksManager.createTask(task);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parseId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        Managers managers = new Managers();
        FileBackedTasksManager fileBackedTasksManager1 =
                FileBackedTasksManager.loadFromFile(managers.getDefaultHistoryManager(),
                        new File("src/service/storage/taskStorage.csv"));
        HttpTaskServer httpTaskServer = new HttpTaskServer(fileBackedTasksManager1);
        httpTaskServer.start();
        httpTaskServer.stop();
    }
}
