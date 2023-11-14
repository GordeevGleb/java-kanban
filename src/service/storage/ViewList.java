package service.storage;


import model.Task;
import service.HistoryManager;

import java.util.ArrayList;

import java.util.List;

public class ViewList {
    private  List<Task> viewList = new ArrayList<>();


    public List<Task> getViewList() {
        return viewList;
    }

    public void setViewList(List<Task> viewList) {
        this.viewList = viewList;
    }
    public void add(Task task) {
        viewList.add(task);
    }
}
