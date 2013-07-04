package yoloswagswag.swe_app;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends Activity {

    String code;
    public static final String USER_CODE_STORAGE ="userCodeStorage";
    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    public static final String LAST_TIME_STORAGE = "lastTimeStorage";
    SharedPreferences userCodeSett;
    SharedPreferences selectedTimesSett;
    File f;
    Calendar startTime;

    // Probandencodescreen
    // wird nur beim ersten Aufruf der App aufgerufen und dann erst wieder wenn .csv gelöscht oder
    // umbenannt wurde

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE,0);
        SharedPreferences pastAlarmStorage=getSharedPreferences("pastAlarmsStorage",0);
        SharedPreferences lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);

        code = userCodeSett.getString("userCode",null);

        f= new File(new File(Environment.getExternalStorageDirectory(),"PsychoTest"), code+".csv");

        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);


        if (f.exists() && code!=null){
            startActivity(new Intent(this, chillActivity.class));
            finish();
        } else {

            setContentView(R.layout.activity_main);

            userCodeSett.edit().clear();
            userCodeSett.edit().commit();
            selectedTimesSett.edit().clear();
            selectedTimesSett.edit().commit();
            pastAlarmStorage.edit().clear();
            pastAlarmStorage.edit().commit();
            lastTimeSett.edit().clear();
            lastTimeSett.edit().commit();

            startTime = new GregorianCalendar();
        }

    }

    // ...?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Bei Betätigung des OK-Buttons
    // ...?
    public void okMain(View view) {

        TextView error = (TextView) findViewById(R.id.errorMessage);

        code = ((EditText) findViewById(R.id.codeEdit)).getText().toString();

        if(code.length()==5){
            userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
            SharedPreferences.Editor codeEditor = userCodeSett.edit();

            codeEditor.putString("userCode", code);
            codeEditor.commit();


            // erstellt Ordner Psychotest und darin die .csv Datei auf der SD-Karte
            try {
                File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
                dir.mkdirs();
                File f = new File(dir, code+".csv");
                f.createNewFile();

                //Tabellenkopf erstellen
                FileWriter writer = new FileWriter(f ,true);
                writer.write("Code;Datum;Alarmzeit;Antwortzeit;Abbruch;Kontakte;Stunden;Minuten\n");
                writer.flush();
                writer.close();
                // gibt Rückmeldung ob .csv Datei erfolgreich erstellt wurde
                Toast.makeText(this,"created "+code+".csv successfull!", Toast.LENGTH_SHORT).show();
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            startActivity(new Intent(this, timeSelector.class));
            finish();
        }
        // Rückmeldung falls Codeeingabe zu kurz
        else
            Toast.makeText(this, "Ihr Code muss aus 5 Zeichen bestehen!", Toast.LENGTH_LONG).show();

    }

}
