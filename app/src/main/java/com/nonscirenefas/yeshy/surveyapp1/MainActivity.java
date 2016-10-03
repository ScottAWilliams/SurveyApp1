package com.nonscirenefas.yeshy.surveyapp1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PHONE_FILENAME = "phone_file";
    private DatabaseReference mDatabase;

    int[] lifestyleArray = new int[8];
    int[] adherenceArray = new int[8];
    String[] surveyResponse = new String[16];
    ArrayList<String> medArray= new ArrayList<String>();
    ArrayList<String> medFrequency= new ArrayList<String>();
    //private ArrayList<Medication> medicationList = new ArrayList<>();
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayAdapter<String> adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String UID = ((MyApplication) this.getApplication()).getUID();
        //String attempt2 = ((MyApplication) MainActivity.this.getApplication()).getPhone();

        //Log.e("Phone2",attempt2);
        getMeds(); // this creates a meds array and a frequency array from those meds
        //TODO: in getMedFrequency function branch off to another function that creates the alarms
        //Don't do it in this onCreate function, do it after the frequency array is finished being formed in the
        //getMedFrequency function.
        /*
        mDatabase.child("app").child("users").child(UID).child("medicine").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((MyApplication) MainActivity.this.getApplication()).resetMedicationList();
                        ArrayList<String> onePerDay = new ArrayList<>();
                        ArrayList<String> twoPerDay = new ArrayList<>();
                        ArrayList<String> threePerDay = new ArrayList<>();
                        ArrayList<String> fourPerDay = new ArrayList<>();
                        // Get user value
                        Log.e("reading",dataSnapshot.toString());

                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            Log.e("reading2", medicine.toString());
                            Log.e("reading3", medicine.getKey());
                            Log.e("reading4", medicine.child(medicine.getKey()).toString());


                            //TODO: I believe error occurs here with setting to type Medication (Problem converting String to Medication)
                            Medication med = medicine.getValue(Medication.class);
                            ((MyApplication) MainActivity.this.getApplication()).addMedication(med);

                            Log.e("reading5", med.getName() + " " + med.getDaysSupply() + " " + med.getFrequency());

                            if(med.getFrequency().equals("Daily")) {
                                onePerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Twice daily")) {
                                twoPerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Three times daily")) {
                                threePerDay.add(med.getName());
                            } else if(med.getFrequency().equals("Four times daily")) {
                                fourPerDay.add(med.getName());
                            }
                        }
                        //User user = dataSnapshot.getValue(User.class);

                        // ...
                        medicationList = ((MyApplication) MainActivity.this.getApplication()).getMedicationList();

                        String oneDay = Arrays.toString(onePerDay.toArray());
                        String twoDay = Arrays.toString(twoPerDay.toArray());
                        String threeDay = Arrays.toString(threePerDay.toArray());
                        String fourDay = Arrays.toString(fourPerDay.toArray());

                        SharedPreferences settings = getSharedPreferences("Medication", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("onePerDay", oneDay);
                        editor.putString("twoPerDay", twoDay);
                        editor.putString("threePerDay", threeDay);
                        editor.putString("fourPerDay", fourDay);
                        editor.commit();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
*/


