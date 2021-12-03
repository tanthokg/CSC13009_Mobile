package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    int[] filters;
    Context context;

    FilterAdapter(int[] filters, Context context) {
        this.filters = filters;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.filterName.setText(String.valueOf(filters[position]));
        holder.filterThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filterName;
        ImageView filterThumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName = (TextView) itemView.findViewById(R.id.filterName);
            filterThumbnail = (ImageView) itemView.findViewById(R.id.filterThumbnail);
        }
    }
}
