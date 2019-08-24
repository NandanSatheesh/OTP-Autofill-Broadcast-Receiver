package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsMessages = null;
        String smsString = "";

        if (bundle != null) {
            Object[] protocolDataUnits = (Object[]) bundle.get("pdus");
            smsMessages = new SmsMessage[protocolDataUnits.length];
            for (int i = 0; i < smsMessages.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) protocolDataUnits[i]);

                smsString += "\r\nMessage: ";
                smsString += smsMessages[i].getMessageBody().toString();
                smsString += "\r\n";

                String sender = smsMessages[i].getOriginatingAddress();
                if (sender.equalsIgnoreCase("OK-BANK")) {
                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message", smsString);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }
            }
        }
    }
}