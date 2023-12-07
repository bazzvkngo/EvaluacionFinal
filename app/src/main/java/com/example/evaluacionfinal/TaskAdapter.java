package com.example.evaluacionfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private AppDatabase db;

    public TaskAdapter(AppDatabase db) {
        this.tasks = new ArrayList<>();
        this.db = db;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTitleTextView.setText(task.getTitle());
        holder.taskDescriptionTextView.setText(task.getDescription());
        holder.taskDueDateTextView.setText(task.getDueDate());
        holder.taskCompletedCheckBox.setChecked(task.isCompleted());

        if (task.isCompleted()) {
            holder.itemView.setBackgroundResource(R.drawable.green_bubble_background);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bubble_background);
        }

        holder.taskCompletedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            updateTaskInDatabase(task);

            if (isChecked) {
                holder.itemView.setBackgroundResource(R.drawable.green_bubble_background);
                Toast.makeText(buttonView.getContext(), "¡Tarea completada!", Toast.LENGTH_SHORT).show();
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bubble_background);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            task.setCompleted(false);
            updateTaskInDatabase(task);

            AddTaskDialogFragment dialogFragment = AddTaskDialogFragment.newInstance(task, position);
            dialogFragment.setTaskEditListener(new TaskListener() {
                @Override
                public void onTaskAdded(Task task) {
                }

                @Override
                public void onTaskUpdated(Task updatedTask, int taskPosition) {
                    tasks.set(taskPosition, updatedTask);
                    notifyItemChanged(taskPosition);
                    updateTaskInDatabase(updatedTask);
                }
            });
            dialogFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        Task taskToRemove = tasks.get(position);
                        removeTaskFromDatabase(taskToRemove);
                        tasks.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "¡Tarea eliminada!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void updateTaskInDatabase(Task task) {
        new AsyncTask<Task, Void, Void>() {
            @Override
            protected Void doInBackground(Task... tasks) {
                db.taskDao().updateTask(tasks[0]);
                return null;
            }
        }.execute(task);
    }

    @SuppressLint("StaticFieldLeak")
    private void removeTaskFromDatabase(Task task) {
        new AsyncTask<Task, Void, Void>() {
            @Override
            protected Void doInBackground(Task... tasks) {
                db.taskDao().delete(tasks[0]);
                return null;
            }
        }.execute(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitleTextView, taskDescriptionTextView, taskDueDateTextView;
        CheckBox taskCompletedCheckBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTextView = itemView.findViewById(R.id.taskTitleTextView);
            taskDescriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            taskDueDateTextView = itemView.findViewById(R.id.taskDueDateTextView);
            taskCompletedCheckBox = itemView.findViewById(R.id.taskCompletedCheckBox);
        }
    }
}
