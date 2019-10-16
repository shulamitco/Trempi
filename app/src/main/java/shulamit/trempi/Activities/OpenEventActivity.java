package shulamit.trempi.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import shulamit.trempi.R;
import shulamit.trempi.models.Event;

public class OpenEventActivity extends AppCompatActivity {
    private EditText txvEventName;
    private Button btnSearchEvent;
    private RecyclerView eventsRecycleView;
    private ArrayList<Event> eventsList;
    private SQLiteDatabase eventsDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_event);
        eventsList = new ArrayList<>();
        txvEventName = findViewById(R.id.txv_event_name);
        btnSearchEvent = findViewById(R.id.btn_search_event);
        eventsRecycleView = findViewById(R.id.ltv_events);
        btnSearchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txvEventName == null || txvEventName.getText().toString().isEmpty())
                {
                    openAlertDialog();
                    loadRecyclerEvents();
                }
                else
                    searchEvent();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadRecyclerEvents();
    }
    private void openAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.fillAllFiled);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
    private void searchEvent() {
        try {
            //get from database firebase or sql i think sql
            eventsList.clear();
            eventsDB = openOrCreateDatabase(getResources().getString(R.string.EVENTS_DB), MODE_PRIVATE, null);
            String sql = "SELECT * FROM eventsList";
            Cursor cursor = eventsDB.rawQuery(sql, null);
            // Get the index for the column name provided
            int eventNameColumn = cursor.getColumnIndex("eventName");
            int dateColumn = cursor.getColumnIndex("date");
            int timeColumn = cursor.getColumnIndex("time");
            int addressColumn = cursor.getColumnIndex("address");
            int isRecyclerColumn = cursor.getColumnIndex("isRecycler");

            // Move to the fir/st row of results & Verify that we have results
            if (cursor.moveToFirst()) {
                do {
                    // Get the results and store them in a String
                    String eventName = cursor.getString(eventNameColumn);
                    String date = cursor.getString(dateColumn);
                    String time = cursor.getString(timeColumn);
                    String  address = cursor.getString(addressColumn);
                    int isRecycler = cursor.getInt(isRecyclerColumn);
                    if(eventName.equals(txvEventName.getText().toString()))
                    {
                        Event event = new Event(eventName, date, time, address,true);
                        eventsList.add(event);
                    }

                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else {
                if(eventsList.size() == 0)
                Toast.makeText(OpenEventActivity.this  , getResources().getString(R.string.event_not_found),Toast.LENGTH_LONG).show();
                loadRecyclerEvents();
            }
            EventsAdapter adapter = new EventsAdapter(eventsList, this);
            eventsRecycleView.setAdapter(adapter);
            eventsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch (Exception e){ }
    }

    private void loadRecyclerEvents() {
        try {
            //get from database firebase or sql i think sql
            eventsList.clear();
            eventsDB = openOrCreateDatabase(getResources().getString(R.string.EVENTS_DB), MODE_PRIVATE, null);
            String sql = "SELECT * FROM eventsList";
            Cursor cursor = eventsDB.rawQuery(sql, null);
            // Get the index for the column name provided
            int eventNameColumn = cursor.getColumnIndex("eventName");
            int dateColumn = cursor.getColumnIndex("date");
            int timeColumn = cursor.getColumnIndex("time");
            int addressColumn = cursor.getColumnIndex("address");
            int isRecyclerColumn = cursor.getColumnIndex("isRecycler");

            // Move to the first row of results & Verify that we have results
            if (cursor.moveToFirst()) {
                do {
                    // Get the results and store them in a String
                    String eventName = cursor.getString(eventNameColumn);
                    String date = cursor.getString(dateColumn);
                    String time = cursor.getString(timeColumn);
                    String  address = cursor.getString(addressColumn);
                    int isRecycler = cursor.getInt(isRecyclerColumn);
                    if(isRecycler == 1)
                    {
                        Event event = new Event(eventName, date, time, address,true);
                        eventsList.add(event);
                    }

                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(OpenEventActivity.this  , getResources().getString(R.string.recycler_event_not_found),Toast.LENGTH_LONG).show();
            }
            EventsAdapter adapter = new EventsAdapter(eventsList, this);
            eventsRecycleView.setAdapter(adapter);
            eventsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch (Exception e){ }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OpenEventActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // open the relevant dialog / activity according to the menu selection
        switch (item.getItemId()) {
            case R.id.refresh_button: {
                loadRecyclerEvents();
                txvEventName.setText("");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
