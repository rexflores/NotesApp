package com.example.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailsActivity extends AppCompatActivity {

    public EditText editTextTitle;
    public EditText editTextContent;
    public Button btnSave;
    public Button btnDelete;
    public DBHelper dbHelper;
    public String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        noteId = intent.getStringExtra("NOTE_ID");

        if (noteId != null) {
            loadNoteData();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    private void loadNoteData() {
        Cursor cursor = dbHelper.getNoteById(noteId);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int contentIndex = cursor.getColumnIndex("content");

            if (titleIndex != -1 && contentIndex != -1) {
                String title = cursor.getString(titleIndex);
                String content = cursor.getString(contentIndex);
                editTextTitle.setText(title);
                editTextContent.setText(content);
            } else {
                // Handle the case where column "title" or "content" doesn't exist
                Toast.makeText(this, "Note data not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the cursor is null or empty
            Toast.makeText(this, "Note data not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == null) {
            dbHelper.insertNoteData(title, content);
            Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateNoteData(noteId, title, content);
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteNote() {
        dbHelper.deleteNoteData(noteId);
        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}

