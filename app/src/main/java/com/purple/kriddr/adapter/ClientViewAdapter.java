package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.purple.kriddr.CircleTransform;
import com.purple.kriddr.PetClientListFragment;
import com.purple.kriddr.R;
import com.purple.kriddr.model.PetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/10/2018.
 */

public class ClientViewAdapter extends RecyclerView.Adapter<ClientViewAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater layoutInflater;
    List<PetModel> clientList;
    ClientViewAdapter.DataFromAdapterToFragment dataFromAdapterToFragment;

    public ClientViewAdapter(Context context,List<PetModel> clientList,ClientViewAdapter.DataFromAdapterToFragment listener) {
        this.context = context;
        this.clientList=clientList;
        this.dataFromAdapterToFragment = listener;

    }

    public interface DataFromAdapterToFragment
    {
        public void getClientinfo(String  pet_id, String owner_id,int Profile_Status);
    }

    @Override
    public ClientViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_adapter_view,parent,false);
        return new ClientViewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClientViewAdapter.MyViewHolder holder, final int position) {

        final PetModel nature = clientList.get(position);

        holder.first_Name.setText(nature.getPet_name());
        holder.second_Name.setText(nature.getOwner_name());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //PetModel model = clientList.get(position);
                 int ProfileStatus;
                if(nature.getProfile_status().equalsIgnoreCase("verified")){
                   ProfileStatus= PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal();
                }
                else{
                    ProfileStatus= PetClientListFragment.PROFILE_STATUS.NOT_VERIFIED.ordinal();
                }
                dataFromAdapterToFragment.getClientinfo(nature.getPet_id(),nature.getOwwner_id(),ProfileStatus);

            }
        });
        if(nature.getPhoto().isEmpty() || nature.getPhoto().equals(""))
        {
            holder.imageView.setImageResource(R.drawable.defaultpetphoto);
        }
        else {
            Glide.with(context).load(nature.getPhoto()).transform(new CircleTransform(context)).into(holder.imageView);
        }



    }

    @Override
    public int getItemCount() {
        return clientList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView first_Name,second_Name;
        ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);

            first_Name = (TextView)itemView.findViewById(R.id.first_name);
            second_Name = (TextView)itemView.findViewById(R.id.second_name);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
        }
    }
}
