package shulamit.trempi.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import shulamit.trempi.R;
import shulamit.trempi.models.Contact;


public class AddContactActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    private ArrayList<Contact> contacts;
    private RecyclerView lsvContact;
    private EditText txvSearchByName;
    private Button banSearch;
    private FloatingActionButton btnSend;
    private String eventName;
    private ContactAdapter adapter;
    private CheckBox ckbSelectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Intent intent = getIntent();
        lsvContact = findViewById(R.id.contacts_list_view_id);
        txvSearchByName = findViewById(R.id.txv_name_to_search);
        banSearch = findViewById(R.id.btn_search_contact);
        btnSend = findViewById(R.id.btn_send_sms);
        ckbSelectAll = findViewById(R.id.ckb_select_all);
        contacts = new ArrayList<>();
        adapter = new ContactAdapter(contacts, this, false);
        eventName = intent.getStringExtra(getResources().getString(R.string.event_name_extra));
        contacts = new ArrayList<>();
        setListeners();
    }

    /**
     * set the button listeners
     */
    private void setListeners() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        banSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txvSearchByName != null && !txvSearchByName.getText().toString().isEmpty())
                {
                    searchContact();
                }
            }
        });
        ckbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ckbSelectAll.isChecked())
                    connectAdapter(true);
                else
                    connectAdapter(false);




            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadContacts();//load the Contact from the content provider
    }

    // Read all contacts from Content Provider in Contacts App.
    public void loadContacts()
    {
        if(isPermissionToReadContactsOK())//check permission
            getContacts();

    }

    private void getContacts() {
        {
            contacts.clear();

            String name, phone;

            ContentResolver resolver = getContentResolver();
            Uri contactsTableUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = resolver.query(contactsTableUri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

            if(cursor != null)
            {
                if(cursor.moveToNext())
                {
                    // there is at least ONE contact
                    do
                    {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contact contact = new Contact(name,phone);//create a person and add it to the list
                        contacts.add(contact);
                    }
                    while(cursor.moveToNext());

                    cursor.close();
                }
                else
                    // Empty - No contacts
                    showNotExistAtAllAlert();
                connectAdapter(false);//connect adapter
            }
            else
                // problem with resolver query
                Toast.makeText(AddContactActivity.this,getResources().getString(R.string.problem_to_get_contacts),Toast.LENGTH_LONG).show();
        }
    }
    /**
     * the function deal with the search of contact, pass every contact and if its contains the input display it.
     */
    private void searchContact() {
        contacts.clear();
        String name, phone;

        ContentResolver resolver = getContentResolver();//get the contacts
        Uri contactsTableUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = resolver.query(contactsTableUri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        if(cursor != null)//pass the contacts
        {
            if(cursor.moveToNext())
            {
                // there is at least ONE contact
                do {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if (name.contains(txvSearchByName.getText().toString())) {
                        phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contact contact = new Contact(name, phone);//create a person and add it to the list
                        contacts.add(contact);
                    }
                }
                while(cursor.moveToNext());
                cursor.close();
                if(contacts.size()==0)//no such contacts
                    showNotExistAlert();
            }
            else
                // Empty - No contacts
                showNotExistAtAllAlert();
            connectAdapter(false);
        }
        else
            // problem with resolver query
            Toast.makeText(AddContactActivity.this,getResources().getString(R.string.problem_to_get_contacts),Toast.LENGTH_LONG).show();
    }

    /**
     * connect the adapter to the recycler view
     */
    private void connectAdapter(boolean isChecked) {
        adapter = new ContactAdapter(contacts, this, isChecked);
        lsvContact.setAdapter(adapter);
        lsvContact.setLayoutManager(new LinearLayoutManager(this));
    }
    /**
     * display contacts not exist alers
     */
    private void showNotExistAtAllAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.messageNotExistAtAll);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    /**
     * display alert if contact not exist on the device
     */
    private void showNotExistAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher_app_round);
        alertDialog.setTitle(R.string.oops);
        alertDialog.setMessage(R.string.messageNotExist);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    // Check Runtime Permission for READ_CONTACTS
    public boolean isPermissionToReadContactsOK()
    {
        // check if permission for READ_CONTACTS is granted ?
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
        {
            // show requestPermissions dialog
            ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
    }


    /**
     * pass every chosen contact and send him an invitation sms
     */
    private void sendSms() {
        ArrayList <Contact> sentSmsTo= adapter.getSelected();
        if(askForSmsPermission()) {//check sms permission
            if (sentSmsTo != null) {
                for (Contact contact : sentSmsTo) {//permission accepted
                    sendSingleSms(contact);
                }
                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.sms_sent), Toast.LENGTH_LONG).show();
                onBackPressed();

            } else {//permission denied
                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.no_selection), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendSingleSms(Contact contact) {
        SmsManager smgr = SmsManager.getDefault();
        smgr.sendTextMessage(contact.getNumber(), null, getResources().getString(R.string.hii)+"\n"+ getResources().getString(R.string.contacts_sms) +" "+ eventName+".\n"+ getResources().getString(R.string.search_invitation), null, null);
    }

    private boolean askForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(AddContactActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
        {
            // show requestPermissions dialog
            ActivityCompat.requestPermissions(AddContactActivity.this ,new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts();
                }
                else {
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.permission_request), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                 If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();

                } else {
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.permission_request), Toast.LENGTH_LONG).show();

                }
                break;
            }
        }
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
                loadContacts();
                txvSearchByName.setText("");
                ckbSelectAll.setChecked(false);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    }
