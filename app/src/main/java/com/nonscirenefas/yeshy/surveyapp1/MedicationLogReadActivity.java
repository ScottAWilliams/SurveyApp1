package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class MedicationLogReadActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_medication_log_read);


/*
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
*/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = ((MyApplication) this.getApplication()).getUID();

        //getWindow().setLayout((int)(width*.8),(int)(height*.55));

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
                        records.add("Date: "+date);
                        //Log.e("hey","hey");
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        System.out.println(dataSnapshot);
                        if (it.hasNext()){
                            records.add(" ");
                        }
                        while (it.hasNext()) {
                            DataSnapshot medicine = it.next();
                            //Log.e("reading2", medicine.toString());
                            //Log.e("reading3", medicine.getKey());
                            //Log.e("reading4", medicine.getValue().toString());


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

                        }
                        String [] mArray = new String[records.size()];
                        mArray = records.toArray(mArray);
                        final ListView lv = (ListView) findViewById(R.id.medicationListView);
                        adapter = new ArrayAdapter<String>(MedicationLogReadActivity.this,R.layout.log_read,mArray);
                        //setListAdapter(adapter);
                       // lv.setAdapter(new MedicationAdapter(MedicationLogReadActivity.this, mArray));
                        lv.setAdapter(adapter);
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