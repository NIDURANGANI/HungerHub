package com.example.hungerhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Load the image using Glide (image URL or resource)
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);

        // Set the title, description, quantity, expiry, and location
        holder.recTitle.setText("Title: " + dataList.get(position).getDataTitle());
        holder.recDesc.setText("Description: " + dataList.get(position).getDataDesc());
        holder.recQty.setText("Serves: " + dataList.get(position).getDataQty());
        holder.recExpiry.setText("Expiry: " + dataList.get(position).getDataExpiry());
        holder.recLocation.setText("Pickup: " + dataList.get(position).getDataLocation());



        // Set click listener for the card view
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the data to the detail activity
                Intent intent = new Intent(context, activity_detail.class);
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Quantity", dataList.get(holder.getAdapterPosition()).getDataQty());
                intent.putExtra("Expiry", dataList.get(holder.getAdapterPosition()).getDataExpiry());
                intent.putExtra("Location", dataList.get(holder.getAdapterPosition()).getDataLocation());
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, recDesc, recQty, recExpiry, recLocation;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize all the views
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recQty = itemView.findViewById(R.id.recPriority);
        recExpiry = itemView.findViewById(R.id.recExpiry);
        recLocation = itemView.findViewById(R.id.recLocation);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}
