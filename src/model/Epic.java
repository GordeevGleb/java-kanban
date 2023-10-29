package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, SubTask> epicSteps = new HashMap<>();
    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public HashMap<Integer, SubTask> getEpicSteps() {
        return epicSteps;
    }
    public HashMap<Integer, SubTask> addSubtask(SubTask subTask) {
        epicSteps.put(subTask.getId(), subTask);
        checkStatus();
        return epicSteps;
    }

    public HashMap<Integer, SubTask> removeAllSteps() {
        epicSteps.clear();
        checkStatus();
        return epicSteps;
    }

    public HashMap<Integer, SubTask> removeStepById(int id) {
        epicSteps.remove(id);
        checkStatus();
        return epicSteps;
    }


    private void checkStatus() {
        ArrayList<Status> statusList = new ArrayList<>();
        for (Integer key : epicSteps.keySet()) {
            statusList.add(epicSteps.get(key).getStatus());
        }
        if (statusList.size() > 0) {
            if (!(statusList.contains(Status.NEW)) && !(statusList.contains(Status.IN_PROGRESS))
                    && (statusList.size() > 0))
                this.setStatus(Status.DONE);
            else if (statusList.contains(Status.IN_PROGRESS) || (statusList.contains(Status.DONE)))
                this.setStatus(Status.IN_PROGRESS);
        }
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
}
