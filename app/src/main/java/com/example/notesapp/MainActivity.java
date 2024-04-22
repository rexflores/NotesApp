package com.example.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener {

    private RecyclerView recyclerViewNotes;
    private NoteAdapter adapter;
    private DBHelper dbHelper;
    private Button btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DBHelper instance
        dbHelper = new DBHelper(this);

        // Initialize RecyclerView and set layout manager
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with data from DBHelper and set it to RecyclerView
        adapter = new NoteAdapter(dbHelper.getAllNotes(), this);
        recyclerViewNotes.setAdapter(adapter);

        // Initialize "Add Note" button and set click listener to navigate to NoteDetailsActivity
        btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh RecyclerView when activity resumes to reflect any changes
        adapter.swapCursor(dbHelper.getAllNotes());
    }

    // Handle item click events from NoteAdapter
    @Override
    public void onItemClick(String noteId) {
        // Launch NoteDetailsActivity with the selected note ID
        Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
        intent.putExtra("NOTE_ID", noteId);
        startActivity(intent);
    }
}
