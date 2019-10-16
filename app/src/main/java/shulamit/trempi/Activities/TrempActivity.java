package shulamit.trempi.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import shulamit.trempi.R;

public class TrempActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    private String trempName;
    private String trempNumber;
    private String trempStartPosition;
    private int trempEmptyPlaces;
    private String trempEventName;
    private TextView txtCarName;
    private TextView txtCarNumber;
    private TextView txtCarPlaces;
    private TextView txtCarExitPlace;
    private TextView txtCarEventName;
    private TextView txtCarExitTime;
    private Button btnTakePlace;
    private Button btnTakingPlace;
    private Button btnOkSetPlaces;
    private EditText txvHowManyPlaces;
    private Button btnSetPlaces;
    private EditText txvSetPlaces;
    private String emptyPlaces;
    private int trempId;
    private SQLiteDatabase trempsDB = null;
    private String userName;
    private String userNumber;
    private String trempExitTime;
    private final String fakeNumber = "+972546908192";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tremp);
        btnTakePlace = findViewById(R.id.btn_take_place);
        btnTakingPlace = findViewById(R.id.btn_ok_take_place);
        txvHowManyPlaces = findViewById(R.id.txv_places_number);
        txtCarName = findViewById(R.id.txv_car_name);
        txtCarNumber = findViewById(R.id.txv_car_number);
        txtCarPlaces = findViewById(R.id.txv_car_places);
        txtCarExitPlace = findViewById(R.id.txv_car_exit_position);
        btnOkSetPlaces = findViewById(R.id.btn_ok_set_places);
        btnSetPlaces = findViewById(R.id.btn_set_place);
        txvSetPlaces= findViewById(R.id.places_to_set);
        Intent intent = getIntent();
        txtCarName = findViewById(R.id.txv_car_name);
        txtCarNumber = findViewById(R.id.txv_car_number);
        txtCarPlaces = findViewById(R.id.txv_car_places);
        txtCarEventName = findViewById(R.id.txv_car_event_name);
        txtCarExitTime = findViewById(R.id.txv_car_exit_time);

        trempName = intent.getStringExtra(getResources().getString(R.string.tremp_name_extra));
        trempNumber = intent.getStringExtra(getResources().getString(R.string.tremp_number_extra));
        trempEmptyPlaces = intent.getIntExtra(getResources().getString(R.string.tremp_empty_places_extra),0);
        trempStartPosition = intent.getStringExtra(getResources().getString(R.string.tremp_start_position_extra));
        trempEventName = intent.getStringExtra(getResources().getString(R.string.tremp_event_name_extra));
        trempId = intent.getIntExtra(getResources().getString(R.string.tremp_id_extra),0);
        trempExitTime = intent.getStringExtra(getResources().getString(R.string.tremp_exit_time_extra));
        getUserDetails();
        setListeners();
        if(userNumber.equals(trempNumber)){
            btnSetPlaces.setVisibility(View.VISIBLE);
        }
      }

    /**
     * get user detail from shared preference file
     */
    private void getUserDetails() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.USER_DETAILS), Context.MODE_PRIVATE);
        userName = sharedPref.getString(getResources().getString(R.string.USER_NAME),"");
        userNumber = sharedPref.getString(getResources().getString(R.string.USER_NUMBER),"");
    }

    private void setListeners() {
        btnTakePlace.setOnClickListener(new View.OnClickListener() {
            @Override//open edit text to chose number of places
            public void onClick(View v) {
                txvHowManyPlaces.setVisibility(View.VISIBLE);
                btnTakingPlace.setVisibility(View.VISIBLE);
                btnTakePlace.setVisibility(View.GONE);
            }
        });

        btnTakingPlace.setOnClickListener(new View.OnClickListener() {
            @Override//make sure the place is valid
            public void onClick(View v) {
                if(txvHowManyPlaces == null || txvHowManyPlaces.getText().toString().isEmpty())
                {
                    openEnterAmountAlert();
                    getToStart();
                }
                else {
                    emptyPlaces = txvHowManyPlaces.getText().toString();

                    if (Integer.parseInt(emptyPlaces) > trempEmptyPlaces) {
                        noPlacesAlert();
                        txvHowManyPlaces.setText("");
                    } else if (Integer.parseInt(emptyPlaces) < 1)//if the number we got is < 1
                        atLeastOnePlaceAlert();
                    else//delete or update tremp
                    {
                        updatePlaces();
                        btnTakePlace.setVisibility(View.VISIBLE);
                        txvHowManyPlaces.setVisibility(View.GONE);
                        btnTakingPlace.setVisibility(View.GONE);
                        txvHowManyPlaces.setText("");

                    }
                }
                getToStart();

            }
        });
        btnSetPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvSetPlaces.setVisibility(View.VISIBLE);
                btnSetPlaces.setVisibility(View.GONE);
                btnOkSetPlaces.setVisibility(View.VISIBLE);

            }
        });
        btnOkSetPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvSetPlaces.setVisibility(View.GONE);
                btnOkSetPlaces.setVisibility(View.GONE);
                btnSetPlaces.setVisibility(View.VISIBLE);
                if (txvSetPlaces == null || txvSetPlaces.getText().toString().isEmpty())
                    openEnterAmountAlert();
                else {
                    if (Integer.parseInt(txvSetPlaces.getText().toString()) < 1)
                        atLeastOnePlaceAlert();
                    else {
                        setPlaces(Integer.parseInt(txvSetPlaces.getText().toString()));
                        updatePlacesOnScreen(Integer.parseInt(txvSetPlaces.getText().toString()) + trempEmptyPlaces);
                        txvSetPlaces.setText("");
                    }
                }

                getToStart();
            }
        });
    }

    private void getToStart() {
        txvSetPlaces.setText("");
        txvHowManyPlaces.setText("");
        txvHowManyPlaces.setVisibility(View.GONE);
        txvSetPlaces.setVisibility(View.GONE);
        btnTakePlace.setVisibility(View.VISIBLE);
        btnSetPlaces.setVisibility(View.VISIBLE);
        btnTakingPlace.setVisibility(View.GONE);
        btnOkSetPlaces.setVisibility(View.GONE);
    }

    private void openEnterAmountAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.messageEmptyField);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void updatePlacesOnScreen(int newPlaces) {
        txtCarPlaces.setText("left " + newPlaces+ " empty places");
        trempEmptyPlaces = newPlaces;
    }

    private void setPlaces(int newPlaces) {
        trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);
        int num = trempEmptyPlaces + newPlaces;
        String sql = "UPDATE trempsList SET emptyPlaces = " + num + " WHERE id = " + trempId + ";";
        trempsDB.execSQL(sql);
        Toast.makeText(TrempActivity.this, getResources().getString(R.string.tremp_updated),Toast.LENGTH_LONG).show();
    }

    private void atLeastOnePlaceAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.more_places_message);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void updatePlaces() {

        if(Integer.parseInt(emptyPlaces) < trempEmptyPlaces){
            updateTremp();
        }
        else if(Integer.parseInt(emptyPlaces) == trempEmptyPlaces){
            unableTremp();
        }
        sendSms();
        updatePlacesOnScreen(trempEmptyPlaces - Integer.parseInt(emptyPlaces));
    }

    private void sendSms() {
        if(askForSmsPermission())
        {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(fakeNumber, null, getResources().getString(R.string.hii)+"\n"+getResources().getString(R.string.driver_sms)+" "+getResources().getString(R.string.driver_trempi)+getResources().getString(R.string.driver_to) +" "+trempEventName+ "\n" + userName+".",null, null);
            Toast.makeText(TrempActivity.this, getResources().getString(R.string.sms_driver),Toast.LENGTH_LONG).show();
        }
    }

    private boolean askForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(TrempActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
        {
            // show requestPermissions dialog
            ActivityCompat.requestPermissions(TrempActivity.this ,new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();

                } else {
                    Toast.makeText(TrempActivity.this, getResources().getString(R.string.permission_request), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    /***
     *  update tremp in  database
     */
    private void updateTremp() {
        trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);
        int num = trempEmptyPlaces - Integer.parseInt(emptyPlaces);
        String sql = "UPDATE trempsList SET emptyPlaces = " + num + " WHERE id = " + trempId + ";";
        trempsDB.execSQL(sql);
        Toast.makeText(TrempActivity.this, getResources().getString(R.string.tremp_updated),Toast.LENGTH_LONG).show();
    }

    /**
     *   unable matching id in database
     */
    private void unableTremp() {
        // Get the id to unable
        trempsDB = openOrCreateDatabase(getResources().getString(R.string.TREMPS_DB), MODE_PRIVATE, null);
        int num = trempEmptyPlaces - Integer.parseInt(emptyPlaces);
        String sql = "UPDATE trempsList SET emptyPlaces = " + num + ", isEnable = 0 WHERE id = " + trempId + ";";
        trempsDB.execSQL(sql);
        Toast.makeText(TrempActivity.this, getResources().getString(R.string.tremp_updated),Toast.LENGTH_LONG).show();




    }

    @Override
    protected void onStart() {
        super.onStart();
        txtCarPlaces.setText("left " + trempEmptyPlaces+ " empty places");
        txtCarNumber.setText("phone number: " + trempNumber);
        txtCarName.setText("name: " + trempName);
        txtCarExitPlace.setText("exit from: " + trempStartPosition );
        txtCarEventName.setText("event name: " + trempEventName );
        txtCarExitTime.setText("exit time: " + trempExitTime );


    }

    private void noPlacesAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.messageNoPlaces);
        alertDialog.setCancelable(true);
        alertDialog.show();

    }
}
