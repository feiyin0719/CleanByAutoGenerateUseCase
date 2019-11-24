package com.example.cleanbyautogenerateusecase.presentation.view;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanbyautogenerateusecase.R;
import com.example.cleanbyautogenerateusecase.data.entity.Task;
import com.example.cleanbyautogenerateusecase.presentation.contract.TaskContract;
import com.example.cleanbyautogenerateusecase.presentation.presenter.TaskPresenter;

public class MainActivity extends AppCompatActivity implements TaskContract.View {
    private TextView taskName;
    private TaskContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskName = findViewById(R.id.taskName);
        presenter = new TaskPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.takeView(this);
            presenter.getTask(1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null)
            presenter.dropView();
    }

    @Override
    public void showTask(Task task) {
        taskName.setText(task.getName());
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
