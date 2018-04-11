package com.purple.kriddr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.purple.kriddr.adapter.AutoCompleteSearchAdapter;
import com.purple.kriddr.com.expand.collection.Genre;
import com.purple.kriddr.com.expand.collection.GenreAdapter;
import com.purple.kriddr.com.expand.collection.PaymentRecivedViewHolder;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.purple.kriddr.model.InvoiceViewListModel;
import com.purple.kriddr.model.PaymentModel;
import com.purple.kriddr.model.PaymentReceivedModel;
import com.purple.kriddr.model.PetDetailsModel;
import com.purple.kriddr.model.PetModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 3/23/2018.
 */

public class InvoiceViewFragment extends Fragment implements PaymentRecivedViewHolder.checkChangeListener {

    View rootView;
    GenFragmentCall_Main fragmentCall_mainObj;
    UserModel userModel;
    ActionBarUtil actionBarUtilObj;
    InvoiceViewListModel invoiceViewListModel;
    PetDetailsModel petDetailsModel;
    LinearLayout invoice_main_layout, invoice_view_layout;
    String tab_sel_status = "1";
    TabLayout tabLayout;
    JSONObject invoiceviewJsonObject, petdetailsJsonObject, invoicedetailsinfoJsonObject;
    ArrayList<InvoiceViewListModel> invoicelist;
    PaymentModel paymentList;
    ArrayList<PaymentReceivedModel> paymentReceivedModel = new ArrayList<>();
    List<Genre> listGen_invoice_dtls;
    List<String> listDataHeader;
    List<HashMap<Integer, List<String>>> hashlist_inv_Hdr;
    ArrayList<GenreAdapter> list_adaapter;
    ArrayList<InvoiceDetailsInfoModel> invocieDetailsInfoList;
    Button btnConfirm;
    AutoCompleteSearchAdapter adapter;
    String name_UsrSearch = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.invoice_form, container, false);

        invoice_main_layout = (LinearLayout) rootView.findViewById(R.id.invoice_main_layout);

        btnConfirm = (Button) rootView.findViewById(R.id.btnConfirm);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        list_adaapter = new ArrayList<>();
        paymentList = new PaymentModel();


        paymentList.setUser_id(userModel.getId());
        paymentList.setStatus("2");


        setupTabIcons();

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.getImgBack().setVisibility(View.INVISIBLE);
        actionBarUtilObj.getImgSettings().setVisibility(View.VISIBLE);
        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.search);

        actionBarUtilObj.setTitle("INVOICE");

        actionBarUtilObj.getImgSettings().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"GETIMGESETT",Toast.LENGTH_SHORT).show();

                if(actionBarUtilObj.getEditText().getVisibility() == View.VISIBLE)
                {
                    paymentReceivedModel.clear();
                    listGen_invoice_dtls.clear();
                    listDataHeader.clear();
                    invocieDetailsInfoList.clear();
                    invoicelist.clear();
                    hashlist_inv_Hdr.clear();
                    list_adaapter.clear();
                    invoice_main_layout.removeAllViews();
                    name_UsrSearch = actionBarUtilObj.getEditText().getText().toString();


                    invoiceList(getResources().getString(R.string.url_reference) + "client_invoice_details.php", tab_sel_status);

//                    actionBarUtilObj.getEditText().addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//
//                        }
//                    });


                }
                 else {
                    actionBarUtilObj.getEditText().setVisibility(View.VISIBLE);
                    actionBarUtilObj.getTitle().setVisibility(View.INVISIBLE);
                }

            }
        });


        tabLayout.getTabAt(0).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int Pos = tab.getPosition();
                switch (Pos) {
                    case 0:
                        paymentReceivedModel.clear();
                        listGen_invoice_dtls.clear();
                        listDataHeader.clear();
                        invocieDetailsInfoList.clear();
                        invoicelist.clear();
                        hashlist_inv_Hdr.clear();
                        list_adaapter.clear();
                        invoice_main_layout.removeAllViews();

                        tab_sel_status = "1";
                        break;
                    case 1:

                        paymentReceivedModel.clear();
                        listGen_invoice_dtls.clear();
                        listDataHeader.clear();
                        invocieDetailsInfoList.clear();
                        invoicelist.clear();
                        hashlist_inv_Hdr.clear();
                        list_adaapter.clear();
                        invoice_main_layout.removeAllViews();

                        tab_sel_status = "2";
                        break;

                }

                if (NetworkConnection.isOnline(getActivity())) {
                    invoiceList(getResources().getString(R.string.url_reference) + "client_invoice_details.php", tab_sel_status);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (NetworkConnection.isOnline(getActivity())) {
            invoiceList(getResources().getString(R.string.url_reference) + "client_invoice_details.php", "1");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }





    public void load_data(String url)
    {


        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("LOADDDATE","LOADDDATE"+s);

                myProgressDialog.hide();

                try
                {

                    JSONObject jsonObject = new JSONObject(s);
                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");
                    if(result.equalsIgnoreCase("Success"))
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.payment_received_confirm))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                                        //  genFragmentCall_mainObj.Fragment_call(new ClientFragment(),"clintback",null);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


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
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();

                String jsonValues = gson.toJson(paymentList);

                Log.d("JSONRESVAVAL","JSONRESVAVAL"+jsonValues);

                final String requestBody = jsonValues.toString();


                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }


            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);


    }




    public void pop_up_window(String message) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.custom_invoicepopup, null);

        alertDialog.setView(dialogView);


        final AlertDialog dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView image = (ImageView) dialogView.findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        TextView textView = (TextView)dialogView.findViewById(R.id.text1);

         textView.setText(message);


        Button button = (Button) dialogView.findViewById(R.id.submit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkConnection.isOnline(getActivity())) {
                    load_data(getResources().getString(R.string.url_reference) + "pet_invoice_status_update.php");
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            }


            }
        });

        dialog.show();
    }






    public void invoiceList(String url, final String StatusFlg) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("INVEIRES", "INVEIRES" + s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("invoices_list");
                    invoicelist = new ArrayList<>();


                    for (int index = 0; index < jsonArray.length(); index++) {
                        invoiceViewListModel = new InvoiceViewListModel();
                        invoiceviewJsonObject = jsonArray.getJSONObject(index);
                        invoiceViewListModel.setMonth_year(invoiceviewJsonObject.getString("month_year"));
                        invoiceViewListModel.setMonth_amount(invoiceviewJsonObject.getString("month_amount"));
                        invoiceViewListModel.setMonth_int(invoiceviewJsonObject.getString("month_int"));
                        invoiceViewListModel.setInvoice_year(invoiceviewJsonObject.getString("invoice_year"));
                        listGen_invoice_dtls = new ArrayList<>();
                        // invoice_view();
                        hashlist_inv_Hdr = new ArrayList<>();
                        JSONArray jsonArray1 = invoiceviewJsonObject.getJSONArray("pet_details");
                        for (int index1 = 0; index1 < jsonArray1.length(); index1++) {
                            petDetailsModel = new PetDetailsModel();
                            petdetailsJsonObject = jsonArray1.getJSONObject(index1);
                            petDetailsModel.setPet_name(petdetailsJsonObject.getString("pet_name"));
                            petDetailsModel.setOwner_name(petdetailsJsonObject.getString("owner_name"));
                            petDetailsModel.setPet_id(petdetailsJsonObject.getString("pet_id"));
                            petDetailsModel.setPet_month_amount(petdetailsJsonObject.getString("pet_month_amount"));
                            if (tab_sel_status.equalsIgnoreCase("1")) {
                                petDetailsModel.setPay_recd_status("pending");
                            } else {
                                petDetailsModel.setPay_recd_status("received");
                            }
                            listDataHeader = new ArrayList<>();
                            listDataHeader.add(petDetailsModel.getPet_name());
                            listDataHeader.add(petDetailsModel.getOwner_name());
                            listDataHeader.add(petDetailsModel.getPet_month_amount());
                            listDataHeader.add(petDetailsModel.getPay_recd_status());
                            listDataHeader.add(invoiceViewListModel.getMonth_int());
                            listDataHeader.add(invoiceViewListModel.getInvoice_year());
                            listDataHeader.add(petDetailsModel.getPet_id()+"//"+petDetailsModel.getOwner_name());
                            HashMap<Integer, List<String>> HdrDtls = new HashMap<>();
                            HdrDtls.put(index1, listDataHeader);
                            hashlist_inv_Hdr.add(HdrDtls);


                            JSONArray jsonDetails = petdetailsJsonObject.getJSONArray("details");

                            invocieDetailsInfoList = new ArrayList<>();

                            for (int index2 = 0; index2 < jsonDetails.length(); index2++) {
                                InvoiceDetailsInfoModel invoiceDetailsInfoModel = new InvoiceDetailsInfoModel();
                                invoicedetailsinfoJsonObject = jsonDetails.getJSONObject(index2);

                                String invoiceId = invoicedetailsinfoJsonObject.getString("invoice_id");
                                String invoiceDetailsId = invoicedetailsinfoJsonObject.getString("invoice_details_id");
                                String invocieService = invoicedetailsinfoJsonObject.getString("service");
                                String invoiceAmount = invoicedetailsinfoJsonObject.getString("amount");
                                String invoiceDate = invoicedetailsinfoJsonObject.getString("invoice_month_day");

                                invoiceDetailsInfoModel.setInvoice_id(invoiceId);
                                invoiceDetailsInfoModel.setInvoice_details_id(invoiceDetailsId);
                                invoiceDetailsInfoModel.setService(invocieService);
                                invoiceDetailsInfoModel.setAmount(invoiceAmount);
                                invoiceDetailsInfoModel.setInvoice_month_day(invoiceDate);

                                invocieDetailsInfoList.add(invoiceDetailsInfoModel);

                            }

                            //listDataChild.put(index1,invocieDetailsInfoList);

                            if (StatusFlg.equalsIgnoreCase("1")) {
                                InvoiceDetailsInfoModel paymentModel = new InvoiceDetailsInfoModel();
                                paymentModel.setPaymentRecd(false);

                                paymentModel.setCreateFooterViewPaymentRecd(true);
                                invocieDetailsInfoList.add(paymentModel);
                            }
                            getGenre(listDataHeader, index1, invocieDetailsInfoList);


                            petDetailsModel.setGetInfo(invocieDetailsInfoList);


                        }

                        month_View(invoiceViewListModel.getMonth_year());
                        AddCardView();

                        invoicelist.add(invoiceViewListModel);


                    }

                    Log.d("INVOCIELSITSIZE", "INVOCIELSITSIZE" + invoicelist.size());

                  /*  for(int k=0;k<invoicelist.size();k++) {
                        invoice_view(k);
                    }*/


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

                Log.d("USERID", "USERID" + userModel.getId() + "SEARC "+name_UsrSearch);
                params.put("user_id", userModel.getId());
                params.put("status", StatusFlg);
                params.put("search",name_UsrSearch);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);

    }


    private void setupTabIcons() {

        View tabOne = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn, null);
        TextView txtTab1 = (TextView) tabOne.findViewById(R.id.tab);
        txtTab1.setText("Payment Pending");
        TabLayout.Tab tab = tabLayout.newTab();
        tabLayout.addTab(tab);
        tab.setCustomView(tabOne);
        tabOne.setBackgroundResource(R.drawable.rounded_cornor_tab);


        View tabTwo = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn, null);
        TextView txtTab2 = (TextView) tabTwo.findViewById(R.id.tab);
        txtTab2.setText("Payment Received");
        TabLayout.Tab tab1 = tabLayout.newTab();
        tabLayout.addTab(tab1);
        tab1.setCustomView(tabTwo);
        tabTwo.setBackgroundResource(R.drawable.rounded_cornor_tab_right);

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


    public void getGenre(List<String> dtlHdr, int Index, List<InvoiceDetailsInfoModel> modelList) {
        listGen_invoice_dtls.add(new Genre(dtlHdr.get(0), Index, dtlHdr, modelList));
    }

    public void month_View(String month_invoice) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.invoice_month_text, null);
        TextView month_title = (TextView) view.findViewById(R.id.month_text);
        month_title.setText(month_invoice);
        invoice_main_layout.addView(view);
    }

    public void AddCardView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.invoice_card_view, null);
        invoice_main_layout.addView(view);
        RecyclerView exp_inv_recyclerView = (RecyclerView) view.findViewById(R.id.invoice_view_layout);
        //exp_inv_recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        exp_inv_recyclerView.setLayoutManager(layoutManager);
        GenreAdapter adapter = new GenreAdapter(list_adaapter.size(), hashlist_inv_Hdr, listGen_invoice_dtls, getContext(), this,tab_sel_status);
        //  adapter.setHasStableIds(true);
        exp_inv_recyclerView.setAdapter(adapter);
        list_adaapter.add(adapter);

       /* adapter.setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
               group.getTitle()
                adapter.getGroups();
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {

            }
        });*/
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="Please confirm that you've received payment from ";
                String Owner_STR = "";
                for(int index=0; index < paymentReceivedModel.size(); index++)
                {
                    PaymentReceivedModel paymentReceivedModelObj=paymentReceivedModel.get(index);
                    ArrayList<String> pets_list=paymentReceivedModelObj.getPet_id();

                    for(String pets:pets_list){
                        if(!Owner_STR.equalsIgnoreCase(""))
                        {
                            Owner_STR=Owner_STR+", ";
                        }

                        String pets_split[]=pets.split("//");
                        Owner_STR=Owner_STR+pets_split[1];
                    }
                }
                message=message+Owner_STR;
                pop_up_window(message);




            }
        });

    }

    @Override
    public void checkChange(int AdapterPos, boolean valueint, int HDRINDEX) {


        View view = invoice_main_layout.getChildAt((AdapterPos * 2) + 1);
        RecyclerView recyclerView = view.findViewById(R.id.invoice_view_layout);
        GenreAdapter adapter = list_adaapter.get(AdapterPos);
        // adapter.getGroupCollection().size();
        /*adapter.getGroupCollection().get()*/
        //Toast.makeText(getContext(),"Size:"+adapter.getGroupCollection().size(),Toast.LENGTH_SHORT).show();


        ArrayList<HashMap<Integer, List<String>>> hdrDtls = new ArrayList<>(adapter.getHdrDtls());


        listGen_invoice_dtls = new ArrayList<>();
        for (int i = 0; i < hdrDtls.size(); i++) {
            ArrayList<InvoiceDetailsInfoModel> invoiceDetailsInfoModel = new ArrayList<>(adapter.getGroupCollection().get(i).getItems());
            HashMap<Integer, List<String>> dtls = hdrDtls.get(i);
            ArrayList<String> TitleList = new ArrayList<>(dtls.get(i));
            //TitleList.remove(3);
            InvoiceDetailsInfoModel obj_invoice = invoiceDetailsInfoModel.get(invoiceDetailsInfoModel.size() - 1);
            //Toast.makeText(context,"Hello"+TitleList.get(0),Toast.LENGTH_SHORT).show();
            ArrayList<String> Pet_ID = new ArrayList<>();
            PaymentReceivedModel paymentReceived = null;
            boolean isModelPresent = false;
            int List_INDEX = 0;
            if (i == HDRINDEX) {

                if (paymentReceivedModel.size() > 0) {

                    for (int index = 0; index < paymentReceivedModel.size(); index++) {
                        paymentReceived = paymentReceivedModel.get(index);

                        if (paymentReceived != null) {
                            if (paymentReceived.getMonth_id().equalsIgnoreCase(TitleList.get(4))) {
                                Pet_ID = paymentReceived.getPet_id();
                                isModelPresent = true;
                                List_INDEX=index;
                                break;
                            }
                        }

                    }
                }
                if (!isModelPresent) {
                    btnConfirm.setVisibility(View.VISIBLE);
                    paymentReceived = new PaymentReceivedModel();
                    paymentReceived.setMonth_id(TitleList.get(4));
                    paymentReceived.setYear(TitleList.get(5));
                    paymentReceivedModel.add(paymentReceived);

                }


                String pet_id = TitleList.get(6);
                if (valueint) {
                    //  TitleList.remove(3);
                    Pet_ID.add(pet_id);
                    TitleList.set(3, "received");
                    obj_invoice.setPaymentRecd(true);

                } else {
                    if (Pet_ID.size() > 0) {
                        for (int j = 0; j < Pet_ID.size(); j++) {
                            String  Temp=Pet_ID.get(j);
                            if (pet_id.equalsIgnoreCase(Temp)) {
                                Pet_ID.remove(j);
                                if(Pet_ID.size()==0){
                                    paymentReceivedModel.remove(List_INDEX);
                                    if(paymentReceivedModel.size()==0){
                                        btnConfirm.setVisibility(View.GONE);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    //  TitleList.remove(3);
                    TitleList.set(3, "pending");
                    //notifyItemChanged(HDRINDEX);
                    obj_invoice.setPaymentRecd(false);
                }
                paymentReceived.setPet_id(Pet_ID);
                dtls.put(HDRINDEX, TitleList);
                hdrDtls.set(HDRINDEX, dtls);
                invoiceDetailsInfoModel.set(invoiceDetailsInfoModel.size() - 1, obj_invoice);

            }
            paymentList.setPay_recd(paymentReceivedModel);
            getGenre(TitleList, i, invoiceDetailsInfoModel);
        }

        //exp_inv_recyclerView.setHasFixedSize(true);
        recyclerView.removeAllViewsInLayout();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        GenreAdapter Updateadapter = new GenreAdapter(AdapterPos, hdrDtls, listGen_invoice_dtls, getContext(), this,tab_sel_status);
        // adapter.setHasStableIds(true);
        recyclerView.setAdapter(Updateadapter);
        recyclerView.requestLayout();
        list_adaapter.set(AdapterPos, Updateadapter);

 /*       //hdrDtls.remove(HDRINDEX);
        //dtls.remove(HDRINDEX);
       *//* dtls.put(HDRINDEX,TitleList);
        hdrDtls.set(HDRINDEX,dtls);
        adapter.getHdrDtls().clear();
       *//* //notifyItemChanged(HDRINDEX);

     //   adapter.notifyItemChanged(HDRINDEX);

//        adapter.getHdrDtls().addAll(hdrDtls);
        adapter.getHdrDtls().get(HDRINDEX).put(HDRINDEX,TitleList);
        adapter.getGroupCollection().get(HDRINDEX).getItems().set(invoiceDetailsInfoModel.size()-1,obj_invoice);
        list_adaapter.set(AdapterPos,adapter);
        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);
   //     adapter.notifyDataSetChanged();

     //    adapter.notify();*/
    }
}