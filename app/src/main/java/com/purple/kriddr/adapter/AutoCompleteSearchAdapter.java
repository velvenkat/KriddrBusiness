package com.purple.kriddr.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.purple.kriddr.CircleTransform;
import com.purple.kriddr.ClientViewDetailsFragment;
import com.purple.kriddr.PetClientListFragment;
import com.purple.kriddr.R;
import com.purple.kriddr.model.PetModel;
import com.purple.kriddr.util.GenFragmentCall_Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/16/2018.
 */

public class AutoCompleteSearchAdapter extends ArrayAdapter<PetModel> {


    Context context;
    int resource, textViewResourceId;
    List<PetModel> items, tempItems, suggestions;
    boolean isPet;
    AutoCompleteSearchAdapter.DataFromAdapterToFragment dataFromAdapterToFragment;

    public AutoCompleteSearchAdapter(Context context, int resource, int textViewResourceId, List<PetModel> items, boolean isPetVal,AutoCompleteSearchAdapter.DataFromAdapterToFragment dataToFragment) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.isPet=isPetVal;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<PetModel>(items); // this makes the difference.
        suggestions = new ArrayList<PetModel>();

        this.dataFromAdapterToFragment=dataToFragment;


    }

    public interface DataFromAdapterToFragment
    {
        public void getClientDetails(String  pet_id, String owner_id,int Profile_Status);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_people, null);
        }
        final PetModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) convertView.findViewById(R.id.name);
            TextView lconName = (TextView) convertView.findViewById(R.id.contact);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            if (lblName != null)
                lblName.setText(people.getPet_name());
                lconName.setText(people.getOwner_name());
               Glide.with(context).load(people.getPhoto()).transform(new CircleTransform(context)).into(imageView);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"Created_Dt"+people.getCreated(),Toast.LENGTH_LONG).show();
                int Profile_Status;
                if(people.getProfile_status().equalsIgnoreCase("verified")){
                 Profile_Status= PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal();
                }
                else {
                    Profile_Status= PetClientListFragment.PROFILE_STATUS.NOT_VERIFIED.ordinal();
                }
                dataFromAdapterToFragment.getClientDetails(people.getPet_id(),people.getOwwner_id(),Profile_Status);
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str ;
            if(isPet){
                str = ((PetModel) resultValue).getPet_name();
            }
            else
             str = ((PetModel) resultValue).getOwner_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (PetModel people : tempItems) {
                    if(isPet) {
                        if (people.getPet_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }
                    else{
                        if (people.getOwner_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<PetModel> filterList = (ArrayList<PetModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (PetModel people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
