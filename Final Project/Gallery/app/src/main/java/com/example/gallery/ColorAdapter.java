package com.example.gallery;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    String[] colors;
    Context context;
    EditImageView editImageView;

    public ColorAdapter(String[] colors, Context context, EditImageView editImageView) {
        this.colors = colors;
        this.context = context;
        this.editImageView = editImageView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnColor.setBackgroundColor(Color.parseColor(colors[position]));
        holder.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.setBrushColor(colors[holder.getAdapterPosition()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnColor = (Button) itemView.findViewById(R.id.btnColor);
        }
    }
}
