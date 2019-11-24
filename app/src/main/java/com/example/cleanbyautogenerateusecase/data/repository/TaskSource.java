package com.example.cleanbyautogenerateusecase.data.repository;

import com.example.cleanbyautogenerateusecase.data.entity.Task;

public interface TaskSource {
    Task get(int id);

    boolean save(Task task);

    boolean delete(Task task);
}
