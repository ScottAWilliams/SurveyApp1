package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

public class LifestyleFeedbackActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    Context ctx;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_feedback);
        //ctx = this;

        String[] posArray = getResources().getStringArray(R.array.LifestylePositiveMessagesArray);
        String[] negArray = getResources().getStringArray(R.array.LifestyleNegativeMessagesArray);
        Intent i = getIntent();
        int[] surveyResponse = i.getIntArrayExtra("surveyResponse");

        for(int ind=0; ind<surveyResponse.length;ind++){
            //if (surveyResponse(ind)
        }




        Button feedbackComplete = (Button) findViewById(R.id.finishButton);

        feedbackComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LifestyleFeedbackActivity.this, SurveySelectionActivity.class);
                startActivity(i);
            }
        });

    }

}
