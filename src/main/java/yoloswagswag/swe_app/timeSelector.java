package yoloswagswag.swe_app;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class timeSelector extends Activity {

    /* this is where the selected timeslots are stored */
    Calendar[][] timeSelector = new Calendar[7][4];

    /* the currently selected day  - initialised with monday*/
    Calendar currentDate = Calendar.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_selector);

    /* default settings */
        for(int i=0;i < timeSelector.length; i++){
            timeSelector[i][0] = ;
            timeSelector[i][1] = ;
            timeSelector[i][2] = ;
            timeSelector[i][3] = ;
        }
    }

    public void okTime(View view){
        /* nur, wenn die Zeiten korrekt gewaehlt wurden*/
        startActivity(new Intent(this, chillActivity.class));
    }

    public void daySelect(View view){
        switch(view.getId()){
            case (R.id.btnMon):
        }
    }

    public void timeSelect(View view){

        switch(view.getId()){
            case R.id.btn09:
                break;
            case R.id.btn10:
                break;
            case R.id.btn11:
                break;
            case R.id.btn12:
                break;
            case R.id.btn13:
                break;
            case R.id.btn14:
                break;
            case R.id.btn15:
                break;
            case R.id.btn16:
                break;
            case R.id.btn17:
                break;
            case R.id.btn18:
                break;
            case R.id.btn19:
                break;
            case R.id.btn20:
                break;
            case R.id.btn21:
                break;
            case R.id.btn22:
                break;
            case R.id.btn23:
                break;
        }
    }
}