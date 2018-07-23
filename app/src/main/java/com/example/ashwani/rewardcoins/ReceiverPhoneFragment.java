package com.example.ashwani.rewardcoins;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
//                isMobileNbRegistered(receiverPhone.getText().toString());
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

//    private void isMobileNbRegistered(final CharSequence s) {
//        Log.d(TAG, "isMobileNbRegistered: checking the mobile in db" + s);
//        boolean b=progressBar.hasFocus();
////        boolean b=progressBar.requestFocus();
//        Log.d(TAG, "isMobileNbRegistered: is focus progress bar"+b);
//        if (s.length() == 10) {
//            Map<String, Object> jsonParams = new ArrayMap<>();
//            //put something inside the map, could be null
//            jsonParams.put("pl", receiverPhone.getText().toString());
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
//                    (new JSONObject(jsonParams)).toString());
//            //serviceCaller is the interface initialized with retrofit.create...
//            Call<PhoneNoResponse> call = CoinApi.getCoinService().phoneNumberCheck(body);
//
//            call.enqueue(new Callback<PhoneNoResponse>() {
//                @Override
//                public void onResponse(Call<PhoneNoResponse> call, Response<PhoneNoResponse> response) {
//                    String status = response.body().getSuccess().toString();
//
//                    if (status.equals("true") & receiverPhone.getError() == null) {
//
//                        Toast.makeText(getActivity(), "valid no, yes " + s, Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onResponse: phone Number in database : yes " + s);
//
//                        //invoking the method in the Member Activity
//                        if (fragmentActionListener != null) {
//                            Bundle bundle = new Bundle();
//                            bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ACTION_VALUE_PROCEED_SELECTED);
//                            bundle.putString(FragmentActionListener.KEY_NAME_RECEIVER, response.body().getName());
//                            bundle.putString(FragmentActionListener.KEY_RECEIVER_PHONE, receiverPhone.getEditableText().toString());
//
//                            fragmentActionListener.onActionPerformed(bundle);
//                        }
//
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        receiverPhone.setError("PhoneNo not in DB");
//                    }
//                }
//                @Override
//                public void onFailure(Call<PhoneNoResponse> call, Throwable t) {
//                    receiverPhone.setError("Re-enter the PhoneNo");
//                    progressBar.setVisibility(View.GONE);
//                }
//            });
//        }
//    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}
