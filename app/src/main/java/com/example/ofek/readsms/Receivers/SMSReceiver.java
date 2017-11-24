package com.example.ofek.readsms.Receivers;
import android.content.*;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.*;

import com.example.ofek.readsms.MainActivity;

public class SMSReceiver extends BroadcastReceiver {

    private String messageBody="-1";

    @Override
	public void onReceive(Context p1, Intent p2) {
		try {
			SmsMessage[] smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(p2);
			messageBody = smsMessage[0].getMessageBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int resultcode = getResultCode();
        Toast.makeText(p1,messageBody, Toast.LENGTH_SHORT).show();
        Log.d("  RESULT=", Integer.toString(resultcode));
	}


	public interface OnReceivedSMS{
		void readSMS();

	}

}
