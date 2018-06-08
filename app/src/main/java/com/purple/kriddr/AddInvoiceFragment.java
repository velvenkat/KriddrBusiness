package com.purple.kriddr;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.InvoiceModel;
import com.purple.kriddr.model.InvoiceValueModel;
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
 * Created by pf-05 on 2/25/2018.
 */

public class AddInvoiceFragment extends Fragment {

    View rootView;
    Button next_button;
    Spinner category_service;
    TextView add_service,total_val;
    static LinearLayout service_layout;
    int index = 0;
    ArrayList<String> invoiceserviceIdList = new ArrayList<>();
    ArrayList<String> invoiceserviceNameList = new ArrayList<>();
    String invoice_service_id = "", invoice_service_name = "";
    ArrayAdapter<String> invoice_adapter;
    ActionBarUtil actionBarUtilObj;
    InvoiceModel flower = new InvoiceModel();
    GenFragmentCall_Main fragmentCall_mainObj;
    EditText price_value;
    ImageView imageView1;
    UserModel userModel;
    PetListModel petListModel;
    String pet_id = "" ,pet_photo = "";
    int INVOICE_ADDED = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.invoice_request, container, false);


        Bundle bundle_args=getArguments();
        if(bundle_args!=null) {
            pet_id = bundle_args.getString("pet_id", null);
            pet_photo = bundle_args.getString("pet_photo",null);
            flower.setPet_id(pet_id);
            flower.setUser_id(userModel.getId());
        }

        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        next_button = (Button) rootView.findViewById(R.id.next_button);
        service_layout = (LinearLayout) rootView.findViewById(R.id.service_layout);
        add_service = (TextView) rootView.findViewById(R.id.add_service);
        total_val = (TextView)rootView.findViewById(R.id.total_val);



        if(!pet_photo.equals(""))
        {
            Glide.with(getActivity()).load(pet_photo).transform(new CircleTransform(getActivity())).into(imageView1);
        }
        else
        {
            Glide.with(getActivity()).load(R.drawable.defaultpetphoto).transform(new CircleTransform(getActivity())).into(imageView1);
        }



        if (NetworkConnection.isOnline(getActivity())) {
            invoiceserviceDetails(getResources().getString(R.string.url_reference) + "master_data.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        INVOICE_ADDED++;
        add_invoice_service();


        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                INVOICE_ADDED++;
                add_invoice_service();

            }
        });


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String invoice_select = "", invoice_value = "";

                for (int index = 0; index < flower.getServices().size(); index++)
                {
                    invoice_select = flower.getServices().get(index).getInvoice_service_id();
                    invoice_value = flower.getServices().get(index).getAmount();
                }

                if (invoice_select.equalsIgnoreCase("Select") || invoice_value.equals(""))
                {
                    Toast.makeText(getActivity(), "Please choose the valid data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Bundle bundle= new Bundle();
                    bundle.putString("pet_photo",pet_photo);
                    bundle.putParcelable("flower_model", flower);
                    fragmentCall_mainObj.Fragment_call(new AddInvoiceCommentsFragment(), "addinvoicecomments", bundle);

                }

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }

    public void setAdapter() {
        invoice_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, invoiceserviceNameList);
        invoice_adapter.setDropDownViewResource(R.layout.simple_spinner_contact);
        category_service.setAdapter(invoice_adapter);
    }

    public void add_invoice_service() {
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.invoice_fields, null);
        category_service = (Spinner) view1.findViewById(R.id.category_service);
        price_value = (EditText) view1.findViewById(R.id.enter_value);

        setAdapter();
        price_value.setTag(INVOICE_ADDED);
        category_service.setTag(INVOICE_ADDED);
        category_service.setOnItemSelectedListener(new SpinnerSelectedListener(INVOICE_ADDED));
        price_value.addTextChangedListener(new TextChangeListener(price_value, INVOICE_ADDED));



        service_layout.addView(view1);
    }

    class SpinnerSelectedListener implements  AdapterView.OnItemSelectedListener{
        int tag_PosVal;
        public SpinnerSelectedListener(int TagValue){
            tag_PosVal=TagValue;

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            String inv_id = "";

            inv_id = invoiceserviceIdList.get(i).toString();

            boolean isnotExist=true;
            for (int index = 0; index < flower.getServices().size(); index++) {
                String invoiceid = flower.getServices().get(index).getInvoice_service_id();

                if (invoiceid.equalsIgnoreCase(inv_id) && !(inv_id.equalsIgnoreCase("Select"))) {
                    isnotExist = false;
                    ((Spinner) adapterView.findViewById(R.id.category_service)).setSelection(0);
                    Toast.makeText(getActivity(), "This service already choosed", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(isnotExist) {
                if (flower.getServices().size() >= tag_PosVal + 1) {
                   flower.getServices().get(tag_PosVal).setInvoice_service_id(inv_id);
                } else {
                    InvoiceValueModel invoice = new InvoiceValueModel();
                    invoice.setInvoice_service_id(inv_id);
                    flower.getServices().add(invoice);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class TextChangeListener implements TextWatcher {
        int tag_PosVal;
        EditText edtTxt_price;
        boolean isEdited = false;

        public TextChangeListener(EditText _editText, int tagValue) {
            tag_PosVal = tagValue;
            edtTxt_price = _editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (isEdited == false) {
                isEdited = true;
                String price = (String.valueOf(charSequence));
                price = price.replaceAll("[^0-9]", "").trim();
                edtTxt_price.setText("$" + price);

            } else {
                isEdited = false;
                edtTxt_price.setSelection(edtTxt_price.getText().length());

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {


                String price = edtTxt_price.getText().toString();

                price = price.replaceAll("[^0-9]", "").trim();

                InvoiceValueModel invoiceValueModel = flower.getServices().get(tag_PosVal);
                invoiceValueModel.setAmount(price);
                //isEdited=false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            int total = 0;
            for(int index = 0; index < flower.getServices().size(); index++) {
                try
                {
                    int invoice_val = Integer.parseInt(flower.getServices().get(index).getAmount());
                    Log.d("VALJSE","VALJSE"+invoice_val);
                    total = total + invoice_val;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            total_val.setText(String.valueOf(" $"+total));


        }
    }

    private void invoiceserviceDetails(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                invoiceserviceIdList.clear();
                invoiceserviceNameList.clear();


                invoiceserviceIdList.add("Select");
                invoiceserviceNameList.add("Select");

                try {

                    JSONArray jsonArray = new JSONArray(s);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        invoice_service_id = obj.getString("invoice_service_id");
                        invoice_service_name = obj.getString("invoice_service_name");

                        invoiceserviceIdList.add(invoice_service_id);
                        invoiceserviceNameList.add(invoice_service_name);
                        category_service.setEnabled(true);


                    }

                    setAdapter();


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
                params.put("master_data", "invoice_services");

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
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

        }

    }
}
