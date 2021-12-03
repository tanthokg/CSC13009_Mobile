package com.example.homework09;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    ArrayList<Newspaper> newspaperArrayList;
    Context context;
    private OnItemClickListener listener;

    public DataAdapter(Context context,ArrayList<Newspaper> newspaperArrayList) {
        this.newspaperArrayList = newspaperArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder Holder,int i)
    {
        ViewHolder viewHolder = (ViewHolder) Holder;
        TextView textView = viewHolder.textview;
        textView.setText(newspaperArrayList.get(i).getNewspaperName());
        ImageView imgView=viewHolder.imageview;
        imgView.setImageResource(newspaperArrayList.get(i).getImageid());
        final int index = viewHolder.getAdapterPosition();
        viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           showDataItem(newspaperArrayList.get(index));}
        });
    }

    @Override
    public int getItemCount() {
        return newspaperArrayList.size();
    }

    private void showDataItem(Newspaper newspaperItem) {
        Intent intent = new Intent(context,ShowCategories.class);
            intent.putExtra("newspaper", newspaperItem.getNewspaperName());
       ((MainActivity)context).startActivity(intent);
    }

    public interface OnItemClickListener {
        void onItemClick(Newspaper newspaper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;
        public ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.txtTitle);
            imageview = (ImageView) itemView.findViewById(R.id.logoImgView);
        }
    }
}