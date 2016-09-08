package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Yeshy on 7/13/2016.
 */
public class QuestionLogActivity extends AppCompatActivity {
    float questionPopUp = 20;
    float answerPopUp = 18;
    int selectedOption = -1;
    String [] key;
    int questionNum;
    int alreadySelected;
    int na;
    private RadioButton mCurrentlyCheckedRB;
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        //Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_question_log);
        Intent i = getIntent();
        String question = i.getStringExtra("firstKeyName");
        questionNum = Integer.parseInt(i.getStringExtra("questionNum"));
        alreadySelected = i.getIntExtra("alreadySelected",-1);
        String [] answers = i.getStringArrayExtra("answersArray");
        key = i.getStringArrayExtra("keyArray");
        na = i.getIntExtra("notApplicable",-1);

        TextView questionTextView = (TextView) findViewById(R.id.question);
        questionTextView.setText(question);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answersRadioGroup);

        int counter = 0;
        for(String e: answers) {
            final RadioButton rowRadioButton = new RadioButton(this);
            rowRadioButton.setId(R.id.answer);
            rowRadioButton.setTag(Integer.toString(counter));
            // set some properties of rowTextView or something
            rowRadioButton.setText(e);
            rowRadioButton.setTextSize(answerPopUp);
            rowRadioButton.setPadding(0,50,0,50);
            rowRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption = Integer.parseInt(view.getTag().toString());
                    //==================================================
// checks if we already have a checked radiobutton. If we don't, we can assume that
// the user clicked the current one to check it, so we can remember it.
                    if(mCurrentlyCheckedRB == null){
                        mCurrentlyCheckedRB = (RadioButton) view;
                        mCurrentlyCheckedRB.setChecked(true);
                    }

// If the user clicks on a RadioButton that we've already stored as being checked, we
// don't want to do anything.
                    if(mCurrentlyCheckedRB == view)
                        return;

// Otherwise, uncheck the currently checked RadioButton, check the newly checked
// RadioButton, and store it for future reference.
                    mCurrentlyCheckedRB.setChecked(false);
                    ((RadioButton)view).setChecked(true);
                    mCurrentlyCheckedRB = (RadioButton)view;

//========================================================
                }
            });

            if(counter== alreadySelected) {
                rowRadioButton.setSelected(true);
            }

            // add the textview to the linearlayout
            linearLayout.addView(rowRadioButton);
            counter++;
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.55));
    }

    public void onSubmit(View view) {
        Log.e("selectedOption", Integer.toString(selectedOption));
        Log.e("na", Integer.toString(na));
        if(selectedOption == -1) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        } else {
            if(na == 1) {
                if (0 == selectedOption) {
                    Intent intent = new Intent();
                    intent.putExtra("questionNum", questionNum);
                    intent.putExtra("option", selectedOption);
                    intent.putExtra("rightorwrong", 2); //2 means not applicable
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            for(String k: key) {
                if(Integer.parseInt(k) == selectedOption) {
                    Intent intent = new Intent();
                    intent.putExtra("questionNum", questionNum);
                    intent.putExtra("option", selectedOption);
                    intent.putExtra("rightorwrong", 0); //0 means right
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            Intent intent = new Intent();
            intent.putExtra("option", selectedOption);
            intent.putExtra("questionNum", questionNum);
            intent.putExtra("rightorwrong", 1); //1 means wrong
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}