package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    String[] filters;
    Context context;
    Bitmap original;
    EditImageView editImageView;

    public FilterAdapter(String[] filters, Context context, Bitmap original, EditImageView editImageView) {
        this.filters = filters;
        this.context = context;
        this.original = original;
        this.editImageView = editImageView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterUtility filterUtility = new FilterUtility();
        String name = filters[position];
        holder.filterName.setText(name);
        Glide.with(context).asBitmap().load(filterUtility.setFilter(original, name)).into(holder.filterThumbnail);

        holder.filterThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.setColorMatrix(filterUtility.getFilter(name));
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
