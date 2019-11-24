package com.example.cleanbyautogenerateusecase.presentation.contract;

import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.presentation.presenter.BasePresenter;
import com.example.cleanbyautogenerateusecase.presentation.view.BaseView;

public interface TaskContract {
    public interface Presenter extends BasePresenter<TaskContract.View> {
        void getTask(int id);
    }

    public interface View extends BaseView<TaskContract.Presenter> {
        void showTask(Task task);
        void showError(String message);
    }
}
