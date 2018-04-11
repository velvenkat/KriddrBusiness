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
import com.purple.kriddr.CircleTransform;
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
        public void getClientInfo(String  pet_id, String owner_id);
    }

    @Override
    public PetClientListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_client_list_adapter,parent,false);
        return new PetClientListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PetClientListAdapter.MyViewHolder holder, int position) {

        final PetModel nature = clientList.get(position);
        Log.d("ONCIMB","ONCIMB");

        holder.first_Name.setText(nature.getPet_name());
        holder.second_Name.setText(nature.getOwner_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("PETCLIEMADA","PETCLIEMADA"+nature.getOwwner_id() + "PET ID"+nature.getPet_id());

                dataFromAdapterToFragment.getClientInfo(nature.getPet_id(),nature.getOwwner_id());
            }
        });

        //  Glide.with(context).load(ImageList.get(position)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.imageView);


        //Rounded Set Image through XML
        // Glide.with(context).load(ImageList.get(position)).bitmapTransform(new CenterCrop(context)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.imageView);

        Glide.with(context).load(nature.getPhoto()).transform(new CircleTransform(context)).into(holder.imageView);




//        Picasso.with(context)
//                .load(ImageList.get(position))
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        // loaded bitmap is here (bitmap)
//
//                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
//                        drawable.setCircular(true);
//                        holder.imageView.setImageDrawable(drawable);
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });




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

            first_Name = (TextView)itemView.findViewById(R.id.name);
            second_Name = (TextView)itemView.findViewById(R.id.contact);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}
