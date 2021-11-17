package com.example.gallery;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    protected ArrayList<String> albums;

    public AlbumsAdapter(ArrayList<String> albums) {
        this.albums = albums;
    }

    public void setAlbums(ArrayList<String> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public void addAlbum(String newAlbum) {
        this.albums.add(newAlbum);
        notifyItemInserted(albums.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.albumName.setText(albums.get(position));
        holder.albumItemCount.setText(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumThumbnail;
        TextView albumName, albumItemCount;
        ImageView albumItemMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumThumbnail = itemView.findViewById(R.id.albumThumbnail);
            albumName = itemView.findViewById(R.id.albumName);
            albumItemCount = itemView.findViewById(R.id.albumItemCount);
            albumItemMenu = itemView.findViewById(R.id.albumItemMenu);

            albumItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu(albumItemMenu);
                }
            });
        }
        private void popupMenu(View itemView) {
            String currentAlbum = albums.get(getAdapterPosition());
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView);
            popupMenu.inflate(R.menu.album_item_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.editAlbum:
                            View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.add_album_form, null);
                            TextView txtFormTitle = view.findViewById(R.id.txtFormTitle);
                            EditText editText = view.findViewById(R.id.edtAlbumName);

                            AlertDialog.Builder editDialog = new AlertDialog.Builder(itemView.getContext());
                            editDialog.setView(view);
                            txtFormTitle.setText("Edit Album Name");

                            editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    albums.set(getAdapterPosition(), editText.getText().toString());
                                    notifyItemChanged(getAdapterPosition());
                                    Toast.makeText(itemView.getContext(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            editDialog.create();
                            editDialog.show();

                            break;
                        case R.id.deleteAlbum:
                            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(itemView.getContext());
                            confirmDialog.setMessage("Are you sure to delete this album?");

                            albums.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(itemView.getContext(), "Album " + getAdapterPosition() + " Deleted", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }
}
