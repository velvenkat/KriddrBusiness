package com.purple.kriddr;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purple.kriddr.adapter.ClientViewAdapter;
import com.purple.kriddr.adapter.PetClientListAdapter;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.PetModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.parser.PetListParser;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 2/13/2018.
 */

public class PetClientListFragment extends android.support.v4.app.Fragment implements PetClientListAdapter.DataFromAdapterToFragment {

    View rootView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    PetModel petModel;
    Button add_new_pet_client;

    List<PetModel> feedlist;
    TextView no_client;


    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main fragmentCall_mainObj;
    UserModel userModel;

    String pet_id = "", phone_number = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.pet_client_list,container,false);


        ((KridderNavigationActivity)getActivity()).setNavigationVisibility(false);

        Bundle bundle = getArguments();

        if(bundle!=null) {
            try {
                //phone_number = bundle.getString("mobile_number");

                Log.d("SAMR", "SAMR");


                petModel = bundle.getParcelable("pet_parent");
                phone_number=petModel.getMobile();

                Log.d("CYLRES", "CYLRES" + phone_number + "FEDRDS " + petModel.getAddress() + "SRO " + petModel.getOwner_name());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        add_new_pet_client = (Button)rootView.findViewById(R.id.add_new_pet_client);
        no_client = (TextView)rootView.findViewById(R.id.no_client);


        add_new_pet_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("mobile_number",phone_number);
                bundle.putParcelable("pet_parent",petModel);
                fragmentCall_mainObj.Fragment_call(new ClientCreationFragment(),"petparentlist",bundle);

            }
        });

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);



        actionBarUtilObj.setTitle("Back");





        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });



        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.client_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);

//
//        mAdapter = new PetClientListAdapter(getActivity(),first_Name,second_Name,ImageList);
//        mRecyclerView.setAdapter(mAdapter);


        if (NetworkConnection.isOnline(getActivity())) {
            petList(getResources().getString(R.string.url_reference) +"pet_list.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if(context instanceof FragmentCallInterface)
        {
            FragmentCallInterface callInterface = (FragmentCallInterface)context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil)
        {
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }


    }

    public void display_data()
    {

        if(feedlist != null) {

            mRecyclerView.setAdapter(new PetClientListAdapter(getActivity(),feedlist,this));

//            mRecyclerView.scrollToPosition(1);
            LinearLayoutManager manager=(LinearLayoutManager)mRecyclerView.getLayoutManager();

            manager.scrollToPositionWithOffset(feedlist.size()-1,0);



        }
    }


    public void petList(String url)
    {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS","LISTOFCLIENTS"+s);


                try
                {

                    JSONArray jsonArray = new JSONArray(s);
                    for(int i=0; i < jsonArray.length(); i++)
                    {

                        JSONObject parentObject = jsonArray.getJSONObject(i);

                        String pet_id = parentObject.getString("pet_id");

                        if(pet_id.equals("Empty") || pet_id.contains("Empty"))
                        {
                            no_client.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                feedlist = PetListParser.parseFeed(s);
                display_data();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progress.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("USERID","USERID"+userModel.getId() + "MOBIL "+phone_number);
                params.put("user_id",userModel.getId());
                params.put("mobile",phone_number);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);

    }



    @Override
    public void getClientInfo(String pet_id, String owner_id) {
        Bundle bundle=new Bundle();
        bundle.putString("pet_id",pet_id);
        fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(),"clientDetail",bundle);
    }

}
