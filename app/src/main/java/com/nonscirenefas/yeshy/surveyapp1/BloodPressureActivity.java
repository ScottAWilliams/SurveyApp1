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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by Yeshy on 7/12/2016.
 */
public class BloodPressureActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Context ctx;
    private DatabaseReference mDatabase;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<String> bpDates = new ArrayList<String>();
    ArrayList<String> recordedBP = new ArrayList<String>();
    final ArrayList<ArrayList<Integer>> sysMeasurements = new ArrayList<ArrayList<Integer>>();
    final ArrayList<ArrayList<Integer>> diaMeasurements = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> sysFinal = new ArrayList<ArrayList<Integer>>();
     ArrayList<ArrayList<Integer>> diaFinal = new ArrayList<ArrayList<Integer>>();

    String goal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Blood Pressure Records");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mDatabase= FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();
        mDatabase.child("app").child("users").child(UID).child("bloodPressureLog").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //records = new String[]{" "," "," "," "};
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        //Log.e("DataSnapgetKey", dataSnapshot.getValue().toString());
                        System.out.println(dataSnapshot);
                        int g=0;
                        while (it.hasNext()) {
                            DataSnapshot bpDate = (DataSnapshot) it.next();
                            String key = bpDate.getKey().toString();
                            String value = bpDate.getValue().toString();
                            bpDates.add(g,key);
                            recordedBP.add(g,value);
                            //Log.e("DataSnapgetValue2", recordedBP.toString();
                            g++;
                        }
                        setBPValues(bpDates,recordedBP);
                        //parse dia and sys measurements

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        mDatabase.child("app").child("users").child(UID).child("bloodpressuregoal").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String goal = dataSnapshot.getValue().toString();
                        Log.e("BP goal 1",goal);
                        initializeCalendar(goal);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }

                });

        //initializeCalendar();



    }
    public void setBPValues(ArrayList<String> bpDates, ArrayList<String> recordedBP){
        Log.e("Correct??", bpDates.toString());
        Log.e("Correct???", recordedBP.toString());

        for(int i = 0; i< bpDates.size(); i++){
            ArrayList <Integer> sysDay = new ArrayList <Integer>();
            ArrayList <Integer> diaDay = new ArrayList <Integer>();
            String currentValue = recordedBP.get(i);
            int indComma = currentValue.indexOf(",");
            int indDash;
            int indEquals;
            while (indComma!=-1){
                indComma = currentValue.indexOf(",");
                indDash = currentValue.indexOf("-");
                indEquals = currentValue.indexOf("=");
                sysDay.add(Integer.parseInt(currentValue.substring(indEquals+1,indDash)));
                Log.e("sysDayValue",currentValue.substring(indEquals+1,indDash));
                if (indComma==-1){
                    diaDay.add(Integer.parseInt(currentValue.substring(indDash+1,currentValue.length()-1)));
                    Log.e("diaDayValue",currentValue.substring(indDash+1,currentValue.length()-1));
                }
                else{
                    diaDay.add(Integer.parseInt(currentValue.substring(indDash+1,indComma)));
                    Log.e("diaDayValue",currentValue.substring(indDash+1,indComma));
                    currentValue = currentValue.substring(indComma+1,currentValue.length());
                }
            }
            Log.e("CorrectSys", sysDay.toString());
            Log.e("CorrectDia", diaDay.toString());
            addArray(i,sysDay,diaDay);
            //sysDay.removeAll(sysDay);
            //diaDay.removeAll(diaDay);
        }

    }

    public void addArray(int i,ArrayList<Integer> sysDay, ArrayList<Integer> diaDay){
        Log.e("GottenSys", sysDay.toString());
        Log.e("GottenDia", diaDay.toString());
        sysMeasurements.add(i,sysDay);
        diaMeasurements.add(i,diaDay);
        setSysMeasurements(sysMeasurements);
        setDiaMeasurements(diaMeasurements);
        Log.e("CorrectFinal1", sysMeasurements.toString());
        Log.e("CorrectFinal2", diaMeasurements.toString());
    }
    public void setSysMeasurements(ArrayList<ArrayList<Integer>> sysMeasurements){
        Log.e("eeeeee", sysMeasurements.toString());
        sysMeasurements = sysMeasurements;
    }

    public void setDiaMeasurements(ArrayList<ArrayList<Integer>> diaMeasurements){
        Log.e("eeeeee", diaMeasurements.toString());
        diaMeasurements = diaMeasurements;
    }

    public ArrayList<ArrayList<Integer>> returnSysMeasurements(){
        return sysMeasurements;
    }

    public ArrayList<ArrayList<Integer>> returnDiaMeasurements(){
        return diaMeasurements;
    }

    public void initializeCalendar(String goal) {
        MCalendarView calendar = (MCalendarView) findViewById(R.id.calendar);
        // sets whether to show the week number.
        //calendar.setShowWeekNumber(false);

        String goalSys = goal.substring(goal.lastIndexOf(" ")+1,goal.indexOf("/"));
        String goalDia = goal.substring(goal.indexOf("/")+1,goal.length());
        Log.e("Works?Sys", returnSysMeasurements().toString());
        Log.e("Works?Dia", returnDiaMeasurements().toString());
        int foundDia;
        int foundSys;
        int month;
        int day;

        for (int i=0;i<returnSysMeasurements().size();i++) {
            if (returnDiaMeasurements().get(i).size()>0) {
                foundDia = returnDiaMeasurements().get(i).get(0);
                for (int g = 1; g < returnDiaMeasurements().get(i).size(); g++) {
                    if (returnDiaMeasurements().get(i).get(g) > foundDia) {
                        foundDia = returnDiaMeasurements().get(i).get(g);
                    }
                }
                foundSys = returnSysMeasurements().get(i).get(0);
                for (int g = 1; g < returnSysMeasurements().get(i).size(); g++) {
                    if (returnSysMeasurements().get(i).get(g) > foundDia) {
                        foundSys = returnSysMeasurements().get(i).get(g);
                    }
                }
                month = Integer.parseInt(bpDates.get(i).substring(0, bpDates.get(i).indexOf("-")));
                Log.e("month", String.valueOf(month));
                Log.e("day", bpDates.get(i).substring(bpDates.get(i).indexOf("-") + 1, bpDates.get(i).length()));
                day = Integer.parseInt(bpDates.get(i).substring(bpDates.get(i).indexOf("-") + 1, bpDates.get(i).length()));
                Log.e("day", String.valueOf(day));
            }
            else{
                foundDia=0;foundSys=0;
                month = Integer.parseInt(bpDates.get(i).substring(0, bpDates.get(i).indexOf("-")));
                Log.e("month", String.valueOf(month));
                Log.e("day", bpDates.get(i).substring(bpDates.get(i).indexOf("-") + 1, bpDates.get(i).length()));
                day = Integer.parseInt(bpDates.get(i).substring(bpDates.get(i).indexOf("-") + 1, bpDates.get(i).length()));
                Log.e("day", String.valueOf(day));
            }


            if (Integer.parseInt(goalDia) < foundDia | Integer.parseInt(goalSys) < foundSys) {
                calendar.markDate(
                        new DateData(2016,month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)
                        ));
                //Log.e("markDate",month+"/"+day);
            } else {
                calendar.markDate(
                        new DateData(2016, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)
                        ));
                //Log.e("markDate",month+"/"+day);
            }
        }

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        //calendar.setFirstDayOfWeek(1);

        //have this part relay to the database
        calendar.markDate(
                new DateData(2016, 7, 2).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN)
                ));
//Change colors based on what's in Firebase bloodPressureLog compared to goal
        //get dates and logged systolic and diastolic measurements





        //sets the listener to be notified upon selected date change.
        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                Intent i = new Intent(ctx, BloodPressureLogActivity.class);
                i.putExtra("date", String.format("%d-%d", date.getMonth(), date.getDay()));
                Log.e("nrp",String.format("%d-%d", date.getMonth(), date.getDay()));
                startActivity(i);
                /*
                Snackbar.make(view, String.format("%d-%d", date.getMonth(), date.getDay()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(BloodPressureActivity.this, String.format("%d-%d", date.getMonth(), date.getDay()), Toast.LENGTH_SHORT).show();
                */
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
        int id = item.getItemId();
        if (id  == R.id.nav_home){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_bloodpressure) {
            //Intent i = new Intent(this, BloodPressureActivity.class);
            //startActivity(i);
        }  else if (id == R.id.nav_medication) {
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
