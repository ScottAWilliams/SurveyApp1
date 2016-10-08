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

import java.util.ArrayList;
import java.util.Iterator;

public class SurveySelectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDatabase;
    Context ctx;
    public static final String PREFS_NAME = "MyPrefsFile";
    final ArrayList<String> surveyFinalDate = new ArrayList<>();
    String surveyDate;
    int surYear;
    int surMonth;
    int surDay;
    int curYear;
    int curMonth;
    int curDay;

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

    public void setDatesArray(ArrayList<String> surveyDates){
        //ArrayList<String> surveyReturnDates = new ArrayList<>();
        Log.e("SurveyDates",surveyDates.toString());
        surveyFinalDate.addAll(surveyDates);
    }


    public void initializeSurveyButtons() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        final String currentDate = dateFormat.format(cal.getTime());
        Log.e("is0",currentDate.substring(currentDate.indexOf("-")+1,currentDate.indexOf("-")+2));


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

                                    String currDate="";
                                    if (Integer.parseInt(currentDate.substring(currentDate.indexOf("-")+1,currentDate.indexOf("-")+2))==0){
                                        currDate = currDate.concat(currentDate.substring(0,currentDate.indexOf("-")+1)).concat(currentDate.substring(currentDate.indexOf("-")+2,currentDate.length()));
                                    }
                                    else{
                                        currDate = currentDate;
                                    }
Log.e("surv",surveyDate);

                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));



                                    GregorianCalendar oldDate = new GregorianCalendar();
                                    oldDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    oldDate.set(GregorianCalendar.MONTH, 1);
                                    oldDate.set(GregorianCalendar.YEAR, 1990);
                                    GregorianCalendar curr = new GregorianCalendar();
                                    curr.set(GregorianCalendar.DAY_OF_MONTH, curDay);
                                    curr.set(GregorianCalendar.MONTH, curMonth);
                                    curr.set(GregorianCalendar.YEAR, curYear);
                                    GregorianCalendar surv = new GregorianCalendar();
                                    surv.set(GregorianCalendar.DAY_OF_MONTH, surDay);
                                    surv.set(GregorianCalendar.MONTH, surMonth);
                                    surv.set(GregorianCalendar.YEAR, surYear);
                                    int daysPassed=(curr.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR))-(surv.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR));



                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SurveySelectionActivity.this, LifestyleFeedbackActivity.class);
                                        i.putExtra("month", surMonth); //number corresponds to survey
                                        i.putExtra("day", surDay); //number corresponds to survey
                                        i.putExtra("year", surYear); //number corresponds to survey
                                        startActivity(i);

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

                                    String currDate="";
                                    if (Integer.parseInt(currentDate.substring(currentDate.indexOf("-")+1,currentDate.indexOf("-")+2))==0){
                                        currDate = currDate.concat(currentDate.substring(0,currentDate.indexOf("-")+1)).concat(currentDate.substring(currentDate.indexOf("-")+2,currentDate.length()));
                                    }
                                    else{
                                        currDate = currentDate;
                                    }


                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));


                                    GregorianCalendar oldDate = new GregorianCalendar();
                                    oldDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    oldDate.set(GregorianCalendar.MONTH, 1);
                                    oldDate.set(GregorianCalendar.YEAR, 1990);
                                    GregorianCalendar curr = new GregorianCalendar();
                                    curr.set(GregorianCalendar.DAY_OF_MONTH, curDay);
                                    curr.set(GregorianCalendar.MONTH, curMonth);
                                    curr.set(GregorianCalendar.YEAR, curYear);
                                    GregorianCalendar surv = new GregorianCalendar();
                                    surv.set(GregorianCalendar.DAY_OF_MONTH, surDay);
                                    surv.set(GregorianCalendar.MONTH, surMonth);
                                    surv.set(GregorianCalendar.YEAR, surYear);
                                    int daysPassed=(curr.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR))-(surv.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR));



                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(SurveySelectionActivity.this, AdherenceFeedbackActivity.class);
                                        i.putExtra("month", surMonth); //number corresponds to survey
                                        i.putExtra("day", surDay); //number corresponds to survey
                                        i.putExtra("year", surYear); //number corresponds to survey
                                        startActivity(i);

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

                                    String currDate="";
                                    if (Integer.parseInt(currentDate.substring(currentDate.indexOf("-")+1,currentDate.indexOf("-")+2))==0){
                                        currDate = currDate.concat(currentDate.substring(0,currentDate.indexOf("-")+1)).concat(currentDate.substring(currentDate.indexOf("-")+2,currentDate.length()));
                                    }
                                    else{
                                        currDate = currentDate;
                                    }


                                    surYear = Integer.parseInt(surveyDate.substring(0, surveyDate.indexOf("-")));
                                    surMonth = Integer.parseInt(surveyDate.substring(surveyDate.indexOf("-") + 1, surveyDate.lastIndexOf("-")));
                                    surDay = Integer.parseInt(surveyDate.substring(surveyDate.lastIndexOf("-") + 1, surveyDate.length()));

                                    curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                                    curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                                    curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));



                                    GregorianCalendar oldDate = new GregorianCalendar();
                                    oldDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
                                    oldDate.set(GregorianCalendar.MONTH, 1);
                                    oldDate.set(GregorianCalendar.YEAR, 1990);
                                    GregorianCalendar curr = new GregorianCalendar();
                                    curr.set(GregorianCalendar.DAY_OF_MONTH, curDay);
                                    curr.set(GregorianCalendar.MONTH, curMonth);
                                    curr.set(GregorianCalendar.YEAR, curYear);
                                    GregorianCalendar surv = new GregorianCalendar();
                                    surv.set(GregorianCalendar.DAY_OF_MONTH, surDay);
                                    surv.set(GregorianCalendar.MONTH, surMonth);
                                    surv.set(GregorianCalendar.YEAR, surYear);
                                    int daysPassed=(curr.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR))-(surv.get(GregorianCalendar.DAY_OF_YEAR)-oldDate.get(GregorianCalendar.DAY_OF_YEAR));



                                    if (daysPassed<31) {
                                        Toast.makeText(ctx, "You've taken this survey in the past month, please take again in " + (31-daysPassed) + " days.", Toast.LENGTH_SHORT).show();

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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        mDatabase.child("app").child("users").child(UID).child("pharmanumber").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phonenumber = dataSnapshot.getValue().toString();
                        Log.e("Phone",phonenumber);
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
