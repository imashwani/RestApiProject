package com.example.ashwani.rewardcoins;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwani.rewardcoins.Data.CoinApi;
import com.example.ashwani.rewardcoins.Data.TransactionResponse;

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
public class SendCoinFragment extends Fragment {

    FragmentActionListener fragmentActionListener;

    Button sendCoinBT;
    EditText coinsToSend, referenceNoEt;
    TextView receiverPhoneTV, receiverNameTV, userCoins;
    String receiverName, receiverPhone;
    Bundle receivedBundle;
    View root;
    String userType = "";
    RadioGroup radioGroup;
    String modeOfTxn = "";

    public SendCoinFragment() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_send_coin, container, false);
        receivedBundle = getArguments();
        userType = receivedBundle.get(FragmentActionListener.KEY_USER_TYPE).toString();
        Log.d(TAG, "onCreateView: userType: " + userType);

        initView();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.cash_rb) {
                    referenceNoEt.setVisibility(View.GONE);
                } else {
                    referenceNoEt.setVisibility(View.VISIBLE);
                }

                switch (checkedId) {
                    case R.id.online_rb:
                        modeOfTxn = "Online";
                        break;
                    case R.id.cash_rb:
                        modeOfTxn = "Cash";
                        break;
                    case R.id.cheque_rb:
                        modeOfTxn = "Cheque";
                        break;


                }
            }
        });
        RadioButton online = root.findViewById(R.id.online_rb);
        online.setChecked(true);

        sendCoinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinsToSend.getText().length() > 0) {

                    sendCoinsShowDialog(coinsToSend.getEditableText().toString());
                } else {
                    coinsToSend.setError("Enter no. of coins");
                }


            }
        });
        Log.d(TAG, "onCreateView: " + userType.equals("C"));
        if (!(userType.equals("C"))) {
            coinsToSend.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isValidData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    isValidData(s.toString());
                }
            });

        }

        return root;
    }


    private void isValidData(String money) {
        Log.d(TAG, "isValidData: money log " + money);

        if (money.equals("") || money == null) {
            hideSendCoinBT();
        } else if (Integer.parseInt(money) <= Integer.parseInt(userCoins.getText().toString())) {
            Log.d(TAG, "isValidData: passed " + Integer.parseInt(money) + " has: " + Integer.parseInt(userCoins.getText().toString()));
            sendCoinBT.setVisibility(View.VISIBLE);
        } else {
            hideSendCoinBT();
            Log.d(TAG, "isValidData: setting some error");
            coinsToSend.setError("You have only" + Integer.parseInt(userCoins.getText().toString()) + " coins");
        }
    }

    private void hideSendCoinBT() {
        sendCoinBT.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        receiverPhoneTV = root.findViewById(R.id.receiver_phone_tv);
        receiverNameTV = root.findViewById(R.id.receiver_name_tv);
        sendCoinBT = root.findViewById(R.id.send_coin_button);
        coinsToSend = root.findViewById(R.id.coins_to_Send);
        userCoins = getActivity().findViewById(R.id.user_coin_tv);
        referenceNoEt = root.findViewById(R.id.reference_no);
        referenceNoEt.setVisibility(View.GONE);

        receiverName = receivedBundle.getString(FragmentActionListener.KEY_NAME_RECEIVER);
        receiverPhone = receivedBundle.getString(FragmentActionListener.KEY_RECEIVER_PHONE);
        radioGroup = root.findViewById(R.id.mode_radio_grp);


        receiverPhoneTV.setText(receiverPhone);
        receiverNameTV.setText(receiverName);
    }


    private void sendCoinsShowDialog(String number) {


        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null

        jsonParams.put("TOS", userType);
        jsonParams.put("TOR", receivedBundle.get(FragmentActionListener.KEY_RECEIVER_TYPE).toString());
        jsonParams.put("mobr", receivedBundle.get(FragmentActionListener.KEY_RECEIVER_PHONE).toString());
        jsonParams.put("mobs", receivedBundle.get(FragmentActionListener.KEY_SENDER_PHONE).toString());
        jsonParams.put("amt", Integer.parseInt(number));
        jsonParams.put("tmod", modeOfTxn);
        jsonParams.put("tid", referenceNoEt.getText().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());
        //serviceCaller is the interface initialized with retrofit.create...
        Call<TransactionResponse> call = CoinApi.getCoinService().coinTransaction(body);

        Log.d(TAG, "onClick: " + jsonParams);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                String status = response.body().getSuccess().toString();

                if (status.equals("true")) {
                    Toast.makeText(getActivity(), "The transaction is successful",
                            Toast.LENGTH_SHORT).show();
                    if (fragmentActionListener != null) {
                        Log.d(TAG, "onResponse: not null " + fragmentActionListener);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_SEND_SELECTED);

                        bundle.putString(FragmentActionListener.KEY_NAME_RECEIVER, receiverName);
                        bundle.putString(FragmentActionListener.KEY_RECEIVER_PHONE, receiverPhone);
                        bundle.putString(FragmentActionListener.KEY_TXN_AMMOUNT, response.body().getTamt().toString());
                        bundle.putString(FragmentActionListener.KEY_TXN_TIME, response.body().getTime());
                        int updatedUserBalance = 0;
                        if (!userType.equals("C")) {
                            updatedUserBalance = response.body().getUbal();
                        }
                        bundle.putString(fragmentActionListener.KEY_UPDATED_BAL, String.valueOf(updatedUserBalance));

                        fragmentActionListener.onActionPerformed(bundle);
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Unsuccessful transaction; some error occurred",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "transaction didn't went through",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}
