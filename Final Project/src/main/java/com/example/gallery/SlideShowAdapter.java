package com.example.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.SliderViewHolder>{

    private ViewPager2 viewPager2;
    private ArrayList<String> paths;
    Context context;

    SlideShowAdapter(Context context, ViewPager2 viewPager2, ArrayList<String> paths) {
        this.context = context;
        this.viewPager2 = viewPager2;
        this.paths = paths;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slideshow_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        String picturePath = paths.get(position);
        Glide.with(context).asBitmap().load(picturePath).into(holder.imageView);

        if(position == paths.size() - 2)
        {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(String path)
        {
            Glide.with(context).asBitmap().load(path).into(imageView);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            paths.addAll(paths);
            notifyDataSetChanged();
        }
    };
}
