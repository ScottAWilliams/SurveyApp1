package com.nonscirenefas.yeshy.surveyapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yeshy on 3/8/2016.
 */
public class LoginActivity extends Activity {

    public boolean check = false;
    public static final String PREFS_NAME = "MyPrefsFile";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Context ctx;
    View v;
    String USER_FILENAME = "user_file";
    String PHONE_FILENAME = "phone_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String UIDstored = settings.getString("UID", "Default");
        Log.d("UID", UIDstored);


/*
        if(!UIDstored.equals("Default")) {
            ((MyApplication) this.getApplication()).setUID(UIDstored);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
*/
        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("UID", user.getUid());
                    editor.commit();

                    ((MyApplication) LoginActivity.this.getApplication()).setUID(user.getUid());

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish(); //dunno if this'll fuck stuff up. shouldn't be a problem if the phone isn't shit
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        try {
            FileInputStream fin = openFileInput(USER_FILENAME);
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            Log.e("Login Attempt", temp);
            if (temp.length()>1){
                login(temp+ "@mercer.edu","password",v);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //fucntion that uses silent setSilent(silent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void startMain(View v) {
        AutoCompleteTextView mEdit = (AutoCompleteTextView)findViewById(R.id.username);

        try {
            FileOutputStream fos = openFileOutput(USER_FILENAME, Context.MODE_WORLD_READABLE);
            fos.write(mEdit.getText().toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String email = mEdit.getText().toString() + "@mercer.edu";
        String password = "password";
        Log.d("username", email);
        Log.d("password", password);

        login(email, password, v);
    }

    public void login(String email, String password, View v) {
        final View view = v;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("authorization", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("authorization", "signInWithEmail:failed", task.getException());
                            Toast.makeText(ctx,"Login Does Not Exist", Toast.LENGTH_LONG).show();
                            deleteFile(USER_FILENAME);

                        }

                        // ...
                    }
                });


    }


}

