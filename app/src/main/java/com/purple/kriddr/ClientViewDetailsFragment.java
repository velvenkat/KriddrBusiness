package com.purple.kriddr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.purple.kriddr.adapter.InvocieListAdapter;
import com.purple.kriddr.adapter.NotesAdapter;
import com.purple.kriddr.adapter.RecordsAdapter;
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
import com.purple.kriddr.parser.PetListParser;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pf-05 on 2/19/2018.
 */

public class ClientViewDetailsFragment extends Fragment implements RecordsAdapter.DataFromAdapterToFragment, View.OnClickListener{

    View rootView;
    TextView profile_name, profile_dob, person_text, mobile_text,email_text, location_text, prefered_contact_text, brand_value, protein_value, servings_value,no_data_items;
    ImageView imageView1, edit_petparent, food_edit, add_Notes, img_plus_btn, img_next_btn;
    JSONObject petJsonObject, invoiceJsonObject, documentJsonObject, notesJsonObject, invoiceValueJsonObject;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    String pet_id, owner_id;
    RecyclerView mRecyclerView, mRecyclerView1, invoice_list;
    RecyclerView.LayoutManager mLayoutManager;
    RecordsAdapter mAdapter;
    NotesAdapter notesAdapter;
    InvoiceListAdapterView invoiceAdapter;
    Button add_invoice;
    String prefered_msg;
    AlertDialog dialog;
    PetListModel petListModel;
    DocumentModel documentModel;
    NotesModel notesModel;
    ArrayList<DocumentModel> documentList;
    ArrayList<NotesModel> notesList;
    ArrayList<InvoiceListModel> invoiceList;
    ArrayList<InvoiceDateValues> invocieDateList;
    RelativeLayout see_more_layout;
    ExpandExpandableListView invoice_data;
    LinearLayout invoice_main_layout;
    InvoiceListModel invoiceListModel;
    private int PICK_IMAGE_REQUEST = 1;
    Uri picUri;
    private Bitmap bitmap;
    String image = "";

    InvocieListAdapter invocieListAdapter;
//    ListView invoice_list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.client_profile_view, container, false);


