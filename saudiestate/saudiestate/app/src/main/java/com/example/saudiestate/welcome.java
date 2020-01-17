package com.example.saudiestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void textView4 (View v) {
        Intent intent = new Intent(welcome.this,welcome2.class);
        startActivity(intent);

    }
}
