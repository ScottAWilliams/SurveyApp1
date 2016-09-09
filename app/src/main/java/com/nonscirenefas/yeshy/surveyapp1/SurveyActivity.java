package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Yeshy on 7/12/2016.
 */
public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView mListView;
    public static final String PREFS_NAME = "MyPrefsFile";
    Context ctx;
    int surveyNum;
    Toolbar toolbar;
    TextView amountOfQuestions;
    ProgressBar progressBar;
    final static int GET_RESULT_TEXT = 0;
    ArrayList<String[]> questionchoices = new ArrayList<>();
    ArrayList<String[]> key = new ArrayList<>();
    int [] naArray;
    int [] answers;
    int [] rightorwrong;
    float textsize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ctx = this;

        Intent i = getIntent();
        surveyNum = i.getIntExtra("name", 0);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Survey");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        amountOfQuestions = (TextView) findViewById(R.id.surveyquestionnumber);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        System.out.println(surveyNum);
        initializeQuestionList(surveyNum);
    }

    private void initializeQuestionList(int surveyNumber){
        if(surveyNumber == 0) {
            String[] mArray = getResources().getStringArray(R.array.medicationArray);
            System.out.println(mArray.toString());
            final ListView lv = (ListView) findViewById(R.id.questionListView);
            lv.setAdapter(new MedicationAdapter(SurveyActivity.this, mArray));
        } else if (surveyNumber == 1) {
            toolbar.setSubtitle("Lifestyle Survey");
            //get questions from xml file
            String[] mArray = getResources().getStringArray(R.array.LifestyleSurvey);
            ArrayList<String> questions = new ArrayList<>();

            for(String full: mArray){
                String[] split = full.split("_");
                questions.add(split[0]);
                questionchoices.add(Arrays.copyOfRange(split, 1, split.length));
            }
            System.out.println(mArray.toString());
            String[] questionArray = new String[questions.size()];
            questionArray = questions.toArray(questionArray);

            //get key from xml file
            String[] keyArray = getResources().getStringArray(R.array.LifestyleSurveyAnswers);
            for(String full: keyArray){
                String[] split = full.split("_");
                key.add(Arrays.copyOfRange(split, 0, split.length));
            }

            //get notApplicable list from xml file
            String[] naArray1 = getResources().getStringArray(R.array.LifestyleSurveyAnswersNA);
            naArray = new int[naArray1.length];
            int counter = 0;
            for(String full: naArray1){
                naArray[counter] = Integer.parseInt(full);
                counter++;
            }

            //initialize results arrays
            answers = new int[mArray.length];
            Arrays.fill(answers, -1); //default for unanswered
            rightorwrong = new int[mArray.length];
            Arrays.fill(rightorwrong, 1); //default for wrong

            //set listview to questions
            final ListView lv = (ListView) findViewById(R.id.questionListView);
            lv.setAdapter(new QuestionAdapter(SurveyActivity.this, questionArray));

            //set textview to amount of questions
            String text = Integer.toString(mArray.length) + " questions";
            amountOfQuestions.setText(text);

        } else if(surveyNumber == 2) {
            toolbar.setSubtitle("Adherence Survey");
            //get questions from xml file
            String[] mArray = getResources().getStringArray(R.array.AdherenceSurvey);
            ArrayList<String> questions = new ArrayList<>();

            for(String full: mArray){
                String[] split = full.split("_");
                questions.add(split[0]);
                questionchoices.add(Arrays.copyOfRange(split, 1, split.length));
            }
            System.out.println(mArray.toString());
            String[] questionArray = new String[questions.size()];
            questionArray = questions.toArray(questionArray);

            //get key from xml file
            String[] keyArray = getResources().getStringArray(R.array.AdherenceSurveyAnswers);
            for(String full: keyArray){
                String[] split = full.split("_");
                key.add(Arrays.copyOfRange(split, 0, split.length));
            }

            //get notApplicable list from xml file
            String[] naArray1 = getResources().getStringArray(R.array.AdherenceSurveyAnswersNA);
            naArray = new int[naArray1.length];
            int counter = 0;
            for(String full: naArray1){
                naArray[counter] = Integer.parseInt(full);
                counter++;
            }

            //initialize results arrays
            answers = new int[mArray.length];
            Arrays.fill(answers, -1); //default for unanswered
            rightorwrong = new int[mArray.length];
            Arrays.fill(rightorwrong, 1); //default for wrong

            //set listview to questions
            final ListView lv = (ListView) findViewById(R.id.questionListView);
            lv.setAdapter(new QuestionAdapter(SurveyActivity.this, questionArray));

            //set textview to amount of questions
            String text = Integer.toString(mArray.length) + " questions";
            amountOfQuestions.setText(text);

        } else if (surveyNumber == 3) {
            toolbar.setSubtitle("Literacy Survey");
            //get questions from xml file
            String[] mArray = getResources().getStringArray(R.array.LiteracySurvey);
            ArrayList<String> questions = new ArrayList<>();

            for(String full: mArray){
                String[] split = full.split("_");
                questions.add(split[0]);
                questionchoices.add(Arrays.copyOfRange(split, 1, split.length));
            }
            System.out.println(mArray.toString());
            String[] questionArray = new String[questions.size()];
            questionArray = questions.toArray(questionArray);

            //get key from xml file
            String[] keyArray = getResources().getStringArray(R.array.LiteracySurveyAnswers);
            for(String full: keyArray){
                String[] split = full.split("_");
                key.add(Arrays.copyOfRange(split, 0, split.length));
            }

            //get notApplicable list from xml file
            String[] naArray1 = getResources().getStringArray(R.array.LiteracySurveyAnswersNA);
            naArray = new int[naArray1.length];
            int counter = 0;
            for(String full: naArray1){
                naArray[counter] = Integer.parseInt(full);
                counter++;
            }

            //initialize results arrays
            answers = new int[mArray.length];
            Arrays.fill(answers, -1); //default for unanswered
            rightorwrong = new int[mArray.length];
            Arrays.fill(rightorwrong, 1); //default for wrong

            //set listview to questions
            final ListView lv = (ListView) findViewById(R.id.questionListView);
            lv.setAdapter(new QuestionAdapter(SurveyActivity.this, questionArray));

            //set textview to amount of questions
            String text = Integer.toString(mArray.length) + " questions";
            amountOfQuestions.setText(text);
        }
    }

    public void launchChoices(View v) {
        TextView t = (TextView) v.findViewById(R.id.text);
        TextView n = (TextView) v.findViewById(R.id.questionNum);
        t.setTextSize(textsize);
        n.setTextSize(textsize);
        String text = t.getText().toString();
        String number = n.getText().toString();
        int questionNum = Integer.parseInt(number);


        Intent i = new Intent(SurveyActivity.this, QuestionLogActivity.class);
        i.putExtra("firstKeyName", text);
        i.putExtra("notApplicable", naArray[questionNum]);
        i.putExtra("alreadySelected", answers[questionNum]);
        i.putExtra("questionNum", number);
        i.putExtra("answersArray", questionchoices.get(questionNum));
        i.putExtra("keyArray", key.get(questionNum));
        startActivityForResult(i, GET_RESULT_TEXT);
        System.out.println();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mListView = (ListView)findViewById(R.id.questionListView);
        if (requestCode == GET_RESULT_TEXT) {
            if(resultCode == RESULT_OK){
                int questionNum = data.getIntExtra("questionNum",0);
                int optionchoice = data.getIntExtra("option",-2);
                int rightorwrongchoice = data.getIntExtra("rightorwrong",-2);

                answers[questionNum] = optionchoice;
                rightorwrong[questionNum] = rightorwrongchoice;

                //set progressbar to the amount of questions answered.
                int counter = 0;
                int qnum = 0;
                for(int x: answers) {
                    if(x != -1) {
                        counter++;
                        //TODO: Add code here for changed color of listeview completed
                        mListView.getChildAt(qnum).setBackgroundColor(Color.LTGRAY);
                    }

                    qnum++;

                }
                progressBar.setMax(answers.length);
                progressBar.setProgress(counter);
            }
        }
    }

    public void submit(View view) {
        Log.e("answersArr", Arrays.toString(answers));
        Log.e("rightorwrongarr",Arrays.toString(rightorwrong));
        Log.e("naArray",Arrays.toString(naArray));

        if(surveyNum == 0) {
            ((MyApplication) SurveyActivity.this.getApplication()).setLifestyleSurveyAnswers(rightorwrong);
        } else if(surveyNum == 1) {
            //
        } else if(surveyNum == 2) {
            //
        }
        else if(surveyNum == 3) {
            //
        }
        //more code that puts stuff to a database
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id  ==R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, SurveySelectionActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:6783600636"));
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String UIDstored = settings.getString("UID", "Default");
            Log.e("logout", UIDstored);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UID", "Default");
            editor.commit();

            UIDstored = settings.getString("UID", "Default");
            Log.e("logout", UIDstored);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_hipaa) {
            Intent i = new Intent(this, HIPAAActivity.class);
            startActivity(i);

        }
        else if (id == R.id.nav_informedconsent) {
            Intent i = new Intent(this, InformedConsentActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
