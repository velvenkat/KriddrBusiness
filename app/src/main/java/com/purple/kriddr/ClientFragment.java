package com.purple.kriddr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purple.kriddr.adapter.AutoCompleteSearchAdapter;
import com.purple.kriddr.adapter.ClientViewAdapter;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 2/10/2018.
 */

public class ClientFragment extends Fragment implements ClientViewAdapter.DataFromAdapterToFragment, AutoCompleteSearchAdapter.DataFromAdapterToFragment {
    View rootView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> sortNameList = new ArrayList<>();
    Spinner sort_by_most_recent;
    ArrayAdapter<String> dataAdapter;
    Button add_client;
    ActionBarUtil actionBarUtilObj;
    Dialog mBottomSheetDialog;

    GenFragmentCall_Main fragmentCall_mainObj;
    List<PetModel> feedlist;
    String select_sort = "", pet_id = "";
    UserModel userModel;
    private RelativeLayout no_clients, items_layout;
    AutoCompleteSearchAdapter adapter;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.client_view, container, false);


        ((KridderNavigationActivity) getActivity()).setNavigationVisibility(true);


        sortNameList.clear();

        sortNameList.add("Sort by most recent");
        sortNameList.add("Sort alphabetically");


        no_clients = (RelativeLayout) rootView.findViewById(R.id.no_cleint_layout);
        items_layout = (RelativeLayout) rootView.findViewById(R.id.items_layout);
        sort_by_most_recent = (Spinner) rootView.findViewById(R.id.sort_by_most_recent);
        add_client = (Button) rootView.findViewById(R.id.add_client);

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.INVISIBLE);
        actionBarUtilObj.getTitle().setVisibility(View.VISIBLE);

        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.search);

        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(R.layout.search_layout); // your custom view.
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.TOP);

                AutoCompleteTextView search_pet = (AutoCompleteTextView) mBottomSheetDialog.getWindow().findViewById(R.id.search_pet);
                search_pet.setThreshold(1);
                AutoCompleteTextView search_owner = (AutoCompleteTextView) mBottomSheetDialog.getWindow().findViewById(R.id.search_owner);
                search_owner.setThreshold(1);

                setAutoSearchAdapter(search_pet, true);
                setAutoSearchAdapter(search_owner, false);
                mBottomSheetDialog.show();


            }
        });


        actionBarUtilObj.setTitle("CLIENT");


        add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("PETID", "PETID" + pet_id);
              /*  if (pet_id.equals("Empty")) {
                    fragmentCall_mainObj.Fragment_call(new PetClientCreationFragment(), "ClientCrt", null);
                } else {*/
                pop_up_window();

                //}


            }
        });


        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, sortNameList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_contact);


        sort_by_most_recent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    List<PetModel> TempPetList = new ArrayList<>(feedlist);

                    if (position == 1) {
                        Collections.sort(TempPetList, new Comparator<PetModel>() {
                            public int compare(PetModel s1, PetModel s2) {
                                // Write your logic here.
                                return (s1.getPet_name().compareToIgnoreCase(s2.getPet_name()));
                            }
                        });
                        display_data(TempPetList);
                    } else {
                        display_data(feedlist);
                    }
                    //
                } catch (Exception e) {

                }
                //Log.d("MOSSORT","MOSSORT"+select_sort);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sort_by_most_recent.setAdapter(dataAdapter);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.client_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (NetworkConnection.isOnline(getActivity())) {
            petList(getResources().getString(R.string.url_reference) + "pet_list.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Fragment mContent;
        if (savedInstanceState != null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "CLIFRAST");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, mContent, "clientfrag");
            fragmentTransaction.addToBackStack("clientfrag");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //To save the instance state
        getActivity().getSupportFragmentManager().putFragment(outState, "CLIFRAST", this);
    }

    public void onResume() {
        super.onResume();
        //    business.requestFocus();


        handle_BackKey();
    }


    public void setAutoSearchAdapter(AutoCompleteTextView txtView, boolean isPet) {

        if (feedlist != null) {
            adapter = new AutoCompleteSearchAdapter(getContext(), R.layout.search_layout, R.id.search_pet, feedlist, isPet, (AutoCompleteSearchAdapter.DataFromAdapterToFragment) this);
            txtView.setAdapter(adapter);
        }


    }

    public void handle_BackKey() {
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

    }

    public void alert_Back(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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
        AlertDialog alert = builder.create();
        alert.show();

        Button positiveButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FF0000"));

        negativeButton.setTextColor(Color.parseColor("#FF0000"));

    }

    public void display_data(List<PetModel> petListDisplay) {

        if (petListDisplay != null) {


            mAdapter = new ClientViewAdapter(getActivity(), petListDisplay, this);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    public void pop_up_window() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.custom_popup, null);

        alertDialog.setView(dialogView);
//                alertDialog.setMessage("Adding a pet client");


        TextView text = (TextView) dialogView.findViewById(R.id.text);
        text.setText("Adding a pet client");

        final EditText edit_text_number = (EditText) dialogView.findViewById(R.id.edit_text_number);

        edit_text_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));


        final AlertDialog dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        Button button = (Button) dialogView.findViewById(R.id.submit_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mobile_number = edit_text_number.getText().toString().trim();

                final String blockCharacterSet = " -,/.@%`'\"\\=~#^|$%&*!";

                InputFilter filter = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        if (source != null && blockCharacterSet.contains(("" + source))) {
                            return "";
                        }
                        return null;
                    }
                };

                edit_text_number.setFilters(new InputFilter[]{filter});

                mobile_number = mobile_number.replaceAll("[^0-9]", "").trim();


                if (mobile_number.equals("") || mobile_number.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_mobile_num), Toast.LENGTH_SHORT).show();
                } else if (mobile_number.length() < 10) {
                    Toast.makeText(getActivity(), "Please enter 10 digit phone number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.cancel();
                    srch_pets(getResources().getString(R.string.url_reference) + "pet_list.php", mobile_number);

                  /*  int findOwnerIndex = 0;
                    if (feedlist.size() > 0) {
                        for (int i = 0; i < feedlist.size(); i++)/ {
                            if (mobile_number.equals(feedlist.get(i).getMobile())) {
                                //String owner_id = feedlist.get(i).getOwwner_id();
                                break;
                            }
                            findOwnerIndex++;
                        }

                        Bundle bundle = new Bundle();

                        if (findOwnerIndex >= feedlist.size()) {
                            Log.d("THISREA", "THISREA");
                            bundle.putString("mobile_no", mobile_number);
                            fragmentCall_mainObj.Fragment_call(new PetClientCreationFragment(), "ClientCrt", bundle);
                        } else {
                            PetModel petModel = feedlist.get(findOwnerIndex);
                            bundle.putParcelable("pet_parent", petModel);
                            fragmentCall_mainObj.Fragment_call(new PetClientListFragment(), "petparentlist", bundle);
                        }
                    }*/

                }


            }
        });

        dialog.show();
    }


    public void petList(String url) {
        final MyProgressDialog myProgressDialog=new MyProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);
                myProgressDialog.hide();

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray ar = jsonObject.getJSONArray("response");
                    JSONObject parentObject = ar.getJSONObject(0);
                    pet_id = parentObject.getString("pet_id");

                    if (pet_id.equalsIgnoreCase("empty")) {
                        no_clients.setVisibility(View.VISIBLE);
                        items_layout.setVisibility(View.GONE);
                    } else {

                        feedlist = PetListParser.parseFeed(s);
                        Log.e("dsdf","va"+feedlist.get(0).getCreated());
                        Collections.sort(feedlist, new Comparator<PetModel>() {
                            public int compare(PetModel s1, PetModel s2) {
                                // Write your logic here.
                                return (s2.getCreated().compareTo(s1.getCreated()));
                            }
                        });

                        display_data(feedlist);

                    }


                } catch (Exception e) {
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

                Log.d("USERID", "USERID" + userModel.getId());
                params.put("user_id", userModel.getId());
                return params;
            }

        };
        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }

    public void srch_pets(String url, final String srch_mobileNo) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);
                myProgressDialog.hide();

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    JSONObject parentObject = jsonArray.getJSONObject(0);

                    String pet_id = parentObject.getString("pet_id");

                    if (pet_id.equals("Empty") || pet_id.contains("Empty") || pet_id.equals("")) {
                        //  no_client.setVisibility(View.VISIBLE);
                        //mRecyclerView.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile_no", srch_mobileNo);
                        fragmentCall_mainObj.Fragment_call(new Parent_Pet_Creation_Fragment(), "cr_pet_parent", bundle);

                    } else {

                        ArrayList<PetModel> Pet_List = new ArrayList<>(PetListParser.parseFeed(s));
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("pet_list", Pet_List);

                        fragmentCall_mainObj.Fragment_call(new PetClientListFragment(), "vw_client_list", bundle);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                /** display_data(); */


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            myProgressDialog.hide();

                // progress.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("mobile", srch_mobileNo);
                params.put("user_id",userModel.getId());
                return params;
            }

        };
        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public void getClientinfo(String pet_id, String owner_id,int ProfileStatus) {
        //   mBottomSheetDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("pet_id", pet_id);
        bundle.putInt("share_status",PetClientListFragment.SHARE_STATUS.ADDED.ordinal());
        bundle.putInt("profile_status",ProfileStatus);
        fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(), "clientDetail", bundle);
    }

    @Override
    public void getClientDetails(String pet_id, String owner_id,int ProfileStatus) {
        mBottomSheetDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("pet_id", pet_id);
        bundle.putInt("share_status",PetClientListFragment.SHARE_STATUS.ADDED.ordinal());
        bundle.putInt("profile_status",ProfileStatus);
        fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(), "clientDetails", bundle);
    }
}


