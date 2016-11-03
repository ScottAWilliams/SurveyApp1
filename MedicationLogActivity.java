package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Yeshy on 7/13/2016.
 */
public class MedicationLogActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private NfcAdapter nfcAdapter;
    Spinner spinner;
    String medicine;
    String date;
    ArrayList<Medication> medicationList;
    String [] mArray;
    String [] passedArray;
    //String [] records = new String [ ]{ " ", " "," "," ", " "};

    protected void onCreate(Bundle savedInstanceState) {
        final Intent i = getIntent();
        String action = i.getAction();

        date = i.getStringExtra("date");
        //Log.e("date",date);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_log);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
        }else if(nfcAdapter.isEnabled()){
            Toast.makeText(this,
                    "NFC supported!",
                    Toast.LENGTH_LONG).show();
            Button btnTag = (Button)findViewById(R.id.btnTag);
            btnTag.setVisibility(View.VISIBLE);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MedicationLogActivity.this, nfcTag.class);
                    startActivity(i);
                    /*
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int width = dm.widthPixels;
                    int height = dm.heightPixels;

                    getWindow().setLayout((int)(width*.8),(int)(height*.55));

                    WindowManager.LayoutParams p = new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                    String tagInfo = tag.toString() + "\n";

                    tagInfo += "\nTag Id: \n";
                    byte[] tagId = tag.getId();
                    tagInfo += "length = " + tagId.length +"\n";
                    for(int i=0; i<tagId.length; i++){
                        tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                    }
                    tagInfo += "\n";

                    String[] techList = tag.getTechList();
                    tagInfo += "\nTech List\n";
                    tagInfo += "length = " + techList.length +"\n";
                    for(int i=0; i<techList.length; i++){
                        tagInfo += techList[i] + "\n ";
                    }

                    //textViewInfo.setText(tagInfo);
                    */
                }

            });
        }
        else if(!nfcAdapter.isEnabled()){ //Your device doesn't support NFC
            Toast.makeText(this,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
        }



        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        //String c = ((MyApplication) this.getApplication()).getMedicationList().getValue
        //Log.d("medicine", c);

        //medicationList = ((MyApplication) this.getApplication()).getMedicationList();
        final String [] medNames = ((MyApplication) this.getApplication()).getMedicationNames();


        mDatabase.child("app").child("users").child(UID).child("medicine").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> records = new ArrayList<>();
                        //records = new String[]{" "," "," "," "};
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        System.out.println(dataSnapshot);
                        int g=0;
                        while (it.hasNext()) {
                            DataSnapshot medicine = it.next();
                            String attempts =  medicine.child("name").getValue().toString();
                            records.add(attempts);
                            //records[g] = (medicine.child("name").getValue().toString());
                            g++;
                        }
                        mArray = new String[records.size()];
                        mArray = records.toArray(mArray);
                        setArray(mArray);
                        //Log.e("mArray1", Arrays.toString(mArray));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });



//Log.e("mArray2", Arrays.toString(passedArray));


    }

    public void setArray(String [] x){

        final Spinner spinner = (Spinner) findViewById(R.id.medicationSpinner);
// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MedicationLogActivity.this, android.R.layout.simple_spinner_item, x);
        //ArrayAdapter<CharSequence> spinnerArrayAdapter= ArrayAdapter.createFromResource(this,R.array.medicationArray,android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);


        //spinner.setPrompt(records[0]);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //Log.v("item", (String) parent.getItemAtPosition(position));
                medicine = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //spinner.setPrompt(records[0]);
            }
        });
    }

    public void returnToCal(View v){
        Intent i = new Intent(this, MedicationActivity.class);
        startActivity(i);
        finish();
    }

    public void submit(View v) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        String time;
        if (Build.VERSION.SDK_INT >= 23 ) {
            time = timePicker.getHour() + ":";
            int minute = timePicker.getMinute();
            if (minute <10){
                time+="0"+minute;
            }
            else{
                time+=minute;
            }

        } else {
            time = timePicker.getCurrentHour() + ":";
            int minute = timePicker.getCurrentMinute();
            if (minute <10){
                time+="0"+minute;
            }
            else{
                time+=minute;
            }
        }
        if (medicine !=" ") {
            //medicine = records.get(0);
            mDatabase.child("app").child("users").child(UID).child("medicineLog").child(date).child(time).setValue(medicine);

            Snackbar.make(v, "Your medication has been logged at " + time, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else{
            Snackbar.make(v,
                    "Please select a medication.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }

    public void checkLogs(View v) {
        Intent i = new Intent(this, MedicationLogReadActivity.class);
        i.putExtra("date", date);
        startActivity(i);
    }
}