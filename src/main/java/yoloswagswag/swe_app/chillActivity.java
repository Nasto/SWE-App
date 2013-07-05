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

    /*
    * @class chillActivity
    * @brief Bildschirm zwischen den Abfragen. Gibt Auskunft über nächste Alarmzeit und Zeitraum bis
    * dahin. Ebenso Möglichkeit zum Zeitauswahlbildschirm zu springen.
    * @file chillActivity.java
    */

public class chillActivity extends Activity {

    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    SharedPreferences selectedTimesSett;

    /*
     * @brief Anzeige nächste Alarmzeit und Zeit bis dahin.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chill_screen);

        // nächste Zeit für Umfrage
        final TextView nextTime = (TextView) findViewById(R.id.nextTime);

        // verbleibende Zeit bis zur nächsten Umfrage
        final TextView nextTimeLeft = (TextView) findViewById(R.id.nextTimeLeft);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE, 0);

        // Variable für momentane Zeit
        final Time currentTime = new Time();

        // Handler, der für das Update der Methode benötigt wird
        final Handler handler = new Handler();

        // Mehode für das Setzen der nächsten Umfragezeit + der verbleibenden Zeit bis dahin
        handler.post(new Runnable() {


            @Override
            public void run() {
                // currentTime auf momentane Zeit setzen
                currentTime.setToNow();

                // momentaner Wochentag
                int day = currentTime.weekDay;

                // aktuelle Stunde
                int hour = currentTime.hour;

                // Time-Slot
                int slot = 0;

                // durch die Time-Slots gehen, überprüfen, ob die aktuelle Stunde kleiner ist
                // und Slot auswählen
                for (int i = 0; i <= 3; i++) {
                    if (hour < (selectedTimesSett.getInt("day" + day + "slot" + i, 0))) {
                        slot = selectedTimesSett.getInt("day" + day + "slot" + i, 0);
                        break;
                    }
                }


                // NÄCHSTE UMFRAGEZEIT BESTIMMEN & SETZEN

                // falls slot nicht 0 ist, nächste Zeit auf slot setzen
                if (slot != 0) {
                    nextTime.setText(slot + ":00 Uhr");
                }
                // ansonsten zum nächsten Tag wechseln
                else if (day != 6) {
                    day++;
                    nextTime.setText(Integer.toString(selectedTimesSett.getInt("day" + (day) + "slot" + slot, 2)) + ":00 Uhr");
                }
                // Wochenübergang, falls der nächste Tag Montag ist (da day nur eine range von 0 bis 6 hat)
                else {
                    day = 0;
                    nextTime.setText(Integer.toString(selectedTimesSett.getInt("day" + (day) + "slot" + slot, 2)) + ":00 Uhr");
                }


                // VERBLEIBENDE ZEIT BIS ZU NÄCHSTER UMFRAGE BERECHNEN

                // falls der nächste Slot nicht mehr im heutigen Tag liegt
                if (slot == 0) {
                    nextTimeLeft.setText((currentTime.minute == 0) ? (selectedTimesSett.getInt("day" + day + "slot" + slot, 0)) + (Math.abs(24 - currentTime.hour)) + ":00"
                            : selectedTimesSett.getInt("day" + day + "slot" + slot, 0) + (Math.abs(24 - (currentTime.hour - 1))) + ":" + ((60 - currentTime.minute < 10) ? "0" + (60 - currentTime.minute) : 60 - currentTime.minute));
                }
                // falls nächster Slot doch heute ist
                else {
                    nextTimeLeft.setText(((currentTime.minute == 0) ? (slot - currentTime.hour) + ":00"
                            : (slot - (currentTime.hour + 1)) + ":" + ((60 - currentTime.minute < 10) ? "0" + (60 - currentTime.minute) : 60 - currentTime.minute)));
                }

                // Update alle 30 Sekunden (wichtig für verbleibende Zeit)
                handler.postDelayed(this, 30000);
            }
        });
    }

    /*
     * @brief Aufruf des Zeitauswahlbildschirm bei Betätigung des Buttons
      */
    public void timeChange(View view){
        startActivity(new Intent(this, timeSelector.class));
        finish();
    }
    // Aufruf der Umfrage
    public void simulate(View view){
        startActivity(new Intent(this, pollActivity.class));
        finish();
    }
}
