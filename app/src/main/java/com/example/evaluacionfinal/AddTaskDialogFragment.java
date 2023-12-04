package com.example.evaluacionfinal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonAdd, buttonDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task_dialog, container, false);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextDueDate = view.findViewById(R.id.editTextDueDate);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonDate = view.findViewById(R.id.buttonDate);

        buttonDate.setOnClickListener(v -> showDatePickerDialog());

        buttonAdd.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String dueDate = editTextDueDate.getText().toString();
            Task newTask = new Task(title, description, dueDate);
            TaskListener listener = (TaskListener) getActivity();
            listener.onTaskAdded(newTask);
            dismiss();
        });

        return view;
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
