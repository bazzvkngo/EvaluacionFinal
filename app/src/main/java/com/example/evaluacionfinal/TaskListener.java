package com.example.evaluacionfinal;

public interface TaskListener {
    void onTaskAdded(Task task);

    void onTaskUpdated(Task updatedTask, int position);
}
