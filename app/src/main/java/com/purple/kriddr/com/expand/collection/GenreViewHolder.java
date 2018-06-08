package com.purple.kriddr.com.expand.collection;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Niranjan Reddy on 16-03-2018.
 */

public class GenreViewHolder extends GroupViewHolder {

        private TextView genreTitle,txtPetParentName,txtTotalAmt;
        private ImageView imgExpIndicator,imgTick;

        public GenreViewHolder(View itemView) {
            super(itemView);
            genreTitle = itemView.findViewById(R.id.txtPetName);
            txtPetParentName = itemView.findViewById(R.id.txtPetParentName);
            txtTotalAmt = itemView.findViewById(R.id.txtTotalAmt);
            imgExpIndicator=itemView.findViewById(R.id.imgIndicator);
            imgTick=itemView.findViewById(R.id.imgTick);
            if(imgExpIndicator.getTag()==null){
             imgExpIndicator.setTag("up");
             imgExpIndicator.setImageResource(R.drawable.dxpan);
            }
        }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        ImageView imgIndicator=(ImageView) v.findViewById(R.id.imgIndicator);
        if(imgIndicator.getTag().toString().equalsIgnoreCase("up")){
            imgIndicator.setImageResource(R.drawable.expan);
            imgIndicator.setTag("down");

        }
        else
        {
            imgIndicator.setImageResource(R.drawable.dxpan);
            imgIndicator.setTag("up");
        }
    }

   public void setImgTickChange(boolean visiblle){
            if(visiblle) {
                imgTick.setVisibility(View.VISIBLE);

                txtTotalAmt.setTextColor(Color.parseColor("#a3d69a"));
            }
            else
                imgTick.setVisibility(View.INVISIBLE);
   }

    public void setGenreTitle(String petName, String petParent, String amount) {
            genreTitle.setText(petName);
            txtPetParentName.setText(petParent);
            txtTotalAmt.setText("$"+amount);
        }
}