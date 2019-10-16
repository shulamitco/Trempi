package shulamit.trempi.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import shulamit.trempi.R;
import shulamit.trempi.models.Event;

public class CreateEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText txvEventName;
    private TextView txvDate;
    private TextView txvTime;
    private Button btnInviteContacts;
    private EditText txvAddress;
    private CheckBox cmbIsRecycler;
    private String time;
    private String date;
    private Button btnOkCreateEvent;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        txvEventName = findViewById(R.id.txv_name_of_event);
        txvTime = findViewById(R.id.txv_time_of_event);
        txvDate = findViewById(R.id.txv_date_of_event);
        btnInviteContacts = findViewById(R.id.btn_invite_contacts);
        txvAddress = findViewById(R.id.txv_address_of_event);
        cmbIsRecycler = findViewById(R.id.cmb_is_recycler);
        btnOkCreateEvent = findViewById(R.id.btn_ok_create_event);
        setListeners();

    }
    //set all listeners
    private void setListeners() {
        //get the chosen time
        txvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePiker = new TimePickerFragment();
                timePiker.show(getSupportFragmentManager(),"time picker");

            }
        });
        //get the chosen date
        txvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePiker = new DatePickerFragment();
                datePiker.show(getSupportFragmentManager(),"date picker");
            }
        });
        //open the contacts screen to invite people
        btnInviteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txvEventName == null || txvEventName.getText().toString().isEmpty())
                    openEventNameAlertDialog();//have to insert event name first
                else//go to the contacts screen
                    {
                    Intent intent = new Intent(CreateEventActivity.this, AddContactActivity.class);//go to add people
                    intent.putExtra(getResources().getString(R.string.event_name_extra), txvEventName.getText().toString());
                    startActivity(intent);
                }

            }
        });
        //ok button
        btnOkCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emptyFieldExist())//if there is an empty field - > open alrts
                    openAllFullAlertDialog();
                else
                {
                    if(EventNameNotExist())//if the event name not already exist
                    {
                        event = new Event(txvEventName.getText().toString(),date,time,txvAddress.getText().toString(),cmbIsRecycler.isChecked());
                        saveEventInDB();//save event in db
                        Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);//move to the main screen
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     *
     * @return false if thre is an empty field and false otherwise
     * */
    private boolean emptyFieldExist() {
        if(txvEventName == null||txvAddress == null||date == null||time == null)
        {
            return true;
        }
        if(txvEventName.getText().toString().isEmpty()||txvAddress.getText().toString().isEmpty())
        {
            return true;
        }
        return false;
    }

    /**
     * save event in db
     */
    private void saveEventInDB() {
        // Get the events details
        SQLiteDatabase eventsDB = openOrCreateDatabase(getResources().getString(R.string.EVENTS_DB), MODE_PRIVATE, null);
        // Execute SQL statement to insert new data
        String sql = "INSERT INTO eventsList (eventName, date,time, address, isRecycler) VALUES ('" + event.getName() + "', '" + event.getDate() +"', '" + event.getTime()+ "', '" + event.getAddress() + "' , '" + event.isRecycler() + "');";
        eventsDB.execSQL(sql);
        // Execute SQL statement to insert new data
        Toast.makeText(this,  " event was insert!", Toast.LENGTH_LONG).show();
    }

    /**
     * @return true if the event name not exist
     */
    private boolean EventNameNotExist() {
        try {
            //get from database firebase or sql i think sql
            SQLiteDatabase eventsDB = openOrCreateDatabase(getResources().getString(R.string.EVENTS_DB), MODE_PRIVATE, null);
            String sql = "SELECT eventName FROM eventsList";
            Cursor cursor = eventsDB.rawQuery(sql, null);
            // Get the index for the column name provided
            int eventNameColumn = cursor.getColumnIndex("eventName");

            // Move to the first row of results & Verify that we have results
            if (cursor.moveToFirst()) {
                do {
                    // Get the results and store them in a String
                    String eventName = cursor.getString(eventNameColumn);

                    if(eventName.equals(txvEventName.getText().toString()))
                    {
                        openAlertAllredyExistDialog();
                        return false;
                    }

                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else { }
        }
        catch (Exception e){ }
        return true;
    }
    /**
     * display alert if the event name already exist
     */
    private void openEventNameAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.messageEventName);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    /**
     * if there is an empty field display alert
     */
    private void openAllFullAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.fillAllFiled);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    /**
     * if the event name already exist disply alert
     */
    private void openAlertAllredyExistDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.titleExist);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        txvTime.setText(time);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + "/" + year;
        txvDate.setText(date);
    }
}
