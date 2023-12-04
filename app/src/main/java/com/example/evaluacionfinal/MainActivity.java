package com.example.evaluacionfinal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private FloatingActionButton fabAddTask;
    private final ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> showAddTaskDialog());

        adapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(adapter);
    }

    private void showAddTaskDialog() {
        AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddTaskDialogFragment");
    }

    @Override
    public void onTaskAdded(Task task) {
        taskList.add(task);
        adapter.notifyDataSetChanged();
    }
}
