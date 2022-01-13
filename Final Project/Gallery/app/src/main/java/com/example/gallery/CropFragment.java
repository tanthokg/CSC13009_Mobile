package com.example.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CropFragment extends Fragment {

    Context context;
    EditImageView editImageView;
    static CropFragment cropFragment;
    RecyclerView cropRecView;
    ImageButton btnCheckCrop;

    public static CropFragment getInstance(Context context, EditImageView editImageView) {
        if (cropFragment != null)
            return cropFragment;
        return new CropFragment(context, editImageView);
    }

    public CropFragment(Context context, EditImageView editImageView) {
        this.context = context;
        this.editImageView = editImageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_crop_fragment, container, false);

        cropRecView = (RecyclerView) view.findViewById(R.id.customCropRecView);

        btnCheckCrop = (ImageButton) view.findViewById(R.id.btnCheckCrop);
        editImageView.enableCrop();


        btnCheckCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditImageActivity) context).onMsgFromFragToEdit("CROP", "CHECK", null);
            }
        });

        return view;
    }
}