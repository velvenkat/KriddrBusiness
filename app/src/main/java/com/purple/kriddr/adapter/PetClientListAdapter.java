package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purple.kriddr.CircleTransform;
import com.purple.kriddr.PetClientListFragment;
import com.purple.kriddr.R;
import com.purple.kriddr.model.PetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/13/2018.
 */

public class PetClientListAdapter extends RecyclerView.Adapter<PetClientListAdapter.MyViewHolder>  {

    Context context;
    private LayoutInflater layoutInflater;
    List<PetModel> clientList;
    PetClientListAdapter.DataFromAdapterToFragment dataFromAdapterToFragment;


    public PetClientListAdapter(Context context, List<PetModel> clientList,PetClientListAdapter.DataFromAdapterToFragment listener) {
        this.context = context;
        this.clientList=clientList;
        this.dataFromAdapterToFragment = listener;

    }

    public interface DataFromAdapterToFragment
    {
        public void getClientInfo(String  pet_id, String owner_id,int Share_Status,int Profile_Status);
    }

    @Override
    public PetClientListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_client_list_adapter,parent,false);
        return new PetClientListAdapter.MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final PetClientListAdapter.MyViewHolder holder, int position) {

        final PetModel nature = clientList.get(position);

        holder.first_Name.setText(nature.getPet_name());
        holder.second_Name.setText(nature.getOwner_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ShareStatus;
                int ProfileStatus;
                if(nature.getShared_with_business().trim().equalsIgnoreCase("pending")){
                 ShareStatus= PetClientListFragment.SHARE_STATUS.PENDING.ordinal();

                }
                else if(nature.getShared_with_business().equalsIgnoreCase("approved")){
                    ShareStatus= PetClientListFragment.SHARE_STATUS.ADDED.ordinal();

                }
                else{
                    ShareStatus= PetClientListFragment.SHARE_STATUS.NOT_ADDED.ordinal();
                }
                if(nature.getProfile_status().equalsIgnoreCase("verified")){
                 ProfileStatus=PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal();
                }
                else{
                    ProfileStatus=PetClientListFragment.PROFILE_STATUS.NOT_VERIFIED.ordinal();
                }
                dataFromAdapterToFragment.getClientInfo(nature.getPet_id(),nature.getOwwner_id(),ShareStatus,ProfileStatus);
            }
        });

        if(nature.getPhoto().isEmpty() || nature.getPhoto().equals(""))
        {
            holder.imageView.setImageResource(R.drawable.defaultpetphoto);
        }
        else
        {
            Glide.with(context).load(nature.getPhoto()).transform(new CircleTransform(context)).into(holder.imageView);
        }

        if(nature.getShared_with_business().trim().equalsIgnoreCase("pending")){
            holder.btnReqStatus.setText("Pending");
        }
        else if(nature.getShared_with_business().equalsIgnoreCase("approved")){
            holder.btnReqStatus.setText("Added");
        }
        else{

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
        TextView btnReqStatus;


        public MyViewHolder(View itemView) {
            super(itemView);

            first_Name = (TextView)itemView.findViewById(R.id.name);
            second_Name = (TextView)itemView.findViewById(R.id.contact);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            btnReqStatus=(TextView)itemView.findViewById(R.id.btnReqStatus);
        }
    }
}
