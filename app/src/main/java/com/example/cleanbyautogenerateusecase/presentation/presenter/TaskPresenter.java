package com.example.cleanbyautogenerateusecase.presentation.presenter;

import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.data.repository.TaskRepository;
import com.example.cleanbyautogenerateusecase.domain.TaskUseCasesImpl.GetTaskUseCase;
import com.example.cleanbyautogenerateusecase.presentation.contract.TaskContract;
import com.iffly.clean.usecase.UseCase;
import com.iffly.clean.usecase.executor.AppExecutors;

public class TaskPresenter implements TaskContract.Presenter {

    private TaskContract.View view;
    private GetTaskUseCase getTaskUseCase;

    public TaskPresenter() {
        this.getTaskUseCase = new GetTaskUseCase(AppExecutors.getINSTANCE(), TaskRepository.getInstance());
    }

    @Override
    public void getTask(int id) {
        if (getTaskUseCase != null) {
            getTaskUseCase.execute(new UseCase.CallBack<Task>() {
                @Override
                public void onSucess(Task result) {
                    view.showTask(result);
                }

                @Override
                public void onError() {
                    view.showError("get failed");

                }
            }, id);
        } else {
            view.showError("use case is null");
        }
    }

    @Override
    public void takeView(TaskContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }
}
