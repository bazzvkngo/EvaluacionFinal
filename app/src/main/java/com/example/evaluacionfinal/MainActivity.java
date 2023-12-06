package com.example.evaluacionfinal;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private FloatingActionButton fabAddTask, fabInstructions;
    private final ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> showAddTaskDialog());

        fabInstructions = findViewById(R.id.fabInstructions);
        fabInstructions.setOnClickListener(view -> showInstructionsDialog());

        adapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(adapter);
    }

    private void showAddTaskDialog() {
        AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddTaskDialogFragment");
    }

    private void showInstructionsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Guía Rápida: Gestión de Tareas")
                .setMessage("Añadir Tarea: Toque el botón (+) para crear una nueva tarea.\n\n" +
                        "Completar Campos: Rellene todos los campos requeridos para definir la tarea.\n\n" +
                        "Fecha de Tarea: Elija una fecha actual o futura como plazo.\n\n" +
                        "Editar Tarea: Toque una tarea existente para modificar sus detalles.\n\n" +
                        "Eliminar Tarea: Mantenga presionada una tarea y confirme para eliminarla.\n\n" +
                        "Marcar como Completada: Seleccione la casilla de verificación una vez finalizada la tarea.")
                .setPositiveButton("Aceptar", null)
                .show();
    }

    @Override
    public void onTaskAdded(Task task) {
        taskList.add(task);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskUpdated(Task updatedTask, int position) {
        taskList.set(position, updatedTask);
        adapter.notifyItemChanged(position);
    }
}
