package com.purple.kriddr;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.purple.kriddr.adapter.BusinessRecordAdapter;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.NetworkConnection;

import java.util.ArrayList;

/**
 * Created by pf-05 on 2/5/2018.
 */

public class ProfileFragment extends Fragment {

    View rootView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    BusinessRecordAdapter mAdapter;
    ArrayList<String> record_nameList = new ArrayList<>();
    UserModel userModel;
    TextView bus_name_value, bus_phone_value, address_Value, name_value, email_value;
    ImageView business_image, img_unLink;
    ActionBarUtil actionBarUtilObj;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("PROFILEFRAGMENTCALL", "PROFILEFRAGMENTCALL");

        rootView = inflater.inflate(R.layout.business_profile_main, container, false);

        bus_name_value = (TextView) rootView.findViewById(R.id.bus_name_value);
        bus_phone_value = (TextView) rootView.findViewById(R.id.bus_phone_value);
        address_Value = (TextView) rootView.findViewById(R.id.address_Value);
        name_value = (TextView) rootView.findViewById(R.id.name_value);
        email_value = (TextView) rootView.findViewById(R.id.email_value);
        business_image = (ImageView) rootView.findViewById(R.id.business_image);

        img_unLink = (ImageView) rootView.findViewById(R.id.img_unLink);


        setHasOptionsMenu(true);


        Log.d("JSMKRD", "JSMKRD" + userModel.getBusiness_name() + "BUPGONO " + userModel.getBusiness_phone() + "BUADD " + userModel.getBusiness_address());


        bus_name_value.setText(userModel.getBusiness_name());
        bus_phone_value.setText(userModel.getBusiness_phone());
        address_Value.setText(userModel.getBusiness_address());
        name_value.setText(userModel.getName());
        email_value.setText(userModel.getEmail());


        record_nameList.add("11/7/17");
        record_nameList.add("12/7/17");
        record_nameList.add("13/7/17");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lst_view);
        mRecyclerView.setHasFixedSize(true);

//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));


        mAdapter = new BusinessRecordAdapter(record_nameList, getActivity());
        mRecyclerView.setAdapter(mAdapter);


        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.INVISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.menusetting);

        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomDialog();
            }
        });


        actionBarUtilObj.setTitle("BUSINESS PROFILE");


        LayoutInflater layoutInflater1 = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addView1 = layoutInflater1.inflate(R.layout.bottom_layout, null);


        container.addView(addView1);


        if (NetworkConnection.isOnline(getActivity())) {
            //Glide.with(getActivity()).load(userModel.getLogo_url()).into(business_image);
            //Glide.with(getActivity()).load(userModel.getLogo_url()).into(business_image).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(business_image);
            Glide.with(getActivity()).load(userModel.getLogo_url()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(business_image);

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        img_unLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.EDIT_PROFILE.ordinal());
                intent.putExtra(KridderNavigationActivity.USER_MODEL_TAG, userModel);
                getActivity().finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


            }
        });


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    alert_Back(getActivity());
                    return true;
                }
                return false;
            }
        });


        return rootView;

    }


    public void alert_Back(Context context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setMessage(context.getResources().getString(R.string.do_you_want_exit))
                .setCancelable(false)
                .setNegativeButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().finish();
                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();


        Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FF0000"));
        //positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

        negativeButton.setTextColor(Color.parseColor("#FF0000"));
        //negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

    }


    public void setBottomDialog() {
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Terms & Conditions");
        menu_list.add("Privacy Policy");
        menu_list.add("Sign Out");
        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.hide();
            }
        });
        list_SettingsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(),"Position :"+position,Toast.LENGTH_SHORT).show();

                switch (position)
                {
                    case 2:

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(R.string.logout))
                                .setCancelable(true)
                                .setMessage(getResources().getString(R.string.logout_warning))
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dbhelp entry = new dbhelp(getActivity());
                                        entry.open();
                                        entry.logout_user();
                                        entry.close();

                                        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                                        getActivity().finish();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);



                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);

                        positiveButton.setTextColor(Color.parseColor("#FF0000"));
                        //positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

                        negativeButton.setTextColor(Color.parseColor("#FF0000"));
                        //negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

                        break;


                }
            }
            }
        );

    }


    @Override
    public void onAttach(Context context) {

        Log.d("ONATTACHCRE", "ONATTACHCRE");
        InterfaceUserModel interfaceUserModel;

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }
        if(context instanceof InterfaceActionBarUtil){
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();
        }

        super.onAttach(context);
    }



}
