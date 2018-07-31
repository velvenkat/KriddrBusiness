package com.purple.kriddr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.InvoiceModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pf-05 on 3/5/2018.
 */

public class AddInvoiceCommentsFragment extends Fragment {

    View rootView;
    ImageView imageView1;
    EditText text_comments;
    String pet_photo = "", pet_comments = "";
    Button add_invoice;
    InvoiceModel flower;
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.invoice_comments,container,false);

        Bundle bundle_args=getArguments();
        if(bundle_args!=null) {

            pet_photo = bundle_args.getString("pet_photo",null);
            flower = bundle_args.getParcelable("flower_model");


        }

        imageView1 = (ImageView)rootView.findViewById(R.id.imageView1);
        text_comments = (EditText)rootView.findViewById(R.id.textcmt);
        add_invoice = (Button)rootView.findViewById(R.id.next_button);


        if(!pet_photo.equals(""))
        {
            Glide.with(getActivity()).load(pet_photo).transform(new CircleTransform(getActivity())).into(imageView1);
        }
        else
        {
            Glide.with(getActivity()).load(R.drawable.defaultpetphoto).transform(new CircleTransform(getActivity())).into(imageView1);
        }


        add_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pet_comments = text_comments.getText().toString().trim();

                if(pet_comments.equals(""))
                {
                   flower.setComments("Thanks for coming in, see you next time!");
                }
                else
                {
                    flower.setComments(pet_comments);
                }

                    if (NetworkConnection.isOnline(getActivity())) {
                        submitData(getResources().getString(R.string.url_reference) + "pet_invoice_creation.php");
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }




            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if(savedInstanceState != null)
        {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState,"ADD_INV_CMT_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,mContent,"addinvoicecomments");
            fragmentTransaction.addToBackStack("addinvoicecomments");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        getActivity().getSupportFragmentManager().putFragment(outState,"ADD_INV_CMT_STATE",this);
    }
    public void onResume(){
        super.onResume();
        text_comments.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    text_comments.clearFocus();
                }
                return false;
            }
        });
        _handleBackKey();
    }


    public void _handleBackKey(){
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setMessage("Do you want to go back?")
                .setCancelable(false)
                .setNegativeButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStackImmediate();
                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();


        Button positiveButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alert.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FF0000"));
        //positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));

        negativeButton.setTextColor(Color.parseColor("#FF0000"));
        //negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));

    }



    private void submitData(String url)
    {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();

                myProgressDialog.hide();

                try
                {

                    JSONObject jsonObject = new JSONObject(s);
                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");
                    if(result.equalsIgnoreCase("Success"))
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.invoice_creation))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                        Bundle bundle= new Bundle();
                                        bundle.putString("pet_id",flower.getPet_id());
                                        bundle.putString("pet_photo",pet_photo);
                                        bundle.putInt("share_status", PetClientListFragment.SHARE_STATUS.ADDED.ordinal());
                                        fragmentCall_mainObj.Fragment_call(new ClientViewDetailsFragment(),"clientViewice",bundle);



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

                String jsonValues = gson.toJson(flower);

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


}
