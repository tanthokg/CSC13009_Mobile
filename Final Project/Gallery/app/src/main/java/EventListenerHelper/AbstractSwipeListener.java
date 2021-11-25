package EventListenerHelper;


import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*
*   Using: implement swipe next/prev for Pictures in Pictures Fragments by overriding
*       onSwipeLeft()
*       onSwipeRight()
* */
public class AbstractSwipeListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    protected Activity context;

    //Constructor
    public AbstractSwipeListener (Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        // Swipe distances
        private final int SWIPE_MIN_DISTANCE = 100;
        private final int SWIPE_MAX_DISTANCE = 500;
        private final int SWIPE_MIN_VELOCITY = 100;

        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {

                float dx = Math.abs(e2.getX() - e1.getX());
                float dy = Math.abs(e2.getY() - e1.getY());

                if (dx > SWIPE_MAX_DISTANCE || dy > SWIPE_MAX_DISTANCE) { return false;}

                velocityX = Math.abs(velocityX);
                velocityY = Math.abs(velocityY);

                //Swipe horizontally
                if (velocityX > SWIPE_MIN_VELOCITY && dx > SWIPE_MIN_DISTANCE) {

                    if (e1.getX() > e2.getX())
                    {
                        onSwipeRight();
                    }
                    else
                        {
                            onSwipeLeft();
                        }

                    return true;
                }

                //Swipe vertically
                if (velocityY > SWIPE_MIN_VELOCITY && dy > SWIPE_MIN_DISTANCE) {

                    if (e1.getY() > e2.getY())
                    {
                        onSwipeUp();
                    } else
                        {
                            onSwipeDown();
                        }

                    return true;
                }

            } catch (Exception exception)
            {
                exception.printStackTrace();
            } finally
            {
                return false;
            }
        }
    }

    //For overriding
    public void onSwipeRight() {/*do nothing*/}
    public void onSwipeLeft() {/*do nothing*/}
    public void onSwipeUp() {/*do nothing*/}
    public void onSwipeDown() {/*do nothing*/}

}
