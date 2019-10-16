
package shulamit.trempi.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import shulamit.trempi.R;
import shulamit.trempi.models.Contact;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> contacts;
    private ArrayList<Contact> selectedContacts;
    private boolean isSelectAll;

    public ContactAdapter(ArrayList<Contact> contacts, Context context,boolean isSelectAll) {
        this.contacts = contacts;
        this.context = context;
        this.selectedContacts = new ArrayList<>();
        this.isSelectAll = isSelectAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ContactAdapter.ViewHolder holder = new ContactAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtContactItrem.setText(contacts.get(position).toString());//set the contact details.
        if(isSelectAll)
            viewHolder.ckbContactItem.setChecked(true);
        viewHolder.ckbContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//if the contacts are chosen add it to the selected contacts
                if (viewHolder.ckbContactItem.isChecked()) {
                    selectedContacts.add(contacts.get(position));
                }
            }
        });
    }

    /**
     * @return array list with the selected contacts
     */
    public ArrayList getSelected() {
        if (selectedContacts != null && selectedContacts.size() >= 1) {
            return selectedContacts;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox ckbContactItem;
        private TextView txtContactItrem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ckbContactItem = itemView.findViewById(R.id.ckb_is_checked);
            txtContactItrem = itemView.findViewById(R.id.txt_contact);
        }
    }
    @Override
    public void onViewRecycled(@NonNull ContactAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
