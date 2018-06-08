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
import com.purple.kriddr.model.PetModel;


import java.util.ArrayList;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    ArrayList<DocumentModel> docList = new ArrayList<>();
    Context context;
    RecordsAdapter.DataFromAdapterToFragment dataFromAdapterToFragment;


    public RecordsAdapter(ArrayList<DocumentModel> docList,Context context,RecordsAdapter.DataFromAdapterToFragment listener)
    {

        this.docList = docList;
        this.context = context;
        this.dataFromAdapterToFragment = listener;

    }

    public interface DataFromAdapterToFragment
    {
        public void getRecordsinfo(String doc_id);
    }


    @Override
    public RecordsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_adapter,parent,false);
        return new RecordsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecordsAdapter.MyViewHolder holder, final int position) {

        Glide.with(context).load(docList.get(position).getDocument()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.record_image);

        if(docList.get(position).getCreated().equalsIgnoreCase("Empty"))
        {
            holder.record_name.setVisibility(View.GONE);
        }
        else
        {
            holder.record_name.setText(docList.get(position).getCreated());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataFromAdapterToFragment.getRecordsinfo(docList.get(position).getDocuments_id());

            }
        });


    }

    @Override
    public int getItemCount() {
        return docList.size();

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView record_name;
        ImageView record_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            record_image = (ImageView)itemView.findViewById(R.id.recordimagView);
            record_name = (TextView)itemView.findViewById(R.id.record_name);

        }
    }
}
