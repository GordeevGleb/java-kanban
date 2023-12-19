package model;

import java.util.Objects;

public class SubTask extends Task{
    private int masterId;
    public SubTask(String name, String description, Status status, int masterId) {
        super(name, description, status);
        this.masterId = masterId;
    }

    public int getMasterId() {
        return masterId;
    }

    @Override
    public String toString() {
        return "SubTask{} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return getMasterId() == subTask.getMasterId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMasterId());
    }
}
