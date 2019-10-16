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
import shulamit.trempi.models.Tremp;

class TrempsAdapter extends RecyclerView.Adapter<TrempsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Tremp> tremps;

    public TrempsAdapter(ArrayList<Tremp> tremps, Context context) {
        this.tremps = tremps;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tremp_item, parent, false);
        TrempsAdapter.ViewHolder holder = new TrempsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.trempItem.setText(tremps.get(position).toString());

        if(!tremps.get(position).isEnable())
            viewHolder.trempItem.setBackground(context.getResources().getDrawable(R.drawable.tremp_unable_background));
        else
            viewHolder.trempItem.setBackground(context.getResources().getDrawable(R.drawable.tremp_item_background));



    }

    @Override
    public int getItemCount() {
        return tremps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView trempItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trempItem = itemView.findViewById(R.id.txv_tremp_item);

        }
    }
    @Override
    public void onBindViewHolder(@NonNull final TrempsAdapter.ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        /**
         * on click on tremp item open tremp details
         */
        holder.trempItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrempActivity.class);
                intent.putExtra(context.getResources().getString(R.string.tremp_name_extra),tremps.get(position).getName());
                intent.putExtra(context.getResources().getString(R.string.tremp_number_extra),tremps.get(position).getNumber());
                intent.putExtra(context.getResources().getString(R.string.tremp_empty_places_extra),tremps.get(position).getEmptyPlaces());
                intent.putExtra(context.getResources().getString(R.string.tremp_start_position_extra),tremps.get(position).getStartPosition());
                intent.putExtra(context.getResources().getString(R.string.tremp_id_extra),tremps.get(position).getTrempId());
                intent.putExtra(context.getResources().getString(R.string.tremp_event_name_extra),tremps.get(position).getEventName());
                intent.putExtra(context.getResources().getString(R.string.tremp_exit_time_extra),tremps.get(position).getExitTime());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void onViewRecycled(@NonNull TrempsAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
