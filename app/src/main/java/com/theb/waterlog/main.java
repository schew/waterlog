package com.theb.waterlog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class main extends Activity {
    public final static String EXTRA_MESSAGE = "com.theB.WaterLog.MESSAGE";
    Integer numCups = 0;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.Editor prefEditor = myPrefs.edit();
        numCups = myPrefs.getInt("numCupsDrunk", 0);
        prefEditor.putInt("numCupsDrunk", numCups);
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called when user clicks view intake button
     */
    public void addWater(View view){
        Intent intent = new Intent(this, ViewIntake.class);
        EditText editText = (EditText) findViewById(R.id.edit_cups);
        if (editText.getText().toString().length() > 0) {
            numCups = Integer.parseInt(editText.getText().toString()) + myPrefs.getInt("numCupsDrunk", 0);
        }
        SharedPreferences.Editor prefEditor = myPrefs.edit();
        prefEditor.putInt("numCupsDrunk", numCups);
        prefEditor.commit();
        startActivity(intent);
    }

    public void newDay(View view) {
        numCups = 0;
        SharedPreferences.Editor prefEditor = myPrefs.edit();
        prefEditor.putInt("numCupsDrunk", numCups);
        prefEditor.commit();
        prefEditor.putInt("num_goal", 0);
        prefEditor.commit();
        prefEditor.putBoolean("goal_entered", false);
        prefEditor.commit();
        prefEditor.putBoolean("goal_toast", false);
        prefEditor.commit();
        prefEditor.putBoolean("mid_toast", false);
        prefEditor.commit();

        Context context = getApplicationContext();
        CharSequence text = "Progress cleared!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void viewIntake(View view){
        Intent intent = new Intent(this, ViewIntake.class);
        startActivity(intent);
    }
}
