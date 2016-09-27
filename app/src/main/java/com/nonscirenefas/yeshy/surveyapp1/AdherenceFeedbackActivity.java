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

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

public class AdherenceFeedbackActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Context ctx;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<String> responseArray = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_feedback);
        //ctx = this;

        final TextView tv = (TextView) findViewById(R.id.TitleFeedback);
        tv.setText("Medication Adherence Survey Feedback");

        String[] posArray = getResources().getStringArray(R.array.AdherencePositiveMessagesArray);
        Log.e("posArray", posArray[0]);//Arrays.toString(posArray));
        String[] negArray = getResources().getStringArray(R.array.AdherenceNegativeMessagesArray);
        Log.e("negArray",Arrays.toString(negArray));

        int[] correctChoice = {2,0,0,0,1,0,0,0};
        int[] wrongChoice = {1,1,1,1,2,1,1,0};
        Intent i = getIntent();
        int[] surveyResponse = i.getIntArrayExtra("surveyResponse");


        for(int ind=0; ind<surveyResponse.length;ind++){
            if (surveyResponse[ind]==correctChoice[ind]){
                responseArray.add(Integer.toString(ind+1)+". "+posArray[ind]);
            }
            else if (surveyResponse[ind]==wrongChoice[ind]){
                responseArray.add(Integer.toString(ind+1)+". "+negArray[ind]);
            }
            else if (wrongChoice[ind]==0){
                if (surveyResponse[ind] != 1 & surveyResponse[ind] != 2){
                    responseArray.add(Integer.toString(ind+1)+". "+negArray[ind]);
                }
            }
        }

        final ListView lv = (ListView) findViewById(R.id.LifestyleFeedbackView);
        adapter = new ArrayAdapter<String>(this,R.layout.tip_of_the_day,responseArray);
        //setListAdapter(adapter);
        lv.setAdapter(adapter);


        Button feedbackComplete = (Button) findViewById(R.id.finishButton);

        feedbackComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(AdherenceFeedbackActivity.this, SurveySelectionActivity.class);
                startActivity(i);
            }
        });

    }

}