        ((KridderNavigationActivity) getActivity()).setNavigationVisibility(false);


        Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);


        }

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);


        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                fragmentCall_mainObj.Fragment_call(new ClientFragment(),"fragclient",null);
            }
        });

        actionBarUtilObj.setTitle("BACK");
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCall_mainObj.Fragment_call(new ClientFragment(),"fragclient",null);

            }
        });


        profile_name = (TextView) rootView.findViewById(R.id.profile_name);
        email_text = (TextView)rootView.findViewById(R.id.email_text);
        profile_dob = (TextView) rootView.findViewById(R.id.profile_dob);
        person_text = (TextView) rootView.findViewById(R.id.person_text);
        mobile_text = (TextView) rootView.findViewById(R.id.mobile_text);
        location_text = (TextView) rootView.findViewById(R.id.location_text);
        prefered_contact_text = (TextView) rootView.findViewById(R.id.prefered_contact_text);
        brand_value = (TextView) rootView.findViewById(R.id.brand_value);
        protein_value = (TextView) rootView.findViewById(R.id.protein_value);
        servings_value = (TextView) rootView.findViewById(R.id.servings_value);
        see_more_layout = (RelativeLayout) rootView.findViewById(R.id.see_more_layout);
        add_invoice = (Button) rootView.findViewById(R.id.add_invoice);
        img_next_btn = (ImageView) rootView.findViewById(R.id.img_next_btn);
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        edit_petparent = (ImageView) rootView.findViewById(R.id.edit_petparent);
        food_edit = (ImageView) rootView.findViewById(R.id.food_edit);
        img_plus_btn = (ImageView) rootView.findViewById(R.id.img_plus_btn);
        add_Notes = (ImageView) rootView.findViewById(R.id.add_Notes);
        no_data_items = (TextView)rootView.findViewById(R.id.no_data_items);

        imageView1.setOnClickListener(this);

        invoice_main_layout = (LinearLayout) rootView.findViewById(R.id.invoice_main_layout);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lst_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.notes_list);
        mRecyclerView1.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        img_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("NECTPIS", "NECTPIS");


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

                int lastVisibleItemIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();


                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {

                    linearLayoutManager.smoothScrollToPosition(mRecyclerView, null, lastVisibleItemIndex - 1);

                    LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                }



            }
        });


        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (NetworkConnection.isOnline(getActivity())) {
            clientDetails(getResources().getString(R.string.url_reference) + "pet_details.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        edit_petparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit_pet_parent_pop_up_window();
            }
        });

        food_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_food_pop_up_window();
            }
        });

        add_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ADDNOTES", "ADDNOTES");
                add_notes_pop_up_window();

            }
        });

        img_plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                fragmentCall_mainObj.Fragment_call(new RecordCreationFragment(), "addrecord", bundle);

            }
        });

        add_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("pet_id", pet_id);
                bundle.putString("pet_photo", petListModel.getPhoto());
                fragmentCall_mainObj.Fragment_call(new AddInvoiceFragment(), "addinvoice", bundle);

            }
        });




        return rootView;
    }

    public void handle_BackKey() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    fragmentCall_mainObj.Fragment_call(new ClientFragment(),"fragclient",null);
                    return true;
                }
                return false;
            }
        });

    }

    public void onResume() {
        super.onResume();
        //    business.requestFocus();


        handle_BackKey();
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
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }
    }


    private void clientDetails(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("CLIENTSDETAIL", "CLIENTSDETAIL" + s);


                try {

                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("pet_details");


                    petJsonObject = jsonArray.getJSONObject(0);

                    petListModel = new PetListModel();
                    petListModel.setUser_id(petJsonObject.getString("user_id"));
                    petListModel.setPet_id(petJsonObject.getString("pet_id"));
                    petListModel.setOwner_id(petJsonObject.getString("owner_id"));
                    owner_id = petJsonObject.getString("owner_id");
                    petListModel.setOwner_name(petJsonObject.getString("owner_name"));
                    petListModel.setMobile(petJsonObject.getString("mobile"));
                    petListModel.setEmail(petJsonObject.getString("email"));
                    petListModel.setAddress(petJsonObject.getString("address"));
                    petListModel.setPreferred_contact(petJsonObject.getString("preferred_contact"));
                    petListModel.setPhoto(petJsonObject.getString("photo"));
                    petListModel.setPet_name(petJsonObject.getString("pet_name"));
                    petListModel.setDob(petJsonObject.getString("dob"));
                    petListModel.setBrand(petJsonObject.getString("brand"));
                    petListModel.setProtein(petJsonObject.getString("protein"));
                    petListModel.setPortion_size(petJsonObject.getString("portion_size"));


                    profile_name.setText(petListModel.getPet_name());
                    profile_dob.setText("DOB: " + petListModel.getDob());
                    person_text.setText(petListModel.getOwner_name());
                    mobile_text.setText(petListModel.getMobile());
                    email_text.setText(petListModel.getEmail());
                    location_text.setText(petListModel.getAddress());
                    prefered_contact_text.setText(petListModel.getPreferred_contact());
                    brand_value.setText(petListModel.getBrand());
                    protein_value.setText(petListModel.getProtein());
                    servings_value.setText(petListModel.getPortion_size());


                    if(!petListModel.getPhoto().equals(""))
                    {
                        Glide.with(getActivity()).load(petListModel.getPhoto()).transform(new CircleTransform(getActivity())).into(imageView1);

                    }
                    else
                    {
                        Log.d("NOPATE","NOPATE");
                        Glide.with(getActivity()).load(R.drawable.defaultpetphoto).into(imageView1);

                    }




                    JSONArray jsonArray2 = jsonObject.getJSONArray("documents_list");

                    documentList = new ArrayList<>();

                    for (int index = 0; index < jsonArray2.length(); index++) {

                        documentModel = new DocumentModel();

                        documentJsonObject = jsonArray2.getJSONObject(index);

                        documentModel.setDocuments_id(documentJsonObject.getString("documents_id"));
                        documentModel.setDocument_name(documentJsonObject.getString("document_name"));
                        documentModel.setDocument(documentJsonObject.getString("document"));
                        documentModel.setDocument_category(documentJsonObject.getString("document_category"));
                        documentModel.setDocument_category_id(documentJsonObject.getString("document_category_id"));
                        documentModel.setCreated(documentJsonObject.getString("created"));

                        documentList.add(documentModel);

                    }



                    recordAdapter();
                    LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    manager.scrollToPositionWithOffset(documentList.size() - 1, 0);


                    JSONArray jsonArray3 = jsonObject.getJSONArray("notes_list");

                    notesList = new ArrayList<>();

                    for (int index = 0; index < jsonArray3.length(); index++) {
                        notesModel = new NotesModel();

                        notesJsonObject = jsonArray3.getJSONObject(index);

                        notesModel.setPet_notes_id(notesJsonObject.getString("pet_notes_id"));
                        notesModel.setNotes(notesJsonObject.getString("notes"));
                        notesModel.setCreated(notesJsonObject.getString("created"));
                        notesList.add(notesModel);

                    }

                    if (notesList.size() >= 3) {
                        see_more_layout.setVisibility(View.VISIBLE);
                        see_more_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Bundle bundle = new Bundle();
                                bundle.putString("pet_id", pet_id);
                                fragmentCall_mainObj.Fragment_call(new NotesFragment(), "petparentlist", bundle);

                            }
                        });
                    }

                    notesAdapter = new NotesAdapter(notesList, getActivity());
                    mRecyclerView1.setAdapter(notesAdapter);


                    JSONArray jsonArray4 = jsonObject.getJSONArray("invoices_list");
                    invoiceList = new ArrayList<InvoiceListModel>();
                    invoice_main_layout.removeAllViews();

                    for (int index = 0; index < jsonArray4.length(); index++) {
                        invoiceListModel = new InvoiceListModel();
                        invocieDateList = new ArrayList<InvoiceDateValues>();

                        invoiceJsonObject = jsonArray4.getJSONObject(index);

                        invoiceListModel.setMonth_year(invoiceJsonObject.getString("month_year"));
                        invoiceListModel.setMonth_total(invoiceJsonObject.getString("month_amount"));

                        JSONArray jsonDetails = invoiceJsonObject.getJSONArray("details");

                        Log.d("JSDERND", "JSDERND" + jsonDetails);

                        for (int index1 = 0; index1 < jsonDetails.length(); index1++) {
                            Log.d("INJDS ", "INJDS " + jsonDetails);
                            InvoiceDateValues invoiceDateValues = new InvoiceDateValues();

                            invoiceValueJsonObject = jsonDetails.getJSONObject(index1);

                            Log.d("INVOCEVALESE", "INVOCEVALESE" + invoiceValueJsonObject.getString("invoice_id"));
                            String invoiceId = invoiceValueJsonObject.getString("invoice_id");
                            String invoiceDetailsId = invoiceValueJsonObject.getString("invoice_details_id");
                            String invocieService = invoiceValueJsonObject.getString("service");
                            String invoiceAmount = invoiceValueJsonObject.getString("amount");
                            String invoiceDate = invoiceValueJsonObject.getString("invoice_month_day");


                            Log.d("invoiceId", "invoiceId" + invoiceId);
                            invoiceDateValues.setInvoice_id(invoiceId);
                            invoiceDateValues.setInvoice_details_id(invoiceDetailsId);
                            invoiceDateValues.setService(invocieService);
                            invoiceDateValues.setAmount(invoiceAmount);
                            invoiceDateValues.setInvoice_date(invoiceDate);

                            invocieDateList.add(invoiceDateValues);
                        }
                        invoiceListModel.setGetInfo(invocieDateList);
                        invoiceList.add(invoiceListModel);


                    }
                    for(int k=0;k<invoiceList.size();k++) {
                        invoice_view(k);
                    }


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

                Log.d("PETINFO", "PETINFO" + userModel.getId() + "BUMAID" + pet_id);
