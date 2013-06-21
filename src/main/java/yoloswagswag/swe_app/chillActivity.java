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
    SharedPreferences selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE, 0);
    Calendar nextPollTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chill_screen);
        TextView nextTime = (TextView) findViewById(R.id.nextTime);


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
