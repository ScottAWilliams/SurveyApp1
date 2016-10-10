package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdherenceFeedbackActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Context ctx;
    public static final String MSURVEY_FILENAME = "msurvey_file";
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<String> responsePosArray = new ArrayList<>();
    ArrayList<String> responseNegArray = new ArrayList<>();
    ArrayList<String> responseArray = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_feedback);
        //ctx = this;

        final TextView tv = (TextView) findViewById(R.id.TitleFeedback);
        tv.setText("Medication Adherence Survey Feedback");

        final String[] posArray = getResources().getStringArray(R.array.AdherencePositiveMessagesArray);
        final String[] negArray = getResources().getStringArray(R.array.AdherenceNegativeMessagesArray);

        final int[] surveyResponse = new int[8];
        final int[] correctChoice = {2,0,0,0,1,0,0,0};
        final int[] wrongChoice = {1,1,1,1,2,1,1,0};



        Intent i = getIntent();
        String surveyDate = i.getStringExtra("date");
        deleteFile(MSURVEY_FILENAME);
        try {
            FileOutputStream fos = openFileOutput(MSURVEY_FILENAME, Context.MODE_WORLD_READABLE);
            fos.write(surveyDate.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
Log.e("Date",surveyDate);
        startAlarm(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) AdherenceFeedbackActivity.this.getApplication()).getUID();
        mDatabase.child("app").child("users").child(UID).child("adherencesurveyanswersRW").child(surveyDate).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String response = dataSnapshot.getValue().toString();
                        ArrayList<String> responses = new ArrayList<String>(Arrays.asList(response.split(",")));
                        for (int i=0;i<responses.size();i++) {
                            if(i==responses.size()-1) {
                                surveyResponse[i] =  Integer.parseInt(responses.get(i).substring(1, (responses.get(i)).length()-1));
                            }
                            else {
                                surveyResponse[i] = Integer.parseInt(responses.get(i).substring(1, (responses.get(i)).length()));
                            }
                        }

                        for(int ind=0; ind<surveyResponse.length;ind++){
                            if (surveyResponse[ind]==correctChoice[ind]){
                                responsePosArray.add(Integer.toString(ind+1)+". "+posArray[ind]);
                            }
                            else if ((wrongChoice[ind]==0 | surveyResponse[ind]==wrongChoice[ind]) & ind!=7){
                                responseNegArray.add(Integer.toString(ind+1)+". "+negArray[ind]);
                            }

                            if (ind == 7 & surveyResponse[ind]!= 1){
                                if (surveyResponse[ind] != 2) {
                                    responseNegArray.add(Integer.toString(ind + 1) + ". " + negArray[negArray.length - 1]);
                                }
                            }

                        }
                        if( !responsePosArray.isEmpty()) {
                            responseArray.add("Positive Feedback");
                            responseArray.addAll(responsePosArray);
                        }
                        if( !responsePosArray.isEmpty()&!responseNegArray.isEmpty()) {
                            responseArray.add(" ");
                        }
                        if(!responseNegArray.isEmpty()) {
                            responseArray.add("Educational Feedback");
                            responseArray.addAll(responseNegArray);
                        }
                        final ListView lv = (ListView) findViewById(R.id.LifestyleFeedbackView);
                        adapter = new ArrayAdapter<String>(AdherenceFeedbackActivity.this,R.layout.tip_of_the_day,responseArray);
                        //setListAdapter(adapter);
                        lv.setAdapter(adapter);


                        Button feedbackComplete = (Button) findViewById(R.id.finishButton);

                        feedbackComplete.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent i = new Intent(AdherenceFeedbackActivity.this, SurveySelectionActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });



    }

    public void startAlarm(Context context){

        //Intent i = getIntent();
        //String surDate = i.getStringExtra("date");
        Intent intent = new Intent(this,MonthlyReminderService.class);
        int received = intent.getIntExtra("recieved", 0);
        intent.putExtra("received", received);
        //int type = intent.getIntExtra("type", 0);
        //Log.e("SurveyType", Integer.toString(type));
        //intent.putExtra("type", type);
        //intent.putExtra("date", surDate);
        //intent.putExtra("type", "Medication Adherence");
        //Calendar now = Calendar.getInstance();
        //intent.putExtra("dayofYear",now.get(Calendar.DAY_OF_YEAR));
        startService(intent);
    }

}
