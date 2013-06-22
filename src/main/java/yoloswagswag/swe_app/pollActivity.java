package yoloswagswag.swe_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
    private Calendar currentTime;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_screen);

        lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);
        selectedTimesStorage = getSharedPreferences("selectedTimesStorage",0);
        currentTime = new GregorianCalendar();
        i =0;
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
        nextAlarmTime.set(Calendar.SECOND, 0);
        //nextAlarmTime.add(Calendar.SECOND, 20);

        TextView pollText = (TextView) findViewById(R.id.pollText);

        SharedPreferences pastAlarmStorage=getSharedPreferences("pastAlarmsStorage",0);
        SharedPreferences.Editor editor = pastAlarmStorage.edit();
        editor.putInt("day", currentTime.get(Calendar.DAY_OF_WEEK));
        editor.putInt("slot", i-1);

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

        EditText numberText = (EditText) findViewById(R.id.pollNrEdit);
        EditText timeText = (EditText) findViewById(R.id.pollTimeEdit);
        EditText minuteText = (EditText) findViewById(R.id.pollMinuteEdit);
        SharedPreferences userCodeStorage=getSharedPreferences("userCodeStorage",0);
        String code = userCodeStorage.getString("userCode",null);
        currentTime = new GregorianCalendar();
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
            File f = new File(dir, code+".csv");
            FileWriter writer = new FileWriter(f ,true);
            writer.write(code+";"+currentTime.get(Calendar.DAY_OF_MONTH)+"."+currentTime.get(Calendar.MONTH)+"."+currentTime.get(Calendar.YEAR)+";"+selectedTimesStorage.getInt("day"+(currentTime.get(Calendar.DAY_OF_WEEK)-1)+"slot"+(i-1), 0)+":00;"+currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE)+";0;"+numberText.getText()+";"+timeText.getText()+";"+minuteText.getText()+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        if (!this.isTaskRoot()){
            this.finish();
        } else {
            startActivity(new Intent(this, chillActivity.class));
            this.finish();
        }
    }

    public void cancelPoll(View view){
        /* ungültigen Wert in csv schreiben, neuen Alarm setzen, View schließen */
        SharedPreferences userCodeStorage=getSharedPreferences("userCodeStorage",0);
        String code = userCodeStorage.getString("userCode",null);
        currentTime = new GregorianCalendar();
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
            File f = new File(dir, code+".csv");
            FileWriter writer = new FileWriter(f ,true);
            writer.write(code+";"+currentTime.get(Calendar.DAY_OF_MONTH)+"."+currentTime.get(Calendar.MONTH)+"."+currentTime.get(Calendar.YEAR)+";"+selectedTimesStorage.getInt("day"+(currentTime.get(Calendar.DAY_OF_WEEK)-1)+"slot"+(i-1), 0)+":00;-77;1;-77;-77;-77\n");
            writer.flush();
            writer.close();
        } catch (IOException e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        this.finish();
    }
}