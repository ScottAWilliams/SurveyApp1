package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lindsayherron on 9/14/16.
 */
public class HealthSurvey extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS_NAME = "MyPrefsFile";
    final ArrayList<String> questions = new ArrayList<>();
    String[] questionArray = new String[questions.size()];
    String buttonID;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_survey);
        //setTheme(R.style.myDialog);
        //showLocationDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.setSubtitle("Literacy Survey");
        LinearLayout layout_main= (LinearLayout) findViewById(R.id.main_ll);
        //get questions from xml file
        String[] mArray = getResources().getStringArray(R.array.LiteracySurvey);
        ArrayList<String> questions = new ArrayList<>();

        for (String full : mArray) {
            String[] split = full.split("_");
            questions.add(split[0]);
            questionchoices.add(Arrays.copyOfRange(split, 1, split.length));
        }
        System.out.println(mArray.toString());
        questionArray = new String[questions.size()];
        questionArray = questions.toArray(questionArray);

        //get key from xml file
        String[] keyArray = getResources().getStringArray(R.array.LiteracySurveyAnswers);
        for (String full : keyArray) {
            String[] split = full.split("_");
            key.add(Arrays.copyOfRange(split, 0, split.length));
        }

        //get notApplicable list from xml file
        String[] naArray1 = getResources().getStringArray(R.array.LiteracySurveyAnswersNA);
        naArray = new int[naArray1.length];
        int counter = 0;
        for (String full : naArray1) {
            naArray[counter] = Integer.parseInt(full);
            counter++;
        }

        //initialize results arrays
        answers = new int[mArray.length];
        Arrays.fill(answers, -1); //default for unanswered
        rightorwrong = new int[mArray.length];
        Arrays.fill(rightorwrong, 1); //default for wrong


        final TextView questionsView = (TextView) layout_main.findViewById(R.id.questionView);
        //questionsView.setText("Click an above button to start the survey.");

        LinearLayout layout = (LinearLayout) findViewById(R.id.lv);

        for (int i = 1; i <= 36; i++) {
            final Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btnTag.setText("Question " + i);
            btnTag.setId(Integer.parseInt(Integer.toString(i)));
            layout.addView(btnTag);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionsView.setText(questionArray[btnTag.getId()]);

                }
            });


        }
    }








    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id  == R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);

        }else if (id == R.id.nav_surveys) {
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
            //Intent i = new Intent(this, HIPAAActivity.class);
            //startActivity(i);

        }
        else if (id == R.id.nav_informedconsent) {
            Intent i = new Intent(this, InformedConsentActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}


