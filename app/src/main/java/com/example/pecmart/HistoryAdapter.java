package com.example.pecmart;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{

    private List<Completed> completed;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public HistoryAdapter(Context context , List<Completed> list)
    {
        completed = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvtitle,tvprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvprice = itemView.findViewById(R.id.tvprice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onItemClicked(completed.indexOf((Completed) v.getTag()));

                }
            });
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_row_layout,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, int i) {

        viewHolder.itemView.setTag(completed.get(i));

        viewHolder.tvtitle.setText(completed.get(i).getItemTitle());
        viewHolder.tvprice.setText("Rs."+completed.get(i).getItemPrice());

    }

    @Override
    public int getItemCount() {
        return completed.size();
    }
}
