package yoloswagswag.swe_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private MediaPlayer mediaPlayer;
    private CountDownTimer soundTimer;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_screen);

        playSound(this, getAlarmUri());
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
        startNewAlarm(nextAlarmTime);

        TextView pollText = (TextView) findViewById(R.id.pollText);

        SharedPreferences pastAlarmStorage=getSharedPreferences("pastAlarmsStorage",0);
        SharedPreferences.Editor editor = pastAlarmStorage.edit();
        editor.putInt("day", currentDay.get(Calendar.DAY_OF_WEEK));
        editor.putInt("slot", nextSlot - 1);

        int lastHour = lastTimeSett.getInt("lastHour",25);

        if(lastHour != 25){
            pollText.setText(getString(R.string.pollText)+ ((currentDay.get(Calendar.HOUR_OF_DAY)<lastHour) ? " gestrigen " : " ")
                + "Signal um "+lastTimeSett.getInt("lastHour",0)+":00 Uhr Kontakt?");
        }


    }

    private boolean hasLastPollBeenFilled(){
        //ToDo hat user auf Anfrage reagiert?
        int lastHour = lastTimeSett.getInt("lastHour",25);
        int lastSlot = 4;
        switch (lastHour){
            case 9:
            case 10:
            case 11:
            case 12:
                lastSlot=0;
                break;
            case 13:
            case 14:
            case 15:
                lastSlot=1;
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                lastSlot=2;
                break;
            case 20:
            case 21:
            case 22:
            case 23:
                lastSlot=3;
                break;
            default:
                break;

        }

        return false;
    }

    private void playSound(Context context, Uri alert){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        soundTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                stopSound(mediaPlayer);
            }
        };
        soundTimer.start();
    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    private void stopTimer(CountDownTimer timer){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void stopSound(MediaPlayer player){
        try {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
                player = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void startNewAlarm(Calendar alarmTime){
        Intent intent = new Intent(this, pollActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    }

    private void setLastHourAndMinuteToPreferences(){
        SharedPreferences.Editor editor = lastTimeSett.edit();
        editor.putInt("lastHour", currentDay.get(Calendar.HOUR_OF_DAY));
        editor.putInt("lastMinute", currentDay.get(Calendar.MINUTE));
        editor.commit();
    }

    public void okPoll(View view){
        stopTimer(soundTimer);
        stopSound(mediaPlayer);
        EditText numberText = (EditText) findViewById(R.id.pollNrEdit);
        EditText hourText = (EditText) findViewById(R.id.pollHourEdit);
        EditText minuteText = (EditText) findViewById(R.id.pollMinuteEdit);

        int minDiff = currentDay.get(Calendar.MINUTE)-lastTimeSett.getInt("lastMinute", 0) + (currentDay.get(Calendar.HOUR_OF_DAY)-lastTimeSett.getInt("lastHour",0))*60;
        int inputTime = (Integer.parseInt(hourText.getText().toString()))*60 + Integer.parseInt(minuteText.getText().toString());

        if(inputTime < minDiff){
            SharedPreferences userCodeStorage=getSharedPreferences("userCodeStorage",0);
            String code = userCodeStorage.getString("userCode",null);
            Calendar updatedCurrentDay = new GregorianCalendar();

            try {
                File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
                File f = new File(dir, code+".csv");
                FileWriter writer = new FileWriter(f ,true);
                writer.write(code+";"
                        +updatedCurrentDay.get(Calendar.DAY_OF_MONTH)+"."+updatedCurrentDay.get(Calendar.MONTH)+"."+updatedCurrentDay.get(Calendar.YEAR)+";"
                        +selectedTimesStorage.getInt("day"+(updatedCurrentDay.get(Calendar.DAY_OF_WEEK)-1)+"slot"+(nextSlot -1), 0)+":00;"
                        +updatedCurrentDay.get(Calendar.HOUR_OF_DAY)+":"+((updatedCurrentDay.get(Calendar.MINUTE)<10) ? "0"+updatedCurrentDay.get(Calendar.MINUTE) : updatedCurrentDay.get(Calendar.MINUTE))+";"
                        +"0;"
                        +numberText.getText()+";"
                        +hourText.getText()+";"
                        +minuteText.getText()+"\n");
                writer.flush();
                writer.close();
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            setLastHourAndMinuteToPreferences();

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
        stopTimer(soundTimer);
        stopSound(mediaPlayer);
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

        setLastHourAndMinuteToPreferences();

        this.finish();
    }
}