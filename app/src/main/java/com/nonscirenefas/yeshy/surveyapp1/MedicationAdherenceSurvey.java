package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


/**
 * Created by Javier 9/18/16.
 */
public class MedicationAdherenceSurvey extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    Button btnTag;
    int qnum;
    Context ctx;
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
    int selected =0;
    public int questCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_adherence_survey);
        //Button testButton = (Button) findViewById(R.id.TESTBUTTON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.setSubtitle("Medication Adherence Survey");
        LinearLayout layout_main = (LinearLayout) findViewById(R.id.main_ll);
        //get questions from xml file
        String[] mArray = getResources().getStringArray(R.array.AdherenceSurvey);
        final ArrayList<String> questions = new ArrayList<>();


        for (String full : mArray) {
            String[] split = full.split("_");
            questions.add(split[0]);
            questionchoices.add(Arrays.copyOfRange(split, 1, split.length));
        }

        System.out.println(mArray.toString());
        questionArray = new String[questions.size()];
        questionArray = questions.toArray(questionArray);

        //get key from xml file
        final String[] keyArray = getResources().getStringArray(R.array.AdherenceSurveyAnswers);
        for (String full : keyArray) {
            String[] split = full.split("_");
            key.add(Arrays.copyOfRange(split, 0, split.length));
        }

        //get notApplicable list from xml file
        String[] naArray1 = getResources().getStringArray(R.array.AdherenceSurveyAnswersNA);
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

        final HorizontalScrollView horScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        final TextView questionsView = (TextView) layout_main.findViewById(R.id.questionView);
        //questionsView.setText("Click an above button to start the survey.");
        final Button button = (Button) findViewById(R.id.submitButton);
        final Button buttonNext = (Button) findViewById(R.id.nextButton);
        final Button buttonPrev = (Button) findViewById(R.id.prevButton);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lv);

        for (int i = 0; i < 8; i++) {
            final Button btnTag = new Button(this);
            btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btnTag.setText("Question " + (i + 1));
            btnTag.setId(Integer.parseInt(Integer.toString(i)));
            layout.addView(btnTag);
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qnum = btnTag.getId();
                    questCount = btnTag.getId();
                    horScrollView.scrollTo(qnum*388,0);
                    if (qnum<7){
                        if (qnum==0){
                            buttonPrev.setVisibility(view.INVISIBLE);
                        }
                        buttonNext.setVisibility(view.VISIBLE);
                    }
                    if (qnum>0){
                        if (qnum==7){
                            buttonNext.setVisibility(view.INVISIBLE);
                        }
                        buttonPrev.setVisibility(view.VISIBLE);
                    }
                    questionsView.setText(Integer.toString(qnum+1)+".  "+questionArray[qnum]);


                    final RadioButton opt1 = (RadioButton) findViewById(R.id.opt1);
                    final RadioButton opt2 = (RadioButton) findViewById(R.id.opt2);
                    final RadioButton opt3 = (RadioButton) findViewById(R.id.opt3);
                    final RadioButton opt4 = (RadioButton) findViewById(R.id.opt4);
                    final RadioButton opt5 = (RadioButton) findViewById(R.id.opt5);

                    opt1.setText(questionchoices.get(qnum)[0]);
                    opt1.setVisibility(view.VISIBLE);
                    opt2.setText(questionchoices.get(qnum)[1]);
                    opt2.setVisibility(view.VISIBLE);
                    //Set other buttons invisible
                    opt3.setVisibility(view.INVISIBLE);
                    opt4.setVisibility(view.INVISIBLE);
                    opt5.setVisibility(view.INVISIBLE);

                    if (qnum == 7){
                        opt3.setText(questionchoices.get(qnum)[2]);
                        opt3.setVisibility(view.VISIBLE);
                        opt4.setText(questionchoices.get(qnum)[3]);
                        opt4.setVisibility(view.VISIBLE);
                        opt5.setText(questionchoices.get(qnum)[4]);
                        opt5.setVisibility(view.VISIBLE);
                    }

                    if (answers[qnum] == 1) {
                        opt1.setChecked(true);
                        opt2.setChecked(false);
                        opt3.setChecked(false);
                        opt4.setChecked(false);
                        opt5.setChecked(false);
                    }
                    else if (answers[qnum] == 2){
                        opt1.setChecked(false);
                        opt2.setChecked(true);
                        opt3.setChecked(false);
                        opt4.setChecked(false);
                        opt5.setChecked(false);
                    }
                    else if (answers[qnum] == 3){
                        opt1.setChecked(false);
                        opt2.setChecked(false);
                        opt3.setChecked(true);
                        opt4.setChecked(false);
                        opt5.setChecked(false);
                    }
                    else if (answers[qnum] == 4) {
                        opt1.setChecked(false);
                        opt2.setChecked(false);
                        opt3.setChecked(false);
                        opt4.setChecked(true);
                        opt5.setChecked(false);

                    }else if(answers[qnum] == 5){
                        opt1.setChecked(false);
                        opt2.setChecked(false);
                        opt3.setChecked(false);
                        opt4.setChecked(false);
                        opt5.setChecked(true);
                    }else{
                        opt1.setChecked(false);
                        opt2.setChecked(false);
                        opt3.setChecked(false);
                        opt4.setChecked(false);
                        opt5.setChecked(false);
                    }


                    opt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                opt2.setChecked(false);
                                opt3.setChecked(false);
                                opt4.setChecked(false);
                                opt5.setChecked(false);
                                if (btnTag.getId()==qnum){
                                    //btnTag.setBackgroundResource(R.drawable.button_bg);
                                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                                    answers[qnum] = 1;
                                }
                                //TextView t;
                                //t.setText("The option, Option 1, has been checked below.");

                            }
                            int selected = 0;
                            for (int x : answers) {
                                if (x != -1) {
                                    selected++;
                                }
                                if (selected>7) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                    opt2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                answers[qnum] = 2;
                                opt1.setChecked(false);
                                opt3.setChecked(false);
                                opt4.setChecked(false);
                                opt5.setChecked(false);
                                if (btnTag.getId()==qnum){
                                    //btnTag.setBackgroundResource(R.drawable.button_bg);
                                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                                    answers[qnum] = 2;
                                }
                                //TextView t;
                                //t.setText("The option, Option 2, has been checked below.");
                            }
                            int selected = 0;
                            for (int x : answers) {
                                if (x != -1) {
                                    selected++;
                                }
                                if (selected>7) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                    opt3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                answers[qnum] = 3;
                                opt1.setChecked(false);
                                opt2.setChecked(false);
                                opt4.setChecked(false);
                                opt5.setChecked(false);
                                if (btnTag.getId()==qnum){
                                    //btnTag.setBackgroundResource(R.drawable.horizontal_buttons_scroll);
                                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                                    answers[qnum] = 3;
                                }
                                //TextView t;
                                //t.setText("The option, Option 3, has been checked below.");
                            }
                            int selected = 0;
                            for (int x : answers) {
                                if (x != -1) {
                                    selected++;
                                }
                                if (selected>7) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                    opt4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {

                                answers[qnum] = 4;
                                opt1.setChecked(false);
                                opt2.setChecked(false);
                                opt3.setChecked(false);
                                opt5.setChecked(false);
                                if (btnTag.getId()==qnum){
                                    //btnTag.setBackgroundResource(R.drawable.button_bg);
                                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                                    answers[qnum] = 4;
                                }
                                //TextView t;
                                //t.setText("The option, Option 3, has been checked below.");
                            }
                            int selected = 0;
                            for (int x : answers) {
                                if (x != -1) {
                                    selected++;
                                }
                                if (selected>7) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                    opt5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {

                                answers[qnum] = 5;
                                opt1.setChecked(false);
                                opt2.setChecked(false);
                                opt3.setChecked(false);
                                opt4.setChecked(false);
                                if (btnTag.getId()==qnum){
                                    //btnTag.setBackgroundResource(R.drawable.button_bg);
                                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                                    answers[qnum] = 5;
                                }
                                //TextView t;
                                //t.setText("The option, Option 3, has been checked below.");
                            }
                            int selected = 0;
                            for (int x : answers) {
                                if (x != -1) {
                                    selected++;
                                }
                                if (selected>7) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });


                }
            });

        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                questCount++;
                btnTag = (Button) findViewById(qnum+1);
                if (questCount < 8){
                    btnTag.performClick();
                }

            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                questCount--;
                btnTag = (Button) findViewById(qnum-1);
                if (questCount >= 0) {
                    btnTag.performClick();
                }
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selected=0;
                int nonselected = 0;
                for (int x : answers) {
                    if (x != -1) {
                        selected++;
                    }
                    else{
                        nonselected++;
                    }
                }
                if (selected>7) {
                    Snackbar.make(v, "Medication Adherence Survey Successfully Completed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Calendar now = Calendar.getInstance();
                    String year = Integer.toString(now.get(Calendar.YEAR));
                    String month = Integer.toString(now.get(Calendar.MONTH) + 1); // Note: zero based!
                    String day = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
                    String UID = ((MyApplication) MedicationAdherenceSurvey.this.getApplication()).getUID();


                    mDatabase.child("app").child("users").child(UID).child("adherencesurveyanswersRW").child(year+"-"+month+"-"+day).setValue(Arrays.toString(answers));


                    Intent i = new Intent(MedicationAdherenceSurvey.this, AdherenceFeedbackActivity.class);
                    i.putExtra("surveyResponse",answers);
                    startActivity(i);
                }
                else{
                    Snackbar.make(v, "Please complete all 8 questions. "+nonselected+" questions remain. Please scroll through the buttons above.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });



    }
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        mDatabase.child("app").child("users").child(UID).child("pharmanumber").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phonenumber = dataSnapshot.getValue().toString();
                        Log.e("Phone",phonenumber);
                        ((MyApplication) MedicationAdherenceSurvey.this.getApplication()).setPharmaPhone(phonenumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();

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
            i.setData(Uri.parse("tel:"+tel));
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
