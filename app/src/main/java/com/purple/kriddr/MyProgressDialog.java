package com.purple.kriddr;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

import dmax.dialog.SpotsDialog;

/**
 * Created by pf-05 on 2/20/2018.
 */

public class MyProgressDialog {

    AlertDialog progress;


    public MyProgressDialog(Context context) {

        progress = new SpotsDialog(context);
       // progress.setCancelable(false);
       // progress.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
       /* progress.setIndeterminate(true);
        progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_circle));*/
        //progress.();

    }

    public void show()
    {
        progress.show();
    }
    public void hide()
    {
        progress.dismiss();
    }
}
