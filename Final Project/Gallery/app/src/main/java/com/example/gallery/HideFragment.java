package com.example.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

import Helper.SortHelper;

public class HideFragment extends Fragment {
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

    private Menu _menu;

    private FloatingActionButton btnAdd;
    MainActivity main;

    //default is increase sort type
    private SortHelper.SortType sortType
            = SortHelper.SortType.INCREASE;

    //default is sort by name
    private SortHelper.SortCriteria sortCriteria
            = SortHelper.SortCriteria.NAME;

    public static HideFragment getInstance(Context context) {
        return new HideFragment(context);
    }

    HideFragment(Context context) {
        this.context = context;
        this.pathFolder = "Hide";
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

        // Remove the up-key back arrow and name folder on Action Bar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
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

    void readPicturesInFolder() {
        try {
            File pictureFile = new File(pathFolder);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return !s.toLowerCase(Locale.ROOT).startsWith(".trashed") &&
                            !s.toLowerCase(Locale.ROOT).startsWith(".hide") &&
                            (s.toLowerCase().endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg"));
                }
            };
            allFiles = pictureFile.listFiles();
            pictureFiles = pictureFile.listFiles(filter);
            paths = new ArrayList<String>();
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
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
        SortHelper.sort(pictureFiles, sortCriteria, sortType);
        reupdateFilePaths();
    }

    void showAllPictures(ArrayList<String> paths) {
        // Send a string path to the adapter. The adapter will create everything from the provided path
        // This implementation is not permanent
        // Update on Nov 29, 2021: send a list of paths to the adapter to utilize this fragment for albums
        picturesAdapter = new PicturesAdapter(context, paths, spanCount);
        picturesRecView.setAdapter(picturesAdapter);
        picturesRecView.setLayoutManager(new GridLayoutManager(context, spanCount));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update pictures view when LargeImage activity is finished
        readPicturesInAlbum();
    }

    // Inflate button to change how many columns of images are displayed
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.picture_top_menu, menu);

        _menu = menu;

        //Remove "Empty Trashed" option and "Recover All" option
        //menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);

