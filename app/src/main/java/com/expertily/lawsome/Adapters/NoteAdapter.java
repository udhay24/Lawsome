package com.expertily.lawsome.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expertily.lawsome.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> ids;
    private ArrayList<String> texts;
    private ArrayList<String> timestamps;

    public NoteAdapter(Context act, ArrayList<String> ids, ArrayList<String> texts, ArrayList<String> timestamps) {
        this.context = act;
        this.ids = ids;
        this.texts = texts;
        this.timestamps = timestamps;
    }


    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteAdapter.ViewHolder viewHolder, int i) {
        viewHolder.noteid.setText(ids.get(i));
        viewHolder.notetext.setText(texts.get(i));
        viewHolder.notedate.setText(timestamps.get(i));
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteid;
        private TextView notetext;
        private TextView notedate;

        public ViewHolder(View view) {
            super(view);
            noteid = view.findViewById(R.id.noteid);
            notetext = view.findViewById(R.id.notetext);
            notedate = view.findViewById(R.id.notedate);
        }
    }

}