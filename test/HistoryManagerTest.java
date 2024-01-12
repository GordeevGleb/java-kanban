import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    HistoryManager historyManager;
    Task task;
    Epic epic;
    SubTask subTask;
    @BeforeEach
    public void create() {
        historyManager = new InMemoryHistoryManager();
        epic = new Epic("Test Epic Name", "Test Epic Description", Status.NEW);
        task = new Task("Test Task Name", "Test Task Description", Status.DONE);

    }
@Test
    public void add() {
historyManager.add(epic);
List<Task> savedHistoryList = historyManager.getHistory();
assertNotNull(savedHistoryList);
assertEquals(1, savedHistoryList.size());
    }

@Test
    public void remove() {
historyManager.add(epic);
historyManager.remove(epic.getId());
List<Task> savedHistoryList = historyManager.getHistory();
assertEquals(0, savedHistoryList.size());
    }
}
