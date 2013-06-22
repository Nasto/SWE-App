package yoloswagswag.swe_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
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
    Calendar nextPollTime;
    Calendar currentDate = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chill_screen);
        final TextView nextTime = (TextView) findViewById(R.id.nextTime);
        final TextView nextTimeLeft = (TextView) findViewById(R.id.nextTimeLeft);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE, 0);

        final Time now = new Time();
        final Handler h=new Handler();
        h.post(new Runnable(){


                @Override
                public void run() {
                    now.setToNow();

                    int day = now.weekDay;
                    int hour = now.hour;
                    int minute = now.minute;
                    int slot = 0;

                    for (int i = 0; i<=3; i++) {
                        if (hour < (selectedTimesSett.getInt("day"+day+"slot"+i,0))){
                            slot = selectedTimesSett.getInt("day"+day+"slot"+i,0);
                        }
                    }
                    if (slot != 0){
                        nextTime.setText(slot+":00 Uhr");
                    }
                    else if (day != 6){
                        day++;
                        nextTime.setText(Integer.toString(selectedTimesSett.getInt("day"+(day)+"slot"+slot,2))+":00 Uhr");
                    }else{
                        day = 0;
                        nextTime.setText(Integer.toString(selectedTimesSett.getInt("day"+(day)+"slot"+slot,2))+":00 Uhr");
                    }


                    now.setToNow();
                    if (slot == 0){
                        nextTimeLeft.setText((now.minute==0) ? (selectedTimesSett.getInt("day"+day+"slot"+slot,0))+(Math.abs(0-now.hour))+":00"
                                : selectedTimesSett.getInt("day"+day+"slot"+slot,0)+(Math.abs(24-now.hour-1))+":"+(60-now.minute));
                    }else{
                        nextTimeLeft.setText(((now.minute==0)?(slot-now.hour)+":00"
                                :(slot-now.hour+1)+":"+(60-now.minute)));
                    }
                    h.postDelayed(this,30000);
                    // update every second

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
