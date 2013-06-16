package yoloswagswag.swe_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class timeSelector extends Activity {

    /* this is where the selected timeslots are stored */
    int[][] timeSelector = new int[7][4];

    /* the currently selected day  - initialised with monday*/
    int currentDay = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_selector);

    /* default settings */
        for(int i=0;i < 7; i++){
            timeSelector[i][0] = 1;
            timeSelector[i][1] = 1;
            timeSelector[i][2] = 1;
            timeSelector[i][3] = 1;
        }
    }

    public void timeSelect(View view){
        int slot=0;
        int time=0;
        switch(view.getId()){
            case R.id.btn09:
                slot = 0;
                time = 0;
                break;
            case R.id.btn10:
                slot = 0;
                time = 1;
                break;
            case R.id.btn11:
                slot = 0;
                time = 2;
                break;
            case R.id.btn12:
                slot = 0;
                time = 3;
                break;
            case R.id.btn13:
                slot = 1;
                time = 0;
                break;
            case R.id.btn14:
                slot = 1;
                time = 1;
                break;
            case R.id.btn15:
                slot = 1;
                time = 2;
                break;
            case R.id.btn16:
                slot = 2;
                time = 0;
                break;
            case R.id.btn17:
                slot = 2;
                time = 1;
                break;
            case R.id.btn18:
                slot = 2;
                time = 2;
                break;
            case R.id.btn19:
                slot = 2;
                time = 3;
                break;
            case R.id.btn20:
                slot = 3;
                time = 0;
                break;
            case R.id.btn21:
                slot = 3;
                time = 1;
                break;
            case R.id.btn22:
                slot = 3;
                time = 2;
                break;
            case R.id.btn23:
                slot = 3;
                time = 3;
                break;
        }
        timeSelector[currentDay][slot] = time;

    }
}