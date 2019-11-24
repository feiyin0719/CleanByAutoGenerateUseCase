package com.example.cleanbyautogenerateusecase.data.repository.local;

import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.data.repository.TaskSource;

import java.util.HashMap;
import java.util.Map;

public class LocalTaskSource implements TaskSource {

    private static  Map<Integer,Task> cache = new HashMap<>();
    static {
        cache.put(1,new Task(1,"task1"));
        cache.put(2,new Task(2,"task2"));
    }

    @Override
    public Task get(int id) {
        return cache.get(id);
    }

    @Override
    public boolean save(Task task) {
        cache.put(task.getId(),task);
        return true;
    }

    @Override
    public boolean delete(Task task) {
        cache.remove(task.getId());
        return true;
    }
}
