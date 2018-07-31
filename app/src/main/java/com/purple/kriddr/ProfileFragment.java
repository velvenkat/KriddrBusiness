package com.purple.kriddr;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.purple.kriddr.adapter.BusinessRecordsAdapter;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.DocumentModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pf-05 on 2/5/2018.
 */

public class ProfileFragment extends Fragment implements BusinessRecordsAdapter.DataFromAdaptertoFragment{

    View rootView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> record_nameList = new ArrayList<>();
    UserModel userModel;
    TextView bus_name_value, bus_phone_value, address_Value, name_value, email_value;
    ImageView business_image, img_unLink,img_plus_btn,img_next_btn;
    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main fragmentCall_mainObj;
    ArrayList<DocumentModel> documentList;
    DocumentModel documentModel;
    JSONObject documentJsonObject;
    BusinessRecordsAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("PROFILEFRAGMENTCALL", "PROFILEFRAGMENTCALL");

        rootView = inflater.inflate(R.layout.business_profile_main, container, false);

        ((KridderNavigationActivity) getActivity()).setNavigationVisibility(true);

        bus_name_value = (TextView) rootView.findViewById(R.id.bus_name_value);
        bus_phone_value = (TextView) rootView.findViewById(R.id.bus_phone_value);
        address_Value = (TextView) rootView.findViewById(R.id.address_Value);
        name_value = (TextView) rootView.findViewById(R.id.name_value);
        email_value = (TextView) rootView.findViewById(R.id.email_value);
        business_image = (ImageView) rootView.findViewById(R.id.business_image);

        img_next_btn = (ImageView)rootView.findViewById(R.id.img_next_btn);

        img_unLink = (ImageView) rootView.findViewById(R.id.img_unLink);
        img_plus_btn = (ImageView)rootView.findViewById(R.id.img_plus_btn);


        setHasOptionsMenu(true);
        bus_phone_value.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.INVISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);

        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);
        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);

        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.menusetting);

        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomDialog();
            }
        });


        actionBarUtilObj.setTitle("BUSINESS PROFILE");



        bus_name_value.setText(userModel.getBusiness_name());
        bus_phone_value.setText(userModel.getBusiness_phone().trim());
        address_Value.setText(userModel.getBusiness_address());
        name_value.setText(userModel.getName());
        email_value.setText(userModel.getEmail());




        LayoutInflater layoutInflater1 = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addView1 = layoutInflater1.inflate(R.layout.bottom_layout, null);


        container.addView(addView1);



        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lst_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        img_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

                int lastVisibleItemIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();


                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {

                    linearLayoutManager.smoothScrollToPosition(mRecyclerView, null, lastVisibleItemIndex-1);

                    LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    //Toast.makeText(getActivity(), "" + manager.findFirstCompletelyVisibleItemPosition(), Toast.LENGTH_SHORT).show();
                }




            }
        });


        if (NetworkConnection.isOnline(getActivity())) {

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

        img_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                fragmentCall_mainObj.Fragment_call(new ProfileRecordCreationFragment(), "addrecord", bundle);
            }
        });



        if (NetworkConnection.isOnline(getActivity())) {
            recordList(getResources().getString(R.string.url_reference) + "business_profile_documents_list.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        return rootView;

    }


    public void recordAdapter() {

        mAdapter = new BusinessRecordsAdapter(documentList, getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState != null)
        {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState,"Prof_View_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,mContent,"profviewclient");
            fragmentTransaction.addToBackStack("profviewclient");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        getActivity().getSupportFragmentManager().putFragment(outState,"Prof_View_STATE",this);
    }



    public void recordList(String url) {
        final MyProgressDialog progressDialog=new MyProgressDialog(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);
                progressDialog.hide();
                try
                {
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray jsonArray2 = jsonObject.getJSONArray("details");
                    documentList = new ArrayList<>();

                    for (int index = 0; index < jsonArray2.length(); index++) {

                        documentModel = new DocumentModel();

                        documentJsonObject = jsonArray2.getJSONObject(index);
                        
                        documentModel.setDocument(documentJsonObject.getString("image"));
                        documentModel.setCreated(documentJsonObject.getString("created"));

                        documentList.add(documentModel);

                    }

                    recordAdapter();
                    LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    manager.scrollToPositionWithOffset(documentList.size() - 1, 0);




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progress.hide();
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("USERID", "USERID" + userModel.getId());
                params.put("user_id", userModel.getId());
                return params;
            }

        };
        progressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

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
                    case 0:

                        String url = "https://www.kriddr.com/terms-of-service";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;

                    case 1:

                        String url_lk = "https://www.kriddr.com/privacy-policy";
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        in.setData(Uri.parse(url_lk));
                        startActivity(in);
                        break;




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
                        negativeButton.setTextColor(Color.parseColor("#FF0000"));

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

        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }

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



    @Override
    public void getBusinessRecordsinfo(String doc_id, String image_url) {
        Bundle bundle = new Bundle();
        bundle.putString("image_url",image_url);
        fragmentCall_mainObj.Fragment_call(new BusinessRecordViewDetails(), "businessrecd", bundle);

    }
}