/*
        final ArrayList<String> responseArray = new ArrayList<>();
        final int[] surveyResponses = new int[8];
        Log.e("before Data","Lifestyle");
        mDatabase.child("app").child("users").child(UID).child("lifestylesurveyanswersRW").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("in data","Lifestyle");
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        System.out.println(dataSnapshot);
                        int g=0;
                        Log.e("before while","Lifestyle");
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            String attempts =  medicine.getValue().toString();
                            responseArray.add(attempts);
                            g++;
                        }
                        Log.e("left while","Lifestyle");
                        ArrayList<String> finalresponses = new ArrayList<String>(Arrays.asList(responseArray.get(g-1).split(",")));
                        for (int i=0;i<finalresponses.size();i++) {
                            if(i==finalresponses.size()-1) {
                                surveyResponses[i] =  Integer.parseInt(finalresponses.get(i).substring(1, (finalresponses.get(i)).length()-1));
                            }
                            else {
                                surveyResponses[i] = Integer.parseInt(finalresponses.get(i).substring(1, (finalresponses.get(i)).length()));
                            }
                            Log.e("eeeeeeee1",Arrays.toString(surveyResponses));
                        }
                        Log.e("eee1",Arrays.toString(surveyResponses));
                        setLifestyleResponse(surveyResponses);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }

                });
*/
        findLifestyleFeedback();
        findAdherenceFeedback();
        //startAlarm(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void getMeds(){
        String UID = ((MyApplication) this.getApplication()).getUID();
        mDatabase.child("app").child("users").child(UID).child("medicine").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("reading", dataSnapshot.toString());

                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            Log.e("reading2", medicine.toString());
                            Log.e("reading3", medicine.getKey());
                            medArray.add(medicine.getKey().toString());
                        }
                        Log.e("meds", medArray.toString());

                        getMedFrequency(medArray);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public void getMedFrequency(final ArrayList<String> medArray){
        String UID = ((MyApplication) this.getApplication()).getUID();
        for(int i=0;i<medArray.size();i++) {
            final int finalI = i;
            mDatabase.child("app").child("users").child(UID).child("medicine").child(medArray.get(i)).child("frequency").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("currentMed", medArray.get(finalI));
                            Log.e("reading", dataSnapshot.getValue().toString());
                            medFrequency.add(dataSnapshot.getValue().toString());
                            Log.e("medFrequency", medFrequency.toString());
                            if (finalI==medArray.size()-1){
                                Log.e("medFrequencyFinal", medFrequency.toString());
                                //TODO: Add Alarm function here based on frequency array
                                //setAlarm(medArray,medFrequency) to add the name of the medicine as well
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
        }
    }

    public void startAlarm(Context context) {
        //first notification at 10 AM next day

        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);

        Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 1);
        final int _id = (int) cal.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //second notification at 12 PM next day
        cal.set(Calendar.HOUR_OF_DAY, 12); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 2);
        final int _id1 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id1, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //third notification at 2 PM next day
        cal.set(Calendar.HOUR_OF_DAY, 14); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 3);
        final int _id2 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id2, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
