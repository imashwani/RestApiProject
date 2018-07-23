package com.example.ashwani.rewardcoins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static java.lang.Character.isDigit;

public abstract class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received ka SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
//                    if (!senderAddress.toLowerCase().contains("mango taxi")) {
//                        return;
//                    }

                    Log.d(TAG, "onReceive: " + message);
                    // verification code from sms
                    String verificationCode = getVerificationCode(message);
                    Log.e(TAG, "OTP received: " + verificationCode);
                    updateUI(verificationCode);


                    //service to verify the OTP by sending it to the server
//                    Intent hhtpIntent = new Intent(context, HttpService.class);
//                    hhtpIntent.putExtra("otp", verificationCode);
//                    context.startService(hhtpIntent);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;

        int start = 0;
        int length = 4;
        code = message.substring(start, start + length);
        Log.d(TAG, "getVerificationCode: code: " + code);
        boolean b = true;
        if (code.length() == 4) {

            for (int i = 0; i < 4; i++)
                if (!isDigit(code.charAt(i))) {
                    b = false;
//                    code=null;
                    break;

                }
        }

        return code;
    }

    abstract void updateUI(String code);

}
