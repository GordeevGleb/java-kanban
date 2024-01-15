package model;

import service.InMemoryTaskManager;
import service.util.ManagerSaveException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private LocalTime startTime;
    private long duration;



    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, LocalTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        if (duration < 0) {
            throw new ManagerSaveException("Продолжительность выполнения задания должна быть больше нуля");
        }
        this.duration = duration;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
