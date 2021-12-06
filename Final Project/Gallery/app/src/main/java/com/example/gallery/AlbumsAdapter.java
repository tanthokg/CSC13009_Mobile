package com.example.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    protected ArrayList<String> albums;
    private Context context;

    public AlbumsAdapter(Context context, ArrayList<String> albums) {
        this.context = context;
        this.albums = albums;
    }

    public void setAlbums(ArrayList<String> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public ArrayList<String> getAlbums() {
        return albums;
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
        String currentAlbum = albums.get(position);
        int albumItemCount = AlbumUtility.getInstance(context).findDataByAlbumName(currentAlbum).getPicturePaths().size();
        holder.albumName.setText(currentAlbum);
        holder.albumItemCount.setText(String.format(Locale.ROOT, "%d item(s)",albumItemCount));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumThumbnail;
        TextView albumName, albumItemCount;
        ImageView albumItemMenu;
        MaterialCardView albumItemCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumThumbnail = itemView.findViewById(R.id.albumThumbnail);
            albumName = itemView.findViewById(R.id.albumName);
            albumItemCount = itemView.findViewById(R.id.albumItemCount);
            albumItemMenu = itemView.findViewById(R.id.albumItemMenu);
            albumItemCard = itemView.findViewById(R.id.albumItemCard);

            albumItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlbumPopupMenu(albumItemMenu);
                }
            });
            albumItemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)itemView.getContext()).onMsgFromFragToMain("ALBUM-FLAG",
                            albums.get(getAdapterPosition()));
                }
            });
        }
        
        private void showAlbumPopupMenu(View itemView) {
            String currentAlbum = albums.get(getAdapterPosition());
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView);
            popupMenu.inflate(R.menu.album_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int itemId = item.getItemId();
                    if (R.id.editAlbum == itemId) {
                        handleEditAlbumItem(itemView);
                    } else if (R.id.deleteAlbum == itemId) {
                        handleDeleteAlbumItem(itemView);
                    }
                    return true;
                }
            });
            popupMenu.show();

        }

        private void handleEditAlbumItem(View itemView) {
            int position = getAdapterPosition();
            String oldAlbumName = albums.get(position);
            Context context = itemView.getContext();

            View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.add_album_form, null);
            TextView txtFormTitle = view.findViewById(R.id.txtFormTitle);
            EditText editText = view.findViewById(R.id.edtAlbumName);

            AlertDialog.Builder editDialog = new AlertDialog.Builder(itemView.getContext(), R.style.AlertDialog);
            editDialog.setView(view);
            txtFormTitle.setText("Edit Album Name");
            editText.setText(oldAlbumName);

            editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newAlbumName = editText.getText().toString();
                    albums.set(position, newAlbumName);
                    // Apply changes to database
                    AlbumUtility.getInstance(context).setAllAlbums(albums);
                    AlbumUtility.getInstance(context).editAlbumName(oldAlbumName, newAlbumName);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Renamed to " + newAlbumName, Toast.LENGTH_SHORT).show();
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
        }

        private void handleDeleteAlbumItem(View itemView) {
            int position = getAdapterPosition();
            Context context = itemView.getContext();

            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(itemView.getContext(), R.style.AlertDialog);
            confirmDialog.setMessage("Are you sure to delete this album?");

            confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (AlbumUtility.getInstance(context).deleteAlbum(albums.get(position))) {
                        albums.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Album " + position + " Deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete this album!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            confirmDialog.create();
            confirmDialog.show();
        }
    }
}
