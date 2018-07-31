package com.purple.kriddr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

import android.telephony.PhoneNumberUtils;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pf-05 on 2/19/2018.
 */

public class ClientViewDetailsFragment extends Fragment implements RecordsAdapter.DataFromAdapterToFragment, View.OnClickListener {

    View rootView;
    TextView profile_name, profile_dob, person_text, mobile_text, email_text, location_text, prefered_contact_text, brand_value, protein_value, servings_value, no_data_items;
    ImageView imageView1, edit_petparent, food_edit, add_Notes, img_plus_btn, img_next_btn;
    JSONObject petJsonObject, invoiceJsonObject, documentJsonObject, notesJsonObject, invoiceValueJsonObject;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    String pet_id, owner_id;
    String Old_Dob = "";
    RecyclerView mRecyclerView, mRecyclerView1, invoice_list;
    RecyclerView.LayoutManager mLayoutManager;
    RecordsAdapter mAdapter;
    String upcoming_date;
    String myFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    NotesAdapter notesAdapter;
    InvoiceListAdapterView invoiceAdapter;
    Button add_invoice_reqAccess;
    String prefered_msg;
    AlertDialog dialog;
    PetListModel petListModel;
    DocumentModel documentModel;
    NotesModel notesModel;
    final Calendar myCalendar1 = Calendar.getInstance();
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
    ImageView img_edt_parnt_dtls;
    String image = "";

    InvocieListAdapter invocieListAdapter;
    int Share_STATUS;
    int Profile_STATUS;
//    ListView invoice_list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.client_profile_view, container, false);


        ((KridderNavigationActivity) getActivity()).setNavigationVisibility(false);


