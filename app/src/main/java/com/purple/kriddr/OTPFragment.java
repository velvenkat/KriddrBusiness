package com.purple.kriddr;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purple.kriddr.R;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.parser.UserJsonParser;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class OTPFragment extends Fragment {

    View rootView;
    EditText pin_first_edittext, pin_second_edittext, pin_third_edittext, pin_forth_edittext, pin_fifth_edittext;
    String mobile;
    Button submit;
    String append_OTP;
    List<UserModel> feedslist;
    GenFragmentCall_Main genFragmentCall_main;
    ActionBarUtil actionBarUtilObj;
    String first_pin, second_pin, third_pin, fourth_pin, fifth_pin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.confirmation_layout, container, false);

        pin_first_edittext = (EditText) rootView.findViewById(R.id.pin_first_edittext);
        pin_second_edittext = (EditText) rootView.findViewById(R.id.pin_second_edittext);
        pin_third_edittext = (EditText) rootView.findViewById(R.id.pin_third_edittext);
        pin_forth_edittext = (EditText) rootView.findViewById(R.id.pin_forth_edittext);
        pin_fifth_edittext = (EditText) rootView.findViewById(R.id.pin_fifth_edittext);
        submit = (Button) rootView.findViewById(R.id.submit);



        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.setTitle("Confirmation Code");

        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);


        Bundle bundle = getArguments();

        try {
            mobile = bundle.getString("mobile");
            Log.d("MOBRES", "MOBRES" + mobile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        pin_first_edittext.getText().toString();


        pin_first_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_first_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_second_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });
        pin_second_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_second_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_third_edittext.requestFocus();
                } else {
                    pin_first_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });

        pin_third_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_third_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_forth_edittext.requestFocus();
                } else {
                    pin_second_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });

        pin_forth_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_forth_edittext.getText().toString().trim();
                if (text.length() == 1) {
                    pin_fifth_edittext.requestFocus();
                } else {
                    pin_third_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();
            }
        });


        pin_fifth_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = pin_fifth_edittext.getText().toString().trim();
                if (text.length() == 0) {
                    pin_forth_edittext.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEnteredAllDigit();

            }
        });

        return rootView;


    }
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof FragmentCallInterface){
            genFragmentCall_main=((FragmentCallInterface)context).Get_GenFragCallMainObj();
        }
        if(context instanceof InterfaceActionBarUtil){
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }
    }

    public void isEnteredAllDigit() {

        first_pin = pin_first_edittext.getText().toString().trim();
        second_pin = pin_second_edittext.getText().toString().trim();
        third_pin = pin_third_edittext.getText().toString().trim();
        fourth_pin = pin_forth_edittext.getText().toString().trim();
        fifth_pin = pin_fifth_edittext.getText().toString().trim();


        if (!first_pin.equals("") && !second_pin.equals("") && !third_pin.equals("") && !fourth_pin.equals("") && !fifth_pin.equals("")) {
            if (NetworkConnection.isOnline(getActivity())) {
                passingOTPData(getResources().getString(R.string.url_reference) + "registration_auth.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }


        }


    }


    private void passingOTPData(String url) {
        final String full_otp = first_pin + second_pin + third_pin + fourth_pin + fifth_pin;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("GVTRES", "GVTRES" + s);

                try {
                    feedslist = UserJsonParser.parseFeed(s);
                    // progress.hide();
                    updatedisplay();
                } catch (NoClassDefFoundError e) {
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
                params.put("mobile", mobile);
                params.put("otp", full_otp);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);

    }


    public void updatedisplay() {
        if (feedslist != null) {
            UserModel flower = feedslist.get(0);

            if (flower.getStatus().equals("Active")) {
                dbhelp entry = new dbhelp(getActivity());
                entry.open();
                entry.deleteTable();
                entry.createuser(flower.getId(), flower.getName(), flower.getEmail(), flower.getMobile(), flower.getBusiness_status(), flower.getStatus(), flower.getBusiness_id(), flower.getBusiness_name(), flower.getLogo_url(), flower.getBusiness_phone(), flower.getBusiness_address());
                entry.close();
                if (flower.getBusiness_status().equals("created")) {


                    Intent in = new Intent(getActivity(), KridderNavigationActivity.class);
                    in.putExtra(KridderNavigationActivity.USER_MODEL_TAG, flower);
                    in.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.OTP.ordinal());

                    ((Activity) getActivity()).finish();
                    getActivity().startActivity(in);

                } else {

                   /* Fragment test;
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new CreateBusinessProfile();*/
                    Bundle args = new Bundle();
                    args.putParcelable(KridderNavigationActivity.USER_MODEL_TAG, flower);
                    args.putInt(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.OTP.ordinal());
                    genFragmentCall_main.Fragment_call(new CreateBusinessProfile(),"OTP",args);
                    /*test.setArguments(args);
                    fragmentTransaction.replace(R.id.content_frame, test, "signinfrag");
                    fragmentTransaction.addToBackStack("signinfrag");
                    fragmentTransaction.commit();*/

                }
            } else if (flower.getStatus().equals("Invalid OTP")) {
                Toast.makeText(getActivity(), R.string.invalid_otp, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.account_inactivate, Toast.LENGTH_SHORT).show();
            }


        }

    }
}
