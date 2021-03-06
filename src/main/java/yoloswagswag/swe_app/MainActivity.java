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

   /**
    * @mainpage Android App Soziale Intelligenz
    *
    * @section sec1 Beschreibung
    * Diese App wurde entwickelt um soziale Kontakte zu protokollieren und damit die soziale
    * Intelligenz zu berechnen. Die App wurde im Zusammenhang mit dem Modul "Softwareengineering" im
    * Sommersemester 2013 erstellt.
    *
    * @subsection subsec1 Vorrausetzung
    * Android Version 2.1 oder aufwärts
    *
    * @subsection subsec2 Entwickler
    *
    * Stephan Dörfler<br></br>
    * Florian Heinrich<br></br>
    * Sebastian Wagner<br></br>
    * Sebastian Rohde
    *
    * @subsection subsec3 Screenshots
    *  * <table border="1">
   <tr>
   <th>MainActivity</th>
   <th>timeSelector</th>
   </tr>
   <tr>
   <td><img src="Bilder/codescreen.png" alt="Bilder/codescreen.jpg" width="300" height="500"></td>
   <td><img src="Bilder/timeselector.png" alt="Bilder/timeselector.jpg" width="300" height="500"></td>
   </tr>
   <tr>
   <th>chillActivity</th>
   <th>pollActivity</th>
   </tr>
   <tr>
   <td><img src="Bilder/chillscreen.png" alt="Bilder/chillscreen.jpg" width="300" height="500"></td>
   <td><img src="Bilder/pollscreen.png" alt="Bilder/pollscreen.jpg" width="300" height="500"></td>
   </tr>
   </table>

    * @subsection subsec4 Handhabung
    * Beim ersten Start der App wird der Benutzer aufgefordert seinen Probandencode zu generieren.
    * Mithilfe dieses Codes wird die .csv Datei erstellt und die App springt auf den
    * Zeitauswahlbildschirm. Hier können die Zeiten für die einzelnen Wochentage definiert werden.
    * Mithilfe des OK-Buttons wird der Chill-Screen geöffnet, auf welchem die nächste Alarmzeit
    * und die Zeit bis dahin angezeigt werden. Zusätzlich hat der Benutzer die Möglichkeit auf den
    * Zeitauswahlbildschirm zu gelangen um die Alarmzeiten zu verändern.<br></br> Sobald eine Alarmzeit
    * erreicht wird, klingelt und vibriert das Gerät je nach Handyeinstellungen. Der Poll-Screen wird
    * geöffnet und der Benutzer wird aufgefordert seine Angaben zu machen. Nach der Eingabe und
    * Betätigung des OK-Buttons werden die Daten gespeichert. Er hat zusätzlich die Möglichkeit
    * mit dem Home-Button die App zu minimieren und die Umfrage später auszufüllen.<br></br> Durch Betätigung
    * des Abbruch-Buttons oder des Back-Buttons wird die aktuelle Umfrage geschlossen und eine
    * Abbruchzeile wird in die .csv Datei geschrieben.<br></br> Die App kann nach Abschluss eines
    * Experiments durch Entfernen der .csv Datei zurückgesetzt werden. Ausserdem sollte der
    * Benutzer darauf hingewiesen werden, dass er nach Beendigung des Experiments bis zur Abgabe
    * des Gerätes das Handy ausschalten soll um weitere Abfragen zu verhindern.
    */


    /**
     * @class MainActivity
    *  @brief Diese Klasse ist zur Erstellung des Probandencodes und wird nur beim ersten Start der
    *  App aufgerufen.
    *  @file MainActivity.java
    */

public class MainActivity extends Activity {

    public static final String USER_CODE_STORAGE ="userCodeStorage";
    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    public static final String LAST_TIME_STORAGE = "lastTimeStorage";
    SharedPreferences userCodeSett;
    SharedPreferences selectedTimesSett;

    /**
     * @brief Userdaten werden überprüft
     * <ul>
     *     <li> Wenn Probandencode bereits existiert --> Aufruf {@link chillActivity}</li>
     *     <li> ansonsten Eingabe des Probandencodes</li>
     * </ul>
     * @param Bundle savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE,0);
        SharedPreferences pastAlarmStorage=getSharedPreferences("pastAlarmsStorage",0);
        SharedPreferences lastTimeSett = getSharedPreferences(LAST_TIME_STORAGE, 0);

        String code = userCodeSett.getString("userCode",null);

        File f= new File(new File(Environment.getExternalStorageDirectory(),"PsychoTest"), code+".csv");

        // falls ein Probandencode gepeichert ist und die .csv vorhanden ist, wird diese Activity uebersprungen
        if (f.exists() && code!=null){
            startActivity(new Intent(this, chillActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);

            //neuen User anlegen -> alle alten gespeicherten Werte loeschen
            userCodeSett.edit().clear().commit();
            selectedTimesSett.edit().clear().commit();
            pastAlarmStorage.edit().clear().commit();
            lastTimeSett.edit().clear().commit();
        }

    }

    /**
     * @brief Bei Betaetigung des OK-Buttons, wird geprüft ob Codelänge = 5. Nach erfolgreicher Abfrage
     * wird die Probandencode.csv Datei im Psychotest Ordner erstellt. Bei falscher Eingabe kommt
     * Rückmeldung an User über falsche Eingabe.
     * @param View view
     */
    public void okMain(View view) {
        //String aus dem Eingabefeld holen
        String code = ((EditText) findViewById(R.id.codeEdit)).getText().toString();

        if(code.length()==5){
            boolean flag=true;
            userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
            SharedPreferences.Editor codeEditor = userCodeSett.edit();

            codeEditor.putString("userCode", code);
            codeEditor.commit();


            // erstellt Ordner Psychotest und darin die [Code].csv Datei auf der SD-Karte
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
            } catch (IOException e){
                // falls es Fehler bei Erstellen gibt,kommt die Exception in den Toast
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                flag=false;
            }

            if(flag){
                startActivity(new Intent(this, timeSelector.class));
                finish();
            }
        }
        // Rueckmeldung, falls Codeeingabe zu kurz
        else
            Toast.makeText(this, "Ihr Code muss aus 5 Zeichen bestehen!", Toast.LENGTH_LONG).show();
    }

}
