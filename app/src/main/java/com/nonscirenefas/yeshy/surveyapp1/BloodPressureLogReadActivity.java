package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class BloodPressureLogReadActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String medicine;
    String date;
    String UID;
    ArrayList<Medication> medicationList;
    ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        date = i.getStringExtra("date");


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_log_read);

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
        mDatabase.child("app").child("users").child(UID).child("bloodPressureLog").child(date).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> records = new ArrayList<>();
                        records.add("Date: "+date);
                        //Log.e("hey","hey");
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        //System.out.println(dataSnapshot);
                        if (it.hasNext()){
                            records.add(" ");
                        }
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            //Log.e("reading2", medicine.toString());
                            //Log.e("reading3", medicine.getKey());
                            int colonNdx = medicine.getKey().toString().indexOf(":");
                            String subKey = medicine.getKey().toString().substring(0,colonNdx);
                            int HR =Integer.parseInt(subKey);

                            if(HR >= 12){
                                if(HR > 12){
                                    HR = HR - 12;
                                }
                                records.add(HR + medicine.getKey().substring(colonNdx, colonNdx + 3) + "PM " + medicine.getValue().toString());
                                //Log.e("Corrected Time Reading", HR + medicine.getKey().substring(colonNdx, colonNdx + 3));
                            }else{
                                records.add(medicine.getKey().toString()+ "AM " + medicine.getValue().toString());
                            }

                            //Log.e("reading4", medicine.getValue().toString());


                        }
                        String [] mArray = new String[records.size()];
                        mArray = records.toArray(mArray);
                        //final TextView tv = (TextView) findViewById(R.id.bloodPressureTextView);
                        //tv.setText(date);
                        final ListView lv = (ListView) findViewById(R.id.bloodPressureListView);
                        adapter = new ArrayAdapter<String>(BloodPressureLogReadActivity.this,R.layout.log_read,mArray);
                        //setListAdapter(adapter);
                        lv.setAdapter(adapter);
                        //lv.setAdapter(new MedicationAdapter(BloodPressureLogReadActivity.this, mArray));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public void delete(View v) {
        mDatabase.child("app").child("users").child(UID).child("bloodPressureLog").child(date).setValue(null);
        update();
    }

    public void back(View v) {
        //Intent i = new Intent(this, BloodPressureLogActivity.class);

        //startActivity(i);
        finish();
    }
    public void backToBPCal(View v) {
        Intent i = new Intent(this, BloodPressureActivity.class);
        startActivity(i);
        finish();
    }
}