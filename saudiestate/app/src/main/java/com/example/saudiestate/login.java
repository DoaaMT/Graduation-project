package com.example.saudiestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Locale;

public class login extends AppCompatActivity {

    EditText et_email, et_pass;
    Button login;
    ProgressDialog progress;
    //String Fields
    String userEmailString, userPasswordString;
    FirebaseUser user;
    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef;
    Button forget_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pass = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.button);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String emailForVer = user.getEmail();
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkUserValidation(dataSnapshot , emailForVer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                }
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmailString = et_email.getText().toString();
                userPasswordString = et_pass.getText().toString();
                if (userEmailString.isEmpty()||userPasswordString.isEmpty()){
                    Toast.makeText(login.this, R.string.all_req, Toast.LENGTH_SHORT).show();
                }else {
                    if (Locale.getDefault().getLanguage().equals("ar")) {
                        progress = ProgressDialog.show(login.this, "Saudi Estate",
                                "جاري الان تسجيل الدخول ...", true);
                    } else {
                        progress = ProgressDialog.show(login.this, "Saudi Estate",
                                "Loading ...", true);
                    }
                    mAuth.signInWithEmailAndPassword(userEmailString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        checkUserValidation(dataSnapshot, userEmailString);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                progress.dismiss();
                                if (Locale.getDefault().getLanguage().equals("ar")) {
                                    Toast.makeText(login.this, "فشل تسجيل الدخول", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(login.this, "User Login Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }

            }
        });

    }

    private void checkUserValidation(DataSnapshot dataSnapshot, String emailForVer) {

        if (user.isEmailVerified()) {

            Iterator iterator = dataSnapshot.getChildren().iterator();

            while (iterator.hasNext()) {

                DataSnapshot dataUser = (DataSnapshot) iterator.next();

                if (dataUser.child("Email").getValue().toString().equals(emailForVer)) {


                        Intent in = new Intent(login.this, Main2Activity.class);
                        in.putExtra("USER_KEY", dataUser.child("userKey").getValue().toString());
                        startActivity(in);
                        SharedPreferences.Editor editor = getSharedPreferences("USERS", MODE_PRIVATE).edit();
                        editor.putString("userKey", dataUser.child("userKey").getValue().toString());
                        editor.putString("userName", dataUser.child("Username").getValue().toString());
                        editor.putString("userEmail", dataUser.child("Email").getValue().toString());
                        editor.apply();


                }
            }
        } else {
            if (progress != null) {
                progress.dismiss();
            }

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Your account is not activated  , Check your Email", Snackbar.LENGTH_LONG)
                    .setAction("Send Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(login.this, new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            // Re-enable button

                                            if (task.isSuccessful()) {
                                                Toast.makeText(login.this,
                                                        "Verification email sent to " + user.getEmail(),
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.e("ConfirmEmail", "sendEmailVerification", task.getException());
                                                Toast.makeText(login.this,
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

}