        //If already have password => we don't have "Set password" option
        //else => we don't have "Change password" and "Clear password" option
        if (main.isHavingPassword()) {
            menu.getItem(4).setVisible(false);
        } else {
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(false);
        }
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
        }
        else if (item.getItemId() == R.id.btnSort) { /*do nothing*/}
        else if (item.getItemId() == R.id.sort_by) {/*do nothing*/}
        else if (item.getItemId() == R.id.sort_type){ /*do nothing*/}
        else if (item.getItemId() == R.id.sort_by_name){
            sortCriteria = SortHelper.SortCriteria.NAME;
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        else if (item.getItemId() == R.id.sort_by_last_modified_date) {
            sortCriteria = SortHelper.SortCriteria.LAST_MODIFIED_DATE;
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        else if (item.getItemId() == R.id.sort_by_size) {
            sortCriteria = SortHelper.SortCriteria.FILE_SIZE;
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        else if (item.getItemId() == R.id.sort_type_increase)  {
            sortType = SortHelper.SortType.INCREASE;
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        else if (item.getItemId() == R.id.sort_type_decrease) {
            sortType = SortHelper.SortType.DECREASE;
            SortHelper.sort(pictureFiles, sortCriteria, sortType);
            reupdateFilePaths();
        }
        else if (item.getItemId() == R.id.setPassword) {
            main.onMsgFromFragToMain(HideCreateFragment.FLAG, HideCreateFragment.OPEN_FORM);
        }
        else if (item.getItemId() == R.id.changePassword) {
            main.onMsgFromFragToMain(HideChangePasswordFragment.FLAG, HideChangePasswordFragment.OPEN_FORM);
        }
        else if (item.getItemId() == R.id.clearPassword) {
            main.onMsgFromFragToMain(HideChangePasswordFragment.FLAG, HideChangePasswordFragment.CLEAR_PASSWORD);

            _menu.getItem(4).setVisible(true);
            _menu.getItem(5).setVisible(false);
            _menu.getItem(6).setVisible(false);
        }
        else if (item.getItemId() == R.id.btnSlideshow) {
            if(0 == paths.size())
            {
                Toast.makeText(context, "Nothing to slide show", Toast.LENGTH_SHORT).show();
            }
            else
            {
                int getPositionStartName = pathFolder.lastIndexOf("/");
                String nameFolder = pathFolder.substring(getPositionStartName + 1);
                Intent intent = new Intent(context, SlideShowActivity.class);
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
                    startSupportActionMode(new HideToolbarCallback(context, picturesAdapter));
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
        intent.putExtra("sortCriteria", sortCriteria);
        intent.putExtra("sortType", sortType);
        // Toast.makeText(context, "Position: " + itemPosition, Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    // Delete multiple Images in HideFragments
    public void deleteMulti() {
        //Get selected ids
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images. If current id is selected, add it to a list
        for (int i = 0; i < selected.size(); ++i)
            if (selected.valueAt(i)) paths.add(pictureFiles[selected.keyAt(i)].getAbsolutePath());

        // Start deleting all image selected
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        String message = AppConfig.getInstance(context).getTrashMode()
                ? "Are you sure to move these images to Trashed?"
                : "Are you sure to delete these images?";
        confirmDialog.setMessage(message);
        confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Delete/Move to trash all items in the list
                if (AppConfig.getInstance(context).getTrashMode()) {
                    for (String path: paths) {
                        AlbumUtility.getInstance(context).addToTrashed(path);
                        callScanIntent(context,path);
                    }
                    Toast.makeText(context,"Picture(s) Moved To Trashed",Toast.LENGTH_SHORT).show();
                } else {
                    for (String path: paths) {
                        File file = new File(path);
                        file.delete();
                        AlbumUtility.getInstance(context).deletePictureInAllAlbums(path);
                        callScanIntent(context,path);
                    }
                    Toast.makeText(context,"Picture(s) Deleted On Device",Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
                onResume();
            }
        });
        confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        confirmDialog.create().show();
    }

    public void unhideMulti() {
        //Get selected ids
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images. If current id is selected, add it to a list
        for (int i = 0; i < selected.size(); ++i)
            if (selected.valueAt(i)) paths.add(pictureFiles[selected.keyAt(i)].getAbsolutePath());

        // Start deleting all image selected
        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        dialog.setMessage("Unhide selected pictures?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (String path: paths) {
                    String oldFilename = path.substring(path.lastIndexOf('/') + 1);
                    String newFilename = oldFilename.replace(".hide", "");
                    File directory = new File(path.substring(0, path.lastIndexOf('/')));
                    File from = new File(directory, oldFilename);
                    File to = new File(directory, newFilename);
                    AlbumUtility.getInstance(context).deletePictureInAlbum("Hide", from.getAbsolutePath());

                    if (!from.renameTo(to))
                        Toast.makeText(context, "Error: Cannot Unhide Picture(s)", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "Picture(s) Unhide", Toast.LENGTH_SHORT).show();
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

    public void callScanIntent(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[] { path }, null,null);
    }

    // Select all pictures
    public void selectAll() {
        picturesAdapter.selectAll();
        actionMode.setTitle(paths.size() + " selected");
    }

    // Share multiple Images in HideFragments
    public void shareMulti() {
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> path = new ArrayList<String>();

        // Get paths of selected images
        for (int index = (selected.size() - 1); index >= 0; index--) {
            if (selected.valueAt(index)) {
                //If current id is selected remove the item via key
                path.add(pictureFiles[selected.keyAt(index)].getAbsolutePath());
            }
        }

        try {
            ArrayList<Uri> imageUris = new ArrayList<Uri>();
            for (int i = 0; i < path.size(); i++) {
                File file = new File(path.get(i));
                Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider", file);
                imageUris.add(photoURI);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent, "Share images via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionMode.finish();
    }

    private void reupdateFilePaths() {
        paths.clear();
        for (File file : pictureFiles)
        {
            paths.add(file.getAbsolutePath());
        }
        showAllPictures(paths);
    }

    //Could delete all pictures in hide without opening inflate Hide fragment into screen
    public void deleteAll_forPublic() {

        AlbumData data = AlbumUtility.getInstance(context).findDataByAlbumName(pathFolder);
        ArrayList<String> _paths;
        if (null != data) {
            _paths = data.getPicturePaths();
            File[] _pictureFiles = new File[_paths.size()];
            int i = 0;
            for (String path : _paths) {
                _pictureFiles[i] = new File(path);
                _pictureFiles[i].delete();
                AlbumUtility.getInstance(context).deletePictureInAllAlbums(path);
                callScanIntent(context,path);
                i++;
            }
        } else {
            _paths = new ArrayList<String>();
        }

    }

    public void clearPassword() {
        SharedPreferences mPref = context.
                getSharedPreferences(HideLoginFragment.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(HideLoginFragment.PREF_PASS_NAME, null);
        editor.commit();
    }


    public void resetPassword() {
        //Delete all pictures in hide fragment
        deleteAll_forPublic();

        //Delete password
        clearPassword();
    }

}
