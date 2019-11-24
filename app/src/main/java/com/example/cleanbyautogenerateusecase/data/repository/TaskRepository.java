package com.example.cleanbyautogenerateusecase.data.repository;

import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.data.repository.local.LocalTaskSource;

public class TaskRepository implements TaskSource {
    private static final TaskRepository ourInstance = new TaskRepository();

    public static TaskRepository getInstance() {
        return ourInstance;
    }
    private TaskSource localTask;
    private TaskRepository() {
        localTask = new LocalTaskSource();
    }

    @Override
    public Task get(int id) {
        return localTask.get(id);
    }

    @Override
    public boolean save(Task task) {
        return localTask.save(task);
    }

    @Override
    public boolean delete(Task task) {
        return localTask.delete(task);
    }
}
