package com.purple.kriddr;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

/**
 * Created by pf-05 on 2/20/2018.
 */

public class MyProgressDialog {

    ProgressDialog progress;


    public MyProgressDialog(Context context) {

        progress = new ProgressDialog(context);
        progress.setCancelable(false);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_circle));
        progress.hide();

    }

    public void show()
    {
        progress.show();
    }
    public void hide()
    {
        progress.hide();
    }
}
