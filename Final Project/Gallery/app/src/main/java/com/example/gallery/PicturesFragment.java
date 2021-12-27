package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

public class PicturesFragment extends Fragment implements FragmentCallbacks{
    private RecyclerView picturesRecView;
    private File[] allFiles;
    private File[] pictureFiles;
    private int spanCount = 4;
    ArrayList<String> paths;

    PicturesAdapter picturesAdapter;
    private ActionMode actionMode;

    Context context;
    String pathFolder;
    String type;
    private FloatingActionButton btnAdd, btnCamera, btnUrl;
    private boolean addIsPressed;
    private Animation menuFABShow, menuFABHide;
    private final int CAMERA_CAPTURED = 100;
    MainActivity main;

    public static PicturesFragment getInstance(Context context, String pathFolder, String type) {
        return new PicturesFragment(context, pathFolder, type);
    }

    PicturesFragment(Context context, String pathFolder, String type) {
        this.context = context;
        this.pathFolder = pathFolder;
        this.type = type;
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

        picturesRecView = picturesFragment.findViewById(R.id.picturesRecView);

        btnAdd = (FloatingActionButton) picturesFragment.findViewById(R.id.btnAdd_PicturesFragment);
        btnCamera = (FloatingActionButton) picturesFragment.findViewById(R.id.btnCamera_PicturesFragment);
        btnUrl = (FloatingActionButton) picturesFragment.findViewById(R.id.btnUrl_PicturesFragment);
        if (type.equals("ALBUM")) btnAdd.setVisibility(View.GONE);

        menuFABShow = AnimationUtils.loadAnimation(context, R.anim.menu_button_show);
        menuFABHide = AnimationUtils.loadAnimation(picturesFragment.getContext(), R.anim.menu_bottom_hide);

        addIsPressed = false;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimationButton(addIsPressed);
                setVisibilityButton(addIsPressed);
                addIsPressed = !addIsPressed;
            }
        });

        picturesRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    btnAdd.show();
                }
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    if (addIsPressed) {
                        setAnimationButton(addIsPressed);
                        setVisibilityButton(addIsPressed);
                        addIsPressed = !addIsPressed;
                    }
                    btnAdd.hide();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragToMain("PICTURES-FLAG", "Open Url Dialog");
            }
        });

        if (type.equals("FOLDER")) {
            readPicturesInFolder();
        }
        if (type.equals("ALBUM")) {
            readPicturesInAlbum();
        }
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
                            (s.toLowerCase().endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg"));
                }
            };
            allFiles = pictureFile.listFiles();
            pictureFiles = pictureFile.listFiles(filter);
            paths = new ArrayList<String>();
                for (File file : pictureFiles)
                    paths.add(file.getAbsolutePath());
                showAllPictures(paths);
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
        showAllPictures(paths);
    }

    void showAllPictures(ArrayList<String> paths) {
        // Send a string path to the adapter. The adapter will create everything from the provided path
        // This implementation is not permanent
        // Update on Nov 29, 2021: send a list of paths to the adapter to utilize this fragment for albums
        picturesAdapter = new PicturesAdapter(context, paths, spanCount);
        picturesRecView.setAdapter(picturesAdapter);
        picturesRecView.setLayoutManager(new GridLayoutManager(context, spanCount));
    }

    void setAnimationButton(boolean isPressed) {
        if (isPressed) {
            btnAdd.setImageResource(R.drawable.ic_round_add_24);
            btnCamera.startAnimation(menuFABHide);
            btnUrl.startAnimation(menuFABHide);
        }
        else {
            btnAdd.setImageResource(R.drawable.ic_round_close_24);
            btnCamera.startAnimation(menuFABShow);
            btnUrl.startAnimation(menuFABShow);
        }
    }

    void setVisibilityButton(boolean isPressed) {
        if (isPressed) {
            btnCamera.setVisibility(FloatingActionButton.INVISIBLE);
            btnUrl.setVisibility(FloatingActionButton.INVISIBLE);
        }
        else {
            btnCamera.setVisibility(FloatingActionButton.VISIBLE);
            btnUrl.setVisibility(FloatingActionButton.VISIBLE);
        }
    }

    void openCamera() {
        try {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityFromFragment(this, takePhotoIntent, CAMERA_CAPTURED);
        }
        catch (Exception e) {
            Log.e("Error to open camera! ", e.getMessage());
        }
    }

    private File getFolderDirectory() {
        File pictureDirectory = new File(pathFolder);
        if (!pictureDirectory.exists())
            pictureDirectory.mkdirs();
        return pictureDirectory;
    }

    void saveImage(Bitmap bitmap) {
        File pictureFile = new File(getFolderDirectory(), bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            Log.e("Error to save image! ", e.getMessage());
        }
        readPicturesInFolder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURED) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveImage(bitmap);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update pictures view when LargeImage activity is finished
        if (type.equals("FOLDER")) {
            readPicturesInFolder();
        }
        if (type.equals("ALBUM")) {
            readPicturesInAlbum();
        }
    }

    @Override
    public void onMsgFromMainToFrag(Bitmap result) {
        saveImage(result);
    }

    // Inflate button to change how many columns of images are displayed
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.picture_top_menu, menu);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
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
        else if (R.id.btnSlideshow == id )
        {
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
        } else {
            String request = "";
            if (type.equals("FOLDER")) request = "Turn back folder";
            if (type.equals("ALBUM")) request = "Turn back album";
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
        // Toggle the selection then check if any items are already selected or not
        picturesAdapter.toggleSelection(position);
        boolean hasCheckedItems = picturesAdapter.getSelectedCount() > 0;
        // there are some selected items, start the actionMode
        if (hasCheckedItems && actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).
                    startSupportActionMode(new ToolbarActionModeCallback(context, picturesAdapter));
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

    // Delete multiple Images in PicturesFragments
    public void deleteMulti() {
        //Get selected ids
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images. If current id is selected, add it to a list
        for (int i = 0; i < selected.size(); ++i)
            if (selected.valueAt(i)) paths.add(pictureFiles[selected.keyAt(i)].getAbsolutePath());

        // Start deleting all image selected
        if (type.equals("FOLDER")) {
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
            String message = AppConfig.getInstance(context).getTrashMode() ? "Are you sure to move these images to Trashed?"
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

        if (type.equals("ALBUM")) {
            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
            confirmDialog.setMessage("Are you sure to remove these image from this album?");
            confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Delete every item in the list we had before
                    for (String path : paths) {
                        AlbumUtility.getInstance(context).deletePictureInAlbum(pathFolder, path);
                    }

                    Toast.makeText(context, "Pictures(s) removed from album", Toast.LENGTH_SHORT).show();
                    actionMode.finish();
                    onResume();
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

    public void callScanIntent(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[] { path }, null,null);
    }

    // Share multiple Images in PicturesFragments
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

    // Select all pictures
    public void selectAll() {
        picturesAdapter.selectAll();
        actionMode.setTitle(paths.size() + " selected");
    }

    // add multiple images to album
    public void addToAlbum() {
        SparseBooleanArray selected = picturesAdapter.getSelectedIds();
        ArrayList<String> paths = new ArrayList<String>();

        // Get paths of selected images
        for (int index = 0; index < selected.size() ; index++) {
            if (selected.valueAt(index)) {
                //If current id is selected remove the item via key
                paths.add(pictureFiles[selected.keyAt(index)].getAbsolutePath());
            }
        }

        View addToAlbumView = LayoutInflater.from(context).inflate(R.layout.choose_album_form, null);
        ListView chooseAlbumListView = addToAlbumView.findViewById(R.id.chooseAlbumListView);

        ArrayList<String> albums = AlbumUtility.getInstance(context).getAllAlbums();
        albums.removeIf(album -> album.equals("Favorite") || album.equals("Trashed"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_multiple_choice, albums);
        chooseAlbumListView.setAdapter(adapter);

        AlertDialog.Builder addToAlbumDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
        addToAlbumDialog.setView(addToAlbumView);
        ArrayList<String> chosen = new ArrayList<String>();

        addToAlbumDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //String picturePath = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                for (int index = 0; index < chooseAlbumListView.getCount(); ++index) {
                    if (chooseAlbumListView.isItemChecked(index))
                        chosen.add(chooseAlbumListView.getItemAtPosition(index).toString());
                }
                for (String s: chosen) {
                    for (String path:paths) {
                        AlbumUtility.getInstance(context).addPictureToAlbum(s, path);
                    }
                }
                actionMode.finish();
                Toast.makeText(context, "Added to selected albums", Toast.LENGTH_SHORT).show();
            }
        });
        addToAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "CANCELED", Toast.LENGTH_SHORT).show();
            }
        });
        addToAlbumDialog.create().show();
    }
}
