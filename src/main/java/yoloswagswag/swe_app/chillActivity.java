package yoloswagswag.swe_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.text.format.Time;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class chillActivity extends Activity {

    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    SharedPreferences selectedTimesSett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chill_screen);
        final TextView nextTime = (TextView) findViewById(R.id.nextTime);       // next time for poll
        final TextView nextTimeLeft = (TextView) findViewById(R.id.nextTimeLeft);   // time left till next poll
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE, 0);

        final Time now = new Time();    // variable for current time
        final Handler h = new Handler(); // handler necessary for updating the method
        h.post(new Runnable(){


                @Override
                public void run() {
                    now.setToNow(); // sets now to current time

                    int day = now.weekDay;  // current day
                    int hour = now.hour;    // current hour
                    int slot = 0;   // time-slot

                    // go through time slots by checking whether current hour is smaller and select slot
                    for (int i = 0; i<=3; i++) {
                        if (hour < (selectedTimesSett.getInt("day"+day+"slot"+i,0))){
                            slot = selectedTimesSett.getInt("day"+day+"slot"+i,0);
                            break;
                        }
                    }

                    // SET NEXT TIME FOR POLL

                    // if slot is not 0 set next time to slot
                    if (slot != 0){
                        nextTime.setText(slot+":00 Uhr");
                    }
                    // else change day to next day
                    else if (day != 6){
                        day++;
                        nextTime.setText(Integer.toString(selectedTimesSett.getInt("day"+(day)+"slot"+slot,2))+":00 Uhr");
                    }
                    // if next day is Monday (day only ranges from 0 to 6)
                    else
                    {
                        day = 0;
                        nextTime.setText(Integer.toString(selectedTimesSett.getInt("day"+(day)+"slot"+slot,2))+":00 Uhr");
                    }


                    // CALCULATE TIME LEFT TILL NEXT POLL

                    // if next slot is not today
                    if (slot == 0){
                        nextTimeLeft.setText((now.minute==0) ? (selectedTimesSett.getInt("day"+day+"slot"+slot,0))+(Math.abs(0-now.hour))+":00"
                                : selectedTimesSett.getInt("day"+day+"slot"+slot,0)+(Math.abs(24-now.hour-1))+":"+((60-now.minute<10)? "0"+(60-now.minute) : 60-now.minute));
                    }
                    // if next slot is today
                    else
                    {
                        nextTimeLeft.setText(((now.minute==0)?(slot-now.hour)+":00"
                                :(slot-(now.hour+1))+":"+((60-now.minute<10)? "0"+(60-now.minute) : 60-now.minute)));
                    }

                    // update the whole thing every 30 seconds (important for time left till next poll)
                    h.postDelayed(this,30000);
                }
            });
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
