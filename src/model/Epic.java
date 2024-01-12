package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {
    private HashMap<Integer, SubTask> epicSteps = new HashMap<>();

    private LocalTime endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description, Status status, LocalTime startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    public HashMap<Integer, SubTask> getEpicSteps() {
        return epicSteps;
    }
    public void addSubtask(SubTask subTask) {
        epicSteps.put(subTask.getId(), subTask);
        checkStatus();
    }

    public void removeAllSteps() {
        epicSteps.clear();
        checkStatus();
    }

    public void removeStepById(int id) {
         epicSteps.remove(id);
        checkStatus();
    }
public void setStartTime() {
        startTime = epicSteps.values().stream()
                .filter(subTask -> Optional.ofNullable(subTask.getStartTime()).isPresent())
                .map(Task::getStartTime)
                .min(LocalTime::compareTo)
                .orElse(null);
}
    @Override
    public  LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }
    public void setDuration() {
        duration = epicSteps.values().stream()
                .mapToInt(subtask -> subtask.getDuration())
                .sum();
    }
public void setEndTime() {
    endTime = epicSteps.values().stream()
            .filter(subTask -> Optional.ofNullable(subTask.getEndTime()).isPresent())
            .map(SubTask::getEndTime)
            .max(LocalTime::compareTo)
            .orElse(null);
}
    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    public void checkStatus() {
        ArrayList<Status> statusList = new ArrayList<>();
        for (Integer key : epicSteps.keySet()) {
            statusList.add(epicSteps.get(key).getStatus());
        }
        if (!(statusList.contains(Status.NEW)) && !(statusList.contains(Status.IN_PROGRESS))
                && (statusList.size() > 0))
            this.setStatus(Status.DONE);
        else if (statusList.contains(Status.IN_PROGRESS) || (statusList.contains(Status.DONE)))
            this.setStatus(Status.IN_PROGRESS);
        else
            this.setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "Epic{} " + super.toString();
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
