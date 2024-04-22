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

        dbHelper = new DBHelper(this);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(dbHelper.getAllNotes(), this);
        recyclerViewNotes.setAdapter(adapter);

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
        adapter.swapCursor(dbHelper.getAllNotes());
    }

    @Override
    public void onItemClick(String noteId) {
        Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
        intent.putExtra("NOTE_ID", noteId);
        startActivity(intent);
    }
}
