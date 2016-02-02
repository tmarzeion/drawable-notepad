package com.example.tomek.notepad;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoteAdapter extends ArrayAdapter<Note> {

    Context context;
    int layoutResourceId;
    Note data[] = null;

    public NoteAdapter(Context context, int layoutResourceId, Note[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NoteHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new NoteHolder();
            holder.noteTitle = (TextView)row.findViewById(R.id.noteTitle);
            holder.noteContent = (TextView)row.findViewById(R.id.noteContent);

            row.setTag(holder);
        }
        else
        {
            holder = (NoteHolder)row.getTag();
        }

        Note note = data[position];
        holder.noteTitle.setText("Note No: " + note.getId());
        holder.noteContent.setText(note.getRawText());

        return row;
    }

    static class NoteHolder
    {
        TextView noteTitle;
        TextView noteContent;
    }
}