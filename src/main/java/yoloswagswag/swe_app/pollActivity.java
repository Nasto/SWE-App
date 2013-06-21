package yoloswagswag.swe_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class pollActivity extends Activity {

    public static final String LAST_TIME_STORAGE = "lastTimeStorage";
    SharedPreferences lastTimeSett;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_screen);

        lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);

        TextView pollText = (TextView) findViewById(R.id.pollText);

        if(lastTimeSett.contains("last")){
            pollText.setText(getString(R.id.pollText)+lastTimeSett.getString("last","XX:XX")+"Uhr?");
        } else {
            pollText.setText(getString(R.string.pollFirstText));
        }
    }

    public void okPoll(View view){
        Calendar currentTime = new GregorianCalendar();
        SharedPreferences.Editor editor = lastTimeSett.edit();
        editor.putString("last", currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE));
        /* Daten überprüfen, in csv schreiben, neuen Alarm setzen, Chillscreen öffnen */


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