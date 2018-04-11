package com.purple.kriddr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;

public class MainActivity extends AppCompatActivity implements FragmentCallInterface, InterfaceActionBarUtil {
    UserModel userModelObj;
    GenFragmentCall_Main genFragmentCall_main;
    ActionBarUtil actionBarUtilObj;


    public static enum Screens {

        OTP, SIGNIN, SIGNUP, CREATE_PROFILE, EDIT_PROFILE, SPLASH_SCREEN, UPDATE_PROFILE, CLIENT_VIEW_DETAILS;

    }

    public static String SCREEN_FROM_TAG = "screen_from";
    public static String SCREEN_OTP = "otp";
    int Screen_From_Val;
    public static String SCREEN_EDIT_PROF = "edit_Prof";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        try {

            Screen_From_Val = getIntent().getExtras().getInt(SCREEN_FROM_TAG, -1);


            if (Screen_From_Val == -1) {
                Screen_From_Val = Screens.SPLASH_SCREEN.ordinal();
            }
            userModelObj = getIntent().getParcelableExtra(KridderNavigationActivity.USER_MODEL_TAG);


            Log.d("PageFrom", "SCREEN From" + Screen_From_Val + "VA " + userModelObj.getBusiness_name());

        } catch (Exception e) {
            e.printStackTrace();
        }

        genFragmentCall_main = new GenFragmentCall_Main(this);
        actionBarUtilObj=new ActionBarUtil(this);
        ((ImageView)actionBarUtilObj.getImgSettings()).setVisibility(View.INVISIBLE);
        ImageView home_img = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.img_actionBarBack);
        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //     Fragment a = getFragmentManager().findFragmentById(R.id.content_frame);
                MainActivity.this.getFragmentManager().popBackStackImmediate();

            }
        });


        if ((userModelObj != null && userModelObj.getBusiness_status().equals("notcreated")) || Screen_From_Val == Screens.EDIT_PROFILE.ordinal()) {
            Log.d("CRETPROFCALL", "CRETPROFCALL");
            Bundle args = new Bundle();
            if(Screen_From_Val==Screens.EDIT_PROFILE.ordinal())
            args.putInt(SCREEN_FROM_TAG, Screens.EDIT_PROFILE.ordinal());
            else
                args.putInt(SCREEN_FROM_TAG, Screens.CREATE_PROFILE.ordinal());
            args.putParcelable(KridderNavigationActivity.USER_MODEL_TAG, userModelObj);
            genFragmentCall_main.Fragment_call(new CreateBusinessProfile(), "createBusProf", args);
        } else {
            genFragmentCall_main.Fragment_call(new MainFragment(), "MainFrag", null);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        final int frag_count = getSupportFragmentManager().getBackStackEntryCount();
        // Toast.makeText(MainActivity.this,"Count "+frag_count,Toast.LENGTH_SHORT).show();
        Log.d("FRAGCOUNT", "FRAGCOUNT" + frag_count);


        if (frag_count == 0) {
            finish();
        }
    }

    @Override
    public GenFragmentCall_Main Get_GenFragCallMainObj() {
        return genFragmentCall_main;
    }

    @Override
    public ActionBarUtil getActionBarUtilObj() {
        return actionBarUtilObj;
    }
}
