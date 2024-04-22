package com.example.notesapp;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Cursor cursor;
    public OnItemClickListener listener;

    public NoteAdapter(Cursor cursor, OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(String noteId);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");
        int idIndex = cursor.getColumnIndex("id"); // Get the index of the "id" column

        if (titleIndex == -1 || contentIndex == -1 || idIndex == -1) {
            // Handle the case where column "title", "content", or "id" doesn't exist
            return;
        }

        final String noteId = cursor.getString(idIndex); // Retrieve the note ID
        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);

        holder.textTitle.setText(title);
        holder.textContent.setText(content);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(noteId);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;
        public TextView textContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
        }
    }

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

