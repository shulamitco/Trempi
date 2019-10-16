package shulamit.trempi.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

import shulamit.trempi.R;
import shulamit.trempi.models.Tremp;

public class TrempsActivity extends AppCompatActivity {
    private FloatingActionButton fabCreateTremp;
    private RecyclerView trempsRecycleView;
    private ArrayList<Tremp> trempsList;
    private SQLiteDatabase trempsDB = null;
    private String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tremps);
        trempsList = new ArrayList<>();
        Intent intentInPut = getIntent();
        eventName = intentInPut.getStringExtra(getResources().getString(R.string.event_name_extra));
        this.setTitle(getResources().getString(R.string.tremps)+" "+eventName.toUpperCase());

        trempsRecycleView = findViewById(R.id.ltv_tremps);
        fabCreateTremp = findViewById(R.id.fab_add_tremp);
        fabCreateTremp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrempsActivity.this, AddTrempActivity.class);
                intent.putExtra(getResources().getString(R.string.event_name_extra),eventName);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadEventTremps();
    }

    private void loadEventTremps() {
        try {
            //get from database firebase or sql i think sql
            trempsList.clear();
            trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);
            String sql = "SELECT * FROM trempsList";
            Cursor cursor = trempsDB.rawQuery(sql, null);
            // Get the index for the column name provided
            int eventNameColumn = cursor.getColumnIndex("eventName");
            int startPositionColumn = cursor.getColumnIndex("startPosition");
            int exitTimeColumn = cursor.getColumnIndex("exit_time");
            int emptyPlacesColumn = cursor.getColumnIndex("emptyPlaces");
            int nameColumn = cursor.getColumnIndex("name");
            int numberColumn = cursor.getColumnIndex("number");
            int idColumn = cursor.getColumnIndex("id");
            int isEnableColumn = cursor.getColumnIndex("isEnable");
            Intent intent = this.getIntent();
            String currEventName = intent.getStringExtra(getResources().getString(R.string.event_name_extra));
            // Move to the first row of results & Verify that we have results
            if (cursor.moveToFirst()) {
                do {
                    // Get the results and store them in a String
                    String eventName = cursor.getString(eventNameColumn);
                    String time = cursor.getString(exitTimeColumn);
                    String  startPosition = cursor.getString(startPositionColumn);
                    int emptyPlaces = cursor.getInt(emptyPlacesColumn);
                    String name = cursor.getString(nameColumn);
                    String number = cursor.getString(numberColumn);
                    int id = cursor.getInt(idColumn);
                    boolean isEnable = isEnable(cursor.getInt(isEnableColumn));
                    if(eventName.equals(currEventName))
                    {
                        Tremp tremp = new Tremp(eventName, startPosition, time, emptyPlaces, name, number, id, isEnable);
                        trempsList.add(tremp);
                    }


                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(TrempsActivity.this  , getResources().getString(R.string.tremps_not_found),Toast.LENGTH_LONG).show();
            }
            TrempsAdapter adapter = new TrempsAdapter(trempsList, this);
            trempsRecycleView.setAdapter(adapter);
            trempsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch (Exception e){ }
    }

    private boolean isEnable(int isEnable) {
        if(isEnable == 1)
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TrempsActivity.this, OpenEventActivity.class);
        startActivity(intent);
    }

}
