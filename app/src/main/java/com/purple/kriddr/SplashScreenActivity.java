package com.purple.kriddr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.widget.Toast;

import com.purple.kriddr.R;
import com.purple.kriddr.model.UserModel;


/**
 * Created by pf-05 on 2/5/2018.
 */

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    Context mContext;
    int PERMISSION_REQ_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        screen_Handler();

    }

    public void screen_Handler() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                try {
                    SQLiteDatabase db2 = openOrCreateDatabase("kriddr", MODE_PRIVATE, null);
                    Cursor c = db2.rawQuery("SELECT * FROM "+dbhelp.DATABASE_TABLE, null);
                   // c.moveToFirst();
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        String id = c.getString(c.getColumnIndex(dbhelp.KEY_USID));
                        String bus_status = c.getString(c.getColumnIndex(dbhelp.KEY_BUS_STATUS));
                        String email = c.getString(c.getColumnIndex(dbhelp.KEY_EMAIL));
                        String phone = c.getString(c.getColumnIndex(dbhelp.KEY_PHONE));
                        String name = c.getString(c.getColumnIndex(dbhelp.KEY_USER));
                        String user_status = c.getString(c.getColumnIndex(dbhelp.KEY_STATUS));
                        String business_id = c.getString(c.getColumnIndex(dbhelp.KEY_BUSINESS_ID));
                        String url = c.getString(c.getColumnIndex(dbhelp.KEY_URL));
                        String business_name = c.getString(c.getColumnIndex(dbhelp.KEY_BUSINESS_NAME));
                        String business_phone = c.getString(c.getColumnIndex(dbhelp.KEY_BUSINESS_PHONE));
                        String business_address = c.getString(c.getColumnIndex(dbhelp.KEY_BUSINESS_ADDRESS));
                        UserModel user_model = new UserModel();
                        user_model.setId(id);
                        user_model.setBusiness_status(bus_status);
                        user_model.setEmail(email);
                        user_model.setMobile(phone);
                        user_model.setName(name);
                        user_model.setStatus(user_status);
                        user_model.setBusiness_id(business_id);
                        user_model.setLogo_url(url);
                        user_model.setBusiness_name(business_name);
                        user_model.setBusiness_phone(business_phone);
                        user_model.setBusiness_address(business_address);
                        Log.d("CUSTIDDF", "CUSTIDDF" + id);

                        if (!id.equals("") && bus_status.equals("created")) {

                            Intent intent = new Intent(SplashScreenActivity.this, KridderNavigationActivity.class);
                            intent.putExtra(KridderNavigationActivity.USER_MODEL_TAG, user_model);
                            SplashScreenActivity.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        } else if (!id.equals("") && bus_status.equals("notcreated")) {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            Log.d("AAQWID", "AAQWID" + id);
                            SplashScreenActivity.this.finish();
                            intent.putExtra(KridderNavigationActivity.USER_MODEL_TAG, user_model);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    } else {

                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        //  Log.d("AAQWID","AAQWID"+id);
                        SplashScreenActivity.this.finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } catch (Exception e) {

                    Toast.makeText(SplashScreenActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, SPLASH_TIME_OUT);
    }


}
