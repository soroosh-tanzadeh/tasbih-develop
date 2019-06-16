package ir.maxivity.tasbih.tools;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public abstract class CustomGestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {

    private static final String TAG = CustomGestureDetector.class.getSimpleName();

    private View view;

    private boolean selectionStart;

    public CustomGestureDetector(View view) {
        this.view = view;
    }

    //FOR GESTURE
    @Override
    public boolean onFling(MotionEvent motionEventOne, MotionEvent motionEventTwo, float velocityX, float velocityY) {
        if (motionEventOne == null || motionEventTwo == null) {
            return false;
        } else if (motionEventOne.getPointerCount() > 1 || motionEventTwo.getPointerCount() > 1) {
            return false;
        } else {
            if (isSelectionStart()) {

                Log.d(TAG, "ME 1 : X - " + motionEventOne.getX());
                Log.d(TAG, "ME 1 : Y - " + motionEventOne.getY());
                Log.d(TAG, "ME 2 : X - " + motionEventTwo.getX());
                Log.d(TAG, "ME 2 : Y - " + motionEventTwo.getY());
                Log.d(TAG, "Velocity Of X - " + velocityX);
                Log.d(TAG, "Velocity Of Y - " + velocityY);

            } else {

                try {
                    ///////////////////////////////////////////////////////////////////////////////

                    Log.d(TAG, "ME 1 : X - " + motionEventOne.getX());
                    Log.d(TAG, "ME 1 : Y - " + motionEventOne.getY());
                    Log.d(TAG, "ME 2 : X - " + motionEventTwo.getX());
                    Log.d(TAG, "ME 2 : Y - " + motionEventTwo.getY());
                    Log.d(TAG, "Velocity Of X - " + velocityX);
                    Log.d(TAG, "Velocity Of Y - " + velocityY);

                    float mRightToLeftCover = motionEventOne.getX() - motionEventTwo.getX();

                    float mTopToBottomCover = motionEventTwo.getY() - motionEventOne.getY();

                    float mVelocityX = velocityX;

                    float mVelocityY = velocityY;

                    Log.i(TAG, "mRightToLeftCover : " + mRightToLeftCover);

                    Log.i(TAG, "mTopToBottomCover : " + mTopToBottomCover);

                    Log.i(TAG, "mVelocityX : " + mVelocityX);

                    Log.i(TAG, "mVelocityY : " + mVelocityY);

                    if (mRightToLeftCover >= 0) {
                        if (mTopToBottomCover >= 0) {
                            if (mTopToBottomCover < 100) {
                                if (mRightToLeftCover > 100) {
                                    Log.d(TAG, "1. R =>> L");
                                    onRightToLeftSwap();
                                }
                            } else {
                                if (mRightToLeftCover < 100) {
                                    Log.d(TAG, "9. T ==>> B");
                                    onTopToBottomSwap();
                                } else {
                                    Log.d(TAG, "2. T ==>> B, R =>> L");
                                }
                            }
                        } else {
                            if (mTopToBottomCover > -100) {
                                if (mRightToLeftCover > 100) {
                                    Log.d(TAG, "3. R =>> L");
                                    onRightToLeftSwap();
                                }
                            } else {
                                if (mRightToLeftCover < 100) {
                                    Log.d(TAG, "10. B ==>> T");
                                    onBottomToTopSwap();
                                } else {
                                    Log.d(TAG, "4. B ==>> T, R =>> L");
                                }
                            }
                        }
                    } else if (mRightToLeftCover < 0) {
                        if (mTopToBottomCover >= 0) {
                            if (mTopToBottomCover < 100) {
                                if (mRightToLeftCover > -100) {
                                    Log.d(TAG, "5. L =>> R");
                                    onLeftToRightSwap();
                                }
                            } else {
                                if (mRightToLeftCover > -100) {
                                    Log.d(TAG, "11. T ==>> B");
                                    onTopToBottomSwap();
                                } else {
                                    Log.d(TAG, "6. T ==>> B, L =>> R");
                                }
                            }
                        } else {
                            if (mTopToBottomCover > -100) {
                                if (mRightToLeftCover < -100) {
                                    Log.d(TAG, "7. L =>> R");
                                    onLeftToRightSwap();
                                }
                            } else {
                                if (mRightToLeftCover < -100) {
                                    Log.d(TAG, "12. B ==>> T");
                                    onBottomToTopSwap();
                                } else {
                                    Log.d(TAG, "8. B ==>> T, L =>> R");
                                }
                            }
                        }
                    }

                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //////////////////////////////////////////////////////////////////////////////


            return false;
        }
    }

    //EXPERIMENTAL PURPOSE
    public abstract void onLeftToRightSwap();

    public abstract void onRightToLeftSwap();

    public abstract void onTopToBottomSwap();

    public abstract void onBottomToTopSwap();

    public abstract void onLeftToRightTopToBottomDiagonalSwap();

    public abstract void onLeftToRightBottomToTopDiagonalSwap();

    public abstract void onRightToLeftTopToBottomDiagonalSwap();

    public abstract void onRightToLeftBottomToTopDiagonalSwap();

    //SINGLE AND DOUBLE TABS
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "On Single Tap");
        Log.d(TAG, "Selection Start : " + selectionStart);
        Log.d(TAG, "ME 1 : X - " + e.getX());
        Log.d(TAG, "ME 1 : Y - " + e.getY());
        onSingleTap();
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "On Double Tap");
        onDoubleTap();
        return super.onDoubleTap(e);
    }

    public abstract void onSingleTap();

    public abstract void onDoubleTap();

    public boolean isSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(boolean selectionStart) {
        this.selectionStart = selectionStart;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        onLongPressPerformed(e);
        super.onLongPress(e);
    }

    public abstract void onLongPressPerformed(MotionEvent e);
}