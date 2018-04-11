package com.purple.kriddr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.purple.kriddr.iface.SmsListener;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class IncomingSMS extends BroadcastReceiver {


    private static SmsListener mListener;

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();


        try
        {
            if(bundle != null)
            {
                final Object[] pdusObj = (Object[])bundle.get("pdus");

                for(int i=0; i < pdusObj.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    mListener.messageReceived(message);

                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void bindListener(SmsListener listener)
    {
        mListener = listener;
    }

}
