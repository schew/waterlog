package com.theb.waterlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class ViewIntake extends Activity {
    public final static String EXTRA_MESSAGE = "com.theB.WaterLog.MESSAGE";
    Integer numCups = 0;
    Integer goal = 0;
    TextView cups;
    SharedPreferences myPrefs;
    TextView progress;
    boolean goalEntered;
    boolean midPointToast;
    boolean goalToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_intake);
        Intent intent = getIntent();

        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        goalEntered = myPrefs.getBoolean("goal_entered", false);
        goal = myPrefs.getInt("num_goal", 0);
        numCups = myPrefs.getInt("numCupsDrunk", 0);
        cups = (TextView) findViewById(R.id.cups_drunk);
        cups.setText(numCups.toString());

        ImageButton setGoal = (ImageButton) findViewById(R.id.set_goal);
        setGoal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //Save goal
                EditText editText = (EditText) findViewById(R.id.edit_goal);
                if (editText.getText().toString().length() > 0) {
                    goal = Integer.parseInt(editText.getText().toString());

                    SharedPreferences.Editor prefEditor = myPrefs.edit();
                    prefEditor.putInt("num_goal", goal);
                    prefEditor.commit();

                    goalEntered = true;
                    prefEditor.putBoolean("goal_entered", goalEntered);
                    prefEditor.commit();

                    midPointToast = false;
                    prefEditor.putBoolean("mid_toast", midPointToast);
                    prefEditor.commit();
                    goalToast = false;
                    prefEditor.putBoolean("goal_toast", goalToast);
                    prefEditor.commit();

                    //Update progress percentage
                    updateGoalProgress();
                    checkToast();
                }
            }
        });
    }

    protected void onStart() {
        super.onStart();
        goalEntered = myPrefs.getBoolean("goal_entered", false);
        if (goalEntered) {
            this.updateGoalProgress();
        }
        this.checkToast();
    }

    protected void updateGoalProgress() {
        progress = (TextView) findViewById(R.id.goal_progress);
        double goal_percent = myPrefs.getInt("num_goal", 0);
        double cup_percent = myPrefs.getInt("numCupsDrunk", 0);
        goal_percent = cup_percent / goal_percent * 100;
        DecimalFormat df = new DecimalFormat("#.00");
        progress.setText("You've reached " + df.format(goal_percent) + "% of your goal!");
    }

    protected void checkToast() {
        SharedPreferences.Editor prefEditor = myPrefs.edit();

        if (goal <= numCups && !goalToast && goalEntered) {
            toast("Congrats! You reached your goal!");
            prefEditor.putBoolean("goal_toast", true);
            prefEditor.commit();
        } else {
            midPointToast = myPrefs.getBoolean("mid_toast", false);
            if ((double)goal/2.0 <= (double)numCups && !midPointToast && goalEntered) {
                toast("You're halfway there!");
                prefEditor.putBoolean("mid_toast", true);
                prefEditor.commit();
            }
        }
    }

    protected void toast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor prefEditor = myPrefs.edit();
        prefEditor.putInt("numCupsDrunk", numCups);
        prefEditor.putInt("num_goal", goal);
        prefEditor.commit();
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

}
