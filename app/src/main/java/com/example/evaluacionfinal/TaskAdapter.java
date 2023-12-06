package com.example.evaluacionfinal;

import android.app.AlertDialog;
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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final ArrayList<Task> tasks;

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
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

        holder.taskCompletedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            if (isChecked) {
                holder.itemView.setBackgroundResource(R.drawable.green_bubble_background);
                Toast.makeText(buttonView.getContext(), "Tarea Completada", Toast.LENGTH_SHORT).show();
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bubble_background);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            AddTaskDialogFragment dialogFragment = AddTaskDialogFragment.newInstance(tasks.get(position), position);
            dialogFragment.setTaskEditListener(new TaskListener() {
                @Override
                public void onTaskAdded(Task task) {
                }

                @Override
                public void onTaskUpdated(Task updatedTask, int taskPosition) {
                    tasks.set(taskPosition, updatedTask);
                    notifyItemChanged(taskPosition);
                }
            });
            dialogFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "EditTaskDialogFragment");
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        tasks.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
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
