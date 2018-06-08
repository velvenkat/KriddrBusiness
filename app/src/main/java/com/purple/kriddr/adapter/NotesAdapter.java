package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.purple.kriddr.R;
import com.purple.kriddr.model.DocumentModel;
import com.purple.kriddr.model.NotesModel;
import com.purple.kriddr.model.PetModel;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    ArrayList<NotesModel> notesList = new ArrayList<>();
    Context context;


    public NotesAdapter(ArrayList<NotesModel> notesList,Context context)
    {
        this.notesList = notesList;
        this.context = context;


    }


    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_adapter,parent,false);
        return new NotesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.MyViewHolder holder, int position) {

        final NotesModel nature = notesList.get(position);


        if(!notesList.get(position).getCreated().equalsIgnoreCase("Empty") && !notesList.get(position).getNotes().equalsIgnoreCase("Empty"))
        {
            holder.notes_date.setText(nature.getCreated());
            holder.notes_desc.setText(nature.getNotes());
        }
        else
        {
            holder.no_notes.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        if(notesList.size() < 3)
        {
            return notesList.size();
        }
        else
        {
            return 3;
        }


    }


    public long getItemId(int position) {
        return position;
    }
    public Object getItem(int position) {
        return notesList.get(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notes_date, notes_desc, no_notes;


        public MyViewHolder(View itemView) {
            super(itemView);

            notes_date = (TextView) itemView.findViewById(R.id.notes_date);
            notes_desc = (TextView)itemView.findViewById(R.id.notes_des);
            no_notes = (TextView)itemView.findViewById(R.id.no_notes);

        }
    }
}
