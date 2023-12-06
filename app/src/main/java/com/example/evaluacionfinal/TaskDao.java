package com.example.evaluacionfinal;

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
    void updateTask(Task task);

    @Delete
    void delete(Task task);
}