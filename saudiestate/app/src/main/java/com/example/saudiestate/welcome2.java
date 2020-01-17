package com.example.saudiestate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class welcome2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
    }
    public void login_button (View v) {
        Intent intent = new Intent(welcome2.this,login.class);
        startActivity(intent);

    }
    public void sign_up_button (View v) {
        Intent intent = new Intent(welcome2.this,signup.class);
        startActivity(intent);

    }
}
