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
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yeshy on 7/12/2016.
 */
public class MedicationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private MedicationActivity medAct;
    private DatabaseReference mDatabase;
    Intent intent;
    Context ctx;
    public static final String PREFS_NAME = "MyPrefsFile";
    int curYear;
    int curMonth;
    int curDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        setContentView(R.layout.activity_medication);
        ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Medication Records");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeCalendar();
        //finish();
    }

    public void initializeCalendar() {
        CalendarView calendarMed = (CalendarView) findViewById(R.id.calendarMed);

        //calendarMed.setBackground(getResources().getDrawable(R.drawable.common_google_signin_btn_icon_dark));

        calendarMed.setSelectedDateVerticalBar(getResources().getDrawable(R.drawable.common_google_signin_btn_text_dark_normal));
        //calendarMed.setDateTextAppearance(R.style.TextTheme);
        calendarMed.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,int day) {
                //finish();
                //Intent i = new Intent(ctx, MedicationLogActivity.class);
                //i.putExtra("date", String.format("%d-%d-%d",year, month+1, day));
                //Log.e("nrp",String.format("%d-%d",year, month+1, day));
                //startActivity(i);
                //finish();




                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                System.out.println(dateFormat.format(cal.getTime()));
                final String currentDate = dateFormat.format(cal.getTime());
                String currDate="";
                    currDate = currentDate;

                curYear = Integer.parseInt(currDate.substring(0, currDate.indexOf("-")));
                curMonth = Integer.parseInt(currDate.substring(currDate.indexOf("-") + 1, currDate.lastIndexOf("-")));
                curDay = Integer.parseInt(currDate.substring(currDate.lastIndexOf("-") + 1, currDate.length()));


                GregorianCalendar old = new GregorianCalendar();
                old.set(GregorianCalendar.DAY_OF_MONTH, day);
                old.set(GregorianCalendar.MONTH, month);
                old.set(GregorianCalendar.YEAR, year);
                Date current = new Date(curYear,curMonth,curDay);// some Dat
                Date survey = new Date(year,month+1,day);// some Date
                Date oldDateTime = new Date(1,1,2010);
                Log.e("surveyMonth",Integer.toString(month));
                int one = (int)getDifferenceDays(oldDateTime,current);
                int two = (int)getDifferenceDays(oldDateTime,survey);


                int daysSince = one-two;

                if (daysSince>=0) {
                    Intent i = new Intent(ctx, MedicationLogActivity.class);
                    //old.set(GregorianCalendar.MONTH, date.getMonth()-1);
                    i.putExtra("date", dateFormat.format(old));//Log.e("nrp",String.format("%d-%d", date.getMonth(), date.getDay()));
                    startActivity(i);
                    //Snackbar.make(view, String.format("%d-%d", date.getMonth(), date.getDay()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{
                    Snackbar.make(view,"Please only edit the current or past days.",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();

        mDatabase.child("app").child("users").child(UID).child("pharmanumber").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phonenumber = dataSnapshot.getValue().toString();
                        Log.e("Phone",phonenumber);
                        ((MyApplication) MedicationActivity.this.getApplication()).setPharmaPhone(phonenumber);
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
            finish();
        }
        else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
            finish();
        }  else if (id == R.id.nav_medication) {
            //Intent i = new Intent(this, MedicationActivity.class);
            //startActivity(i);

        }else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, SurveySelectionActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+tel));
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logout) {
            finish();
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
