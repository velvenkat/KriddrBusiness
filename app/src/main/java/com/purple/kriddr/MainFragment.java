package com.purple.kriddr;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.adapter.SliderPagerAdapter;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class MainFragment extends Fragment {

    View rootView;
    private ViewPager vp_slider;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Map<Integer, Object>> slider_image_list;
    private TextView header_firsttitle, header_secondtitle;
    Button signup_button;
    android.support.v7.app.ActionBar mActionBar;
    private RelativeLayout signin_layout;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_main, container, false);

        signin_layout = rootView.findViewById(R.id.signin_layout);


        signin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genFragmentCall_mainObj.Fragment_call(new SigninFragment(), "SgnInFrag", null);

            }
        });

        actionBarUtilObj.SetActionBarHide();
        init();
        addBottomDots(0);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.hide();*/

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            genFragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if(context instanceof InterfaceActionBarUtil){
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }
    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d("ONSTRATCALL", "ONSTRATCALL");

    }

    @Override
    public void onResume() {
        Log.d("ONRESUMECALL", "ONRESUMECALL");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("ONPAUSECALL", "ONPAUSECALL");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("ONSTOPCALL", "ONSTOPCALL");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("ONDESTROYCALL", "ONDESTROYCALL");
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        Log.d("ONDETACHCALL", "ONDETACHCALL");
        super.onDetach();
    }


    private void init() {
        vp_slider = (ViewPager) rootView.findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) rootView.findViewById(R.id.layoutDots);
        header_firsttitle = (TextView) rootView.findViewById(R.id.header_firsttitle);
        header_secondtitle = (TextView) rootView.findViewById(R.id.header_secondtitle);


        slider_image_list = new ArrayList<>();

        Map<Integer, Object> myList = new HashMap<>();
        myList.put(0, R.drawable.intro1);
        myList.put(1, "Welcome!");
        myList.put(2, "Easily care for your pets");
        slider_image_list.add(myList);
        myList = new HashMap<>();
        myList.put(0, R.drawable.intro2);
        myList.put(1, "Organize");
        myList.put(2, "All in one place");
        slider_image_list.add(myList);
        myList = new HashMap<>();
        myList.put(0, R.drawable.intro3);
        myList.put(1, "Grow");
        myList.put(2, "Spend more time with your \n best friend");
        slider_image_list.add(myList);


        sliderPagerAdapter = new SliderPagerAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        signup_button = (Button) rootView.findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genFragmentCall_mainObj.Fragment_call(new SignupFragment(), "SgnUpFrag", null);
            }
        });
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        int[] colorsActive = getResources().getIntArray(R.array.orange_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.orange_dot_inactive);

        Map<Integer, Object> myList = slider_image_list.get(currentPage);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            header_firsttitle.setText(String.valueOf(myList.get(1)));
            header_secondtitle.setText(String.valueOf(myList.get(2)));

            dots[i].setTextSize(50);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }
}
