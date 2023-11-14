package model;

import java.util.HashMap;

public interface Savable<T extends Task> {
    void save(T t);
    HashMap<Integer, T> get();
}
