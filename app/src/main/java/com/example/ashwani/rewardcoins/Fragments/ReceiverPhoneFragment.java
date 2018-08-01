package com.example.ashwani.rewardcoins.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.UserDataResponse;
import com.example.ashwani.rewardcoins.R;
import com.example.ashwani.rewardcoins.Util;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiverPhoneFragment extends Fragment {

    View root;
    ProgressBar progressBar;
    EditText receiverPhone;
    Button proceed;

    FragmentActionListener fragmentActionListener;

    public ReceiverPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_receiver_phone, container, false);

        progressBar = root.findViewById(R.id.progress_bar);
        receiverPhone = root.findViewById(R.id.receiver_phone_et);
        proceed = root.findViewById(R.id.proceed_bt);
        proceed.setVisibility(View.INVISIBLE);

        receiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidNumber(s.toString());
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                hideSoftKeyboard(getActivity());
                isMobileNbRegistered(receiverPhone.getText().toString());
            }
        });


        return root;
    }

    void isValidNumber(String no) {
        if (no.length() != 10) {
            receiverPhone.setError("Enter 10 digit mobile number");
            proceed.setVisibility(View.INVISIBLE);
        } else {
            proceed.setVisibility(View.VISIBLE);
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void isMobileNbRegistered(final CharSequence s) {
        Log.d(TAG, "isMobileNbRegistered: checking the mobile in db" + s);
        boolean b = progressBar.hasFocus();
//        boolean b=progressBar.requestFocus();
        Log.d(TAG, "isMobileNbRegistered: is focus progress bar" + b);
        if (s.length() == 10) {
            Map<String, Object> jsonParams = new ArrayMap<>();
            //put something inside the map, could be null
            jsonParams.put("mob", receiverPhone.getText().toString());

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    (new JSONObject(jsonParams)).toString());
            //serviceCaller is the interface initialized with retrofit.create...
            Call<UserDataResponse> call = CoinApi.getCoinService().userDataFromPhoneNo(body);
            call.enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    if (response != null) {


                        String status = response.body().getSuccess().toString();
                        Log.d(TAG, "onResponse: response status:" + status);

                        if (status.equals("true") & receiverPhone.getError() == null) {

                            Toast.makeText(getActivity(), "valid no, yes " + s, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: phone_hand_held Number in database : yes " + s);

                            //invoking the method in the Member Activity
                            if (fragmentActionListener != null) {
                                Bundle bundle = new Bundle();
                                SharedPreferences sharedPreferences =
                                        getContext().getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
                                //        token = sharedPreferences.getString(Util.getTokenKey(), "");
                                String senderMob = sharedPreferences.getString(Util.getPhonenoKey(), "");
                                String userType = sharedPreferences.getString(Util.getAccessLevelKey(), "");

                                bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_PROCEED_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_NAME_RECEIVER, response.body().getNm());
                                bundle.putString(FragmentActionListener.KEY_RECEIVER_PHONE, receiverPhone.getEditableText().toString());
                                //getting the type of receiver
                                bundle.putString(FragmentActionListener.KEY_RECEIVER_TYPE, response.body().getTOR());

                                bundle.putString(FragmentActionListener.KEY_SENDER_PHONE, senderMob);
                                String receiverName = response.body().getNm().toString();
                                bundle.putString(FragmentActionListener.KEY_NAME_RECEIVER, receiverName);

                                bundle.putString(FragmentActionListener.KEY_USER_TYPE, userType);

                                Log.d(TAG, "onResponse: receiver phone_hand_held receivedBundle: " + bundle);


                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            receiverPhone.setError("PhoneNo not in DB");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    receiverPhone.setError("Re-enter the PhoneNo");
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}
