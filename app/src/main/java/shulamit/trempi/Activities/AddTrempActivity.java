package shulamit.trempi.Activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

import shulamit.trempi.R;
import shulamit.trempi.models.Tremp;

public class AddTrempActivity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener {
    private String eventName;
    private EditText txvStartPosition;
    private TextView txtExitTime;
    private EditText txvEmptyPlaces;
    private String name;
    private String number;
    private String time;
    private Button btnAddTremp;
    private Tremp tremp;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tremp);
        final Intent intent = this.getIntent();
        eventName = intent.getStringExtra(this.getResources().getString(R.string.event_name_extra));
        getUserDetails();//get the user details - shared preference
        txvStartPosition = findViewById(R.id.txv_start);//connect components
        txtExitTime = findViewById(R.id.txt_exit_time);
        txvEmptyPlaces = findViewById(R.id.txv_empty_places);
        btnAddTremp = findViewById(R.id.btn_add_tremp);
        sharedPref = getSharedPreferences(getString(R.string.USER_DETAILS), Context.MODE_PRIVATE);
        setListeners();
    }

    private void setListeners() {
        btnAddTremp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//add tremp button
                if(!isAllFilesFull())//if the all filed are full
                {
                    int id = sharedPref.getInt(getResources().getString(R.string.TREMP_ID),0);
                    tremp = new Tremp(eventName,txvStartPosition.getText().toString(),time,Integer.parseInt(txvEmptyPlaces.getText().toString()),name,number , id, true);
                    id++;
                    save_next_tremp_id(id);//save the next tremp id
                    saveTrempInDB();//save the tremp in db
                    Intent intent1 = new Intent(AddTrempActivity.this, TrempsActivity.class);//move the the tremps sceen
                    intent1.putExtra(getResources().getString(R.string.event_name_extra),eventName);
                    startActivity(intent1);
                }
                else{//empty filed
                    openAllFullAlertDialog();
                }
            }
        });
        txtExitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//chose exit time from time picker
                DialogFragment timePiker = new TimePickerFragment();
                timePiker.show(getSupportFragmentManager(),"time picker");
            }
        });
    }

    /**
     * save i in the shared preference file
     * @param i - display the next tremp id
     */
    private void save_next_tremp_id(int i) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getResources().getString(R.string.TREMP_ID), i);
        editor.commit();
    }

    /**
     * check that all fields are full
     * @return true if all full and false otherwise
     */
    private boolean isAllFilesFull() {
        if(txvEmptyPlaces == null||txvStartPosition == null||time == null||name == null||number == null||eventName == null)
        {
            return true;
        }
        if(txvEmptyPlaces.getText().toString().isEmpty()||txtExitTime.getText().toString().isEmpty()||txvStartPosition.getText().toString().isEmpty())
        {
            return true;
        }
        return false;

    }

    /**
     * save the new tremp in db
     */
    private void saveTrempInDB() {
        // Get the Tremps db details
        SQLiteDatabase trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);
        // Execute SQL statement to insert new data
        int isEnable;
        if(tremp.isEnable())
            isEnable = 1;
        else
            isEnable = 0;
        String sql = "INSERT INTO trempsList (id, eventName, startPosition, exit_time, emptyPlaces, name, number, isEnable) VALUES ('" + tremp.getTrempId() + "', '" + tremp.getEventName() + "', '" + tremp.getStartPosition() +"', '" + tremp.getExitTime().toString() + "', '" + tremp.getEmptyPlaces() + "' , '" + tremp.getName() + "', '" + tremp.getNumber() + "', '" + isEnable + "');";
        trempsDB.execSQL(sql);
        // Execute SQL statement to insert new data
        Toast.makeText(this,  getResources().getString(R.string.tremp_was_insert), Toast.LENGTH_LONG).show();
    }

    /**
     * get user detail from shared preference file
     */
    private void getUserDetails() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.USER_DETAILS), Context.MODE_PRIVATE);
        name = sharedPref.getString(getResources().getString(R.string.USER_NAME),"");
        number = sharedPref.getString(getResources().getString(R.string.USER_NUMBER),"");
    }

    /**
     * display all fields alert
     */
    private void openAllFullAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.fillAllFiled);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay +":" + minute;
        txtExitTime.setText(time);
    }
}
