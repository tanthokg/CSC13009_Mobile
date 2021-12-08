package LargeImagePackage;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.example.gallery.R;

import java.io.File;
import java.util.HashMap;

import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    static final int MAX_CACHE_SIZE = 16;

    Context context;
    File[] pictureFiles;
    LayoutInflater mLayoutInflater;
    ZoomableImageView imageView;
    static HashMap<String, Bitmap> bitmapCache = new HashMap<String, Bitmap>();

    static private Bitmap getBitmap(String key) {

        //Clear data when the memory is too large
        if (bitmapCache.size() >=  MAX_CACHE_SIZE) {
            bitmapCache.clear();
        }

        //If there isn't the drawable exists => store it
        if (!bitmapCache.containsKey(key))
        {
            bitmapCache.put(key, BitmapFactory.decodeFile(key));
        }

        return bitmapCache.get(key);
    }

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
        // Inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.large_picture_full, container, false);

        ZoomableImageView view = itemView.findViewById(R.id.largePictureFull);

        // setting the image in the imageView
        Glide.with(context).asBitmap()
                .load(getBitmap(pictureFiles[position].getAbsolutePath())).into(view);
        //view.setImageDrawable(getDrawable());
        // LargeImage.currentPosition = position;


        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void setPrimaryItem (ViewGroup container, int position, Object object){
        super.setPrimaryItem(container, position, object);
<<<<<<< HEAD

        imageView = ((View)object).findViewById(R.id.largePictureFull);
        Glide.with(context).asBitmap()
                .load(getBitmap(pictureFiles[position].getAbsolutePath())).into(imageView);
        //imageView.setImageDrawable(getBitmap(pictureFiles[position].getAbsolutePath()));
    }


=======
        imageView = ((View)object).findViewById(R.id.largePictureFull);
        imageView.setImageDrawable(getDrawable(pictureFiles[position].getAbsolutePath()));
    }

>>>>>>> f57d3e689e84281c905902f4733fc80001f4ea5e
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((RelativeLayout) object);
    }

}