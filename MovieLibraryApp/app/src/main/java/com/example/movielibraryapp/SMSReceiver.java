package com.example.movielibraryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;


public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "SMS_FILTER";

    /*
     * This method 'onReceive' will be invoked with each new incoming SMS
     * */
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * Use the Telephony class to extract the incoming messages from the intent
         * */
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();
            String info[] = message.split(";"); //separate each line of text by splitting ";"

            /*
             * Now, for each new message, send a broadcast contains the new message to MainActivity
             * The MainActivity has to tokenize the new message and update the UI
             * */
            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER);
            msgIntent.putExtra("Title", info[0]);
            msgIntent.putExtra("Year", info[1]);
            msgIntent.putExtra("Country", info[2]);
            msgIntent.putExtra("Genre", info[3]);
            msgIntent.putExtra("Cost", info[4]);
            msgIntent.putExtra("Keyword", info[5]);
            msgIntent.putExtra("Hidden Fee",info[6]);
            context.sendBroadcast(msgIntent);
        }
    }
}
