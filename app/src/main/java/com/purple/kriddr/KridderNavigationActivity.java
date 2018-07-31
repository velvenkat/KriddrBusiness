package com.purple.kriddr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;

public class KridderNavigationActivity extends AppCompatActivity implements InterfaceUserModel,FragmentCallInterface,InterfaceActionBarUtil {

    private TextView mTextMessage;
  //  Toolbar toolbar;
    public static String USER_MODEL_TAG = "USER_MODEL_OBJ";
    UserModel userModel;

    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    int  ScreenFromVal;
    BottomNavigationView bottomNavigationView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_client:
                    fragmentCall_mainObj.Fragment_call(new ClientFragment(),"clientfrag",null);
                    return true;
                case R.id.navigation_invoice:

                    fragmentCall_mainObj.Fragment_call(new InvoiceViewFragment(),"invoicefrag",null);
                    return true;
                case R.id.navigation_stats:

                    fragmentCall_mainObj.Fragment_call(new InvoiceHistory(),"invoicehist",null);
                    return true;

                case R.id.navigation_profile:

                    fragmentCall_mainObj.Fragment_call(new ProfileFragment(),"profilefrag",null);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kridder_navigation);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //toolbar = (Toolbar)findViewById(R.id.toolbar);

        userModel =getIntent().getParcelableExtra(KridderNavigationActivity.USER_MODEL_TAG);

        fragmentCall_mainObj=new GenFragmentCall_Main(this);



       ScreenFromVal=getIntent().getIntExtra(MainActivity.SCREEN_FROM_TAG,-1);



      /*  Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.black));
            toolbar.setOverflowIcon(drawable);
        }*/

        actionBarUtilObj=new ActionBarUtil(this);
        actionBarUtilObj.setTitle("CLIENT");
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(ScreenFromVal== MainActivity.Screens.UPDATE_PROFILE.ordinal()){
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
        fragmentCall_mainObj.Fragment_call(new ClientFragment(),"clientfrag",null);
    }




    @Override
    public void setUserModel(UserModel userModelObj) {

        userModel = userModelObj;
    }

    @Override
    public UserModel getUserModel() {
        return userModel;
    }

    @Override
    public GenFragmentCall_Main Get_GenFragCallMainObj() {
        return fragmentCall_mainObj;
    }

    @Override
    public ActionBarUtil getActionBarUtilObj() {
        return actionBarUtilObj;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        final int frag_count = getSupportFragmentManager().getBackStackEntryCount();


        if (frag_count == 0) {
            finish();
        }
    }

    public void setNavigationVisibility(boolean isVisible)
    {
      if(isVisible)
      {
          bottomNavigationView.setVisibility(View.VISIBLE);
      }
      else
      {
          bottomNavigationView.setVisibility(View.GONE);
      }
    }
}
