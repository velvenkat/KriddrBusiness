package com.purple.kriddr.util;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.purple.kriddr.R;

/**
 * Created by Niranjan Reddy on 12-02-2018.
 */

public class ActionBarUtil {
    AppCompatActivity CallActivity;
    ImageView imgBack,imgSettings;
    TextView txtTitle;
    ActionBar actionBar;
    EditText edtSearchText;

    public ActionBarUtil(AppCompatActivity activity){
        CallActivity=activity;
        actionBar=CallActivity.getSupportActionBar();
        SetView();
    }
    public void setActionBarVisible(){
        actionBar.show();
    }
    public void SetActionBarHide(){
        actionBar.hide();
    }
    public void SetView(){
        actionBar.setCustomView(R.layout.search_view);
        actionBar.setDisplayShowCustomEnabled(true);
        View v=actionBar.getCustomView();
        txtTitle=(TextView)v.findViewById(R.id.textBarTitle);
        imgBack=(ImageView)v.findViewById(R.id.img_actionBarBack);
        imgSettings=(ImageView)v.findViewById(R.id.img_settings_menu);
        edtSearchText = (EditText)v.findViewById(R.id.edtSearchText);
    }
    public void setTitle(String title){
        txtTitle.setText(title);
    }
    public ImageView getImgBack(){
        return imgBack;
    }

    public EditText getEditText()
    {
        return edtSearchText;
    }

    public TextView getTitle()
    {
        return txtTitle;
    }


    public ImageView getImgSettings() {
        return imgSettings;
    }

}
