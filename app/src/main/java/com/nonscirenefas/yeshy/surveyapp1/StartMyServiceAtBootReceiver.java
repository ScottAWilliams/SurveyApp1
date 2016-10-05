package com.nonscirenefas.yeshy.surveyapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, ReminderService.class);
            context.startService(serviceIntent);
        }
    }
}
