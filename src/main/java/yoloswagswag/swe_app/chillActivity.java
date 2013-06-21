package yoloswagswag.swe_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import java.util.Calendar;

public class chillActivity extends Activity {

    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    SharedPreferences selectedTimesSett;
    Calendar nextPollTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chill_screen);
        TextView nextTime = (TextView) findViewById(R.id.nextTime);
        TextView nextTimeLeft = (TextView) findViewById(R.id.nextTimeLeft);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE, 0);

        nextTime.setText("loltest");
        nextTimeLeft.setText("still need to figure this out :/");


    }

    public void timeChange(View view){
        startActivity(new Intent(this, timeSelector.class));
        finish();
    }

    public void simulate(View view){
        startActivity(new Intent(this, pollActivity.class));
        finish();
    }
}
