import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    TaskManager taskManager;
    Epic epic;
    @BeforeEach
    public void create() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Test Epic", "Test Epic Description", Status.IN_PROGRESS);
        taskManager.createEpic(epic);
    }
    @Test
    public void shouldSetCurrentStatusWithousSubtasks() {
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }
    @Test
    public void shouldSetNewWithAllNewSubtasks() {
        SubTask subtask1 = new SubTask(
                "Test Subtask1", "Test Subtask description", Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask(
                "Test Subtask2", "Test Subtask description", Status.NEW, epic.getId());
        SubTask subTask3 = new SubTask(
                "Test Subtask3", "Test Subtask description", Status.NEW, epic.getId());
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status);
    }
    @Test
    public void shouldSetDoneWithAllDoneSubtasks() {
        SubTask subtask1 = new SubTask(
                "Test Subtask1", "Test Subtask description", Status.DONE, epic.getId());
        SubTask subTask2 = new SubTask(
                "Test Subtask2", "Test Subtask description", Status.DONE, epic.getId());
        SubTask subTask3 = new SubTask(
                "Test Subtask3", "Test Subtask description", Status.DONE, epic.getId());
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status);
    }
    @Test
    public void shouldSetInProgressWithNewAndDoneSubtasks() {
        SubTask subtask1 = new SubTask(
                "Test Subtask1", "Test Subtask description", Status.DONE, epic.getId());
        SubTask subTask2 = new SubTask(
                "Test Subtask2", "Test Subtask description", Status.NEW, epic.getId());
        SubTask subTask3 = new SubTask(
                "Test Subtask3", "Test Subtask description", Status.DONE, epic.getId());
        SubTask subtask4 = new SubTask(
                "Test Subtask4", "Test Subtask description", Status.NEW, epic.getId());
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subtask4);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }
    @Test
    public void shouldSetInProgressWithAllSubtaskStatuses() {
        SubTask subtask1 = new SubTask(
                "Test Subtask1", "Test Subtask description", Status.IN_PROGRESS, epic.getId());
        SubTask subTask2 = new SubTask(
                "Test Subtask2", "Test Subtask description", Status.NEW, epic.getId());
        SubTask subTask3 = new SubTask(
                "Test Subtask3", "Test Subtask description", Status.DONE, epic.getId());
        SubTask subtask4 = new SubTask(
                "Test Subtask4", "Test Subtask description", Status.NEW, epic.getId());
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subtask4);
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }
}
