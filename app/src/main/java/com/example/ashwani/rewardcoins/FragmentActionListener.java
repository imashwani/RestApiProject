package com.example.ashwani.rewardcoins;

import android.os.Bundle;

public interface FragmentActionListener {
    String ACTION_KEY = "action_key";
    int ACTION_VALUE_PROCEED_SELECTED = 1;
    int ACTION_VALUE_SEND_SELECTED = 2;
    String KEY_BALANCE = "KEY_BALANCE",
            KEY_NAME_RECEIVER = "KEY_NAME_RECEIVER",
            KEY_RECEIVER_PHONE = "KEY_RECEIVER_PHONE",
            KEY_SENDER_PHONE = "KEY_SENDER_PHONE",
            KEY_TXN_TIME = "KEY_TXN_TIME",
            KEY_TXN_AMMOUNT = "KEY_TXN_AMMOUNT",
            KEY_USER_TYPE = "KEY_USER_TYPE",
            KEY_RECEIVER_TYPE = "KEY_RECEIVER_TYPE",
            KEY_UPDATED_BAL = "KEY_UPDATED_BAL";


    void onActionPerformed(Bundle bundle);
}