package com.example.ashwani.rewardcoins;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendCoinFragment extends Fragment {

    FragmentActionListener fragmentActionListener;

    Button sendCoinBT;
    EditText coinsToSend;
    TextView receiverPhoneTV, receiverNameTV, userCoins;
    String receiverName, receiverPhone;
    Bundle bundle;
    View root;

    public SendCoinFragment() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_send_coin, container, false);
        bundle = getArguments();

        initView();


        sendCoinBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendCoinsShowDialog(coinsToSend.getEditableText().toString());

            }
        });
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

        receiverName = bundle.getString(FragmentActionListener.KEY_NAME_RECEIVER);
        receiverPhone = bundle.getString(FragmentActionListener.KEY_RECEIVER_PHONE);


        receiverPhoneTV.setText(receiverPhone);
        receiverNameTV.setText(receiverName);
    }


//    private void sendCoinsShowDialog(String number) {
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        //put something inside the map, could be null
//
//        jsonParams.put("dea", "7974302600");
//        jsonParams.put("pla", receiverPhone);
//        jsonParams.put("pcoin", number);
//        jsonParams.put("dcoin", number);
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
//                (new JSONObject(jsonParams)).toString());
//        //serviceCaller is the interface initialized with retrofit.create...
//        Call<CoinTransferResponse> call = CoinApi.getCoinService().coinTransfer(body);
//
//        Log.d(TAG, "onClick: " + jsonParams);
//
//        call.enqueue(new Callback<CoinTransferResponse>() {
//            @Override
//            public void onResponse(Call<CoinTransferResponse> call, Response<CoinTransferResponse> response) {
//                String status = response.body().getSuccess().toString();
//
//                if (status.equals("true")) {
//                    Toast.makeText(getActivity(), "The transaction is successful",
//                            Toast.LENGTH_SHORT).show();
//                    if (fragmentActionListener != null) {
//                        Log.d(TAG, "onResponse: not null " + fragmentActionListener);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_SEND_SELECTED);
//                        bundle.putString(FragmentActionListener.KEY_NAME_RECEIVER, "Sanju");
//                        bundle.putString(FragmentActionListener.KEY_RECEIVER_PHONE, "987654321");
//                        bundle.putString(FragmentActionListener.KEY_TXN_AMMOUNT, "250");
//                        bundle.putString(FragmentActionListener.KEY_TXN_TIME, "21:55 11/06/18");
//
//                        fragmentActionListener.onActionPerformed(bundle);
//                    }
//                } else {
//                    Toast.makeText(getActivity(),
//                            "Unsuccessful transaction; some error occurred",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CoinTransferResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "transaction didn't went through",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}
