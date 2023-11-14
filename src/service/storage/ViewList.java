package service.storage;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class ViewList {
    private static List<Task> viewList = new ArrayList<>();

    public static List<Task> getViewList() {
        return viewList;
    }
}
