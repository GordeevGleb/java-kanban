import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaskManager;

import java.util.HashMap;

abstract class TaskManagerTest<T extends TaskManager> {
@Test
    abstract void createTask();

    @Test
     abstract void createEpic();

    @Test
    abstract void createSubTask();
    @Test
    abstract void getSubTasksByEpic();


}
