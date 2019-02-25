package com.example.robert.gametemplate;

import android.content.Context;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.Random;
import android.util.Log;



public class SpriteOverlay extends View {


    private static final int BITMAP_SIZE = 64;
    private static final int REFRESH_RATE = 40;
    private final Paint mPainter = new Paint();
    private int mScaledBitmapWidth;
    private Bitmap mScaledBitmap;

    // Location, speed and direction of the object
    private float mXPos, mYPos, mDx, mDy, mRadius, mRadiusSquared;
    private long mRotate, mDRotate;



    public SpriteOverlay(Context context, float x, float y) {
        super(context);

        Log.i("TAG", "Creating Bubble at: x:" + x + " y:" + y);

        // Create a new random number generator to
        // randomize size, rotation, speed and direction
        Random r = new Random();

        // Creates the bubble bitmap for this BubbleView
        createScaledBitmap(r);

        // Radius of the Bitmap
        mRadius = mScaledBitmapWidth / 2;
        mRadiusSquared = mRadius * mRadius;

        // Adjust position to center the bubble under user's finger
        mXPos = x - mRadius;
        mYPos = y - mRadius;

        // Set the BubbleView's speed and direction
        setSpeedAndDirection(r);

        // Set the BubbleView's rotation
        setRotation(r);

        mPainter.setAntiAlias(true);

    }

    private void setRotation(Random r) {


            mDRotate = (r.nextInt(3*BITMAP_SIZE)+1)/mScaledBitmapWidth;

    }

    private void setSpeedAndDirection(Random r) {



                // TODO - Set movement direction and speed
                // Limit movement speed in the x and y
                // direction to [-3..3] pixels per movement.

                // REM: Must update position based on the bitmap dimensions!
                mDx = (r.nextInt(mScaledBitmapWidth * 3) + 1)
                        / (float) mScaledBitmapWidth;

                mDy = (r.nextInt(mScaledBitmapWidth * 3) + 1)
                        / (float) mScaledBitmapWidth;


                //Flip direction??? 50/50 chance
                mDx *= r.nextInt(1) == 0 ? 1 : -1;
                mDy *= r.nextInt(1) == 0 ? 1 : -1;



    }

    private void createScaledBitmap(Random r) {



            // TODO - set scaled bitmap size in range [1..3] * BITMAP_SIZE
            mScaledBitmapWidth = r.nextInt(2 * BITMAP_SIZE) + BITMAP_SIZE;



//        // TODO - create the scaled bitmap using size set above
//        mScaledBitmap = Bitmap.createScaledBitmap(mBitmap,
//                mScaledBitmapWidth, mScaledBitmapWidth, false);
    }

    // Start moving the BubbleView & updating the display
    private void start() {

//        // Creates a WorkerThread
//        ScheduledExecutorService executor = Executors
//                .newScheduledThreadPool(1);
//
//        // Execute the run() in Worker Thread every REFRESH_RATE
//        // milliseconds
//        // Save reference to this job in mMoverFuture
//        mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//
//                // TODO - implement movement logic.
//                // Each time this method is run the BubbleView should
//                // move one step. If the BubbleView exits the display,
//                // stop the BubbleView's Worker Thread.
//                // Otherwise, request that the BubbleView be redrawn.
//
//                if(moveWhileOnScreen()) {
//                    postInvalidate(); // Redraw the view
//                } else {
//                    stop(false);
//                }
//
//
//            }
//        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    }

    // Cancel the Bubble's movement
    // Remove Bubble from mFrame
    // Play pop sound if the BubbleView was popped

    private void stop(final boolean wasPopped) {


    }


    // Draw the Bubble at its current location
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        // save the canvas
        canvas.save();

        // increase the rotation of the original image by mDRotate
        mRotate+=mDRotate;

        // Rotate the canvas by current rotation
        // Hint - Rotate around the bubble's center, not its position
        //Make sure to use CENTER!!!!
        canvas.rotate(mRotate,mXPos + mScaledBitmapWidth/2,mYPos+mScaledBitmapWidth/2);

        // Draw the bitmap at it's new location
        canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);
        // Paint object defines style params for operation...

        // restore the canvas
        canvas.restore();

    }


}
