package com.purple.kriddr;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.purple.kriddr.adapter.AutoCompleteSearchAdapter;
import com.purple.kriddr.adapter.NotesAdapter;
import com.purple.kriddr.adapter.RecordSearchAdapter;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.DocumentModel;
import com.purple.kriddr.model.InvoiceDateValues;
import com.purple.kriddr.model.InvoiceListModel;
import com.purple.kriddr.model.NotesModel;
import com.purple.kriddr.model.PetListModel;
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
 * Created by pf-05 on 3/12/2018.
 */

public class RecordViewDetails extends Fragment implements RecordSearchAdapter.DataFromAdapterToFragment{

    View rootView;
    UserModel userModel;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    String pet_id = "",doc_id = "";
    TextView record_name,category_name;
    ImageView record_image;
    Dialog mBottomSheetDialog;
    ArrayList<DocumentModel> documentList;
    RecordSearchAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.record_view,container,false);

        record_name = (TextView)rootView.findViewById(R.id.record_name);
        category_name = (TextView)rootView.findViewById(R.id.category_name);
        record_image = (ImageView)rootView.findViewById(R.id.record_image);

        Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);
            doc_id = bundle_args.getString("doc_id",null);
            documentList = bundle_args.getParcelableArrayList("doc_list");


        }



        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(),"fragviewclient",bundle);
            }
        });

        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.search);

        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(R.layout.search_doc); // your custom view.
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.TOP);

                AutoCompleteTextView search_pet = (AutoCompleteTextView) mBottomSheetDialog.getWindow().findViewById(R.id.search_pet);
                search_pet.setThreshold(1);
                AutoCompleteTextView search_owner = (AutoCompleteTextView) mBottomSheetDialog.getWindow().findViewById(R.id.search_owner);
                search_owner.setThreshold(1);

                setAutoSearchAdapter(search_pet,true);
                setAutoSearchAdapter(search_owner,false);
                mBottomSheetDialog.show();


            }
        });


        if (NetworkConnection.isOnline(getActivity())) {
            recordDetails(getResources().getString(R.string.url_reference) + "pet_documents_view.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.support.v4.app.Fragment mContent;
        if(savedInstanceState != null)
        {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState,"Rec_Vw_Dtls");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,mContent,"rec_vw_dtls");
            fragmentTransaction.addToBackStack("rec_vw_dtls");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        getActivity().getSupportFragmentManager().putFragment(outState,"Rec_Vw_Dtls",this);
    }

    public void setAutoSearchAdapter(AutoCompleteTextView txtView, boolean isPet) {
        adapter = new RecordSearchAdapter(getContext(), R.layout.search_layout, R.id.search_pet, documentList, isPet,(RecordSearchAdapter.DataFromAdapterToFragment)this);
        txtView.setAdapter(adapter);


    }

    private void recordDetails(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("DOCUMENTDRES", "DOCUMENTDRES" + s);

                try {

                    JSONObject jsonObject = new JSONObject(s);

                    String name = jsonObject.getString("name");
                    String created = jsonObject.getString("created");
                    String document = jsonObject.getString("document");
                    String document_catagory = jsonObject.getString("document_catagory");

                    record_name.setText(name +" "+created);
                    category_name.setText("Category: "+document_catagory);

                    Glide.with(getActivity()).load(document).into(record_image);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("document_id",doc_id);


                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
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

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();

        }
    }


    @Override
    public void getRecordDetails(String doc_id) {
        mBottomSheetDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("pet_id", pet_id);
        bundle.putString("doc_id",doc_id);
        bundle.putParcelableArrayList("doc_list",documentList);
        fragmentCall_mainObj.Fragment_call(new RecordViewDetails(), "recordview", bundle);
    }
}
