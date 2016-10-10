package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

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
    Spinner spinner;
    String medicine;
    String date;
    ArrayList<Medication> medicationList;
    String [] mArray;
    String [] passedArray;
    //String [] records = new String [ ]{ " ", " "," "," ", " "};

    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        date = i.getStringExtra("date");
        //Log.e("date",date);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_log);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*.8),(int)(height*.55));
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
                            DataSnapshot medicine = (DataSnapshot) it.next();
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
            time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
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