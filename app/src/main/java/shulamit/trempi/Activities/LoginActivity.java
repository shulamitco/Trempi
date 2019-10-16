package shulamit.trempi.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import shulamit.trempi.R;

public class LoginActivity extends AppCompatActivity {
    private Button btnOk;
    private EditText txvName;
    private EditText txvNumber;
    private SQLiteDatabase trempsDB = null;
    private SQLiteDatabase eventsDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnOk = findViewById(R.id.btn_login);
        txvName = findViewById(R.id.txv_name);
        txvNumber = findViewById(R.id.txv_number);
        createTrempsDB();
        createEventsDB();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txvName.getText().toString().isEmpty()||txvNumber.getText().toString().isEmpty())//if one of the filed are empty
                    openAlertDialog();
                else{
                    saveUser();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.USER_DETAILS), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.USER_NAME), txvName.getText().toString());
        editor.putString(getResources().getString(R.string.USER_NUMBER), txvNumber.getText().toString());
        editor.putInt(getResources().getString(R.string.TREMP_ID), 0);
        editor.commit();
    }
    private void openAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.fillAllFiled);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    public void createTrempsDB()
    {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);

            // build an SQL statement to create 'contacts' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS trempsList ( id integer primary key, eventName VARCHAR , startPosition VARCHAR, exit_time integer, emptyPlaces VARCHAR, name VARCHAR, number VARCHAR , isEnable integer);";
            trempsDB.execSQL(sql);
        }

        catch(Exception e){
            Log.d("debug", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
    }
    public void createEventsDB()
    {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            eventsDB = openOrCreateDatabase(getResources().getString(R.string.EVENTS_DB), MODE_PRIVATE, null);

            // build an SQL statement to create 'contacts' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS eventsList (eventName VARCHAR primary key, date Date, time Date, address VARCHAR, isRecycler integer );";
            eventsDB.execSQL(sql);
        }

        catch(Exception e){
            Log.d("debug", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
    }
}
