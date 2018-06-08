package com.purple.kriddr;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

/**
 * Created by pf-05 on 5/7/2018.
 */

public class BusinessRecordViewDetails extends android.support.v4.app.Fragment {

    View rootView;
    ImageView record_image;
    RelativeLayout bottom_layout;
    String image_url;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.record_view, container, false);

        ((KridderNavigationActivity) getActivity()).setNavigationVisibility(false);

        bottom_layout = (RelativeLayout)rootView.findViewById(R.id.bottom_layout);
        bottom_layout.setVisibility(View.GONE);


        PhotoView photoView = (PhotoView)rootView.findViewById(R.id.record_image);




        Bundle bundle_args = getArguments();
            if (bundle_args != null) {
                image_url = bundle_args.getString("image_url", null);

                if (NetworkConnection.isOnline(getActivity())) {

                    Glide.with(getActivity()).load(image_url).diskCacheStrategy(DiskCacheStrategy.NONE).thumbnail(0.5f).skipMemoryCache(true).into(photoView);

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }


            }








        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                fragmentCall_mainObj.Fragment_call(new ProfileFragment(),"profviewclient",bundle);

            }
        });

        actionBarUtilObj.getImgSettings().setVisibility(View.INVISIBLE);


        actionBarUtilObj.setTitle("Back");




        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }

    }
}
