package com.justinclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {
    String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void launchMain(View view){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("EXTRA_MESSAGE", filename);
            startActivity(intent);
        }

}
