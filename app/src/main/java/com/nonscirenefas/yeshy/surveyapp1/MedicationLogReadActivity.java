package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * Created by Yeshy on 7/13/2016.
 */
public class MedicationLogReadActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String medicine;
    String date;
    String UID;
    ArrayList<Medication> medicationList;
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        date = i.getStringExtra("date");


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_log_read);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = ((MyApplication) this.getApplication()).getUID();

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
        update();

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //String UID = ((MyApplication) this.getApplication()).getUID();
        //mDatabase.child("app").child("users").child(UID).child("medicine");
    }

    public void update() {
        mDatabase.child("app").child("users").child(UID).child("medicineLog").child(date).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> records = new ArrayList<>();
                        Log.e("hey","hey");
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        System.out.println(dataSnapshot);
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            Log.e("reading2", medicine.toString());
                            Log.e("reading3", medicine.getKey());
                            Log.e("reading4", medicine.getValue().toString());
                            records.add(medicine.getKey() + " " + medicine.getValue().toString());
                        }
                        String [] mArray = new String[records.size()];
                        mArray = records.toArray(mArray);
                        final ListView lv = (ListView) findViewById(R.id.medicationListView);
                        lv.setAdapter(new MedicationAdapter(MedicationLogReadActivity.this, mArray));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public void delete(View v) {
        mDatabase.child("app").child("users").child(UID).child("medicineLog").child(date).setValue(null);
        update();
    }

    public void back(View v) {
        finish();
    }
}