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
    Toolbar toolbar;
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
//                    mTextMessage.setText(R.string.title_home);

                    fragmentCall_mainObj.Fragment_call(new ClientFragment(),"clientfrag",null);

                    return true;
                case R.id.navigation_invoice:
                  //  mTextMessage.setText(R.string.title_dashboard);

                   // fragmentCall_mainObj.Fragment_call(new InvoiceFragment(),"invoicefrag",null);

                    fragmentCall_mainObj.Fragment_call(new InvoiceViewFragment(),"invoicefrag",null);
                    return true;
                case R.id.navigation_stats:
                  //  mTextMessage.setText(R.string.title_notifications);

                    fragmentCall_mainObj.Fragment_call(new InvoiceHistory(),"invoicehist",null);
                    return true;

                case R.id.navigation_profile:
                  //  mTextMessage.setText(R.string.title_dashboard);

                   // fragmentCall_mainObj=new GenFragmentCall_Main(KridderNavigationActivity.this);
                    fragmentCall_mainObj.Fragment_call(new ProfileFragment(),"profilefrag",null);
                 /*   Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new ProfileFragment();
                    fragmentTransaction.replace(R.id.frame_layout,test,"profilefrag");
                    fragmentTransaction.addToBackStack("profilefrag");
                    fragmentTransaction.commit();*/
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kridder_navigation);

        Fragment mContent;
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "addinvoice");

            fragmentCall_mainObj.Fragment_call(mContent,"addInvoice",null);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        userModel =getIntent().getParcelableExtra(KridderNavigationActivity.USER_MODEL_TAG);

        fragmentCall_mainObj=new GenFragmentCall_Main(this);

       fragmentCall_mainObj.Fragment_call(new ClientFragment(),"clientfrag",null);

        //Toast.makeText(this,"USERID"+userModel.getId(),Toast.LENGTH_SHORT).show();
       ScreenFromVal=getIntent().getIntExtra(MainActivity.SCREEN_FROM_TAG,-1);



        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.black));
            toolbar.setOverflowIcon(drawable);
        }

        actionBarUtilObj=new ActionBarUtil(this);
        actionBarUtilObj.setTitle("CLIENT");
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(ScreenFromVal== MainActivity.Screens.UPDATE_PROFILE.ordinal()){
            navigation.setSelectedItemId(R.id.navigation_profile);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instanc
        Fragment mContent=null;
        boolean isAddInvoice=false;
       for(Fragment fragment:getSupportFragmentManager().getFragments()){
           String tag=fragment.getTag();
           if(tag.equalsIgnoreCase("addinvoice")){
               isAddInvoice=true;
               break;
           }
       }
       if(isAddInvoice)
        getSupportFragmentManager().putFragment(outState, "addinvoice", mContent);
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
        // Toast.makeText(MainActivity.this,"Count "+frag_count,Toast.LENGTH_SHORT).show();
        Log.d("FRAGCOUNT", "FRAGCOUNT" + frag_count);


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
