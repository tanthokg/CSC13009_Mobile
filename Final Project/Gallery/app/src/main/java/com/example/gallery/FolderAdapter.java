package com.example.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    Context context;
    ArrayList<String> folders;

    public FolderAdapter(Context context, ArrayList<String> folders) {
        this.context = context;
        this.folders = folders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_picture_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the name of folder
        // Just display name folder: Facebook, Messenger,...not path: emulator/0/Pictures/...
        int getPositionStartName = folders.get(position).lastIndexOf("/");
        String nameFolder = folders.get(position).substring(getPositionStartName + 1);
        holder.txtFolderName.setText(nameFolder);

        holder.folderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                // Send to Main Activity the path of the folder selected
                ((MainActivity)context).onMsgFromFragToMain("FOLDER-FLAG", folders.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView folderItem;
        private TextView txtFolderName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderItem = (ImageView) itemView.findViewById(R.id.folderItem);
            txtFolderName = (TextView) itemView.findViewById(R.id.txtFolderPath);
        }
    }
}