        Bundle bundle_args = getArguments();
        if (bundle_args != null) {
            pet_id = bundle_args.getString("pet_id", null);
            Share_STATUS = bundle_args.getInt("share_status");
            Profile_STATUS = bundle_args.getInt("profile_status");

        }


        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);


        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                fragmentCall_mainObj.Fragment_call(new ClientFragment(), "fragclient", null);
            }
        });

        actionBarUtilObj.setTitle("BACK");
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentCall_mainObj.Fragment_call(new ClientFragment(), "fragclient", null);

            }
        });


        profile_name = (TextView) rootView.findViewById(R.id.profile_name);
        email_text = (TextView) rootView.findViewById(R.id.email_text);
        profile_dob = (TextView) rootView.findViewById(R.id.profile_dob);
        person_text = (TextView) rootView.findViewById(R.id.person_text);
        mobile_text = (TextView) rootView.findViewById(R.id.mobile_text);
        img_edt_parnt_dtls = (ImageView) rootView.findViewById(R.id.img_edt_parnt_dtls);
        location_text = (TextView) rootView.findViewById(R.id.location_text);
        prefered_contact_text = (TextView) rootView.findViewById(R.id.prefered_contact_text);
        brand_value = (TextView) rootView.findViewById(R.id.brand_value);
        protein_value = (TextView) rootView.findViewById(R.id.protein_value);
        servings_value = (TextView) rootView.findViewById(R.id.servings_value);
        see_more_layout = (RelativeLayout) rootView.findViewById(R.id.see_more_layout);
        add_invoice_reqAccess = (Button) rootView.findViewById(R.id.add_invoice);
        img_next_btn = (ImageView) rootView.findViewById(R.id.img_next_btn);
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);

        food_edit = (ImageView) rootView.findViewById(R.id.food_edit);
        img_plus_btn = (ImageView) rootView.findViewById(R.id.img_plus_btn);
        add_Notes = (ImageView) rootView.findViewById(R.id.add_Notes);
        no_data_items = (TextView) rootView.findViewById(R.id.no_data_items);


        invoice_main_layout = (LinearLayout) rootView.findViewById(R.id.invoice_main_layout);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lst_view);


        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.notes_list);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        img_edt_parnt_dtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_pet_parent_pop_up_window();
            }
        });
        mobile_text.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
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
                bundle.putString("owner_id", owner_id);

                bundle.putInt("profile_status", Profile_STATUS);


                fragmentCall_mainObj.Fragment_call(new RecordCreationFragment(), "addrecord", bundle);

            }
        });

        add_invoice_reqAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Share_STATUS == PetClientListFragment.SHARE_STATUS.ADDED.ordinal()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pet_id", pet_id);
                    bundle.putString("pet_photo", petListModel.getPhoto());
                    fragmentCall_mainObj.Fragment_call(new AddInvoiceFragment(), "addinvoice", bundle);
                }
                /*else if ((Profile_STATUS==PetClientListFragment.PROFILE_STATUS.NOT_VERIFIED.ordinal()) && (Share_STATUS == PetClientListFragment.SHARE_STATUS.NOT_ADDED.ordinal())) {
                    req_Access(getResources().getString(R.string.url_reference) + "pet_profile_share.php");
                }*/
                else {
                    req_Access(getResources().getString(R.string.url_reference) + "pet_profile_share.php");
                }

            }
        });

        if (Share_STATUS == PetClientListFragment.SHARE_STATUS.ADDED.ordinal()) {
            add_invoice_reqAccess.setText("Add Invoice");
            imageView1.setOnClickListener(this);

        } else if (Share_STATUS == PetClientListFragment.SHARE_STATUS.PENDING.ordinal()) {
            add_invoice_reqAccess.setText("Waiting for approval");
            add_invoice_reqAccess.setEnabled(false);

            food_edit.setVisibility(View.INVISIBLE);
            add_Notes.setVisibility(View.INVISIBLE);
            img_plus_btn.setVisibility(View.INVISIBLE);
            img_edt_parnt_dtls.setVisibility(View.INVISIBLE);

        } else if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
            add_invoice_reqAccess.setText("Request Access");

            food_edit.setVisibility(View.INVISIBLE);
            add_Notes.setVisibility(View.INVISIBLE);
            img_plus_btn.setVisibility(View.INVISIBLE);
            img_edt_parnt_dtls.setVisibility(View.INVISIBLE);

        } else {
            add_invoice_reqAccess.setText("Get Access");

            food_edit.setVisibility(View.INVISIBLE);
            add_Notes.setVisibility(View.INVISIBLE);
            img_plus_btn.setVisibility(View.INVISIBLE);
            img_edt_parnt_dtls.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }

    public void req_Access(String Url) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
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
                        if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            Toast.makeText(getContext(), "Your request to access the Pet Profile has been sent successfully to the Pet Parent for approval. ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                        }
                        fragmentCall_mainObj.Fragment_call(new ClientFragment(), "fragclient", null);
                    }
                } catch (Exception e) {

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
                Map<String, String> params = new HashMap
                        <String, String>();


                //      Log.d("RESPCIIEFRA", "RESPCIIEFRA" + owner_id + "NAM " + person_name + "MOB " + mobile_number + "PREC " + prefered_msg + "ADD " + address);
                params.put("user_id", userModel.getId());
                params.put("owner_id", owner_id);
                params.put("pet_id", pet_id);
                params.put("user_type", "business");

                if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.NOT_VERIFIED.ordinal()) {
                    params.put("key", "get_access");
                } else
                    params.put("key", "req");

                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }

    public void handle_BackKey() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    fragmentCall_mainObj.Fragment_call(new ClientFragment(), "fragclient", null);
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
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("CLIENTSDETAIL", "CLIENTSDETAIL" + s);
                myProgressDialog.hide();

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
                    petListModel.setD_date(petJsonObject.getString("d_date"));


                    profile_name.setText(petListModel.getPet_name());
                    profile_dob.setText("DOB: " + petListModel.getDob());
                    person_text.setText(petListModel.getOwner_name());
                    /*PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber pn = pnu.parse(petListModel.getMobile(), "US");
                    String pnE164 = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);*/


                    mobile_text.setText(petListModel.getMobile());
                    email_text.setText(petListModel.getEmail());
                    location_text.setText(petListModel.getAddress());
                    if (petListModel.getPreferred_contact().equalsIgnoreCase("text")) {
                        prefered_contact_text.setText("Text message");
                    } else
                        prefered_contact_text.setText("Email");
                    brand_value.setText(petListModel.getBrand());
                    protein_value.setText(petListModel.getProtein());
                    servings_value.setText(petListModel.getPortion_size());


                    if (!petListModel.getPhoto().equals("")) {
                        Glide.with(getActivity()).load(petListModel.getPhoto()).transform(new CircleTransform(getActivity())).into(imageView1);

                    } else {
                        Log.d("NOPATE", "NOPATE");
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
                 /*   LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    manager.scrollToPositionWithOffset(documentList.size() - 1, 0);*/


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
                                bundle.putString("owner_id", owner_id);
                                bundle.putInt("profile_status", Profile_STATUS);
                                fragmentCall_mainObj.Fragment_call(new NotesFragment(), "notes_view", bundle);

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
                    for (int k = 0; k < invoiceList.size(); k++) {
                        invoice_view(k);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                myProgressDialog.hide();

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
        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);
    }


    public void recordAdapter() {

        if (documentList.get(0).getDocuments_id().equalsIgnoreCase("Empty")) {
            no_data_items.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            //   List<DocumentModel> TempDocList = new ArrayList<>(documentList);

            Collections.sort(documentList, new Comparator<DocumentModel>() {
                @Override
                public int compare(DocumentModel p1, DocumentModel p2) {
                    return Integer.parseInt(p2.getDocuments_id()) - Integer.parseInt(p1.getDocuments_id()); // Descending
                }

            });

           /* Collections.sort(TempDocList, new Comparator<DocumentModel>() {
                public int compare(DocumentModel s1, DocumentModel s2) {
                    // Write your logic here.

                    return (s1.getDocuments_id().compareToIgnoreCase(s2.getDocuments_id()));
                }
            });*/
            //setRecordsAdapter(TempDocList);
            mAdapter = new RecordsAdapter(documentList, getActivity(), (RecordsAdapter.DataFromAdapterToFragment) this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.scrollToPosition(0);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("I","ACTIVITYCREATED_CALLED"+Profile_STATUS);
        Fragment mContent;
        if (savedInstanceState != null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "CLIDTL");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, mContent, "cli_dtl");
            fragmentTransaction.addToBackStack("cli_dtl");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //To save the instance state
        Log.e("I","OnSAVE_INSTANCE_CLIENTVW");
        getActivity().getSupportFragmentManager().putFragment(outState, "CLIDTL", this);
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

                if (notes.equals("") || notes.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter the notes", Toast.LENGTH_SHORT).show();
                } else {
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
                params.put("user_type", "business");
                if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {

                } else {
                    params.put("key", "unclaimed");
                    params.put("owner_id", owner_id);
                }
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
                boolean isChanged = false;

                if (brandVal.trim().equalsIgnoreCase(petListModel.getBrand().trim())) {
                    if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                        brandVal = "old";
                    }
                } else {
                    isChanged = true;
                }
                if (protVal.trim().equalsIgnoreCase(petListModel.getProtein().trim())) {
                    if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                        protVal = "old";
                    }

                } else {
                    isChanged = true;
                }
                if (serVal.trim().equalsIgnoreCase(petListModel.getPortion_size().trim())) {
                    if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                        serVal = "old";
                    }

                } else {
                    isChanged = true;
                }
                if (isChanged) {

                    if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                        updateFoodPreference(getResources().getString(R.string.url_reference) + "temp_pet_food_preferences_update.php", brandVal, protVal, serVal);
                    } else {
                        updateFoodPreference(getResources().getString(R.string.url_reference) + "pet_food_preferences_update.php", brandVal, protVal, serVal);
                    }
                } else {
                    Toast.makeText(getContext(), "Nothing you are changed", Toast.LENGTH_LONG).show();
                }

            }
        });


        alertDialog.setView(dialogView);


        dialog.show();

    }


    /* public void edit_pet_parent_pop_up_window() {

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
                     Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
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
     }*/
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
        final EditText enter_email = (EditText) dialogView.findViewById(R.id.email);
        final EditText enter_location = (EditText) dialogView.findViewById(R.id.address);
        final EditText edt_Dob = (EditText) dialogView.findViewById(R.id.dob);
        Button submit_btn = (Button) dialogView.findViewById(R.id.submit_btn);
        edt_Dob.setInputType(InputType.TYPE_NULL);
        title_text.setText("Edit Profile");
        RadioGroup prefred_radio_group = (RadioGroup) dialogView.findViewById(R.id.prefred_radiogroup);
        final RadioButton prefered_text_message = (RadioButton) dialogView.findViewById(R.id.prefered_text_message);
        String formattedDate;
        if (petListModel.getD_date().trim().equalsIgnoreCase("")) {
            edt_Dob.setText("");
            Old_Dob = "";
        } else {
            try {
           //     DateFormat originalFormat = new SimpleDateFormat("dd,MMMM,yyyy", Locale.ENGLISH);
             //   Date date = originalFormat.parse(petListModel.getD_date());
                formattedDate = petListModel.getD_date();  // 20120821
                Log.e("Kriddr", "formatted Date" + formattedDate);
                edt_Dob.setText(formattedDate);
                Old_Dob = formattedDate;

            } catch (Exception e) {
                Log.e("Kriddr Error", "Message " + e.getMessage());
                Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        final RadioButton prefred_email = (RadioButton) dialogView.findViewById(R.id.prefred_email);
        prefered_msg = "Text";
        DatePickerDialog.OnDateSetListener pickup_date;

        pickup_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.v("date", monthOfYear + "");


                updatePickupDate(edt_Dob);

            }

        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), pickup_date, myCalendar1
                .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                myCalendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        edt_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.show();


                updatePickupDate(edt_Dob);
            }
        });
        prefred_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.prefered_text_message:
                        prefered_msg = "Text";
                        break;

                    case R.id.prefred_email:
                        prefered_msg = "Email";
                        break;

                }


            }
        });

        try {

            enter_location.setText(petListModel.getAddress());
            enter_email.setText(petListModel.getEmail());
            // edt_Dob.setText(petListModel.getD_date());
            if (petListModel.getPreferred_contact().equalsIgnoreCase("Text")) {
                Log.d("EMAILDASR", "EMAILDASR");
                prefered_text_message.setChecked(true);
                prefered_text_message.setEnabled(true);
                prefered_msg="text";
            } else if (petListModel.getPreferred_contact().equalsIgnoreCase("Email")) {
                Log.d("TEXRES", "TEXRES");
                prefred_email.setChecked(true);
                prefred_email.setEnabled(true);
                prefered_msg="email";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dob = edt_Dob.getText().toString().trim();
                String address = enter_location.getText().toString().trim();
                String email = enter_email.getText().toString().trim();
                boolean isChanged=false;
                if (email.isEmpty() || email.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the email", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                } else {

                    if (email.trim().equalsIgnoreCase(petListModel.getEmail().trim())) {
                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            email = "old";
                        }
                    }
                    else{
                        isChanged=true;
                    }
                    if (address.trim().equalsIgnoreCase(petListModel.getAddress().trim())) {
                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            address = "old";
                        }
                    }
                    else{
                        isChanged=true;
                    }
                    if (prefered_msg.trim().equalsIgnoreCase(petListModel.getPreferred_contact().trim())) {
                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            prefered_msg = "old";
                        }
                    }
                    else {
                        isChanged=true;
                    }
                    if (Old_Dob.trim().equalsIgnoreCase(dob.trim())) {
                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            upcoming_date = "old";
                        }
                        else{
                            upcoming_date=dob;
                        }

                    } else {
                        if (dob.trim().equalsIgnoreCase("")) {
                            upcoming_date = "";
                            isChanged=true;
                        } else {
                            isChanged=true;
                            //set_upcomig_date(dob);
                            upcoming_date=dob;
                            /*
                            try {


*/
/*
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            //  DateFormat originalFormat = new SimpleDateFormat("dd,MMMM,yyyy", Locale.ENGLISH);
                            Date date = df.parse(dob);
                            upcoming_date = df.format(date);  // 20120821
*//*

                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                                Date date = sdf.parse(dob);
                                upcoming_date = df.format(date);  // 20120821

                                //    Log.e("Kriddr", "formatted Date" + formattedDate);
                            } catch (Exception e) {

                            }
*/
                        }
                    }
                    if (!isChanged) {
                        Toast.makeText(getContext(), "Nothing you are changed", Toast.LENGTH_LONG).show();
                    } else {
                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {
                            updatePetParent(getResources().getString(R.string.url_reference) + "temp_owner_pet_update.php", email, address, prefered_msg, upcoming_date);
                        }
                        else {
                            updatePetParent(getResources().getString(R.string.url_reference) + "unclaimed_pet_owner_details_update.php", email, address, prefered_msg, upcoming_date);
                        }
                    }
                }
            }

        });


        alertDialog.setView(dialogView);


        dialog.show();
    }

    public void set_upcomig_date(String dob){
        try {


/*
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            //  DateFormat originalFormat = new SimpleDateFormat("dd,MMMM,yyyy", Locale.ENGLISH);
                            Date date = df.parse(dob);
                            upcoming_date = df.format(date);  // 20120821
*/
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date date = sdf.parse(dob);
            upcoming_date = df.format(date);  // 20120821

            //    Log.e("Kriddr", "formatted Date" + formattedDate);
        } catch (Exception e) {

        }

    }

    public void updatePickupDate(EditText dob) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));

        if (myCalendar1.getTime().after(cal.getTime())) {
            Toast.makeText(getActivity(), "Please select valid date", Toast.LENGTH_SHORT).show();
        }


        dob.setText(sdf.format(myCalendar1.getTime()));

        //  PickupDate_Str=sdf.format(myCalendar1.getTime());
        String pick_date = sdf.format(myCalendar1.getTime()) + "";


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        upcoming_date = df.format(myCalendar1.getTime());


    }

    public void updateFoodPreference(String url,final String brandVal,final String proteinVal,final String servingVal) {
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
                        if(!(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal())){
                        petListModel.setBrand(brandVal);
                        petListModel.setProtein(proteinVal);
                        petListModel.setPortion_size(servingVal);


                        brand_value.setText(petListModel.getBrand());
                        protein_value.setText(petListModel.getProtein());
                        servings_value.setText(petListModel.getPortion_size());

                            Toast.makeText(getContext(), ""+result, Toast.LENGTH_LONG).show();
                        }
                        else

                        Toast.makeText(getContext(), "Your request to edit the Pet Food Preference has been sent successfully to the Pet Parent for approval. ", Toast.LENGTH_LONG).show();

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
                Toast.makeText(getContext(), "Error :" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                Log.d("FOODRESQSERV", "FOODRESQSERV" + owner_id + "PEID  " + pet_id + "brand " + brandVal + "protein " + proteinVal + "SERV " + servingVal);
                params.put("user_id", userModel.getId());
                params.put("owner_id", owner_id);
                params.put("pet_id", pet_id);
                params.put("brand", brandVal);
                params.put("protein", proteinVal);
                params.put("portion_size", servingVal);
                if (Profile_STATUS == PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()) {

                } else {
                    params.put("key", "unclaimed");
                }
                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }


    public void invoice_view(int Pos) {

        Log.d("INCVIC", "INCVIC");
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.invoice_list_view, null);

        TextView invoice_month = (TextView) view1.findViewById(R.id.head_month_year);
        TextView invoice_total_value = (TextView) view1.findViewById(R.id.head_invoice_name);

        invoice_month.setText(invoiceList.get(Pos).getMonth_year());
        invoice_total_value.setText("$" + invoiceList.get(Pos).getMonth_total());

        invoice_list = (RecyclerView) view1.findViewById(R.id.invoice_list);
        invoice_list.setHasFixedSize(true);

        invoice_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        invoiceAdapter = new InvoiceListAdapterView(invoiceList.get(Pos).getGetInfo(), getActivity());
        invoice_list.setAdapter(invoiceAdapter);

        invoice_main_layout.addView(view1);
    }


    public void updatePetParent(String url, final String email, final String address, final String prefered_msg, final String DOB) {
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

                        if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal())
                        {
                            Toast.makeText(getContext(), "Your request to edit the Pet Parent details has been sent successfully to the Pet Parent for approval. ", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(), ""+result, Toast.LENGTH_LONG).show();
                            petListModel.setEmail(email);
                            petListModel.setAddress(address);

                            petListModel.setPreferred_contact(prefered_msg);
                            petListModel.setD_date(DOB);

                            email_text.setText(petListModel.getEmail());
                            location_text.setText(petListModel.getAddress());
                            if(prefered_msg.equalsIgnoreCase("text")){
                             prefered_contact_text.setText("Text message");
                            }
                            else
                            prefered_contact_text.setText("Email");

                           // DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                            Date date = sdf.parse(DOB);
                         //   upcoming_date = df.format(date);

                            DateFormat originalFormat = new SimpleDateFormat("dd,MMMM,yyyy", Locale.ENGLISH);
                           // Date date = originalFormat.parse(petListModel.getD_date());
                            String formatted_date[]=originalFormat.format(date).split(",");
                            profile_dob.setText(formatted_date[1]+","+formatted_date[2]);  // 20120821

                            /*Log.e("Kriddr", "formatted Date" + formattedDate);
                            edt_Dob.setText(formattedDate);
                            Old_Dob = formattedDate;*/
                        }


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


                //      Log.d("RESPCIIEFRA", "RESPCIIEFRA" + owner_id + "NAM " + person_name + "MOB " + mobile_number + "PREC " + prefered_msg + "ADD " + address);
                params.put("user_id", userModel.getId());
                params.put("owner_id", owner_id);
                params.put("pet_id", pet_id);
                params.put("dob", DOB);
                params.put("preferred_contact", prefered_msg);
                params.put("address", address);
                params.put("email", email);
                if(Profile_STATUS==PetClientListFragment.PROFILE_STATUS.VERIFIED.ordinal()){

                }
                else{
                    params.put("key","unclaimed");
                }
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
        bundle.putString("doc_id", doc_id);
        bundle.putParcelableArrayList("doc_list", documentList);
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


                        updateImageView(getResources().getString(R.string.url_reference) + "pet_photo_edit.php", bitmap);


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
                                .start(getContext(), this);


                    }
                }
            }

        }

    }

    private void updateImageView(String url, final Bitmap bitmap) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LISTOFCLIENTS", "LISTOFCLIENTS" + s);
                myProgressDialog.hide();

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
                myProgressDialog.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                image = getStringImage(bitmap);
                Log.d("LODGRES", "LSORED" + pet_id + "IAMGE" + image);
                params.put("pet_id", pet_id);
                params.put("image", image);
                return params;
            }

        };
        myProgressDialog.show();
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

    public void updatePickupDate() {


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
