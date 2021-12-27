package com.example.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class TrashedFragment extends Fragment {
    private RecyclerView picturesRecView;
    private File[] allFiles;
    private File[] pictureFiles;
    private int spanCount = 4;
    ArrayList<String> paths;

    PicturesAdapter picturesAdapter;
    private ActionMode actionMode;

    private final Context context;
    private final String pathFolder;
    private final String type;

    private FloatingActionButton btnAdd;
    MainActivity main;

    public static TrashedFragment getInstance(Context context) {
        return new TrashedFragment(context);
    }

    TrashedFragment(Context context) {
        this.context = context;
        this.pathFolder = "Trashed";
        this.type = "ALBUM";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        // Show the up-key back arrow and name folder on Action Bar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        int getPositionStartName = pathFolder.lastIndexOf("/");
        String nameFolder = pathFolder.substring(getPositionStartName + 1);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(nameFolder);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View picturesFragment = inflater.inflate(R.layout.pictures_fragment, container, false);

        btnAdd = (FloatingActionButton) picturesFragment.findViewById(R.id.btnAdd_PicturesFragment);
        if (type.equals("ALBUM")) btnAdd.setVisibility(View.GONE);
        picturesRecView = picturesFragment.findViewById(R.id.picturesRecView);

        readPicturesInAlbum();
        implementClickListener();
        return picturesFragment;
    }

    void readPicturesInAlbum() {
        AlbumData data = AlbumUtility.getInstance(context).findDataByAlbumName(pathFolder);
        if (null != data) {
            paths = data.getPicturePaths();
            pictureFiles = new File[paths.size()];
            int i = 0;
            for (String path : paths) {
                pictureFiles[i] = new File(path);
                i++;
            }
        } else {
            paths = new ArrayList<String>();
        }
        showAllPictures(paths);
    }

    void showAllPictures(ArrayList<String> paths) {
        picturesAdapter = new PicturesAdapter(context, paths, spanCount);
        picturesRecView.setAdapter(picturesAdapter);
        picturesRecView.setLayoutManager(new GridLayoutManager(context, spanCount));
    }

    @Override
    public void onResume() {
        super.onResume();
        readPicturesInAlbum();
    }

    // Inflate button to change how many columns of images are displayed
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.picture_top_menu, menu);

        if (!pathFolder.equals("Trashed")) {
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }
        if (pathFolder.equals("Trashed"))
            menu.getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (R.id.btnChangeFormatDisplay == id) {
            if (4 == spanCount) {
                item.setIcon(R.drawable.ic_sharp_grid_view_24);
                spanCount = 3;
            }
            else if (3 == spanCount) {
                item.setIcon(R.drawable.ic_sharp_view_list_24);
                spanCount = 2;
            }
            else if (2 == spanCount) {
                item.setIcon(R.drawable.ic_sharp_view_comfy_24);
                spanCount = 1;
            }
            else {
                item.setIcon(R.drawable.ic_sharp_view_module_24);
                spanCount = 4;
            }
            showAllPictures(paths);
        } else if (R.id.emptyTrashed == id) {
            deleteAllInTrashed();
        } else if (R.id.recoverAll == id) {
            recoverAllInTrashed();
        } else if (R.id.btnSlideshow == id ) {
            if(paths.size() == 0)
            {
                Toast.makeText(context, "Nothing to slide show", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int getPositionStartName = pathFolder.lastIndexOf("/");
                String nameFolder = pathFolder.substring(getPositionStartName + 1);
                Intent intent = new Intent(context, SlideShow.class);
                intent.putExtra("Path to Image Files", paths);
                intent.putExtra("Name Folder", nameFolder);
                context.startActivity(intent);
            }
        }
        else {
            String request = "Turn back album";
            main.onMsgFromFragToMain("PICTURES-FLAG", request);
        }
        return true;
    }

    private void implementClickListener() {
        picturesRecView.addOnItemTouchListener(new RecyclerTouchListener(context, picturesRecView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (actionMode != null)
                    onListItemSelect(position);
                else
                    showLargePicture(pathFolder, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                main.bottomNavigationView.setVisibility(View.GONE);
                onListItemSelect(position);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        //Toggle the selection
        picturesAdapter.toggleSelection(position);
        //Check if any items are already selected or not
        boolean hasCheckedItems = picturesAdapter.getSelectedCount() > 0;
        // there are some selected items, start the actionMode
        if (hasCheckedItems && actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).
                    startSupportActionMode(new TrashToolbarCallback(context, picturesAdapter));
        } else if (!hasCheckedItems && actionMode != null) {
            // there no selected items, finish the actionMode
            actionMode.finish();
        }

        if (actionMode != null)
            //set action mode title on item selection
            actionMode.setTitle(picturesAdapter.getSelectedCount() + " selected");
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }

    private void showLargePicture(String pathToPicturesFolder, int itemPosition) {
        Intent intent = new Intent(context, LargeImage.class);
        // Send the folder path and the current position to the destination activity
        intent.putExtra("pathToPicturesFolder", pathToPicturesFolder);
        intent.putExtra("itemPosition", itemPosition);
        intent.putExtra("itemType", type);
        // Toast.makeText(context, "Position: " + itemPosition, Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    // Delete multiple images in TrashedFragment
    public void deleteMulti() {
        //Get selected ids
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images. If current id is selected, add it to a list
        for (int i = 0; i < selected.size(); ++i)
            if (selected.valueAt(i)) paths.add(pictureFiles[selected.keyAt(i)].getAbsolutePath());

        // Start deleting all image selected
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        dialog.setMessage("Delete selected pictures on device?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete On Device
                for (String path : paths) {
                    File file = new File(path);
                    if (!file.delete())
                        Log.e("Delete files in trashed: ", "Cannot Delete");
                    callScanIntent(context, path);
                }
                // Delete In Trashed
                AlbumUtility.getInstance(context).deleteAllPicturesInAlbum("Trashed");
                onResume();
                Toast.makeText(context, "Picture(s) Deleted On Device", Toast.LENGTH_SHORT).show();
                actionMode.finish();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.create().show();
    }

    public void recoverMulti() {
        //Get selected ids
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images. If current id is selected, add it to a list
        for (int i = 0; i < selected.size(); ++i)
            if (selected.valueAt(i)) paths.add(pictureFiles[selected.keyAt(i)].getAbsolutePath());

        // Start deleting all image selected
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        dialog.setMessage("Recover selected pictures?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (String path: paths) {
                    String oldFilename = path.substring(path.lastIndexOf('/') + 1);
                    String newFilename = oldFilename.replace(".trashed", "");
                    File directory = new File(path.substring(0, path.lastIndexOf('/')));
                    File from = new File(directory, oldFilename);
                    File to = new File(directory, newFilename);
                    AlbumUtility.getInstance(context).deletePictureInAlbum("Trashed", from.getAbsolutePath());

                    if (!from.renameTo(to))
                        Toast.makeText(context, "Error: Cannot Recover Picture(s)", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "Picture(s) Recovered", Toast.LENGTH_SHORT).show();
                actionMode.finish();
                onResume();
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.create().show();
    }

    public void deleteAllInTrashed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        dialog.setMessage("Empty Trash?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete On Device
                ArrayList<String> paths = AlbumUtility.getInstance(context).findDataByAlbumName("Trashed").getPicturePaths();
                for (String path : paths) {
                    File file = new File(path);
                    if (!file.delete())
                        Log.e("Delete files in trashed: ", "Cannot Delete");
                    callScanIntent(context, path);
                }
                // Delete In Trashed
                AlbumUtility.getInstance(context).deleteAllPicturesInAlbum("Trashed");
                onResume();
                Toast.makeText(context, "Emptied Trashed", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.create().show();
    }

    public void recoverAllInTrashed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        dialog.setMessage("Recover all pictures from Trashed?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<String> paths = AlbumUtility.getInstance(context).findDataByAlbumName("Trashed").getPicturePaths();
                for (String path: paths) {
                    String oldFilename = path.substring(path.lastIndexOf('/') + 1);
                    String newFilename = oldFilename.replace(".trashed", "");
                    File directory = new File(path.substring(0, path.lastIndexOf('/')));
                    File from = new File(directory, oldFilename);
                    File to = new File(directory, newFilename);
                    AlbumUtility.getInstance(context).deletePictureInAlbum("Trashed", from.getAbsolutePath());

                    if (!from.renameTo(to))
                        Toast.makeText(context, "Error: Cannot Recover Picture(s)", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "All Pictures Has Been Recovered", Toast.LENGTH_SHORT).show();
                onResume();
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.create().show();
    }

    public void callScanIntent(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[] { path }, null,null);
    }

    // Select all pictures
    public void selectAll() {
        picturesAdapter.selectAll();
        actionMode.setTitle(paths.size() + " selected");
    }
}