//
                params.put("user_id", userModel.getId());
                params.put("pet_id", pet_id);


                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
    }



    public void recordAdapter() {

        if(documentList.get(0).getDocuments_id().equalsIgnoreCase("Empty"))
        {
            no_data_items.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            mAdapter = new RecordsAdapter(documentList, getActivity(),(RecordsAdapter.DataFromAdapterToFragment)this);
            mRecyclerView.setAdapter(mAdapter);

        }



    }




    public void add_notes_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.notes_layout, null);

        alertDialog.setView(dialogView);

        dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        TextView title_text = (TextView) dialogView.findViewById(R.id.notes_text);
        title_text.setText("Notes");
        final EditText enter_notes = (EditText) dialogView.findViewById(R.id.enter_notes);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String notes = enter_notes.getText().toString().trim();

                if(notes.equals("") || notes.isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter the notes",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addNotes(getResources().getString(R.string.url_reference) + "pet_notes_creation.php", notes);

                }



            }
        });

        alertDialog.setView(dialogView);


        dialog.show();


    }


    public void addNotes(String url, final String notes) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("FOODPRERES", "FOODPRERES" + s);

                myProgressDialog.hide();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");

                    if (result.equalsIgnoreCase("Success")) {

                        dialog.cancel();



                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.note_success))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog1, int which) {
                                        dialog1.cancel();
                                        clientDetails(getResources().getString(R.string.url_reference) + "pet_details.php");



                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();




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


                Log.d("FOODRESQSERV", "FOODRESQSERV" + userModel.getId() + "PEID  " + pet_id + "NOT " + notes);

                params.put("user_id", userModel.getId());
                params.put("pet_id", pet_id);
                params.put("notes", notes);
                params.put("user_type","business");

                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }


    public void edit_food_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.food_edit_layout, null);

        alertDialog.setView(dialogView);

        dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        TextView title_text = (TextView) dialogView.findViewById(R.id.title);
        final EditText brand_value = (EditText) dialogView.findViewById(R.id.brand_value);
        final EditText protein_value = (EditText) dialogView.findViewById(R.id.protein_value);
        final EditText servings_value = (EditText) dialogView.findViewById(R.id.servings_value);

        title_text.setText("Edit Food Preferences");

        brand_value.setText(petListModel.getBrand());
        protein_value.setText(petListModel.getProtein());
        servings_value.setText(petListModel.getPortion_size());

        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String brandVal = brand_value.getText().toString().trim();
                String protVal = protein_value.getText().toString().trim();
                String serVal = servings_value.getText().toString().trim();


                updateFoodPreference(getResources().getString(R.string.url_reference) + "pet_food_preferences_update.php", brandVal, protVal, serVal);


            }
        });


        alertDialog.setView(dialogView);


        dialog.show();

    }


    public void edit_pet_parent_pop_up_window() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.pet_parent_edit_layout, null);

        alertDialog.setView(dialogView);

        dialog = alertDialog.create();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        TextView title_text = (TextView) dialogView.findViewById(R.id.title);
        final EditText enter_name = (EditText) dialogView.findViewById(R.id.business_name);
        final EditText enter_mobile = (EditText) dialogView.findViewById(R.id.mobile);
        final EditText enter_email = (EditText)dialogView.findViewById(R.id.email);
        final EditText enter_location = (EditText) dialogView.findViewById(R.id.address);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);

        title_text.setText("Edit Pet Profile");

        RadioGroup prefred_radio_group = (RadioGroup) dialogView.findViewById(R.id.prefred_radiogroup);
        RadioButton prefered_text_message = (RadioButton) dialogView.findViewById(R.id.prefered_text_message);
        RadioButton prefred_email = (RadioButton) dialogView.findViewById(R.id.prefred_email);

        prefered_msg = "Text message";
        prefred_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.prefered_text_message:
                        prefered_msg = "Text message";
                        break;

                    case R.id.prefred_email:
                        prefered_msg = "Email";
                        break;

                }


            }
        });

        try {
            enter_name.setText(petListModel.getOwner_name());
            enter_mobile.setText(petListModel.getMobile());
            enter_location.setText(petListModel.getAddress());
            enter_email.setText(petListModel.getEmail());

            if (petListModel.getPreferred_contact().equalsIgnoreCase("Text message")) {
                Log.d("EMAILDASR", "EMAILDASR");
                prefered_text_message.setChecked(true);
                prefered_text_message.setEnabled(true);
            } else if (petListModel.getPreferred_contact().equalsIgnoreCase("Email")) {
                Log.d("TEXRES", "TEXRES");
                prefred_email.setChecked(true);
                prefred_email.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String person_name = enter_name.getText().toString().trim();
                String mobile_number = enter_mobile.getText().toString().trim();
                String address = enter_location.getText().toString().trim();
                String email = enter_email.getText().toString().trim();

                if (person_name.isEmpty() || person_name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the name", Toast.LENGTH_SHORT).show();
                } else if (mobile_number.isEmpty() || mobile_number.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the mobile", Toast.LENGTH_SHORT).show();
                } else if (userModel.getMobile().equals(mobile_number)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.alread_mobile), Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty() || email.equals(""))
                {
                    Toast.makeText(getActivity(),"Please enter the email",Toast.LENGTH_SHORT).show();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                }

                else {
                    updatePetParent(getResources().getString(R.string.url_reference) + "pet_owner_update.php", person_name, mobile_number,email, address, prefered_msg);
                }

            }
        });


        alertDialog.setView(dialogView);


        dialog.show();
    }


    public void updateFoodPreference(String url, final String brandVal, final String proteinVal, final String servingVal) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("FOODPRERES", "FOODPRERES" + s);

                myProgressDialog.hide();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");

                    if (result.equalsIgnoreCase("Success")) {
                        petListModel.setBrand(brandVal);
                        petListModel.setProtein(proteinVal);
                        petListModel.setPortion_size(servingVal);


                        brand_value.setText(petListModel.getBrand());
                        protein_value.setText(petListModel.getProtein());
                        servings_value.setText(petListModel.getPortion_size());


                        dialog.cancel();
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


                Log.d("FOODRESQSERV", "FOODRESQSERV" + owner_id + "PEID  " + pet_id + "brand " + brandVal + "protein " + proteinVal + "SERV " + servingVal);

                params.put("owner_id", owner_id);
                params.put("pet_id", pet_id);
                params.put("brand", brandVal);
                params.put("protein", proteinVal);
                params.put("portion_size", servingVal);
                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }


    public void invoice_view(int Pos) {

        Log.d("INCVIC", "INCVIC");
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.invoice_list_view, null);

        TextView invoice_month = (TextView)view1.findViewById(R.id.head_month_year);
        TextView invoice_total_value = (TextView)view1.findViewById(R.id.head_invoice_name);

        invoice_month.setText(invoiceList.get(Pos).getMonth_year());
        invoice_total_value.setText("$"+invoiceList.get(Pos).getMonth_total());

        invoice_list = (RecyclerView) view1.findViewById(R.id.invoice_list);
        invoice_list.setHasFixedSize(true);

        invoice_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        invoiceAdapter = new InvoiceListAdapterView(invoiceList.get(Pos).getGetInfo(), getActivity());
        invoice_list.setAdapter(invoiceAdapter);

        invoice_main_layout.addView(view1);
    }



    public void updatePetParent(String url, final String person_name, final String mobile_number, final String email, final String address, final String prefered_msg) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);

                myProgressDialog.hide();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");

                    if (result.equalsIgnoreCase("Success")) {
                        petListModel.setOwner_name(person_name);
                        petListModel.setMobile(mobile_number);
                        petListModel.setEmail(email);
                        petListModel.setAddress(address);
                        petListModel.setPreferred_contact(prefered_msg);


                        person_text.setText(petListModel.getOwner_name());
                        mobile_text.setText(petListModel.getMobile());
                        email_text.setText(petListModel.getEmail());
                        location_text.setText(petListModel.getAddress());
                        prefered_contact_text.setText(petListModel.getPreferred_contact());


                        dialog.cancel();
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


                Log.d("RESPCIIEFRA", "RESPCIIEFRA" + owner_id + "NAM " + person_name + "MOB " + mobile_number + "PREC " + prefered_msg + "ADD " + address);

                params.put("owner_id", owner_id);
                params.put("name", person_name);
                params.put("mobile", mobile_number);
                params.put("preferred_contact", prefered_msg);
                params.put("address", address);
                params.put("email",email);
                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }


    @Override
    public void getRecordsinfo(String doc_id) {

        Bundle bundle = new Bundle();
        bundle.putString("pet_id", pet_id);
        bundle.putString("doc_id",doc_id);
        bundle.putParcelableArrayList("doc_list",documentList);
        fragmentCall_mainObj.Fragment_call(new RecordViewDetails(), "recordview", bundle);

    }


    @Override
    public void onClick(View v) {

        if (v == imageView1) {
            showFileChooser();
        }
    }

    private void showFileChooser() {

        startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        updateImageView(getResources().getString(R.string.url_reference) + "pet_photo_edit.php",bitmap);


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    }


                }
                if (requestCode == PICK_IMAGE_REQUEST) {
                    if (getPickImageResultUri(data) != null) {
                        picUri = getPickImageResultUri(data);

                        CropImage.activity(picUri)
                                .setGuidelines(CropImageView.Guidelines.OFF)
                                .setActivityTitle("My Crop")
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .setCropMenuCropButtonTitle("Done")
                                .setMinCropResultSize(400, 400)
                                .setMaxCropResultSize(1500, 1500)
                                .setAspectRatio(1, 1)
                                .start(getContext(),this);




                    }
                }
            }

        }

    }

    private void updateImageView(String url,final Bitmap bitmap)
    {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);


                try {

                    JSONObject jsonObject = new JSONObject(s);



                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    drawable.setCircular(true);
                    imageView1.setImageDrawable(drawable);


                } catch (Exception e) {
                    e.printStackTrace();
                }


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

                image = getStringImage(bitmap);
                Log.d("LODGRES", "LSORED" + pet_id + "IAMGE"+image);
                params.put("pet_id", pet_id);
                params.put("image",image);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }






    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
}
