package com.example.evaluacionfinal;

import android.util.Log;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE id IN (:taskIds)")
    List<Task> loadAllByIds(int[] taskIds);

    @Query("SELECT * FROM tasks WHERE title LIKE :title LIMIT 1")
    Task findByTitle(String title);

    @Insert
    void insert(Task task);

    @Update
    default void updateTask(Task task) {

        Log.d("TaskDao", "Updating task with ID: " + task.getId());
        updateTaskById(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(), task.isCompleted());
    }

    @Query("UPDATE tasks SET title = :title, description = :description, dueDate = :dueDate, completed = :completed WHERE id = :taskId")
    void updateTaskById(int taskId, String title, String description, String dueDate, boolean completed);

    @Delete
    void delete(Task task);
}
