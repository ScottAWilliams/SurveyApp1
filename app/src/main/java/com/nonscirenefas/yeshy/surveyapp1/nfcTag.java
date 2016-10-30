package com.nonscirenefas.yeshy.surveyapp1;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lindsayherron on 10/28/16.
 */

public class nfcTag extends AppCompatActivity {
    Tag detectedTag;
    TextView txtType,txtSize,txtWrite,txtRead,txtID;
    NfcAdapter nfcAdapter;
    IntentFilter[] readTagFilters;
    PendingIntent pendingIntent;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .55));
        setContentView(R.layout.nfc_tag);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        detectedTag =getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        txtType  = (TextView) findViewById(R.id.txtType);
        txtSize  = (TextView) findViewById(R.id.txtsize);
        txtWrite = (TextView) findViewById(R.id.txtwrite);
        txtRead  = (TextView) findViewById(R.id.txtread);
        txtID  = (TextView) findViewById(R.id.txtid);

        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this,getClass()).
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter filter2     = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        readTagFilters = new IntentFilter[]{tagDetected,filter2};

    }

    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        if(getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
            detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            readFromTag(getIntent());
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, readTagFilters, null);
    }


    public void readFromTag(Intent intent){

        Ndef ndef = Ndef.get(detectedTag);


        try{
            ndef.connect();
ndef.getNdefMessage();
            String value = new String(ndef.getTag().getId(), "UTF-8");
            txtID.setText(value);
            txtType.setText(ndef.getType().toString());
            txtSize.setText(String.valueOf(ndef.getMaxSize()));
            txtWrite.setText(ndef.isWritable() ? "True" : "False");
            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload);
                txtRead.setText(text);


                ndef.close();

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }




        /*
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .55));
        setContentView(R.layout.nfc_tag);

        final Intent i = getIntent();
        String action = i.getAction();


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(i.getAction()) | NfcAdapter.ACTION_TECH_DISCOVERED.equals(i.getAction()) | NfcAdapter.ACTION_TAG_DISCOVERED.equals(i.getAction())) {
            Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            String tagInfo = tag.toString() + "\n";

            tagInfo += "\nTag Id: \n";
            byte[] tagId = tag.getId();
            tagInfo += "length = " + tagId.length + "\n";
            for (int ind = 0; ind < tagId.length; ind++) {
                tagInfo += Integer.toHexString(tagId[ind] & 0xFF) + " ";
            }
            tagInfo += "\n";

            String[] techList = tag.getTechList();
            tagInfo += "\nTech List\n";
            tagInfo += "length = " + techList.length + "\n";
            for (int ind = 0; ind < techList.length; ind++) {
                tagInfo += techList[ind] + "\n ";
            }
            TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);
            textViewInfo.setText(tagInfo);
        }
        Button btnclose = (Button) findViewById(R.id.btnClose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

    }
    */
    public void onClose(){finish();}

}