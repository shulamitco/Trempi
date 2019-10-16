package shulamit.trempi.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shulamit.trempi.R;
import shulamit.trempi.models.Event;


class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    public EventsAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.eventItem.setText(events.get(position).toString());//set the event details
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventItem = itemView.findViewById(R.id.txv_event_item);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        /**
         * on click on event item open tremps list
         */
        holder.eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = events.get(position).getName();
                Intent intent = new Intent(context, TrempsActivity.class);
                intent.putExtra(context.getResources().getString(R.string.event_name_extra),temp);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
