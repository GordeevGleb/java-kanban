package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, SubTask> epicSteps = new HashMap<>();

    public HashMap<Integer, SubTask> getEpicSteps() {
        return epicSteps;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicStepsSize=" + epicSteps.size() +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(getEpicSteps(), epic.getEpicSteps());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEpicSteps());
    }

    public Epic(String name) {
        super(name);
    }

    public HashMap<Integer, SubTask> addStep(SubTask subTask) {
        epicSteps.put(subTask.getId(), subTask);
        return epicSteps;
    }

    public HashMap<Integer, SubTask> removeAllSteps() {
        epicSteps.clear();
        return epicSteps;
    }

    public HashMap<Integer, SubTask> removeStepById(int id) {
        HashMap<Integer, SubTask> resultMap = new HashMap<>();
        for (Integer key : epicSteps.keySet()) {
            if (!(key == id))
                resultMap.put(key, epicSteps.get(key));
        }
        epicSteps = resultMap;
        return epicSteps;
    }

    public Epic checkStatus() {
        ArrayList<Status> statusList = new ArrayList<>();
        for (Integer key : epicSteps.keySet()) {
            statusList.add(epicSteps.get(key).getStatus());
        }
        if (statusList.contains(Status.DONE) || (statusList.contains(Status.IN_PROGRESS)))
            this.setStatus(Status.IN_PROGRESS);
        if (!statusList.contains(Status.NEW) && !(statusList.contains(Status.IN_PROGRESS)))
            this.setStatus(Status.DONE);
        if (statusList.size() == 0)
            this.setStatus(Status.NEW);
        return this;
    }
}
