package com.example.pecmart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class request_userAdapter extends RecyclerView.Adapter<request_userAdapter.ViewHolder>
{

    private List<user> User;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public request_userAdapter(Context context,List<user> list)
    {
        User = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvchar,tvusername,tvuserbranch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //tvchar = itemView.findViewById(R.id.tvchar);
            tvusername = itemView.findViewById(R.id.tvusername);
            tvuserbranch = itemView.findViewById(R.id.tvuserbranch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onItemClicked(User.indexOf(v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public request_userAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myitem_request_row,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull request_userAdapter.ViewHolder viewHolder, int i) {

        viewHolder.itemView.setTag(User.get(i));

        viewHolder.tvusername.setText(User.get(i).getName());
        viewHolder.tvuserbranch.setText(User.get(i).getBranch());
        //viewHolder.tvchar.setText(User.get(i).getName().toUpperCase().charAt(0));

    }

    @Override
    public int getItemCount() {
        return User.size();
    }
}