/*
        //third notification at 10 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 10); //18:32
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) -1);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 1);
        final int _id3 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id3, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //second notification at 12 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 12); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 2);
        final int _id4 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id4, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //third notification at 2 PM current day
        cal.set(Calendar.HOUR_OF_DAY, 14); //18:32
        cal.set(Calendar.MINUTE, 0);
        alarmIntent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent.putExtra("type", 3);
        final int _id5 = (int) cal.getTimeInMillis();
        pendingIntent = PendingIntent.getBroadcast(this, _id5, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        */
    }

    private void initializeMessagesList() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        //Random generator = new Random();
        int num = day % surveyResponse.length;
        //Log.e("num",Integer.toString(num));

        //String [] mArrayBefore = new String[messages.size()];
        //mArrayBefore = messages.toArray(mArrayBefore);
        String[] mArray = {"Messages will appear here once the survey data has loaded."};
        if (num < surveyResponse.length) {
            mArray[0] = surveyResponse[num];
        }

        adapter = new ArrayAdapter<String>(this, R.layout.tip_of_the_day, mArray);
        //setListAdapter(adapter);
        final ListView lv = (ListView) findViewById(R.id.messagesListView);
        lv.setAdapter(adapter);

    }

    public void setLifestyleResponse(int[] intArray1) {
        String[] posArray = getResources().getStringArray(R.array.LifestylePositiveMessagesArray);
        String[] negArray = getResources().getStringArray(R.array.LifestyleNegativeMessagesArray);
        final int[] correctChoice = {5, 1, 2, 2, 1, 2, 2, 5};
        final int[] wrongChoice = {0, 0, 1, 1, 0, 1, 1, 0};

        for (int ind = 0; ind < intArray1.length; ind++) {
            if (intArray1[ind] == correctChoice[ind]) {
                surveyResponse[ind + 8] = posArray[ind];
            } else if (wrongChoice[ind]==0 | intArray1[ind] == wrongChoice[ind]) {
                surveyResponse[ind + 8] = negArray[ind];
            }
        }
    }

    public void setAdherenceResponse(int[] intArray2) {
        String[] posArray1 = getResources().getStringArray(R.array.AdherencePositiveMessagesArray);
        String[] negArray1 = getResources().getStringArray(R.array.AdherenceNegativeMessagesArray);
        final int[] correctChoice1 = {2, 0, 0, 0, 1, 0, 0, 0};
        final int[] wrongChoice1 = {1, 1, 1, 1, 2, 1, 1, 0};
        for (int ind = 0; ind < intArray2.length; ind++) {
            if (intArray2[ind] == correctChoice1[ind]) {
                surveyResponse[ind] = posArray1[ind];
            } else if (wrongChoice1[ind]==0 | intArray2[ind] == wrongChoice1[ind]) {
                surveyResponse[ind] = negArray1[ind];
            }
        }
        initializeMessagesList();
    }

    public void findLifestyleFeedback() {
        String UID = ((MyApplication) this.getApplication()).getUID();
        final ArrayList<String> responseArray = new ArrayList<>();
        final int[] surveyResponses = new int[8];
        mDatabase.child("app").child("users").child(UID).child("lifestylesurveyanswersRW").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        int g = 0;
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            String attempts = medicine.getValue().toString();
                            responseArray.add(attempts);
                            g++;
                        }
                        //Log.e("left while","Adherence");
                        ArrayList<String> finalresponses = new ArrayList<String>(Arrays.asList(responseArray.get(responseArray.size()-1).split(",")));
                        for (int i = 0; i < finalresponses.size(); i++) {
                            if (i == finalresponses.size() - 1) {
                                surveyResponses[i] = Integer.parseInt(finalresponses.get(i).substring(1, (finalresponses.get(i)).length() - 1));
                            } else {
                                surveyResponses[i] = Integer.parseInt(finalresponses.get(i).substring(1, (finalresponses.get(i)).length()));
                            }
                        }
                        setLifestyleResponse(surveyResponses);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }

                });

    }

    public void findAdherenceFeedback() {
        String UID = ((MyApplication) this.getApplication()).getUID();
        final ArrayList<String> responseArray1 = new ArrayList<>();
        final int[] surveyResponses1 = new int[8];
        mDatabase.child("app").child("users").child(UID).child("adherencesurveyanswersRW").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        int g = 0;
                        while (it.hasNext()) {
                            DataSnapshot medicine = (DataSnapshot) it.next();
                            String attempts = medicine.getValue().toString();
                            responseArray1.add(attempts);
                            g++;
                        }
                        //Log.e("left while","Adherence");
                        ArrayList<String> finalresponses1 = new ArrayList<String>(Arrays.asList(responseArray1.get(responseArray1.size()-1).split(",")));
                        for (int i = 0; i < finalresponses1.size(); i++) {
                            if (i == finalresponses1.size() - 1) {
                                surveyResponses1[i] = Integer.parseInt(finalresponses1.get(i).substring(1, (finalresponses1.get(i)).length() - 1));
                            } else {
                                surveyResponses1[i] = Integer.parseInt(finalresponses1.get(i).substring(1, (finalresponses1.get(i)).length()));
                            }
                        }
                        setAdherenceResponse(surveyResponses1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
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
            //finish();
        }
    }


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
                        Log.e("Phone", phonenumber);
                        ((MyApplication) MainActivity.this.getApplication()).setPharmaPhone(phonenumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        String tel = ((MyApplication) this.getApplication()).getPharmaPhone();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_bloodpressure) {
            Intent i = new Intent(this, BloodPressureActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_medication) {
            Intent i = new Intent(this, MedicationActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_surveys) {
            Intent i = new Intent(this, SurveySelectionActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_callmypharmacist) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + tel));
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
        } else if (id == R.id.nav_study_contact) {
            Intent i = new Intent(this, StudyContactsActivity.class);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
