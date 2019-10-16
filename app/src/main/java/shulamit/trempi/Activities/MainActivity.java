package shulamit.trempi.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import shulamit.trempi.R;

public class MainActivity extends AppCompatActivity {
    private Button btnCreateEvent;
    private Button btnOpenEvent;
    private String name;
    private TextView txtHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreateEvent = findViewById(R.id.btn_create_event);
        btnOpenEvent = findViewById(R.id.btn_open_event);
        txtHello = findViewById(R.id.txt_hello);
        getUserDetails();
        txtHello.setText("Hello " + name+"!");
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//open create event activity class by pushing the create event activity butto
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);                startActivity(intent);
            }
        });
        btnOpenEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//open open event activity class by pushing the open open event button
                Intent intent = new Intent(MainActivity.this, OpenEventActivity.class);                startActivity(intent);
            }
        });
    }
    private void getUserDetails() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.USER_DETAILS), Context.MODE_PRIVATE);
        name = sharedPref.getString(getResources().getString(R.string.USER_NAME),"");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // open the relevant dialog / activity according to the menu selection
        switch (item.getItemId()) {
            case R.id.ic_about: {
                openAboutDialog();
                break;
            }
            case R.id.ic_exit: {
                openExitDialog();
                break;
            }
            case R.id.ic_share:{
                shareApp();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void shareApp() {
        String message = this.getResources().getString(R.string.share_message);
        Intent shareAppIntent = new Intent(Intent.ACTION_SEND);
        shareAppIntent.setType("text/plain");
        shareAppIntent.putExtra(Intent.EXTRA_TEXT,message);
        shareAppIntent.putExtra(Intent.EXTRA_SUBJECT,"Meet your friend!");
        startActivity(shareAppIntent);
    }

    private void openAboutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.about_title);
        alertDialog.setMessage(R.string.about);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void openExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.exit_title);
        alertDialog.setMessage( R.string.exit_qustion);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(1); // destroy this activity
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
    }
}
