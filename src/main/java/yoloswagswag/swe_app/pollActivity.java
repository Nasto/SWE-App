package yoloswagswag.swe_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class pollActivity extends Activity {

    public static final String LAST_TIME_STORAGE = "lastTimeStorage";
    private SharedPreferences lastTimeSett;
    private SharedPreferences selectedTimesStorage;
    private Calendar nextAlarmTime;
    private Calendar currentDay;
    private int nextSlot;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_screen);

        lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);
        selectedTimesStorage = getSharedPreferences("selectedTimesStorage",0);
        currentDay = new GregorianCalendar();
        nextSlot =0;
        int slotHour = selectedTimesStorage.getInt("day"+(currentDay.get(Calendar.DAY_OF_WEEK)-1)+"slot"+ nextSlot, 0);
        while(slotHour<= currentDay.get(Calendar.HOUR_OF_DAY)&& nextSlot <4)
        {
            nextSlot++;
            slotHour=selectedTimesStorage.getInt("day"+(currentDay.get(Calendar.DAY_OF_WEEK)-1)+"slot"+ nextSlot, 0);
        }
        if(nextSlot ==4){
            slotHour=selectedTimesStorage.getInt("day"+(currentDay.get(Calendar.DAY_OF_WEEK))+"slot"+0, 0);
        }
        nextAlarmTime = new GregorianCalendar();
        nextAlarmTime.set(Calendar.HOUR_OF_DAY, slotHour);
        nextAlarmTime.set(Calendar.MINUTE, 0);
        nextAlarmTime.set(Calendar.SECOND, 0);

        TextView pollText = (TextView) findViewById(R.id.pollText);

        SharedPreferences pastAlarmStorage=getSharedPreferences("pastAlarmsStorage",0);
        SharedPreferences.Editor editor = pastAlarmStorage.edit();
        editor.putInt("day", currentDay.get(Calendar.DAY_OF_WEEK));
        editor.putInt("slot", nextSlot - 1);

        int lastHour = lastTimeSett.getInt("lastHour",25);

        if(lastHour != 25){
            pollText.setText(getString(R.string.pollText)+ ((currentDay.get(Calendar.HOUR)<lastHour) ? " gestrigen " : " ")
                + "Signal um "+lastTimeSett.getInt("lastHour",0)+":"+lastTimeSett.getInt("lastMinute",0)+" Uhr Kontakt?");
        }
    }

    public void okPoll(View view){
        SharedPreferences.Editor editor = lastTimeSett.edit();

        EditText numberText = (EditText) findViewById(R.id.pollNrEdit);
        EditText hourText = (EditText) findViewById(R.id.pollHourEdit);
        EditText minuteText = (EditText) findViewById(R.id.pollMinuteEdit);

        int minDiff = currentDay.get(Calendar.MINUTE)-lastTimeSett.getInt("lastMinute", 0) + (currentDay.get(Calendar.HOUR_OF_DAY)-lastTimeSett.getInt("lastHour",0))*60;
        int inputTime = (Integer.parseInt(hourText.getText().toString()))*60 + Integer.parseInt(minuteText.getText().toString());

        if(inputTime < minDiff){
            SharedPreferences userCodeStorage=getSharedPreferences("userCodeStorage",0);
            String code = userCodeStorage.getString("userCode",null);
            currentDay = new GregorianCalendar();

            try {
                File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
                File f = new File(dir, code+".csv");
                FileWriter writer = new FileWriter(f ,true);
                writer.write(code+";"
                        +currentDay.get(Calendar.DAY_OF_MONTH)+"."+currentDay.get(Calendar.MONTH)+"."+currentDay.get(Calendar.YEAR)+";"
                        +selectedTimesStorage.getInt("day"+(currentDay.get(Calendar.DAY_OF_WEEK)-1)+"slot"+(nextSlot -1), 0)+":00;"
                        +currentDay.get(Calendar.HOUR_OF_DAY)+":"+((currentDay.get(Calendar.MINUTE)<10) ? "0"+currentDay.get(Calendar.MINUTE) : currentDay.get(Calendar.MINUTE))+";"
                        +"0;"
                        +numberText.getText()+";"
                        +hourText.getText()+";"
                        +minuteText.getText()+"\n");
                writer.flush();
                writer.close();
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(this, pollActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTimeInMillis(), pendingIntent);

            editor.putInt("lastHour", currentDay.get(Calendar.HOUR_OF_DAY));
            editor.putInt("lastMinute", currentDay.get(Calendar.MINUTE));
            editor.commit();

            if (!this.isTaskRoot()){
                this.finish();
            } else {
                startActivity(new Intent(this, chillActivity.class));
                this.finish();
            }
        } else {
            Toast.makeText(this, "So viel Zeit ist seit der letzten Umfrage nicht vergangen!", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelPoll(View view){
        SharedPreferences userCodeStorage=getSharedPreferences("userCodeStorage",0);
        String code = userCodeStorage.getString("userCode",null);
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
            File f = new File(dir, code+".csv");
            FileWriter writer = new FileWriter(f ,true);
            writer.write(code+";"
                    + currentDay.get(Calendar.DAY_OF_MONTH)+"."+ currentDay.get(Calendar.MONTH)+"."+ currentDay.get(Calendar.YEAR)+";"
                    + selectedTimesStorage.getInt("day"+(currentDay.get(Calendar.DAY_OF_WEEK)-1)+"slot"+(nextSlot -1), 0)+":00" +
                    ";-77;1;-77;-77;-77\n");
            writer.flush();
            writer.close();
        } catch (IOException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, pollActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTimeInMillis(),pendingIntent);

        this.finish();
    }
}