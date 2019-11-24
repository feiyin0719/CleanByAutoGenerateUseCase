package com.example.cleanbyautogenerateusecase.domain;
import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.data.repository.TaskRepository;
import com.iffly.clean.usecase.annotations.RequestParams;
import com.iffly.clean.usecase.annotations.ResponseParams;
import com.iffly.clean.usecase.annotations.UseCase;
import com.iffly.clean.usecase.annotations.UseCaseRepository;
import com.iffly.clean.usecase.annotations.UseCases;

@UseCases
public interface TaskUseCases {
    @UseCase
    void get(@UseCaseRepository TaskRepository taskRepository, @RequestParams int id, @ResponseParams Task task);
    @UseCase
    void save(@UseCaseRepository TaskRepository taskRepository, @RequestParams Task task, @ResponseParams Boolean success);
    @UseCase
    void delete(@UseCaseRepository TaskRepository taskRepository, @RequestParams Task task, @ResponseParams Boolean success);
}
