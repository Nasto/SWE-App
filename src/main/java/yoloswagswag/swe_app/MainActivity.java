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

public class MainActivity extends Activity {

    String code;
    public static final String USER_CODE_STORAGE ="userCodeStorage";
    public static final String START_TIME_STORAGE = "startCodeStorage";
    SharedPreferences userCodeSett;
    SharedPreferences startTimeSett;
    File f;
    Calendar startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCodeSett = getSharedPreferences(USER_CODE_STORAGE, 0);

        code = userCodeSett.getString("userCode",null);

        f= new File(Environment.getExternalStorageDirectory(), code+".csv");

        if (f.exists() && code!=null){
            startActivity(new Intent(this, chillActivity.class));
            finish();
        }
        userCodeSett.edit().clear();
        userCodeSett.edit().commit();

        startTime = new GregorianCalendar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Called when the user clicks the Send button */
    public void okMessage(View view) {

        TextView error =(TextView) findViewById(R.id.errorMessage);

        code = ((EditText) findViewById(R.id.codeEdit)).getText().toString();

        if(code.length()==5){
            SharedPreferences.Editor codeEditor = userCodeSett.edit();
            SharedPreferences.Editor timeEditor = startTimeSett.edit();

            codeEditor.putString("userCode", code);
            codeEditor.commit();
            timeEditor.putInt("startYear", startTime.get(Calendar.YEAR));
            timeEditor.putInt("startMonth", startTime.get(Calendar.MONTH));
            timeEditor.putInt("startDay", startTime.get(Calendar.DAY_OF_MONTH));
            timeEditor.putInt("startHour", startTime.get(Calendar.HOUR_OF_DAY));
            timeEditor.commit();

            try {
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput(code+".csv",0));
                out.write("Starting Record for user " + code + " on " + startTime.get(Calendar.HOUR_OF_DAY)+":"+startTime.get(Calendar.MINUTE)+
                        " "+startTime.get(Calendar.DAY_OF_MONTH)+"."+startTime.get(Calendar.MONTH)+"."+startTime.get(Calendar.YEAR)+"./n");
                out.close();
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

            startActivity(new Intent(this, timeSelector.class));
            finish();
        }

        else error.setText("Ihr Code muss aus 5 Zeichen bestehen!");

    }
    
}
