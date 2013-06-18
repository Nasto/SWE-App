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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.String;
import java.util.Calendar;

public class MainActivity extends Activity {

    String code;
    public static final String PREFS_NAME ="userCode";
    SharedPreferences settings;
    File f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PREFS_NAME, 0);

        code = settings.getString("userCode",null);

        f= new File(Environment.getExternalStorageDirectory(), code+".csv");

        if (f.exists() && code!=null){
            startActivity(new Intent(this, chillActivity.class));
            finish();
        }
        settings.edit().clear();
        settings.edit().commit();
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
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("userCode", code);
            editor.commit();

            try {
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput(code+".csv",0));
                out.write("Starting Record for user " + code + " on " + Calendar.getInstance().toString()+"./n");
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
