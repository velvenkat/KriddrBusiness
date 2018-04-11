package com.purple.kriddr;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.purple.kriddr.adapter.NotesAdapter;
import com.purple.kriddr.adapter.NotesListAdapter;
import com.purple.kriddr.adapter.RecordsAdapter;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.DocumentModel;
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
 * Created by pf-05 on 2/22/2018.
 */

public class NotesFragment extends Fragment {

    View rootView;

    RecyclerView mRecycerView;
    RecyclerView.LayoutManager mlayoutManager;
    NotesListAdapter mAdapter;
    JSONObject notesJsonObject;

    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;

    ImageView add_Notes;
    NotesModel notesModel;
    ArrayList<NotesModel> notesList;
    String pet_id = "";
    AlertDialog dialog;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notes_fragment,container,false);

        Bundle bundle_args=getArguments();
        if(bundle_args!=null) {
            pet_id = bundle_args.getString("pet_id", null);


        }

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);


        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.setTitle("BACK");


        add_Notes = (ImageView)rootView.findViewById(R.id.add_Notes);
        mRecycerView = (RecyclerView)rootView.findViewById(R.id.note_list);

        mRecycerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecycerView.setLayoutManager(mlayoutManager);

        if (NetworkConnection.isOnline(getActivity())) {
            notesDetails(getResources().getString(R.string.url_reference) + "pet_details.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        add_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_notes_pop_up_window();
            }
        });


        return rootView;
    }


    public void add_notes_pop_up_window()
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(),R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.notes_layout, null);

        alertDialog.setView(dialogView);

        dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView)dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        TextView title_text = (TextView)dialogView.findViewById(R.id.notes_text);
        title_text.setText("Notes");
        final EditText enter_notes = (EditText)dialogView.findViewById(R.id.enter_notes);
        Button submit_btn = (Button)dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String notes = enter_notes.getText().toString().trim();

                addNotes(getResources().getString(R.string.url_reference) + "pet_notes_creation.php",notes);


            }
        });

        alertDialog.setView(dialogView);



        dialog.show();


    }



    public void addNotes(String url, final String notes)
    {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("FOODPRERES","FOODPRERES"+s);

                myProgressDialog.hide();

                try
                {
                    JSONObject jsonObject = new JSONObject(s);

                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");

                    if(result.equalsIgnoreCase("Success"))
                    {
                        notesDetails(getResources().getString(R.string.url_reference) + "pet_details.php");
                        dialog.cancel();
                    }
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

                myProgressDialog.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.d("FOODRESQSERV","FOODRESQSERV"+userModel.getId() + "PEID  "+pet_id + "NOT "+notes);

                params.put("user_id",userModel.getId());
                params.put("pet_id",pet_id);
                params.put("notes",notes);

                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }




    private void notesDetails(String url) {
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("BASEUNTRRES","BASEUNTRRES"+s);

                try
                {

                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("pet_details");


                    JSONArray jsonArray2 = jsonObject.getJSONArray("documents_list");


                    JSONArray jsonArray3 = jsonObject.getJSONArray("notes_list");

                    notesList = new ArrayList<>();

                    for (int index = 0; index < jsonArray3.length(); index++)
                    {
                        notesModel = new NotesModel();

                        notesJsonObject = jsonArray3.getJSONObject(index);

                        notesModel.setPet_notes_id(notesJsonObject.getString("pet_notes_id"));
                        notesModel.setNotes(notesJsonObject.getString("notes"));
                        notesModel.setCreated(notesJsonObject.getString("created"));
                        notesList.add(notesModel);

                    }






                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                mAdapter = new NotesListAdapter(notesList,getActivity());
                mRecycerView.setAdapter(mAdapter);



//                feedlist = BaseUnitParser.parserFeed(s);
//                display_data();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.d("NTDIS","NTDIS"+ userModel.getId() + "BUMAID"+pet_id);
//
                params.put("user_id",userModel.getId());
                params.put("pet_id",pet_id);



                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
    }

}
