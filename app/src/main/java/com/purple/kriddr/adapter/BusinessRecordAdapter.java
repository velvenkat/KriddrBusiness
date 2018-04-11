package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purple.kriddr.R;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/6/2018.
 */

public class BusinessRecordAdapter extends RecyclerView.Adapter<BusinessRecordAdapter.MyViewHolder> {


    ArrayList<String> record_nameList = new ArrayList<>();
    Context context;


    public BusinessRecordAdapter(ArrayList<String> record_nameList,Context context)
    {
        Log.d("JASIRE","JASIRE"+record_nameList.size());
        this.record_nameList = record_nameList;
        this.context = context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_adapter,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d("PIRSDR","PIRSDR"+record_nameList.get(position));
        holder.record_name.setText(record_nameList.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d("RECNAMLST","RECNAMLST"+record_nameList.size());
        return record_nameList.size();

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
