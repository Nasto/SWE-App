package yoloswagswag.swe_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class pollActivity extends Activity {

    public static final String LAST_TIME_STORAGE = "lastTimeStorage";
    private SharedPreferences lastTimeSett;
    private SharedPreferences selectedTimesStorage;
    private Calendar nextAlarmTime;
    private Calendar currentTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_screen);

        lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);
        selectedTimesStorage = getSharedPreferences("selectedTimesStorage",0);
        currentTime = new GregorianCalendar();
        int i =0;
        int slotHour = selectedTimesStorage.getInt("day"+(currentTime.get(Calendar.DAY_OF_WEEK)-1)+"slot"+i, 0);
        while(slotHour<=currentTime.get(Calendar.HOUR_OF_DAY)&&i<4)
        {
            i++;
            slotHour=selectedTimesStorage.getInt("day"+(currentTime.get(Calendar.DAY_OF_WEEK)-1)+"slot"+i, 0);
        }
        if(i==4){
            slotHour=selectedTimesStorage.getInt("day"+(currentTime.get(Calendar.DAY_OF_WEEK))+"slot"+0, 0);
        }
        nextAlarmTime = new GregorianCalendar();
        nextAlarmTime.set(Calendar.HOUR_OF_DAY, slotHour);
        nextAlarmTime.set(Calendar.MINUTE, 0);
        //nextAlarmTime.add(Calendar.SECOND, 20);

        int bla=nextAlarmTime.get(Calendar.HOUR_OF_DAY);
        TextView pollText = (TextView) findViewById(R.id.pollText);
        pollText.setText(" "+bla);

        if(lastTimeSett.contains("last")){
            pollText.setText(getString(R.id.pollText)+lastTimeSett.getString("last","XX:XX")+" Uhr?");
        } /*else {
            pollText.setText(getString(R.string.pollFirstText));
        }*/
    }

    public void okPoll(View view){
        Calendar currentTime = new GregorianCalendar();
        SharedPreferences.Editor editor = lastTimeSett.edit();
        editor.putString("last", currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE));
        /* Daten überprüfen, in csv schreiben, neuen Alarm setzen, Chillscreen öffnen */


        Intent intent = new Intent(this, pollActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTimeInMillis(),pendingIntent);


        if (!this.isTaskRoot()){
            this.finish();
        } else {
            startActivity(new Intent(this, chillActivity.class));
            this.finish();
        }
    }

    public void cancelPoll(View view){
        /* ungültigen Wert in csv schreiben, neuen Alarm setzen, View schließen */
    }
}