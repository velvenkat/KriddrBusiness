package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.model.NotesModel;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {

    ArrayList<NotesModel> notesList = new ArrayList<>();
    Context context;



    public NotesListAdapter(ArrayList<NotesModel> notesList,Context context)
    {
        this.notesList = notesList;
        this.context = context;

    }


    @Override
    public NotesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_frag,parent,false);
        return new NotesListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesListAdapter.MyViewHolder holder, int position) {

        holder.notes_date.setText(notesList.get(position).getCreated());
        holder.notes_desc.setText(notesList.get(position).getNotes());


    }

    @Override
    public int getItemCount() {

        return  notesList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notes_date, notes_desc;


        public MyViewHolder(View itemView) {
            super(itemView);

            notes_date = (TextView) itemView.findViewById(R.id.notes_date);
            notes_desc = (TextView)itemView.findViewById(R.id.notes_des);

        }
    }


}
