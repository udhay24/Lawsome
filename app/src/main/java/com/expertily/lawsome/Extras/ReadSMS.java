package com.expertily.lawsome.Extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Rishabh on 19/01/18.
 */

public class ReadSMS extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();

            if (sender.contains("EXPOTP")) {

                String messageBody = smsMessage.getMessageBody();

                mListener.messageReceived(messageBody);

            }

        }

    }

    public static void bindListener(SmsListener listener) {

        mListener = listener;

    }

}
