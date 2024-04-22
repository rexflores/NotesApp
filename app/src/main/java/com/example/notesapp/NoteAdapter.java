package com.example.notesapp;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Cursor cursor; // Cursor to hold data from SQLite database
    public OnItemClickListener listener; // Listener for item click events

    // Constructor to initialize the adapter with cursor and listener
    public NoteAdapter(Cursor cursor, OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    // Create view holder for RecyclerView
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item in RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(String noteId);
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        // Get column indices for title, content, and ID
        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");
        int idIndex = cursor.getColumnIndex("id");

        if (titleIndex == -1 || contentIndex == -1 || idIndex == -1) {
            // Handle the case where column "title", "content", or "id" doesn't exist
            return;
        }

        // Retrieve data from cursor
        final String noteId = cursor.getString(idIndex);
        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);

        // Bind data to ViewHolder
        holder.textTitle.setText(title);
        holder.textContent.setText(content);

        // Set click listener for each item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(noteId);
                }
            }
        });
    }

    // Get item count
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // ViewHolder class to hold views for each item in RecyclerView
    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;
        public TextView textContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
        }
    }

    // Swap cursor with new cursor
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }

        cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
