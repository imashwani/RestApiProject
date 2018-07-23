package com.example.ashwani.rewardcoins;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentActionListener {

    private static final String TAG = "Member Activity";
    Toolbar toolbar;
    NavigationView navigationView;
    Button transferCoinsInit;
    LinearLayout formLinearLayout;
    TextView userCoins, phoneNumberNavBar;
    ImageButton crossImageBt;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        //initializing views
        init();

        View header = navigationView.getHeaderView(0);
        phoneNumberNavBar = header.findViewById(R.id.phone_no_navbar);

        if (getIntent().hasExtra("phone")) {
            phoneNumberNavBar.setText(getIntent().getStringExtra("phone"));
        }

        userCoins.setText("");
        if (getIntent().hasExtra("coin")) {
            userCoins.setText(getIntent().getStringExtra("coin"));
        }
        //TODO: implent hide and show method
        transferCoinsInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForm();
            }
        });
        crossImageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideForm();
            }
        });


    }

    private void showForm() {
        transferCoinsInit.setVisibility(View.GONE);
        formLinearLayout.setVisibility(View.VISIBLE);

        for (int i = fragmentManager.getBackStackEntryCount(); i > 0; i--) {
            fragmentManager.popBackStackImmediate();
            Log.d(TAG, "hideForm: popped" + i);
        }
        ReceiverPhoneFragment receiverPhoneFragment = new ReceiverPhoneFragment();
        boolean has =
                fragmentManager.findFragmentById(R.id.fragment_container) instanceof ReceiverPhoneFragment;
        Log.d(TAG, "showForm: has: " + has);
//        if (has) {
//            fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(receiverPhoneFragment);
//            fragmentTransaction.commit();
//            has =fragmentManager.findFragmentById(R.id.fragment_container) instanceof ReceiverPhoneFragment;
//        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                Log.d(TAG, "showForm: killing the " + fragment);
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        addReceiverPhoneFragment();
    }

    private void hideForm() {
        formLinearLayout.setVisibility(View.INVISIBLE);
        transferCoinsInit.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActionPerformed(Bundle bundle) {
        int actionPerformed = bundle.getInt(FragmentActionListener.ACTION_KEY);
        switch (actionPerformed) {
            case FragmentActionListener.ACTION_VALUE_PROCEED_SELECTED:
                addSendCoinFragment(bundle);
                break;
            case FragmentActionListener.ACTION_VALUE_SEND_SELECTED:
                hideForm();
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
                builder.setTitle("Transaction Successful").setCancelable(true);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                View view = getLayoutInflater().inflate(R.layout.dialog_transaction_details, null);

                ((TextView) view.findViewById(R.id.receiver_name_dialog))
                        .setText(bundle.getString(FragmentActionListener.KEY_NAME_RECEIVER));

                ((TextView) view.findViewById(R.id.receiver_phone_dialog))
                        .setText(bundle.getString(FragmentActionListener.KEY_RECEIVER_PHONE));

                String noOfCoins = bundle.getString(FragmentActionListener.KEY_TXN_AMMOUNT) + " coins";
                ((TextView) view.findViewById(R.id.coins_dialog))
                        .setText(noOfCoins);

                ((TextView) view.findViewById(R.id.time_dialog))
                        .append(bundle.getString(FragmentActionListener.KEY_TXN_TIME));

                builder.setView(view).create();
                builder.show();

                break;
        }
    }

    private void addSendCoinFragment(Bundle bundle) {
        fragmentTransaction = fragmentManager.beginTransaction();
        SendCoinFragment sendCoinFragment = new SendCoinFragment();
        sendCoinFragment.setFragmentActionListener(this);

        sendCoinFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, sendCoinFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addReceiverPhoneFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        ReceiverPhoneFragment receiverPhoneFragment = new ReceiverPhoneFragment();
        receiverPhoneFragment.setFragmentActionListener(this);

        fragmentTransaction.add(R.id.fragment_container, receiverPhoneFragment);
        fragmentTransaction.commit();
    }


    private void init() {

        fragmentManager = getSupportFragmentManager();

        formLinearLayout = findViewById(R.id.formLinerLayout);
        formLinearLayout.setVisibility(View.INVISIBLE);

        userCoins = findViewById(R.id.user_coin_tv);
        transferCoinsInit = findViewById(R.id.transfer_coin_init);
        crossImageBt = findViewById(R.id.cross);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("phone", phoneNumberNavBar.getText());
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor =
                    getSharedPreferences("com.example.app", Context.MODE_PRIVATE).edit();
//            editor.putString("token","");
            editor.clear();
            editor.apply();
            Intent intent = new Intent(MemberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_txn_history) {
            Intent intent = new Intent(this, TransactionHistoryActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

