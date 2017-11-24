package com.example.ofek.readsms;


import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofek.readsms.Receivers.SMSReceiver;

public class MainActivity extends AppCompatActivity implements SMSReceiver.OnReceivedSMS{
    private boolean isReceiverRegistered =false;
    private boolean isPermissionGranted =false;
    Button registerBtn;
    SMSReceiver receiver;
    TextView contentTV;
    private static final int SMS_REQUEST = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        registerBtn=findViewById(R.id.registerBtn);
        contentTV=findViewById(R.id.contentTV);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.SEND_SMS) !=
                        PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.RECEIVE_SMS) !=
                                PackageManager.PERMISSION_GRANTED)){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {
                                    Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,Telephony.Sms.Intents.SMS_RECEIVED_ACTION
                            }, SMS_REQUEST);

                }
            }
        });

    }

    private void checkPermissions() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.RECEIVE_SMS)==
                        PackageManager.PERMISSION_GRANTED)) {
                isPermissionGranted=true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SMS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    receiver=new SMSReceiver();
                    Log.e("receiver registered : ","registered");
                    IntentFilter smsFilter=new IntentFilter();
                    smsFilter.addAction(Manifest.permission.READ_SMS);
                    smsFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
                    isReceiverRegistered=registerReceiver(receiver, smsFilter)!=null;
                }
                else {
                    Toast.makeText(this, "Please make sure you allowed all permissions!", Toast.LENGTH_SHORT).show();
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGranted&&!isReceiverRegistered){
            try {
                receiver=new SMSReceiver();
                Log.e("receiver registered : ","registered");
                final IntentFilter smsFilter=new IntentFilter();
                smsFilter.addAction(Manifest.permission.READ_SMS);
                smsFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.registerReceiver(receiver,smsFilter);
                    }
                });
            }
            catch (RuntimeException e){
                e.printStackTrace();
                Log.e("receiver registered : ","unregistered");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPermissionGranted&&isReceiverRegistered) {
            unregisterReceiver(receiver);
            Log.d("Receiver unregistered","unregistered");
        }
    }

    @Override
    public void readSMS() {

    }
}
