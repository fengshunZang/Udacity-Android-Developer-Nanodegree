package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Context mContext;
    private ProgressBar mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mIndicator = (ProgressBar)findViewById(R.id.progress_indicator);
        mIndicator.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void tellJoke(View view) {
        mIndicator.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String s) {
            String aboutJoke = "LOL, new android library shows the joke.";
                mIndicator.setVisibility(View.INVISIBLE);
            //Send an intent to carry the joke to jokedisplayactivity
            Intent intent = new Intent(mContext, com.zangfengshun.jokedisplayactivity.MainActivity.class);
            intent.putExtra("message", s);
            mContext.startActivity(intent);

            Toast.makeText(mContext, aboutJoke, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
