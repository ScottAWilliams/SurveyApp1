package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * Created by Yeshy on 7/13/2016.
 */
public class MedicationLogActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String medicine;
    String date;
    ArrayList<Medication> medicationList;
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        date = i.getStringExtra("date");
        Log.e("date",date);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_log);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*.8),(int)(height*.55));

        medicationList = ((MyApplication) this.getApplication()).getMedicationList();
        String [] medNames = ((MyApplication) this.getApplication()).getMedicationNames();
        //System.out.println(Arrays.toString(medNames));
        String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};
        //String [] medNames = new String[medicationList.size()];
        //int counter = 0;
        //for(int counter = 0; counter < medicationList.size(); counter++) {
        //    medNames[counter] = medicationList.get(counter).getName();
        //    Log.e("hey",medicationList.get(counter).getName());
        //}
        System.out.println(Arrays.toString(medNames));
// Selection of the spinner
        Spinner spinner = (Spinner) findViewById(R.id.medicationSpinner);

// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, medNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Object item = arg0.getItemAtPosition(arg2);
                if (item!=null) {
                    medicine = item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //String UID = ((MyApplication) this.getApplication()).getUID();
        //mDatabase.child("app").child("users").child(UID).child("medicine");
    }

    public void submit(View v) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        String time;
        if (Build.VERSION.SDK_INT >= 23 ) {
            time = timePicker.getHour() + ":" + timePicker.getMinute();
        } else {
            time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
        }
        mDatabase.child("app").child("users").child(UID).child("medicineLog").child(date).child(time).setValue(medicine);
    }

    public void checkLogs(View v) {
        Intent i = new Intent(this, MedicationLogReadActivity.class);
        i.putExtra("date", date);
        startActivity(i);
    }
}