package com.example.pecmart;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
{

    private List<items> Item;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public ItemsAdapter(Context context, List<items> list)
    {
        Item = list;
        activity = (ItemClicked) context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.buy1_row_layout,viewGroup,false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.itemView.setTag(Item.get(i));

        viewHolder.tvtitle.setText(Item.get(i).getTitle());
        viewHolder.tvprice.setText("Rs."+Item.get(i).getPrice());
        viewHolder.tvcat.setText(Item.get(i).getCategory());

        if(Item.get(i).getCategory().equals("Books and Novels"))
        {
            viewHolder.ivcat.setImageResource(R.drawable.book);
        }

        else if(Item.get(i).getCategory().equals("Stationery"))
        {
            viewHolder.ivcat.setImageResource(R.drawable.stationary);
        }

        else if(Item.get(i).getCategory().equals("Hostel Needs"))
        {
            viewHolder.ivcat.setImageResource(R.drawable.hostel);
        }

        else if(Item.get(i).getCategory().equals("Electronics and Appliances"))
        {
            viewHolder.ivcat.setImageResource(R.drawable.electronics);
        }

    }

    @Override
    public int getItemCount() {
        return Item.size();
    }

    public void filterList(List<items> filterdNames) {
        this.Item = filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivcat;
        TextView tvtitle,tvcat,tvprice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvprice = itemView.findViewById(R.id.tvprice);
            tvcat = itemView.findViewById(R.id.tvcat);
            ivcat = itemView.findViewById(R.id.ivcat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onItemClicked(Item.indexOf((items) v.getTag()));

                }
            });
        }
    }

}
