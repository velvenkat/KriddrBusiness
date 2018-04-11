package com.purple.kriddr.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;

import com.purple.kriddr.AddInvoiceCommentsFragment;
import com.purple.kriddr.ProfileFragment;
import com.purple.kriddr.R;
import com.purple.kriddr.model.UserModel;

/**
 * Created by Niranjan Reddy on 11-02-2018.
 */

public class GenFragmentCall_Main {

    AppCompatActivity  Call_Activity=null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    public GenFragmentCall_Main(AppCompatActivity activity ){

    Call_Activity =activity;
    fragmentManager=Call_Activity.getSupportFragmentManager();


    }

    public void Fragment_call(Fragment _scrn_fragment, String tag, Bundle Args){

        fragmentTransaction=fragmentManager.beginTransaction();
        if(_scrn_fragment instanceof AddInvoiceCommentsFragment){
            Fragment invoice=Call_Activity.getSupportFragmentManager().findFragmentByTag("addinvoice");
            fragmentTransaction.hide(invoice);
            fragmentTransaction.add(R.id.frame_layout, _scrn_fragment, tag);
        }
        else {
            fragmentTransaction.replace(R.id.frame_layout, _scrn_fragment, tag);
        }
        fragmentTransaction.addToBackStack(tag);
        if(Args!=null){
            _scrn_fragment.setArguments(Args);
        }
        fragmentTransaction.commit();

    }

}
