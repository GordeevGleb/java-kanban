package service;


import model.Epic;
import model.SubTask;
import model.Task;
import service.util.*;
import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
      Task task = historyList.getTask(id);
      if (task.getClass().equals(Epic.class)) {
          for (Task task1 : historyList.getTasks()) {
              if (task1.getClass().equals(SubTask.class) && (((SubTask) task1).getMasterId() == task.getId())) {
                  historyList.removeNode(task1);
              }
          }
      }
      historyList.removeNode(task);
    }
    @Override
    public void remove(String s) {
        switch (s) {
            case "Task" :
                historyList.removeAllTasks();
                break;
            case "Epic" :
                historyList.removeAllEpic();
            case "SubTask" :
                historyList.removeAllSubTasks();
                break;
        }
    }


    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

}
