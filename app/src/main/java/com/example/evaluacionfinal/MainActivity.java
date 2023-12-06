package com.example.evaluacionfinal;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.room.Room;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private FloatingActionButton fabAddTask, fabInstructions;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> showAddTaskDialog());

        fabInstructions = findViewById(R.id.fabInstructions);
        fabInstructions.setOnClickListener(view -> showInstructionsDialog());

        adapter = new TaskAdapter(db);
        recyclerView.setAdapter(adapter);
        loadTasks();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadTasks() {
        new AsyncTask<Void, Void, List<Task>>() {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                return db.taskDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        }.execute();
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onTaskAdded(Task task) {
        new AsyncTask<Task, Void, Void>() {
            @Override
            protected Void doInBackground(Task... tasks) {
                db.taskDao().insert(tasks[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadTasks();
            }
        }.execute(task);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onTaskUpdated(Task updatedTask, int position) {
        new AsyncTask<Task, Void, Void>() {
            @Override
            protected Void doInBackground(Task... tasks) {
                Log.d("MainActivity", "Attempting to update task in DB with ID: " + tasks[0].getId());
                db.taskDao().updateTask(tasks[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("MainActivity", "Task updated in DB, reloading tasks");
                loadTasks();
            }
        }.execute(updatedTask);
    }
}
