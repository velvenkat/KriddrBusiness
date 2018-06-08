package com.purple.kriddr.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.purple.kriddr.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pf-05 on 1/23/2018.
 */

public class SliderPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context activity;
    ArrayList<Map<Integer,Object>> image_arraylist;



    public SliderPagerAdapter(Context activity,ArrayList<Map<Integer,Object>> image_arraylist) {

        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider,container,false);
        ImageView im_slider = (ImageView)view.findViewById(R.id.im_slider);

        Map<Integer,Object> myList=image_arraylist.get(position);

        Glide.with(activity).load((Integer)myList.get(0)).into(im_slider);



         container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
