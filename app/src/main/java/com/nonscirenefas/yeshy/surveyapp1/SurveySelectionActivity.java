package com.nonscirenefas.yeshy.surveyapp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SurveySelectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDatabase;
    Context ctx;
    public static final String HSURVEY_FILENAME = "hsurvey_file";
    public static final String PREFS_NAME = "MyPrefsFile";
    int surYear;
    int surMonth;
    int surDay;
    int curYear;
    int curMonth;
    int curDay;
    String surveyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_selection);
        ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Survey List");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeSurveyButtons();
    }

    public void initializeSurveyButtons() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Log.e("printed data",dateFormat.format(cal.getTime()));
        final String currDate = dateFormat.format(cal.getTime());
        Log.e("currentDateSurvSelect",currDate);

        Button lifestyleSurvey = (Button) findViewById(R.id.lifestylesurveybutton);
        Button medicalAdherenceSurvey = (Button) findViewById(R.id.medicaladherencesurveybutton);
        Button healthLiteracySurvey = (Button) findViewById(R.id.healthliteracysurveybutton);

        lifestyleSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String UID = ((MyApplication) SurveySelectionActivity.this.getApplication()).getUID();
                mDatabase.child("app").child("users").child(UID).child("lifestylesurveyanswersRW").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> records = new ArrayList<>();
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                //System.out.println(dataSnapshot);
                                if (it.hasNext()) {
                                    while (it.hasNext()) {
                                        DataSnapshot medicine = (DataSnapshot) it.next();

                                        records.add(medicine.getKey());
                                    }
                                    String[] mArray = new String[records.size()];
                                    mArray = records.toArray(mArray);
                                    surveyDate = mArray[mArray.length - 1];

                                    Log.e("survey Date",surveyDate);

                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));



                                    GregorianCalendar old = new GregorianCalendar();
                                    old.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    old.set(GregorianCalendar.MONTH, 1);
                                    old.set(GregorianCalendar.YEAR, 2010);
                                    Date current = new Date(curYear,curMonth,curDay);// some Dat
                                    Date survey = new Date(surYear,surMonth,surDay);// some Date
                                    Date oldDateTime = new Date(1,1,2010);
                                    int one = (int)getDifferenceDays(oldDateTime,current);
                                    int two = (int)getDifferenceDays(oldDateTime,survey);


                                    int daysPassed = one-two;

                                    //Log.e("days since survey",Integer.toString(daysPassed));

                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ctx, LifestyleFeedbackActivity.class);
                                        i.putExtra("date",surveyDate);
                                        //i.putExtra("date", surveyDate); //number corresponds to survey
                                        startActivity(i);
                                        finish();

                                    } else {
                                        Intent i = new Intent(SurveySelectionActivity.this, LifestyleSurvey.class);
                                        //i.putExtra("name", 1); //number corresponds to survey
                                        startActivity(i);
                                    }
                                }
                                else{
                                    Intent i = new Intent(SurveySelectionActivity.this, LifestyleSurvey.class);
                                    //i.putExtra("name", 1); //number corresponds to survey
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
        });

        medicalAdherenceSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String UID = ((MyApplication) SurveySelectionActivity.this.getApplication()).getUID();
                mDatabase.child("app").child("users").child(UID).child("adherencesurveyanswersRW").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> records = new ArrayList<>();
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                //System.out.println(dataSnapshot);
                                if (it.hasNext()) {
                                    while (it.hasNext()) {
                                        DataSnapshot medicine = (DataSnapshot) it.next();

                                        records.add(medicine.getKey());
                                    }
                                    String[] mArray = new String[records.size()];
                                    mArray = records.toArray(mArray);
                                    surveyDate = mArray[mArray.length - 1];

                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));



                                    GregorianCalendar old = new GregorianCalendar();
                                    old.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    old.set(GregorianCalendar.MONTH, 1);
                                    old.set(GregorianCalendar.YEAR, 2010);
                                    Date current = new Date(curYear,curMonth,curDay);// some Dat
                                    Date survey = new Date(surYear,surMonth,surDay);// some Date
                                    Date oldDateTime = new Date(1,1,2010);
                                    int one = (int)getDifferenceDays(oldDateTime,current);
                                    int two = (int)getDifferenceDays(oldDateTime,survey);


                                    int daysPassed = one-two;

                                    //Log.e("days since survey",Integer.toString(daysPassed));

                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(SurveySelectionActivity.this, AdherenceFeedbackActivity.class);
                                        i.putExtra("date", surveyDate);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        Intent i = new Intent(SurveySelectionActivity.this, MedicationAdherenceSurvey.class);
                                        //i.putExtra("name", 1); //number corresponds to survey
                                        startActivity(i);
                                    }
                                }
                                else{
                                    Intent i = new Intent(SurveySelectionActivity.this, MedicationAdherenceSurvey.class);
                                    //i.putExtra("name", 1); //number corresponds to survey
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
        });

        healthLiteracySurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String UID = ((MyApplication) SurveySelectionActivity.this.getApplication()).getUID();
                mDatabase.child("app").child("users").child(UID).child("literacysurveyanswersRW").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> records = new ArrayList<>();
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                //System.out.println(dataSnapshot);
                                if (it.hasNext()) {
                                    while (it.hasNext()) {
                                        DataSnapshot medicine = (DataSnapshot) it.next();

                                        records.add(medicine.getKey());
                                    }
                                    String[] mArray = new String[records.size()];
                                    mArray = records.toArray(mArray);
                                    surveyDate = mArray[mArray.length - 1];

                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));



                                    GregorianCalendar old = new GregorianCalendar();
                                    old.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    old.set(GregorianCalendar.MONTH, 1);
                                    old.set(GregorianCalendar.YEAR, 2010);
                                    Date current = new Date(curYear,curMonth,curDay);// some Dat
                                    Date survey = new Date(surYear,surMonth,surDay);// some Date
                                    Date oldDateTime = new Date(1,1,2010);
                                    int one = (int)getDifferenceDays(oldDateTime,current);
                                    int two = (int)getDifferenceDays(oldDateTime,survey);


                                    int daysPassed = one-two;

                                    //Log.e("days since survey",Integer.toString(daysPassed));

                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();

                                        //Log.e("tryin",surveyDate);
                                        try {
                                            FileOutputStream fos = openFileOutput(HSURVEY_FILENAME, Context.MODE_WORLD_READABLE);
                                            fos.write(surveyDate.getBytes());
                                            fos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            //Log.e("tryin1","doesn't exist?");

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        startAlarm(SurveySelectionActivity.this);
                                    } else {
                                        Intent i = new Intent(SurveySelectionActivity.this, HealthLitParagraphActivity.class);
                                        startActivity(i);
                                    }
                                }
                                else{
                                    Intent i = new Intent(SurveySelectionActivity.this, HealthLitParagraphActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });

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
        //intent.putExtra("date", surveyDate);
        //intent.putExtra("type", "Health Literacy");
        //Calendar now = Calendar.getInstance();
        //intent.putExtra("dayofYear",now.get(Calendar.DAY_OF_YEAR));
        startService(intent);
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


    public long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        mDatabase.child("app").child("users").child(UID).child("pharmanumber").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phonenumber = dataSnapshot.getValue().toString();
                        //Log.e("Phone",phonenumber);
                        ((MyApplication) SurveySelectionActivity.this.getApplication()).setPharmaPhone(phonenumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();

        int id = item.getItemId();
        if (id  == R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
            finish();
        }  else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);
            finish();
        }else if (id == R.id.nav_surveys) {
            //Intent i = new Intent(this, SurveySelectionActivity.class);
            //startActivity(i);

        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+tel));
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String UIDstored = settings.getString("UID", "Default");
            //Log.e("logout", UIDstored);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UID", "Default");
            editor.commit();
            UIDstored = settings.getString("UID", "Default");
            //Log.e("logout", UIDstored);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_study_contact) {
            Intent i = new Intent(this, StudyContactsActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
