package yoloswagswag.swe_app;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.String;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.FileWriter;

public class MainActivity extends Activity {

    String code;
    public static final String USER_CODE_STORAGE ="userCodeStorage";
    public static final String START_TIME_STORAGE = "startTimeStorage";
    public static final String SELECTED_TIMES_STORAGE = "selectedTimesStorage";
    SharedPreferences userCodeSett;
    SharedPreferences startTimeSett;
    SharedPreferences selectedTimesSett;
    File f;
    Calendar startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
        selectedTimesSett = getSharedPreferences(SELECTED_TIMES_STORAGE,0);

        code = userCodeSett.getString("userCode",null);

        f= new File(Environment.getExternalStorageDirectory(), code+".csv");

        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);
        code = userCodeSett.getString("userCode",null);

        if (f.exists() && code!=null){
            startActivity(new Intent(this, chillActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        userCodeSett.edit().clear();
        userCodeSett.edit().commit();
        selectedTimesSett.edit().clear();
        selectedTimesSett.edit().commit();

        startTime = new GregorianCalendar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called when the user clicks the Send button */
    public void okMain(View view) {

        TextView error = (TextView) findViewById(R.id.errorMessage);

        code = ((EditText) findViewById(R.id.codeEdit)).getText().toString();

        if(code.length()==5){
            startTimeSett = getSharedPreferences(START_TIME_STORAGE, 0);
            SharedPreferences.Editor timeEditor = startTimeSett.edit();

            userCodeSett.edit().putString("userCode", code);
            timeEditor.putInt("startYear", startTime.get(Calendar.YEAR));
            timeEditor.putInt("startMonth", startTime.get(Calendar.MONTH));
            timeEditor.putInt("startDay", startTime.get(Calendar.DAY_OF_MONTH));
            timeEditor.putInt("startHour", startTime.get(Calendar.HOUR_OF_DAY));


            try {
                File dir = new File(Environment.getExternalStorageDirectory(),"PsychoTest");
                dir.mkdirs();
                File f = new File(dir, code+".csv");
                f.createNewFile();


                //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(code+".csv",0));
                FileWriter writer = new FileWriter(f ,true);
                writer.write("Starting Record for user " + code + " on " + startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE) +
                        " " + startTime.get(Calendar.DAY_OF_MONTH) + "." + startTime.get(Calendar.MONTH) + "." + startTime.get(Calendar.YEAR) + ".\n");
                writer.flush();
                writer.close();
                /*out.write("Starting Record for user " + code + " on " + startTime.get(Calendar.HOUR_OF_DAY)+":"+startTime.get(Calendar.MINUTE)+
                        " "+startTime.get(Calendar.DAY_OF_MONTH)+"."+startTime.get(Calendar.MONTH)+"."+startTime.get(Calendar.YEAR)+"./n");
                out.close();*/
                Toast.makeText(this,"created "+code+".csv successfull!", Toast.LENGTH_SHORT).show();;
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            startActivity(new Intent(this, timeSelector.class));
            finish();
        }

        else error.setText("Ihr Code muss aus 5 Zeichen bestehen!");

    }
    
}
