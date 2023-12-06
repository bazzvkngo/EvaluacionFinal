package com.example.evaluacionfinal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonAdd, buttonDate;
    private Task existingTask;
    private int taskPosition;

    private TaskListener taskEditListener;

    public static AddTaskDialogFragment newInstance(Task task, int position) {
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("task", task);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            existingTask = (Task) getArguments().getSerializable("task");
            taskPosition = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task_dialog, container, false);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextDueDate = view.findViewById(R.id.editTextDueDate);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonDate = view.findViewById(R.id.buttonDate);

        buttonDate.setOnClickListener(v -> showDatePickerDialog());

        if (existingTask != null) {
            editTextTitle.setText(existingTask.getTitle());
            editTextDescription.setText(existingTask.getDescription());
            editTextDueDate.setText(existingTask.getDueDate());
            buttonAdd.setText("Actualizar tarea");
        }

        buttonAdd.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String dueDate = editTextDueDate.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.DAY_OF_YEAR, -1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date selectedDate = dateFormat.parse(dueDate);
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTime(selectedDate);

                if (selectedCalendar.compareTo(currentCalendar) >= 0) {
                    if (existingTask != null) {
                        existingTask.setTitle(title);
                        existingTask.setDescription(description);
                        existingTask.setDueDate(dueDate);
                        if (taskEditListener != null) {
                            taskEditListener.onTaskUpdated(existingTask, taskPosition);
                            Toast.makeText(getActivity(), "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Task newTask = new Task(title, description, dueDate, false);

                        TaskListener listener = (TaskListener) getActivity();
                        listener.onTaskAdded(newTask);
                        Toast.makeText(getActivity(), "Tarea creada con éxito", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Seleccione una fecha válida", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    public void setTaskEditListener(TaskListener listener) {
        this.taskEditListener = listener;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            editTextDueDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}
