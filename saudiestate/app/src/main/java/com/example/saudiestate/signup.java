package com.example.saudiestate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    Dialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String key_user = "";
    DatabaseReference mDatabaseRef, mUserCheckData;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String emailForVer = user.getEmail();
                    mUserCheckData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                }
            }
        };
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstname = ((EditText) findViewById(R.id.et_firstname)).getText().toString();
                final String lastname = ((EditText) findViewById(R.id.et_lastname)).getText().toString();
                final String username = ((EditText) findViewById(R.id.et_username)).getText().toString();
                final String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
                final String phone = ((EditText) findViewById(R.id.et_phone)).getText().toString();
                final String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
                String confirm_password = ((EditText) findViewById(R.id.et_confirm_pass)).getText().toString();
                if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, R.string.all_req, Snackbar.LENGTH_LONG)
                            .setAction(R.string.close, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else if (!password.equals(confirm_password)) {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, R.string.pas_conf, Snackbar.LENGTH_LONG)
                            .setAction(R.string.close, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                } else if (!validateEmail(email)) {
                    Toast.makeText(signup.this, R.string.email_pattern, Toast.LENGTH_SHORT).show();
                } else {
                    ((TextView) progressDialog.findViewById(R.id.txt_dialog)).setText(R.string.singing);
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                DatabaseReference mChildDatabase = mDatabaseRef.child("Users").push();
                                key_user = mChildDatabase.getKey();
                                mChildDatabase.child("Email").setValue(email);
                                mChildDatabase.child("Fname").setValue(firstname);
                                mChildDatabase.child("Lname").setValue(lastname);
                                mChildDatabase.child("Username").setValue(username);
                                mChildDatabase.child("colliction").setValue("none");
                                mChildDatabase.child("favorite").setValue("none");
                                mChildDatabase.child("password").setValue(password);
                                mChildDatabase.child("phone").setValue(phone);
                                mChildDatabase.child("userKey").setValue(key_user);


                                final FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(signup.this, new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                // Re-enable button

                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(signup.this,
//                                                            "Verification email sent to " + user.getEmail(),
//                                                            Toast.LENGTH_LONG).show();
                                                    if (Locale.getDefault().getLanguage().equals("ar")) {
                                                        Toast.makeText(signup.this, "تم انشاء حساب جديد . تم ارسال رابط التفعيل إلى اميلك", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(signup.this, "User Account Created , Check your email to verfie your account", Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Log.e("ConfirmEmail", "sendEmailVerification", task.getException());
                                                    Toast.makeText(signup.this,
                                                            "Failed to send verification email.",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });


                            } else {
                                progressDialog.dismiss();

                                if (Locale.getDefault().getLanguage().equals("ar")) {
                                    Toast.makeText(signup.this, "فشل انشاء الحساب", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(signup.this, "Failed to create User Account", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //ON ACTIVITY START CHECK USER AUTHENTICATION
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //REMOVE THE LISTENER ON ACTIVITY STOP
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
