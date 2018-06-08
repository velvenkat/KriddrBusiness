package com.purple.kriddr.com.expand.collection;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.purple.kriddr.R;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by Niranjan Reddy on 16-03-2018.
 */

public class PaymentRecivedViewHolder extends ChildViewHolder {


   boolean isLoadViews=false;
   checkChangeListener mListener;
    int HDRINDEX;
    public void setChkPaymentRecd(boolean isCheck,int index) {
        HDRINDEX=index;
        isLoadViews=true;
        if(isCheck){
            chkPaymentRecd.setChecked(true);
        }
        else{
            chkPaymentRecd.setChecked(false);
        }

    }

    CheckBox chkPaymentRecd;
    int AdapterPos;


    public PaymentRecivedViewHolder(int Pos, final View itemView, checkChangeListener listenr) {
        super(itemView);
        mListener=listenr;
        AdapterPos=Pos;
        chkPaymentRecd=(CheckBox)itemView.findViewById(R.id.chk_pay_recd);

        chkPaymentRecd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (chkPaymentRecd.isChecked()) {
                        mListener.checkChange(AdapterPos, true, HDRINDEX);

                    } else {
                        mListener.checkChange(AdapterPos, false, HDRINDEX);
                    }
                }

            
        });

    }


    public interface checkChangeListener{
        public void checkChange(int AdapterPos,boolean value,int pos);
    }
}
