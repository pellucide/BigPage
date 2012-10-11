package com.example.android;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TextView;


public class DragableSpace extends ViewGroup {
    private static final String LOG_TAG = "DragableSpace";
    private static final int OVERSHOOT_AMOUNT = 16;
    private static final int MARGIN = 8;
    
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
 
    Bitmap mBackground;
    private int mScrollX = 0;
    private int mScrollY = 0;
    private int mCurrentRow = 0;
    private int mCurrentColumn = 0;
 
    Bitmap [] bms;
    private float mLastMotionX;
    private float mLastMotionY;
 
    private static final int SNAP_VELOCITY = 300;
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
 
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop = 16;
    private boolean mSnapEnabled = true;
    
    private static int mNumColumns = 1;
    private static int mNumRows = 1;
    
    public void disableSnap() { mSnapEnabled = false; }
    public void enableSnap() { mSnapEnabled = true; }
    public boolean isSnapEnabled() { return (mSnapEnabled); }
    public TableLayout tbl;
    public DragableSpace(Context context) {
        super(context);
 
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
 
        /*this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
                    
        initializeViews(context);
    }
 
    public DragableSpace(Context context, int numColumns, int numRows) {
        super(context);
        mNumColumns = numColumns;
        mNumRows = numRows;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
 
        /* this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
                    
        initializeViews(context);
    }
 
    public DragableSpace(Context context, AttributeSet attrs) {
        super(context, attrs);
 
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
 
        /* this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT ,
                    ViewGroup.LayoutParams.FILL_PARENT));
                    
 
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DragableSpace);
        mNumColumns = a.getInteger(R.styleable.DragableSpace_numColumns, 1);
        mNumRows = a.getInteger(R.styleable.DragableSpace_numRows, 1);
        int mCurrentScreen = a.getInteger(R.styleable.DragableSpace_default_screen, 0);
        mCurrentColumn = mCurrentScreen % mNumColumns;
        mCurrentRow = mCurrentScreen / mNumColumns;
		//mBackground = BitmapFactory.decodeResource(context.getResources(),
			//a.getInteger(R.styleable.DragableSpace_background, 0xff999999));
        initializeViews(context);
    }
 
    private void initializeViews(Context context) {
        View v;
        mScroller = new Scroller(context);
		for(int ii=0; ii<mNumRows; ii++) {
			for (int jj = 0; jj < mNumColumns; jj++) {
				v = new View(context);
				addView(v,ii*mNumColumns + jj);
				
				/*
				 * addView(v,new ViewGroup.LayoutParams(
				 * ViewGroup.LayoutParams.MATCH_PARENT,
				 * ViewGroup.LayoutParams.WRAP_CONTENT));
				 */
			}
		}
		bms = new Bitmap[9];
		changeBackground(R.drawable.green_drops1);
    }
    
    public void changeBackground(int resource) {
		mBackground = BitmapFactory.decodeResource(getContext().getResources(), resource);
		for(int ii=0; ii<mNumRows; ii++) {
			for (int jj = 0; jj < mNumColumns; jj++) {
				Drawable bgSlate = new BitmapDrawable( getRoundedCornerBitmap1(mBackground, ii, jj, 30.0f));
				this.getChildAt(ii*mNumColumns + jj).setBackgroundDrawable(bgSlate);
			}
		}
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w,h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
     
        // We have to make sure our rounded corners have an alpha channel in most cases
        // color is arbitrary. This could be any color that was fully opaque (alpha = 255)
        final int color = 0xff222222;
        
        // We're just reusing xferPaint to paint a normal looking rounded box,
        // the roundPx is the amount we're rounding by.
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
     
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
     
        // We're going to apply this paint using a porter-duff xfer mode.
        // This will allow us to only overwrite certain pixels.
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
     
        return output;
      }
    
    public Bitmap getRoundedCornerBitmap1(Bitmap bitmap, int row, int column, float radius) {
        float[] radii = {0.0f,0.0f, 0.0f,0.0f, 0.0f,0.0f, 0.0f,0.0f};
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int bmsIndex=0;
		
		{
    	    bmsIndex = 4;
		  	if (column == 0){
		   		bmsIndex = 3;
		  	}
    	    if (column==mNumColumns-1){
		   		bmsIndex = 5;
    	    }
		}
		if (row == 0) {
    	    bmsIndex = 1;
		   	if (column == 0){
		  		bmsIndex = 0;
    		    radii[0] = radii[1]= radius;
		   	}
    	    if (column==mNumColumns-1){
		  		bmsIndex = 2;
    		    radii[2] = radii[3]= radius;
    	    }
		}
		if (row == mNumRows-1) {
    	    bmsIndex = 7;
		  	if (column == 0){
		   		bmsIndex = 6;
    		    radii[6] = radii[7]= radius;
		  	}
    	    if (column==mNumColumns-1){
		   		bmsIndex = 8;
    		    radii[4] = radii[5]= radius;
    	    }
		}
	
			
		if (bms[bmsIndex] != null) 
			return bms[bmsIndex];
			
        bms[bmsIndex] = Bitmap.createBitmap(w,h, Config.ARGB_8888);
        Bitmap output = bms[bmsIndex];
        //Bitmap output = Bitmap.createBitmap(w,h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
     
        // We have to make sure our rounded corners have an alpha channel in most cases
        // color is arbitrary. This could be any color that was fully opaque (alpha = 255)
        int color = 0xff429242;
        
        // We're just reusing xferPaint to paint a normal looking rounded box,
        // the roundPx is the amount we're rounding by.
        final Paint paint = new Paint();
        int x1=0,y1=0,x2=w,y2=h;
        
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        
      	/*
    	if (row==0) {
    		if (column==0) 
    		    radii[0] = radii[1]= radius;
    	    if (column==mNumColumns-1)
    		    radii[2] = radii[3]= radius;
    	}
    	
    	
    	if (row==mNumRows-1) {
    		if (column==0)
    		    radii[6] = radii[7]= radius;
    	    if (column==mNumColumns-1)
    		    radii[4] = radii[5]= radius;
        }
        */
    	
    	if (row==0)
    		y1=MARGIN;
    	if (column==mNumColumns-1)
    		x2=w-MARGIN;
    	if (column==0)
    		x1=MARGIN;
    	if (row==mNumRows-1) 
    		y2=h-MARGIN;
    	
        Rect rect = new Rect(x1,y1,x2,y2);
        RectF rectF = new RectF(rect);
      	Path path = new Path();
    	path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        
        // We're going to apply this paint using a porter-duff xfer mode.
        // This will allow us to only overwrite certain pixels.
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        paint.setTextSize(32);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        paint.setColor(0xFFBBBBFF);
        canvas.drawText("Slate-"+row + ","+ column, w/4, h/4, paint);
        canvas.drawText("Slate-"+row + ","+ column, w/2, h/4, paint);
        canvas.drawText("Slate-"+row + ","+ column, w/4, h/2, paint);
        canvas.drawText("Slate-"+row + ","+ column, w/2, h/2, paint);
        paint.setColor(color);
     
		path.rewind();
        
		final RectF cornerRect = new RectF(MARGIN,MARGIN, radius*2+MARGIN, radius*2+MARGIN);
		if (row == 0) {
			if (column == 0) {
			    path.addArc(cornerRect, 180.0f, 90.0f);
			}
		    if ((column == mNumColumns - 1)) {
			    cornerRect.offsetTo(w - 2 * radius-MARGIN, MARGIN);
			    path.addArc(cornerRect, 270.0f, 90.0f);
		    }
		}
		if (row == mNumRows - 1){
			if (column == mNumColumns - 1) {
			    cornerRect.offsetTo(w - 2*radius-MARGIN, h - 2*radius-MARGIN);
			    path.addArc(cornerRect, 0.0f, 90.0f);
		    }
		    if (column == 0) {
			    cornerRect.offsetTo(MARGIN, h - radius*2-MARGIN);
			    path.addArc(cornerRect, 90.0f, 90.0f);
		    }
        }
		if (row == 0) {
			if ((column >= 0) && (column < mNumColumns - 1)) {
			    path.moveTo(radius+MARGIN, MARGIN);
			    path.rLineTo(w - 2*radius - MARGIN, 0);
				path.rLineTo(radius, 0);
			}
			if ((column > 0) && (column <= mNumColumns - 1)) {
				path.moveTo(0,MARGIN);
				path.rLineTo(radius, 0);
			    path.rLineTo(w - 2*radius - MARGIN, 0);
			}
		}
		if (column == mNumColumns - 1) {
			if ((row >= 0) && (row < mNumRows - 1)) {
			    path.moveTo(w-MARGIN, radius+MARGIN);
			    path.rLineTo(0, h - 2 * radius-MARGIN);
				path.rLineTo(0, radius);
			}
			if ((row > 0) && (row <= mNumColumns - 1)) {
				path.moveTo(w-MARGIN, 0);
				path.rLineTo(0, radius);
			    path.rLineTo(0, h - 2 * radius-MARGIN);
			}
		}
		if (column == 0) {
			if ((row >= 0) && (row < mNumRows - 1)) {
			    path.moveTo(MARGIN, radius+MARGIN);
			    path.rLineTo(0, h - 2 * radius-MARGIN);
				path.rLineTo(0, radius);
			}
			if ((row > 0) && (row <= mNumRows - 1)) {
				path.moveTo(MARGIN, 0);
				path.rLineTo(0, radius);
			    path.rLineTo(0, h - 2 * radius-MARGIN);
			}
		}
		if (row == mNumRows - 1) {
			if ((column >= 0) && (column < mNumColumns - 1)) {
			    path.moveTo(radius+MARGIN, h-MARGIN);
			    path.rLineTo(w - 2*radius - MARGIN, 0);
				path.rLineTo(radius, 0);
			}
			if ((column > 0) && (column <= mNumColumns - 1)) {
				path.moveTo(0,h-MARGIN);
				path.rLineTo(radius, 0);
			    path.rLineTo(w - 2*radius - MARGIN, 0);
			}
		}

		color = 0xff444444;
		final Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setColor(color);
		paint1.setStyle(Style.STROKE);
        paint1.setStrokeWidth(4);
        paint1.setShadowLayer(10,4,4,R.color.magic_flame);
        canvas.drawPath(path, paint1);
        
        return output;
    }
    
    @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/* This method JUST determines whether we want to intercept the motion.
		 * If we return true, onTouchEvent will be called and we do the actual
		 * scrolling there.
		 */

		/* Shortcut the most recurring case: the user is in the dragging state
		 * and he is moving his finger. We want to intercept this motion.
		 */
		final int action = ev.getAction();
        Log.i(LOG_TAG, "event:"+action+", touchState:" + mTouchState);
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            Log.d(LOG_TAG, "event-move, touchState != REST");
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			/* mIsBeingDragged == false, otherwise the shortcut would have
			 * caught it. Check whether the user has moved far enough from his
			 * original down touch.
			 */

			/* Locally do absolute value. mLastMotionX is set to the x value of
			 * the down event.
			 */
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
            Log.d(LOG_TAG, "event-move, x="+x+",y="+y+"xDiff="+xDiff+",yDiff="+yDiff);

			boolean xMoved = xDiff > mTouchSlop;
			boolean yMoved = yDiff > mTouchSlop;

			if ((xMoved) || (yMoved)) {
				// Scroll if the user moved far enough along the X axis
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			// Remember location of down touch
			mLastMotionX = x;
			mLastMotionY = y;

			/* If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
            Log.d(LOG_TAG, "event-up, x="+x+",y="+y);
			// Release the drag
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		/* The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		return mTouchState != TOUCH_STATE_REST;
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
 
        final float x = event.getX();
        final float y = event.getY();
 
        Log.i(LOG_TAG, "event:"+event.getAction() + " x="+x+", y="+y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(LOG_TAG, "event:down. x="+x+", y="+y);
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
 
                // Remember where the motion event started
                mLastMotionX = x;
                mLastMotionY = y;
                break;
 
            case MotionEvent.ACTION_UP:
                //Log.i(LOG_TAG, "event:up. x="+x+",y="+y);
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                int velocityY = (int) velocityTracker.getYVelocity();
 
                int newColumn = mCurrentColumn;
                int newRow = mCurrentRow;
                
                if ((velocityX > SNAP_VELOCITY) && (mCurrentColumn >0)) {
                    // Fling hard enough to move left
                    //snapToScreen(mCurrentScreen - 1);
                    newColumn = mCurrentColumn -1;
                } else if ((velocityX < -SNAP_VELOCITY) && (mCurrentColumn < mNumColumns - 1)) {
                    // Fling hard enough to move right
                    //snapToScreen(mCurrentScreen + 1);
                    newColumn = mCurrentColumn+1;
                } else {
                    newColumn = (mScrollX + (getWidth() / 2)) / getWidth();
                    //snapToDestinationX();
                }
               
                if ((velocityY > SNAP_VELOCITY) && (mCurrentRow >0)) {
                    // Fling hard enough to move left
                    //snapToScreen(mCurrentScreen - mNumColumns);
                    newRow = mCurrentRow -1;
                } else if ((velocityY < -SNAP_VELOCITY) && (mCurrentRow < mNumRows - 1)) {
                    // Fling hard enough to move right
                    //snapToScreen(mCurrentScreen + mNumColumns);
                    newRow = mCurrentRow+1;
                } else {
                    newRow = (mScrollY + (getHeight() / 2)) / getHeight();
                    //snapToDestinationY();
                }
                Log.i(LOG_TAG, "event:up. velocity="+velocityX+","+velocityY);
                snapToScreen(newRow, newColumn);
               
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.i(LOG_TAG, "event:cancel. x="+x+",y="+y);
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_MOVE:
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int deltaX = (int) (mLastMotionX - x);
                final int deltaY = (int) (mLastMotionY - y);
                int scrollByX=0, scrollByY=0;
                
                mLastMotionX = x; mLastMotionY = y;
 
                //Log.i(LOG_TAG, "event:move,x="+x+",y="+y+",deltaX="+deltaX+",deltaY="+deltaY+",mScrollX="+mScrollX+",mScrollY="+mScrollY);
 
                
                // calculate the movement of the x- coordinate
                if ((deltaX < 0) &&  (mScrollX +OVERSHOOT_AMOUNT> 0)) {
                    scrollByX = Math.max(-(mScrollX+OVERSHOOT_AMOUNT), deltaX);
                } else if (deltaX > 0) {
                    final int rightMost = getChildAt(mNumColumns-1).getRight() +OVERSHOOT_AMOUNT;
                    final int availableToScroll = rightMost-mScrollX-getWidth();
                    //final int availableToScroll = getChildAt(getChildCount()-1).getRight()-mScrollX-getWidth();
                    //rightMost = (getWidth() * mNumColumns);
                    //Log.d(LOG_TAG,"width="+getWidth()+",rightMost="+rightMost+",availableToScroll="+availableToScroll);
                    if (availableToScroll > 0) {
                        scrollByX = Math.min(availableToScroll, deltaX);
                    }
                }

                // move the y- coordinate
                if ((deltaY < 0) && ((mScrollY +OVERSHOOT_AMOUNT)> 0)) {
                        scrollByY = Math.max(-(mScrollY+OVERSHOOT_AMOUNT), deltaY);
                } else if (deltaY > 0) {
                    int bottomMost = getChildAt(mNumRows*(mNumColumns-1)).getBottom()+OVERSHOOT_AMOUNT;
                    final int availableToScroll = bottomMost-mScrollY-getHeight();
                    //bottomMost = (getHeight() * mNumRows);
                    //Log.d(LOG_TAG,"height="+getHeight()+",bottomMost="+bottomMost+",availableToScroll="+availableToScroll);
                    if (availableToScroll > 0) {
                        scrollByY = Math.min(availableToScroll, deltaY);
                    }
                } 
                if ((scrollByX != 0) || (scrollByY != 0))
                   scrollBy(scrollByX, scrollByY);
                // }
                break; 
        }
        mScrollX = this.getScrollX();
        mScrollY = this.getScrollY();
 
        return true;
    }
 
 
    //TODO: Implement snap to the end of the row or column. After that disable
    //      the snapping between views.
    
    //TODO: Implement overshoot and  coasting(scroll automatically while slowly
    //      decreasing the speed)
    
    public void snapToScreen(int row, int column) {        
        Log.i(LOG_TAG, "snap To Screen-" + row + "," + column);
		if (mSnapEnabled) {
            mCurrentColumn = column;
			final int newX = mCurrentColumn * getWidth();
			final int deltaX = newX - mScrollX;
			
            mCurrentRow = row;
			final int newY = mCurrentRow * getHeight();
			final int deltaY = newY - mScrollY;
            Log.i(LOG_TAG, "mScroll=" + mScrollX +","+ mScrollY + "  new="+newX+","+newY);
			mScroller.startScroll(mScrollX, mScrollY, deltaX, deltaY, Math.abs(deltaX + deltaY) * 3);
			invalidate();
		}
    }
 
/*    public void setToScreen(int whichScreen) {
        Log.i(LOG_TAG, "set To Screen " + whichScreen);
        mCurrentScreen = whichScreen;
        final int newX = whichScreen * getWidth();
        mScroller.startScroll(newX, 0, 0, 0, 10);            
        invalidate();
    }
    */
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = 0;
        int childMaxHeight = 0;
        //final int count = getChildCount();
		Log.d(LOG_TAG, "layouts of the children");
		for (int row = 0; row < mNumRows; row++) {
            int childLeft = 0;
		    for (int column = 0; column < mNumColumns; column++) {
		    	final int childIndex = row * mNumColumns + column;
				final View child = getChildAt(childIndex);
				if (child.getVisibility() != View.GONE) {
					final int childWidth = child.getMeasuredWidth();
					final int childHeight = child.getMeasuredHeight();
					if (childHeight > childMaxHeight)
						childMaxHeight = childHeight;
					final int childRight = childLeft+childWidth;
					final int childBottom = childTop+childHeight;
					child.layout(childLeft, childTop, childRight, childBottom);
					Log.d(LOG_TAG, childIndex+"= ("+childLeft+","+childTop+") and ("+childRight+","+childBottom+")");
					childLeft += childWidth;
				}
			}
            childTop = childTop + childMaxHeight;
		}
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("error mode.");
        }
 
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("error mode.");
        }
 
        // The children are given the same width and height as the workspace
        //final int count = getChildCount();
   		for (int row = 0; row < mNumRows; row++) {
		    for (int column = 0; column < mNumColumns; column++) {
		    	final int childIndex = row * mNumColumns + column;
                getChildAt(childIndex).measure(widthMeasureSpec, heightMeasureSpec);
				Log.d(LOG_TAG, childIndex+"MeasureSpec=("+widthMeasureSpec+","+heightMeasureSpec+")");
		    }
        }
        //setMeasuredDimension(width, height);
        
        Log.i(LOG_TAG, "moving to screen "+mCurrentRow+","+mCurrentRow);
        scrollTo(mCurrentRow * width, mCurrentColumn * height);      
    }  
 
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            mScrollY = mScroller.getCurrY();
            scrollTo(mScrollX, mScrollY);
            invalidate();
            //postInvalidate();
        }
    }
   
    /**
     * Return the parceable instance to be saved
     */
    @Override
    protected Parcelable onSaveInstanceState() {
      final SavedState state = new SavedState(super.onSaveInstanceState());
      state.currentRow = mCurrentRow;
      state.currentColumn = mCurrentColumn;
      return state;
    }
 
 
    /**
     * Restore the previous saved current screen
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
      SavedState savedState = (SavedState) state;
      super.onRestoreInstanceState(savedState.getSuperState());
      if (savedState.currentRow != -1) {
        mCurrentRow = savedState.currentRow;
      }
      if (savedState.currentColumn != -1) {
        mCurrentColumn = savedState.currentColumn;
      }
    }
 
    // ========================= INNER CLASSES ==============================
 
    public interface onViewChangedEvent{      
      void onViewChange (int currentViewIndex);
    }
 
    /**
     * A SavedState which save and load the current screen
     */
    public static class SavedState extends BaseSavedState {
      int currentRow = -1;
      int currentColumn = -1;
 
      /**
       * Internal constructor
       *
       * @param superState
       */
      SavedState(Parcelable superState) {
        super(superState);
      }
 
      /**
       * Private constructor
       *
       * @param in
       */
      private SavedState(Parcel in) {
        super(in);
        currentRow = in.readInt();
        currentColumn = in.readInt();
      }
 
      /**
       * Save the current screen
       */
      @Override
      public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(currentRow);
        out.writeInt(currentColumn);
      }
 
      /**
       * Return a Parcelable creator
       */
      public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
          return new SavedState(in);
        }
 
        public SavedState[] newArray(int size) {
          return new SavedState[size];
        }
      };
    }
}
