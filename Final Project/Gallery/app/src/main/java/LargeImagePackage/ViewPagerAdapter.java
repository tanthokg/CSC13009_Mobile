package LargeImagePackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.gallery.R;

import java.io.File;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    //Intent intent;
    Context context;
    File[] pictureFiles;
    LayoutInflater mLayoutInflater;
    ZoomableImageView imageView;

    public ZoomableImageView getImageView() {return imageView; }

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, File[] pictureFiles) {
        this.context = context;
        this.pictureFiles = pictureFiles;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // return the number of images
        return pictureFiles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.gallery_large_item, container, false);

        // referencing the image view from the item.xml file
        imageView = itemView.findViewById(R.id.largeGalleryItem);

        // setting the image in the imageView
        //Drawable draw = Drawable.createFromPath(pictureFiles[position].getAbsolutePath());
        imageView.setImageDrawable(Drawable.createFromPath(pictureFiles[position].getAbsolutePath()));

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }

}