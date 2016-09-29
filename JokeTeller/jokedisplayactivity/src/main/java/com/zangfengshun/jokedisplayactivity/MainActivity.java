package com.zangfengshun.jokedisplayactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_joke_display);

        TextView jokeDisplay = (TextView)findViewById(R.id.joke_display);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        jokeDisplay.setText(message);
    }
}
