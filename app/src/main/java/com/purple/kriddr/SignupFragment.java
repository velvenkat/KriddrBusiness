package com.purple.kriddr;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
import com.google.gson.Gson;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class SignupFragment extends Fragment {

    EditText flname,mobile,email;
    Button signup_button;
    String first_Name, mobile_No, email_Val;
    String tag_string_req_recieve2 = "string_req_recieve2";
    View rootView;
    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main genFragmentCall_mainObj;
    TextView txtTermsCondts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sgnup_layout,container,false);

        flname = (EditText)rootView.findViewById(R.id.flname);
        mobile = (EditText)rootView.findViewById(R.id.mobile);


        mobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        email = (EditText)rootView.findViewById(R.id.email_val);
        txtTermsCondts = (TextView)rootView.findViewById(R.id.terms_condition);


        String termsStr="By signing up, I agree to Kriddr's <a href=\"https://www.kriddr.com/terms-of-service\">Terms & Conditions</a> and <a href=\"https://www.kriddr.com/privacy-policy\">Privacy Policy</a>";

        txtTermsCondts.setClickable(true);
        txtTermsCondts.setMovementMethod(LinkMovementMethod.getInstance());
        txtTermsCondts.setText(Html.fromHtml(termsStr));
        txtTermsCondts.setLinkTextColor(Color.BLUE);


        actionBarUtilObj.setActionBarVisible();
        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);
        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.setTitle("Sign Up");
        actionBarUtilObj.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        });


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

        mobile.setFilters(new InputFilter[]{filter});

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        signup_button = (Button)rootView.findViewById(R.id.signup_button);

       // mobile.addTextChangedListener(this);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();

            }
        });
        return rootView;
    }

    private void validateData() {
        first_Name = flname.getText().toString().trim();
        mobile_No = mobile.getText().toString().trim();

        mobile_No = mobile_No.replaceAll("[^0-9]","").trim();

        email_Val = email.getText().toString().trim();

        if (first_Name.equals("") || first_Name.isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_flname), Toast.LENGTH_SHORT).show();
        } else if (mobile_No.isEmpty() || mobile_No.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
        }
        else if(email_Val.isEmpty() || email_Val.equals(""))
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_email_val), Toast.LENGTH_SHORT).show();

        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_Val).matches())
        {
            Toast.makeText(getActivity(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(NetworkConnection.isOnline(getActivity()))
            {
                signupData(getResources().getString(R.string.url_reference) + "registration.php");
            }
            else
            {
                Toast.makeText(getActivity(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            }


        }

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

    private void signupData(String url)
    {

        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();

                myProgressDialog.hide();
                Log.d("SIGNUPRES","SIGNUPRES"+s);

                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                    String name = jsonObject.getString("name");
                    String mobile = jsonObject.getString("mobile");
                    if(name.equals("Success"))
                    {

                        Bundle args = new Bundle();
                        args.putString("mobile", mobile);
                        args.putString("type","signup");
                        genFragmentCall_mainObj.Fragment_call(new OTPFragment(),"OTP",args);


                    }

                    else if(name.equals("Exist"))
                    {
                        Toast.makeText(getActivity(),getResources().getString(R.string.already_user_exist),Toast.LENGTH_SHORT).show();

                        Bundle args = new Bundle();
                        args.putString("mobile", mobile_No);
                        genFragmentCall_mainObj.Fragment_call(new SigninFragment(),"siginfrag",args);


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

                Log.d("SIGNUPPASS","SIGNUPPASS"+first_Name + "MOB"+mobile_No + "EMA"+email_Val);

                Gson gson = new Gson();
                params.put("name",first_Name);
                params.put("mobile","91||"+ mobile_No);
                params.put("email",email_Val);



                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

}
