package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LocationViewAdapter extends RecyclerView.Adapter<LocationViewAdapter.ViewHolder> {
    private ArrayList<Location> locations = new ArrayList<>();
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtLocationName;
        private final TextView txtCountry;
        private final TextView txtVisitor;
        private final ImageView locationImage;
        private final CardView itemParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocationName = itemView.findViewById(R.id.txtLocationName);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtVisitor = itemView.findViewById(R.id.txtVisitor);
            locationImage = itemView.findViewById(R.id.locationImage);
            itemParent = itemView.findViewById(R.id.itemParent);
        }
    }

    public LocationViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtLocationName.setText(locations.get(position).getName());
        holder.txtCountry.setText(locations.get(position).getCountry());
        holder.txtVisitor.setText(locations.get(position).getVisitors());
        Glide.with(context)
                .asBitmap()
                .load(locations.get(position).getImageUrl())
                .into(holder.locationImage);

        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, locations.get(holder.getAdapterPosition()).getName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }
}
