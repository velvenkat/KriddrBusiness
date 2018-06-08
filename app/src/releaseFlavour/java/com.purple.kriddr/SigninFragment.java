package com.purple.kriddr;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class SigninFragment extends Fragment {

    View rootView;
    private EditText firstName, mobileNo;
    private Button login_button;
    String first_name, mobile_number;
    String tag_string_req_recieve2 = "string_req_recieve2";
    List<UserModel> feedslist;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    String mobile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.signin_layout, container, false);






        firstName = (EditText) rootView.findViewById(R.id.flname);
        mobileNo = (EditText) rootView.findViewById(R.id.mobile);

        mobileNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        login_button = (Button) rootView.findViewById(R.id.login_button);


        Bundle bundle = getArguments();

        try {
            mobile = bundle.getString("mobile");
            Log.d("MOBRES", "MOBRES" + mobile);
            mobileNo.setText(mobile);

        } catch (Exception e) {
            e.printStackTrace();
        }



        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        });

        actionBarUtilObj.setTitle("Sign In");

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

        mobileNo.setFilters(new InputFilter[]{filter});


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });

        return rootView;
    }
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof FragmentCallInterface){
            genFragmentCall_mainObj=((FragmentCallInterface)context).Get_GenFragCallMainObj();
         }
         if(context instanceof InterfaceActionBarUtil){
             actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

         }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ONSTRWECAL", "ONSTRWECAL");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ONRESVGCALL", "ONRESVGCALL");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ONPASCALL", "ONPASCALL");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("ONSTPCA", "ONSTPCA");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ONDESCA", "ONDESCA");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("ONDETACA", "ONDETACA");
    }

    private void validateData() {

        first_name = firstName.getText().toString().trim();
        mobile_number = mobileNo.getText().toString().trim();

        mobile_number = mobile_number.replaceAll("[^0-9]", "").trim();

        if (first_name.equals("") || first_name.isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_flname), Toast.LENGTH_SHORT).show();
        } else if (mobile_number.isEmpty() || mobile_number.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkConnection.isOnline(getActivity())) {
                Log.d("FUNEDXEEX","FUNEDXEEX");
                loginData(getResources().getString(R.string.url_reference) + "login_auth.php");

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void loginData(String url) {

        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("LOGINRES", "LOGINRES" + s);
                myProgressDialog.hide();


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String name = jsonObject.getString("name");
                    String mobile = jsonObject.getString("mobile");
                    if (name.equals("Success")) {
                        Bundle args = new Bundle();
                        args.putString("mobile", mobile);
                        genFragmentCall_mainObj.Fragment_call(new OTPFragment(),"OTP",args);
                    } else if (name.equals("Empty")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
                    } else if (name.equals("Exist")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.already_user_exist), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//
//                try {
//                    feedslist = UserJsonParser.parseFeed(s);
//                    // progress.hide();
//                    updatedisplay();
//                }catch (NoClassDefFoundError e){
//                    e.printStackTrace();
//                }


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

                Log.d("SIGNUPPASS", "SIGNUPPASS" + first_name + "MOB" + "91||"+ mobile_number);

                Gson gson = new Gson();
                params.put("name", first_name);
                params.put("mobile","1||"+ mobile_number);


                return params;
            }

        };
        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }
}
