package LargeImagePackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;

import androidx.viewpager.widget.ViewPager;

public class ZoomableViewPager extends ViewPager {

    public ZoomableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        ZoomableImageView view = ((ViewPagerAdapter) getAdapter()).getImageView();
        if (null != view){
            return (!view.isZooming() && super.onInterceptTouchEvent(event));
        }

        return  super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ZoomableImageView view = ((ViewPagerAdapter) getAdapter()).getImageView();
        if (null != view){
            return (!view.isZooming() && super.onTouchEvent(event));
        }

        return  super.onTouchEvent(event);
    }

}
