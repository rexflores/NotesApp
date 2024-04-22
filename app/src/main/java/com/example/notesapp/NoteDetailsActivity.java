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

        // Initialize UI components and DBHelper
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        dbHelper = new DBHelper(this);

        // Retrieve note ID from intent
        Intent intent = getIntent();
        noteId = intent.getStringExtra("NOTE_ID");

        // If note ID is provided, load note data for editing
        if (noteId != null) {
            loadNoteData();
        }

        // Set click listener for save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        // Set click listener for delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    // Load note data for editing
    private void loadNoteData() {
        Cursor cursor = dbHelper.getNoteById(noteId);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int contentIndex = cursor.getColumnIndex("content");

            // Check if title and content columns exist
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

    // Save or update the note
    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        // Check if title and content are not empty
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert or update note based on whether note ID is provided
        if (noteId == null) {
            dbHelper.insertNoteData(title, content);
            Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateNoteData(noteId, title, content);
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    // Delete the note
    private void deleteNote() {
        dbHelper.deleteNoteData(noteId);
        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
