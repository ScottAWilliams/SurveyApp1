package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class LifestyleFeedbackActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Context ctx;
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
        tv.setText("Lifestyle Survey Feedback");

        String[] posArray = getResources().getStringArray(R.array.LifestylePositiveMessagesArray);
        String[] negArray = getResources().getStringArray(R.array.LifestyleNegativeMessagesArray);
        responsePosArray.add("Positive Feedback");
        responsePosArray.add(" ");
        responseNegArray.add("Educational Feedback");
        responseNegArray.add(" ");
        int[] correctChoice = {5,1,2,2,1,2,2,5};
        int[] wrongChoice = {0,0,1,1,0,1,1,0};

        Intent i = getIntent();
        int[] surveyResponse = i.getIntArrayExtra("surveyResponse");


        for(int ind=0; ind<surveyResponse.length;ind++){
            if (surveyResponse[ind]==correctChoice[ind]){
                responsePosArray.add(Integer.toString(ind+1)+". "+posArray[ind]);
            }
            else if (wrongChoice[ind]==0 | surveyResponse[ind]==wrongChoice[ind]){
                responseNegArray.add(Integer.toString(ind+1)+". "+negArray[ind]);
            }
        }

        responseArray.addAll(responsePosArray);
        responseArray.add(" ");
        responseArray.addAll(responseNegArray);
        final ListView lv = (ListView) findViewById(R.id.LifestyleFeedbackView);
        adapter = new ArrayAdapter<String>(this,R.layout.tip_of_the_day,responseArray);
        //setListAdapter(adapter);
        lv.setAdapter(adapter);


        Button feedbackComplete = (Button) findViewById(R.id.finishButton);

        feedbackComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LifestyleFeedbackActivity.this, SurveySelectionActivity.class);
                startActivity(i);
            }
        });

    }

}
