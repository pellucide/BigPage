package com.appcumen;


import com.appcumen.adapters.BigPageAdapter;
import com.appcumen.listeners.PageChangeListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;


public class DragableSpace1 extends ViewGroup 
    implements ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "DragableSpace1";
    /* (non-Javadoc)
	 * @see android.view.ViewGroup#checkLayoutParams(android.view.ViewGroup.LayoutParams)
	 */
	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		//Log.d(TAG, "checkLayoutParams");
		// TODO Auto-generated method stub
		return super.checkLayoutParams(p);
	}
	/* (non-Javadoc)
	 * @see android.view.ViewGroup#cleanupLayoutState(android.view.View)
	 */
	@Override
	protected void cleanupLayoutState(View child) {
		Log.d(TAG, "cleanupLayoutState");
		// TODO Auto-generated method stub
		super.cleanupLayoutState(child);
	}
	/* (non-Javadoc)
	 * @see android.view.ViewGroup#dispatchDisplayHint(int)
	 */
	@Override
	public void dispatchDisplayHint(int hint) {
		Log.d(TAG, "dispatchDisplayHint");
		// TODO Auto-generated method stub
		super.dispatchDisplayHint(hint);
	}
	/* (non-Javadoc)
	 * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		//Log.d(TAG, "dispatchDraw");
		super.dispatchDraw(canvas);
	}
	/* (non-Javadoc)
	 * @see android.view.ViewGroup#recomputeViewAttributes(android.view.View)
	 */
	@Override
	public void recomputeViewAttributes(View child) {
		Log.d(TAG, "recomputeViewAttributes");
		// TODO Auto-generated method stub
		super.recomputeViewAttributes(child);
	}
	/* (non-Javadoc)
	 * @see android.view.View#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		Log.d(TAG, "draw");
		// TODO Auto-generated method stub
		super.draw(canvas);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#forceLayout()
	 */
	@Override
	public void requestLayout() {
		Log.d(TAG, "requestLayout");
		// TODO Auto-generated method stub
		super.requestLayout();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#forceLayout()
	 */
	@Override
	public void forceLayout() {
		Log.d(TAG, "forceLayout");
		// TODO Auto-generated method stub
		super.forceLayout();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#isLayoutRequested()
	 */
	@Override
	public boolean isLayoutRequested() {
		Log.d(TAG, "isLayoutRequested");
		// TODO Auto-generated method stub
		return super.isLayoutRequested();
	}
	/* (non-Javadoc)
	 * @see android.view.View#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		Log.d(TAG, "onAttachedToWindow");
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}
	/* (non-Javadoc)
	 * @see android.view.View#onDisplayHint(int)
	 */
	@Override
	protected void onDisplayHint(int hint) {
		Log.d(TAG, "onDisplayHint");
		// TODO Auto-generated method stub
		super.onDisplayHint(hint);
	}
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw");
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	/* (non-Javadoc)
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		Log.d(TAG, "onFinishInflate");
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}
	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "onSizeChanged");
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}
 
    @Override
    public void invalidate() {
    	//Log.d(TAG, "invalidate()");
        super.invalidate();
    }
    
	private static final int SNAP_VELOCITY = 300;
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    private static int OVERSHOOT_AMOUNT = 57;
    private static final int MARGIN = 8;
    private static final int MAX_SETTLE_DURATION = 800; // ms
    private static final int MIN_SETTLE_DURATION = 400; // ms
    private static final int SIDE_BUFFER_COUNT = 2;
    private static final float MINIMUM_SCALE_RATIO = 0.69f;
    private static final float MAXIMUM_SCALE_RATIO = 1.92f;
    
    public enum DirectionMode {
    	HORIZONTAL_DIRECTION_MODE,
    	VERTICAL_DIRECTION_MODE,
    	FREE_FLOW_DIRECTION_MODE,
    	LOCK_FLOW_DIRECTION_MODE
    }
    private DirectionMode mDirectionMode = DirectionMode.LOCK_FLOW_DIRECTION_MODE;
    
    public enum Direction {
    	HORIZONTAL,
    	VERTICAL,
    	FREE_FLOW,
    }
  
    
    private Direction mCurrentDirection = Direction.FREE_FLOW;
    private ScaleGestureDetector scaleGesture;
    private BigPageAdapter mAdapter;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchState = TOUCH_STATE_REST;
 
    private int mScrollX = 0;
    private int mScrollY = 0;
    private int mCurrentRow = -1;
    private int mCurrentCol = -1;
	private int mSideBufferX = SIDE_BUFFER_COUNT;
	private int mSideBufferY = SIDE_BUFFER_COUNT;
    
    Bitmap [] bms;
    Context mCtx;
    private float mLastMotionX;
    private float mLastMotionY;
 
 
    private int mTouchSlop = 12;
    private boolean mSnapEnabled = true;
    
    private int mNumColumns = 1;
    private int mNumRows = 1;
    
    private boolean layoutPending = false;
    public void disableSnap() { mSnapEnabled = false; }
    public void enableSnap() { mSnapEnabled = true; }
    public boolean isSnapEnabled() { return (mSnapEnabled); }

    private PageChangeListener mChangeListener;
    
    SparseArray<SparseArray<View>> views = new SparseArray<SparseArray<View>>(6);
    SparseArray<SparseIntArray> overlapYs = new SparseArray<SparseIntArray>(6);
    SparseArray<SparseIntArray> overlapXs = new SparseArray<SparseIntArray>(6);
    
    private float scaleFactor = 1.0f;
    private float scaleRatio = 1.0f;
    private boolean currentlyZooming = false;
    private float zoomSlop = 0.009f;
    private int mRowBegin;
    private int mRowEnd;
    private int mColBegin;
    private int mColEnd;
    private boolean zoomEnabled=true;
    private long mScaleBeginTime;
	
    //ScaleGestureDetector.OnScaleGestureListener()
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
    	Log.d(TAG, "onScaleBegin");
    	if (!zoomEnabled)
    		return false;
    	currentlyZooming = true;
    	mScaleBeginTime = detector.getEventTime();
    	if (mTouchState == TOUCH_STATE_SCROLLING)
    		return false;
    	return true;
    }
   
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
    	if (!zoomEnabled)
    		return true;
    	float scaleFactor = detector.getScaleFactor();
    	Log.d(TAG, "onScale, scaleFactor="+scaleFactor+", getCurrentSpan="+detector.getCurrentSpan()+", getPrevSpan="+detector.getPreviousSpan());
    
    	if (!changeScaleRatio(scaleFactor)) {
    		return false;
    	} else {
    		requestLayout();
    		return true;
    	}
    }   
     
    private boolean changeScaleRatio(float scaleFactor) {
   		if (currentlyZooming) 
   			Log.d(TAG, "zoom ongoing");
   		Log.d(TAG, "scale: " + scaleFactor);
    	if (Math.abs(1.0f-scaleFactor) < zoomSlop)
    		return false;
    	scaleFactor = (float) Math.sqrt(scaleFactor);
    	
   	    scaleRatio *= scaleFactor;
   	    scaleRatio = Math.max(scaleRatio,MINIMUM_SCALE_RATIO);
   	    scaleRatio = Math.min(scaleRatio,MAXIMUM_SCALE_RATIO);
   		Log.d(TAG, "scaleRatio="+scaleRatio);
   		return true;
    }
   
    
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    	Log.d(TAG, "onScaleEnd");
    	if (!zoomEnabled)
    		return;
    	currentlyZooming = false;
    	if (detector.getEventTime() - mScaleBeginTime < 500) {
    		scaleRatio = 1.0f;
    	}else {
    		changeScaleRatio(scaleFactor);
    		if (Math.abs(scaleRatio - 1.0f) <= 0.015f)
    			scaleRatio = 1.0f;
    	}
   		Log.d(TAG, "scaleRatio="+scaleRatio);
    }
 
    

   
    public void setDirectionMode(DirectionMode directionMode) {
    	mDirectionMode = directionMode;
    	if (mDirectionMode == DirectionMode.HORIZONTAL_DIRECTION_MODE) {
    		mCurrentDirection = Direction.HORIZONTAL;
	        mSideBufferY = 0;
    	}
    	if (mDirectionMode == DirectionMode.VERTICAL_DIRECTION_MODE) {
    		mCurrentDirection = Direction.VERTICAL;
	        mSideBufferX = 0;
    	}
    	if (mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE ||
    		mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE){
    		mCurrentDirection = Direction.FREE_FLOW;
	        if (mSideBufferX != SIDE_BUFFER_COUNT || mSideBufferY != SIDE_BUFFER_COUNT) {
	        	mSideBufferX = SIDE_BUFFER_COUNT;
	        	mSideBufferY = SIDE_BUFFER_COUNT;
	        	calculateRowsAndCols(mCurrentCol, mCurrentRow);
	        	updateBoundaries(mCurrentRow, mCurrentCol);
	        	if (populate_new(mCurrentCol, mCurrentRow, true))
	        		requestLayout();
	        }
    	}
    }
    
    public int getCurrentColumn() {
    	return mCurrentCol;
    }

    public int getCurrentRow() {
    	return mCurrentRow;
    }

    public DragableSpace1(Context context) {
        super(context);
 
        //mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mTouchSlop = ViewConfiguration.getTouchSlop();
 
        /*this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
                    
        
        initializeViews(context);
    }
 
    public int setOverShootPixels(int pixels) {
    	int oldVal = OVERSHOOT_AMOUNT;
    	OVERSHOOT_AMOUNT = pixels;
    	return oldVal;
    }
    
    
    public DragableSpace1(Context context, int numColumns, int numRows) {
        super(context);
        mNumColumns = numColumns;
        mNumRows = numRows;
 
        /* this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
                    
        initializeViews(context);
    }
 
    public DragableSpace1(Context context, AttributeSet attrs) {
        super(context, attrs);
 
 
        /* this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    */
        this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT ,
                    ViewGroup.LayoutParams.MATCH_PARENT));
                    
        /*
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DragableSpace);
        mNumColumns = a.getInteger(R.styleable.DragableSpace_numColumns, 1);
        mNumRows = a.getInteger(R.styleable.DragableSpace_numRows, 1);
        int mCurrentScreen = a.getInteger(R.styleable.DragableSpace_default_screen, 0);
        mCurrentColumn = mCurrentScreen % mNumColumns;
        mCurrentRow = mCurrentScreen / mNumColumns;
		//mBackground = BitmapFactory.decodeResource(context.getResources(),
		//a.getInteger(R.styleable.DragableSpace_background, 0xff999999));
		 */
        
        initializeViews(context);
    }
 
    @SuppressLint("NewApi")
	private void initializeViews(Context context) {
        Log.d(TAG, "initializeViews");
        calculateRowsAndCols(mCurrentCol, mCurrentRow);
       	updateBoundaries(mCurrentRow, mCurrentCol);
       	//Bitmap mBackground = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_drops1);
		//LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mScroller = new Scroller(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        	mScroller.setFriction(3.0f);
		bms = new Bitmap[9];
		
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		//populate(mCurrentColumn, mCurrentRow);
		//requestLayout();
        scaleGesture = new ScaleGestureDetector(context,  this);
        mCtx = context;
        this.setWillNotDraw(true); 
   		Log.d(TAG,"mTouchSlop="+mTouchSlop);
   		this.clearAnimation();
   		this.setAnimation(null);

   		this.setChildrenDrawingCacheEnabled(true);
        setChildrenDrawingOrderEnabled(true);
		//changeBackground(R.drawable.green_drops1);
    }
    
    private void updateBoundaries(int row, int col) {
        Log.d(TAG, "updateBoundaries()");
    	if (mAdapter != null) {
    		int leftCount = Math.min(col, mSideBufferX);
    		int rightCount = Math.min(mAdapter.getColCount() - col-1, mSideBufferX);
    		int topCount = Math.min(row, mSideBufferY);
    		int bottomCount = Math.min(mAdapter.getRowCount() - row-1, mSideBufferY);
    		mRowBegin = Math.max(row - topCount, 0);
    		mRowEnd = Math.min(row + bottomCount, mAdapter.getRowCount()-1);
    		mColBegin = Math.max(col - leftCount, 0);
    		mColEnd = Math.min(col + rightCount, mAdapter.getColCount()-1);
    		//Log.i(TAG, "rowBegin=" + mRowBegin + ",colBegin=" + mColBegin +", rowEnd=" + mRowEnd + ",colEnd=" + mColEnd);
    	}
    }
    
    void calculateRowsAndCols(int posx, int posy) {
        Log.d(TAG, "calculateRowsAndCols(posx="+posx+",posy="+posy);
    	if (mAdapter != null) {
    		int leftCount = Math.min(posx, mSideBufferX);
    		int rightCount = Math.min(mAdapter.getColCount() - posx-1, mSideBufferX);
    		mNumColumns = leftCount + rightCount + 1;
    		int topCount = Math.min(posy, mSideBufferY);
    		int bottomCount = Math.min(mAdapter.getRowCount() - posy-1, mSideBufferY);
    		mNumRows = topCount + bottomCount + 1;
    	}
        Log.d(TAG, "calculateRowsAndCols mNumRows="+mNumRows+",mNumColumns="+mNumColumns);
    }

 

	public void setAdapter(BigPageAdapter adapter) {
		setAdapter(adapter, 0, 0);
	}
	
	public void setAdapter(BigPageAdapter adapter, int posx, int posy) {
        Log.d(TAG, "setAdapter posx="+ posx+", posy="+ posy);
		mAdapter = adapter;
		setCurrentItem(posx, posy);
	}
	
	public void setCurrentItem(int posx, int posy) {
        Log.d(TAG, "setCurrentItem posx="+ posx+", posy="+ posy);
        if (mAdapter == null || mAdapter.getRowCount() == 0 || mAdapter.getColCount() == 0)
        	return;
		
        calculateRowsAndCols(posx, posy);
		if (populate_new(posx, posy,true)) {
			//updateBoundaries has to be called after populate and before requestLayout();
			updateBoundaries(posy, posx);
			requestLayout();
		}
        else {
        	updateBoundaries(posy, posx);
        }
		if (snapToScreen(posy, posx, 100, 100))
			invalidate();
	}
	
	
    /**
     * Adds a child view. If no layout parameters are already set on the child, the
     * default parameters for this ViewGroup are set on the child.
     *
     * @param child the child view to add
     * @param row the row position at which to add the child
     * @param col the column position at which to add the child
     *
     * @see #generateDefaultLayoutParams()
     */
    public void addViewInLayout(View child, int row, int column) {
        //Log.d(TAG, "addViewInLayout");
        child.clearAnimation();
        child.setAnimation(null);
        
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
            }
        }
        super.addViewInLayout(child, row*mNumColumns + column, params);
    }
    
    
    /**
     * Replaces the view at the specified position in the group.
     *
     * @param child the child view to add
     * @param row the row position at which to add the child
     * @param col the column position at which to add the child
     */
    public void replaceViewAt1(View child,int row, int column) {
        Log.d(TAG, "replaceViewAt");
    	int index = row*mNumColumns + column;
        removeViewAt(index);
        addView(child, row, column);
        //requestLayout();
        //invalidate();
    }
    

    
    /**
     * Removes the view at the specified position in the group.
     *
     * @param row the row position at which to add the child
     * @param col the column position at which to add the child
     */
    public void removeViewAt1(int row, int column) {
        Log.d(TAG, "removeViewAt");
    	int index = row*mNumColumns + column;
        removeViewAt(index);
        requestLayout();
        //invalidate();
    }

    
    public void changeAllBackground(int resource) {
		Bitmap bg = BitmapFactory.decodeResource(getContext().getResources(), resource);
		for(int ii=0; ii<mNumRows; ii++) {
			for (int jj = 0; jj < mNumColumns; jj++) {
                changeBackground(bg, ii,  jj);
			}
		}
    }


	
    public void changeBackground(Bitmap backgroundBitmap, int row,  int column) {
		Drawable bgSlate = new BitmapDrawable(mCtx.getResources(), getRoundedCornerBitmap1(backgroundBitmap, row, column, 30.0f));
		this.getChildAt(row*mNumColumns + column).setBackgroundDrawable(bgSlate);
    }

    public View getChildAt(int row,  int column) {
    	//Log.d(TAG,"getChildAt(row="+row+",column="+column);
		return getChildAt((row-mRowBegin)*mNumColumns + column - mColBegin);
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

    	
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		
		final int action = ev.getAction();
        Log.i(TAG, "onTouchIntercept:event:"+action+", touchState:" + mTouchState);
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            Log.d(TAG, "event-move, touchState != REST");
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
            //Log.d(LOG_TAG, "mScroller.isFinished()="+mScroller.isFinished());
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
				//mScroller.forceFinished(true);
				Log.i(TAG, "abort mScroller");
				layoutPending = populate_new(mCurrentCol, mCurrentRow, true);
				layout_real();
			}

			// Remember location of down touch
			mLastMotionX = x;
			mLastMotionY = y;

			/* If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                : TOUCH_STATE_SCROLLING;

			break;

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
            //Log.d(LOG_TAG, "event-move, x="+x+",y="+y+"xDiff="+xDiff+",yDiff="+yDiff);

			boolean xMoved = xDiff > mTouchSlop;
			boolean yMoved = yDiff > mTouchSlop;

			if ((xMoved) || (yMoved)) {
				// Scroll if the user moved far enough along the X axis
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
            //Log.d(LOG_TAG, "event-up, x="+x+",y="+y);
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
    public boolean onTouchEvent(MotionEvent ev) {
        //Log.i(TAG, "onTouchEvent:event:"+ ev.getAction());
        //Log.i(TAG, "onTouchEvent");
    	boolean needsInvalidate = false;
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
 
    	scaleGesture.onTouchEvent(ev);
        final float x = ev.getX();
        final float y = ev.getY();
 
        //Log.i(LOG_TAG, "ev:"+ev.getAction() + " x="+x+", y="+y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(LOG_TAG, "event:down. x="+x+", y="+y);
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    //mScroller.abortAnimation();
                    mScroller.forceFinished(true);
                }
 
        		if (mChangeListener != null) {
        			mChangeListener.onScrollBegin();
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
 
                int newColumn = mCurrentCol;
                int newRow = mCurrentRow;
                
                
                if (mCurrentDirection == Direction.HORIZONTAL ||
               		mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)
                {
                	if ((velocityX > SNAP_VELOCITY) && (mCurrentCol>0)) {
                		// Fling hard enough to move left
                		newColumn = mCurrentCol -1;
                	} else if ((velocityX < -SNAP_VELOCITY) && (mCurrentCol < mAdapter.getColCount() - 1)) {
                		// Fling hard enough to move right
                		newColumn = mCurrentCol+1;
                	} else {
                        if ((mCurrentCol > 0) && (mCurrentCol < mAdapter.getColCount()-1)) {
                        	final int scrollX = getScrollX();
                        	final int currentLeft = views.get(mCurrentRow).get(mCurrentCol).getLeft();
                        	if (scrollX < currentLeft){
                        		final int prevLeft = views.get(mCurrentRow).get(mCurrentCol-1).getLeft();
                        		if ( Math.abs(scrollX - prevLeft) <  Math.abs(scrollX - currentLeft))
                        			newColumn = mCurrentCol-1;
                        	}
                        	else {
                        		final int nextLeft = views.get(mCurrentRow+1).get(mCurrentCol).getLeft();
                        		if ( Math.abs(scrollX - nextLeft) < Math.abs(scrollX - currentLeft))
                        			newColumn = mCurrentCol+1;
                        	}
                        }
                	}
                }
               	newColumn = Math.min(newColumn, mAdapter.getColCount() -1);
               	newColumn = Math.max(newColumn, 0);

                if (mCurrentDirection == Direction.VERTICAL ||
               		mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)
                {
                	if ((velocityY > SNAP_VELOCITY) && (mCurrentRow >0)) {
                		// Fling hard enough to move left
                		newRow = mCurrentRow -1;
                	} else if ((velocityY < -SNAP_VELOCITY) && (mCurrentRow < mAdapter.getRowCount() - 1)) {
                		// Fling hard enough to move right
                		newRow = mCurrentRow+1;
                	} else {
                		if ((mCurrentRow > 0) && (mCurrentRow < mAdapter.getRowCount()-1)) {
                			final int scrollY = getScrollY();
                			final int currentTop = views.get(mCurrentRow).get(mCurrentCol).getTop();
                			if (scrollY < currentTop){
                				final int prevTop = views.get(mCurrentRow-1).get(mCurrentCol).getTop();
                				if ( Math.abs(scrollY - prevTop) <  Math.abs(scrollY - currentTop))
                					newRow = mCurrentRow-1;
                			}
                			else {
                				final int nextTop = views.get(mCurrentRow+1).get(mCurrentCol).getTop();
                				if ( Math.abs(scrollY - nextTop) < Math.abs(scrollY - currentTop))
                					newRow = mCurrentRow+1;
                			}
                		}
                	}
                }
               	newRow = Math.min(newRow, mAdapter.getRowCount() -1);
               	newRow = Math.max(newRow, 0);
		        
                //Log.i(LOG_TAG, "event:up. velocity="+velocityX+","+velocityY);
                //Log.i(LOG_TAG, "event:up. newPosition="+newRow+","+newColumn);
                
        		calculateRowsAndCols(newColumn, newRow);
        		//layoutPending = populate_new(newColumn, newRow);
                needsInvalidate = snapToScreen(newRow, newColumn, velocityX, velocityY);
                
        		if (!needsInvalidate && mChangeListener != null) {
        			mChangeListener.onScrollEnd();
        		}
               
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
				if (mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) {
					mCurrentDirection = Direction.FREE_FLOW;
				}
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.i(LOG_TAG, "event:cancel. x="+x+",y="+y);
                mTouchState = TOUCH_STATE_REST;
				if (mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) {
					mCurrentDirection = Direction.FREE_FLOW;
				}
                break;
            case MotionEvent.ACTION_MOVE:
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int deltaX = (int) (mLastMotionX - x);
                final int deltaY = (int) (mLastMotionY - y);
                int scrollByX=0, scrollByY=0;
                

                //Log.i(TAG, "event:move,x="+x+",y="+y+",deltaX="+deltaX+",deltaY="+deltaY+",mScrollX="+mScrollX+",mScrollY="+mScrollY);

                boolean xMoved = Math.abs(deltaX) > mTouchSlop;
                boolean yMoved = Math.abs(deltaY) > mTouchSlop;
               	if ((mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) &&
           			(mCurrentDirection == Direction.FREE_FLOW)) {
              		if ((xMoved || yMoved) ) {
                		if (Math.abs(deltaX) > Math.abs(deltaY)) {
                			yMoved = false; 
                			mCurrentDirection = Direction.HORIZONTAL;
                		}
                		else{
                			xMoved = false;
                			mCurrentDirection = Direction.VERTICAL;
                		}
                	}
			    }
				if ((mCurrentDirection == Direction.HORIZONTAL ||
					 mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)) {
					mLastMotionX = x;
					// calculate the movement of the x- coordinate
					if ((deltaX < 0)){
						if (this.mCurrentCol <= mSideBufferX) {
							final int leftMost = getChildAt(0).getLeft()-OVERSHOOT_AMOUNT;
							final int availableToScroll = -leftMost+getScrollX();
							Log.d(TAG,"leftMost="+leftMost+",availableToScroll="+availableToScroll);
							if (availableToScroll > 0) 
								scrollByX = Math.min(availableToScroll, deltaX);
						} else
							scrollByX = deltaX;
					} else if (deltaX > 0) {
						if (this.mCurrentCol >= mAdapter.getColCount()-mSideBufferX) {
							final int rightMost = getChildAt(mNumColumns-1).getRight() +OVERSHOOT_AMOUNT;
							final int availableToScroll = rightMost-getScrollX()-getWidth();
							//final int availableToScroll = getChildAt(getChildCount()-1).getRight()-mScrollX-getWidth();
							//rightMost = (getWidth() * mNumColumns);
							//Log.d(LOG_TAG,"width="+getWidth()+",rightMost="+rightMost+",availableToScroll="+availableToScroll);
							if (availableToScroll > 0) 
								scrollByX = Math.min(availableToScroll, deltaX);
						} else 
							scrollByX = deltaX;
					}
				}

				if ((mCurrentDirection == Direction.VERTICAL ||
					 mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)) {
					mLastMotionY = y;	
					// move the y- coordinate
					if ((deltaY < 0) ){
						if (this.mCurrentRow <= mSideBufferY) {
							final int topMost = getChildAt(0).getTop()-OVERSHOOT_AMOUNT;
							final int availableToScroll = -topMost+getScrollY();
							if (availableToScroll > 0) 
								scrollByY = Math.min(availableToScroll, deltaY);
						} else
							scrollByY = deltaY;
					} else if (deltaY > 0) {
						if (this.mCurrentRow >= mAdapter.getRowCount()-mSideBufferY) {
							final int bottomMost = getChildAt((mNumRows-1)*mNumColumns).getBottom()+OVERSHOOT_AMOUNT;
							final int availableToScroll = bottomMost-getScrollY()-getHeight();
							//Log.d(LOG_TAG,"height="+getHeight()+",bottomMost="+bottomMost);
							//Log.d(LOG_TAG,"availableToScroll="+availableToScroll);
							if (availableToScroll > 0) 
								scrollByY = Math.min(availableToScroll, deltaY);
						} else
							scrollByY = deltaY;
					} 
				}
				if ((scrollByX != 0) || (scrollByY != 0)) {
                   scrollBy(scrollByX, scrollByY);
				}
                break; 
                
            default:
            	 break;
        }
        mScrollX = this.getScrollX();
        mScrollY = this.getScrollY();
        if (needsInvalidate) {
        	//requestLayout();
        	postInvalidate();
            //postInvalidateOnAnimation();
        }
        return true;
    }
 
    public boolean onTouchEvent_old(MotionEvent ev) {
        //Log.i(TAG, "onTouchEvent:event:"+ ev);
        Log.i(TAG, "onTouchEvent");
    	boolean needsInvalidate = false;
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
 
    	scaleGesture.onTouchEvent(ev);
        final float x = ev.getX();
        final float y = ev.getY();
 
        //Log.i(LOG_TAG, "ev:"+ev.getAction() + " x="+x+", y="+y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(LOG_TAG, "event:down. x="+x+", y="+y);
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    //mScroller.abortAnimation();
                    mScroller.forceFinished(true);
                }
 
        		if (mChangeListener != null) {
        			mChangeListener.onScrollBegin();
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
 
                int newColumn = mCurrentCol;
                int newRow = mCurrentRow;
                
                
                if (mCurrentDirection == Direction.HORIZONTAL ||
               		mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)
                {
                	if ((velocityX > SNAP_VELOCITY) && (mCurrentCol>0)) {
                		// Fling hard enough to move left
                		newColumn = mCurrentCol -1;
                	} else if ((velocityX < -SNAP_VELOCITY) && (mCurrentCol < mAdapter.getColCount() - 1)) {
                		// Fling hard enough to move right
                		newColumn = mCurrentCol+1;
                	} else {
                		newColumn = (getScrollX() + (getWidth() / 2)) / getWidth();
                	}
                }
               	newColumn = Math.min(newColumn, mAdapter.getColCount() -1);
               	newColumn = Math.max(newColumn, 0);

                if (mCurrentDirection == Direction.VERTICAL ||
               		mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)
                {
                	if ((velocityY > SNAP_VELOCITY) && (mCurrentRow >0)) {
                		// Fling hard enough to move left
                		newRow = mCurrentRow -1;
                	} else if ((velocityY < -SNAP_VELOCITY) && (mCurrentRow < mAdapter.getRowCount() - 1)) {
                		// Fling hard enough to move right
                		newRow = mCurrentRow+1;
                	} else {
                		newRow = (getScrollY() + (getHeight() / 2)) / getHeight();
                	}
				}
               	newRow = Math.min(newRow, mAdapter.getRowCount() -1);
               	newRow = Math.max(newRow, 0);
		        
                //Log.i(LOG_TAG, "event:up. velocity="+velocityX+","+velocityY);
                //Log.i(LOG_TAG, "event:up. newPosition="+newRow+","+newColumn);
                
        		calculateRowsAndCols(newColumn, newRow);
        		layoutPending = populate(newColumn, newRow);
                needsInvalidate = snapToScreen(newRow, newColumn, velocityX, velocityY);
                
        		if (!needsInvalidate && mChangeListener != null) {
        			mChangeListener.onScrollEnd();
        		}
               
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
				if (mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) {
					mCurrentDirection = Direction.FREE_FLOW;
				}
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.i(LOG_TAG, "event:cancel. x="+x+",y="+y);
                mTouchState = TOUCH_STATE_REST;
				if (mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) {
					mCurrentDirection = Direction.FREE_FLOW;
				}
                break;
            case MotionEvent.ACTION_MOVE:
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int deltaX = (int) (mLastMotionX - x);
                final int deltaY = (int) (mLastMotionY - y);
                int scrollByX=0, scrollByY=0;
                

                Log.i(TAG, "event:move,x="+x+",y="+y+",deltaX="+deltaX+",deltaY="+deltaY+",mScrollX="+mScrollX+",mScrollY="+mScrollY);

                boolean xMoved = Math.abs(deltaX) > mTouchSlop;
                boolean yMoved = Math.abs(deltaY) > mTouchSlop;
               	if ((mDirectionMode == DirectionMode.LOCK_FLOW_DIRECTION_MODE) &&
           			(mCurrentDirection == Direction.FREE_FLOW)) {
              		if ((xMoved || yMoved) ) {
                		if (Math.abs(deltaX) > Math.abs(deltaY)) {
                			yMoved = false; 
                			mCurrentDirection = Direction.HORIZONTAL;
                		}
                		else{
                			xMoved = false;
                			mCurrentDirection = Direction.VERTICAL;
                		}
                	}
			    }
				if ((mCurrentDirection == Direction.HORIZONTAL ||
					 mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)) {
					mLastMotionX = x;
					// calculate the movement of the x- coordinate
					if ((deltaX < 0) &&  (getScrollX() +OVERSHOOT_AMOUNT> 0)) {
						scrollByX = Math.max(-(getScrollX()+OVERSHOOT_AMOUNT), deltaX);
					} else if (deltaX > 0) {
						if (this.mCurrentCol >= mAdapter.getColCount()-mSideBufferX) {
							final int rightMost = getChildAt(mNumColumns-1).getRight() +OVERSHOOT_AMOUNT;
							final int availableToScroll = rightMost-getScrollX()-getWidth();
							//final int availableToScroll = getChildAt(getChildCount()-1).getRight()-mScrollX-getWidth();
							//rightMost = (getWidth() * mNumColumns);
							//Log.d(LOG_TAG,"width="+getWidth()+",rightMost="+rightMost+",availableToScroll="+availableToScroll);
							if (availableToScroll > 0) 
								scrollByX = Math.min(availableToScroll, deltaX);
						} else 
							scrollByX = deltaX;
					}
				}

				if ((mCurrentDirection == Direction.VERTICAL ||
					 mDirectionMode == DirectionMode.FREE_FLOW_DIRECTION_MODE)) {
					mLastMotionY = y;	
					// move the y- coordinate
					if ((deltaY < 0) && ((getScrollY() +OVERSHOOT_AMOUNT)> 0)) {
						scrollByY = Math.max(-(getScrollY()+OVERSHOOT_AMOUNT), deltaY);
					} else if (deltaY > 0) {
						if (this.mCurrentRow >= mAdapter.getRowCount()-mSideBufferY) {
							int bottomMost = getChildAt((mNumRows-1)*mNumColumns).getBottom()+OVERSHOOT_AMOUNT;
							final int availableToScroll = bottomMost-getScrollY()-getHeight();
							//Log.d(LOG_TAG,"height="+getHeight()+",bottomMost="+bottomMost);
							//Log.d(LOG_TAG,"availableToScroll="+availableToScroll);
							if (availableToScroll > 0) 
								scrollByY = Math.min(availableToScroll, deltaY);
						} else
							scrollByY = deltaY;
					} 
				}
				if ((scrollByX != 0) || (scrollByY != 0)) {
                   scrollBy(scrollByX, scrollByY);
				}
                break; 
                
            default:
            	 break;
        }
        mScrollX = this.getScrollX();
        mScrollY = this.getScrollY();
        if (needsInvalidate) {
        	//requestLayout();
        	postInvalidate();
            //postInvalidateOnAnimation();
        }
        return true;
    }
 
    private boolean snapToScreen() {        
    	return snapToScreen(mCurrentRow, mCurrentCol, 0, 0);
    }
 

    // We want the duration of the page snap animation to be influenced by the distance that
    // the screen has to travel, however, we don't want this duration to be effected in a
    // purely linear fashion. Instead, we use this method to moderate the effect that the distance
    // of travel has on the overall snap duration.
    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    private int getScaledClientWidth() {
        return (int) ((getWidth() - getPaddingLeft() - getPaddingRight())); //* zoomLevel);
    }
    
    private int getScaledClientHeight() {
        return (int) ((getHeight() - getPaddingTop() - getPaddingBottom())); //* zoomLevel);
    }
    
    //TODO: Implement snap to the end of the row or column. After that disable
    //      the snapping between views.
    
    //TODO: Implement overshoot and  coasting(scroll automatically while slowly
    //      decreasing the speed)
    
    private int determineDuration(int velocityX, int velocityY, int deltaX, int deltaY) {
        final int width = getScaledClientWidth();
        final int halfWidth = width / 2;
        final int height = getScaledClientHeight();
        final int halfHeight = height / 2;
        final float distanceRatioX = Math.min(1f, 1.0f * Math.abs(deltaX) / width);
        final float distanceRatioY = Math.min(1f, 1.0f * Math.abs(deltaY) / height);
        final float distanceX = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatioX);
        final float distanceY = halfHeight + halfHeight * distanceInfluenceForSnapDuration(distanceRatioY);

        int durationX=0, durationY=0;
        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        
        if (velocityX> 0) {
            durationX = 4 * Math.round(1000 * Math.abs(distanceX / velocityX));
        } else {
            final float pageDelta = (float) Math.abs(deltaX) / (width);
            durationX = (int) (pageDelta * 1.5);
        }

        if (velocityY> 0) {
            durationY = 4 * Math.round(1000 * Math.abs(distanceY / velocityY));
        } else {
            final float pageDelta = (float) Math.abs(deltaY) / height;
            durationY = (int) (pageDelta  *1.5);
        }
        //Log.i(TAG, "durations=" + durationX +","+ durationY);
        durationX = Math.min(durationX, MAX_SETTLE_DURATION);
        durationY = Math.min(durationY, MAX_SETTLE_DURATION);
        
        durationX = Math.max(durationX, MIN_SETTLE_DURATION);
        durationY = Math.max(durationY, MIN_SETTLE_DURATION);

        int duration = durationX;
        if (durationY > duration)
        	duration = durationY;
    		
        return duration;
    }
    
    private boolean snapToScreen(int row, int column, int velocityX, int velocityY) {        
         return snapToScreen_new(row, column, velocityX, velocityY);
    }
    
    /*
    private boolean snapToScreen_old(int row, int column, int velocityX, int velocityY) {        
    	boolean needsInvalidate=false;
        Log.i(TAG, "snapToScreen_old-" + row + "," + column);
        Log.i(TAG, "rowBegin=" + mRowBegin + ",colBegin=" + mColBegin);
        
        int overlapY = 0;
        for (int ii=mRowBegin; ii<=mRowEnd;ii++)  {
        	for (int jj=mColBegin; jj<=mColEnd;jj++){
        		if (jj==column)
				if ((overlapYs.get(ii-1) != null)) 
        			overlapY += overlapYs.get(ii).get(jj);
        	}
        	if (ii==row)
        		break;
        }
		if (mSnapEnabled) {
			final int newX =(int) ((column * getWidth()) * scaleRatio);
			final int newY =(int) ((row * getHeight() -overlapY) * scaleRatio);
			
			final int newX1 = (int) (views.get(row).get(column).getLeft() * scaleRatio);
			final int newY1 = (int) (views.get(row).get(column).getTop() * scaleRatio);
			final int deltaX = newX - getScrollX();
			final int deltaY = newY - getScrollY();
			
			int duration = determineDuration(velocityX, velocityY, deltaX, deltaY);
			
            mCurrentCol = column;
            mCurrentRow = row;
            updateBoundaries(row, column);
            
            Log.i(TAG, "mScroll=" + getScrollX() +","+ getScrollY() + " new="+newX+","+newY+" new1="+newX1+","+newY1);
            Log.i(TAG, "delta=" + deltaX +","+ deltaY + "  duration="+duration);
           	if (deltaX != 0 || deltaY != 0) {
           	    mScroller.startScroll(getScrollX(), getScrollY(), deltaX, deltaY, duration);
           		needsInvalidate = true;
           	}
		}
        Log.i(TAG, "snapToScreen:scroll=" + getScrollX() + "," + getScrollY());
        return needsInvalidate;
    }
 
 */
 private boolean snapToScreen_new(int row, int column, int velocityX, int velocityY) {        
    	boolean needsInvalidate=false;
        Log.i(TAG, "snapToScreen_new-" + row + "," + column +"  velocity="+velocityX+","+velocityY);
        //showLayout();
        
        if (mFirstLayout) {
            mCurrentCol = column;
            mCurrentRow = row;
            updateBoundaries(row, column);
        	return true;
        }
        if ((row < mRowBegin) ||
            (row > mRowEnd) ||
            (column < mColBegin) ||
            (column > mColEnd) ) {
        	layoutPending = true;
        	return true;
        }
        
        mCurrentCol = column;
        mCurrentRow = row;
        updateBoundaries(row, column);
        Log.i(TAG, "snapToScreen: current scroll=" + getScrollX() + "," + getScrollY());
		if (mSnapEnabled) {
			//final View child = getChildAt(row, column);
			final View child = views.get(row).get(column);
			//child.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
			final int newX = child.getLeft();
			final int newY = child.getTop();
			final int deltaX = newX - getScrollX();
			final int deltaY = newY - getScrollY();
			
			int duration = determineDuration(velocityX, velocityY, deltaX, deltaY);
            
			Log.i(TAG, "snapToScreen: new scroll=" + newX + "," + newY+
					"  delta=" + deltaX +","+ deltaY + "  duration="+duration);
           	if (deltaX != 0 || deltaY != 0) {
           	    mScroller.startScroll(getScrollX(), getScrollY(), deltaX, deltaY, duration);
           		needsInvalidate = true;
           	}
		}
        return needsInvalidate;
    }
 
 
    @Override
    public void computeScroll() {
    	computeScroll_new();
    }

    public void computeScroll_new() {
     	//Log.i(TAG, "computeScroll()");
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            mScrollY = mScroller.getCurrY();
            final int finalX = mScroller.getFinalX();
            final int finalY = mScroller.getFinalY();
            //Log.i(TAG, "computeScroll()--mScroll=" + mScrollX + "," + mScrollY+" final=" + finalX + "," + finalY);
            final int diffX = Math.abs(finalX - mScrollX);
            final int diffY = Math.abs(finalY - mScrollY);
            if ((diffX<=15) && (diffY<=15)) {
        		layoutPending = populate_new(mCurrentCol, mCurrentRow, true);
            }
            //snap the last two pixels
            if ((diffX<=5) && (diffY<=5)) {
                scrollTo(finalX, finalY);
        		//mScroller.forceFinished(true);
        		mScroller.abortAnimation();
        		//views.get(mCurrentRow).get(mCurrentColumn).setVisibility(View.VISIBLE);
        		//views.get(mCurrentRow+1).get(mCurrentColumn).setVisibility(View.INVISIBLE);
        		if (mChangeListener != null) {
        			mChangeListener.onScrollEnd();
        			mChangeListener.onSwitched(mCurrentCol, mCurrentRow);
        		}
                if (layoutPending) {
                    //Log.i(TAG, "computeScroll() requesting layout");
                	layoutPending = false;
                    this.requestLayout();
                }
            }
            else {
                 scrollTo(mScrollX, mScrollY);
                 invalidate();
            }
	    } else  {
	    	//Log.i(TAG, "point 1");
	    	//mScroller.forceFinished(true);
	    	mScroller.abortAnimation();
	    }
        //showLayout();
    }

 
    int printCounter=0;
    public void computeScroll_old() {
     	//Log.i(TAG, "computeScroll()");
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            mScrollY = mScroller.getCurrY();
            int finalX = mScroller.getFinalX();
            int finalY = mScroller.getFinalY();
            //Log.i(TAG, "computeScroll()--mScroll=" + mScrollX + "," + mScrollY+" final=" + finalX + "," + finalY);
            if ((finalX == mScrollX) && (finalY == mScrollY)) {
                scrollTo(finalX, finalY);
        		mScroller.forceFinished(true);
        		//views.get(mCurrentRow).get(mCurrentColumn).setVisibility(View.VISIBLE);
        		//views.get(mCurrentRow+1).get(mCurrentColumn).setVisibility(View.INVISIBLE);
        		if (mChangeListener != null) {
        			mChangeListener.onScrollEnd();
        			mChangeListener.onSwitched(mCurrentCol, mCurrentRow);
        		}
                printCounter=0;
        		layoutPending = populate_new(mCurrentCol, mCurrentRow, true);
                if (layoutPending) {
                    //Log.i(TAG, "computeScroll() requesting layout");
                	layoutPending = false;
                    this.requestLayout();
                }
            }
            else {
                 scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                 printCounter=1;
                 invalidate();
            }
	    }
        else  {
       		//Log.i(TAG, "computeScroll()scroll=" + getScrollX() + "," + getScrollY());
        	if (printCounter > 0){
        		printCounter--;
        		Log.i(TAG, "point 1");
        		mScroller.forceFinished(true);
        		//mScroller.abortAnimation();
        		//views.get(mCurrentRow).get(mCurrentCol).bringToFront();
        		if (mChangeListener != null) {
        			Log.i(TAG, "point 2");
        			mChangeListener.onSwitched(mCurrentCol, mCurrentRow);
        		}
                if (layoutPending) {
                	Log.i(TAG, "point 3");
                	layoutPending = false;
                    this.requestLayout();
                }
        	}
		}
        //showLayout();
	}
    
    int[] drawOrderMatrix =
    	{    0,  1,  4,  3,  2,
    		 5,  6,  9,  8,  7,
    		15, 16, 19, 18, 17,
    		20, 21, 24, 23, 22,
    		10, 11, 14, 13, 12,
    	};

    /**
     * Returns the index of the child to draw for this iteration. Override this
     * if you want to change the drawing order of children. By default, it
     * returns i.
     * <p>
     * NOTE: In order for this method to be called, you must enable child ordering
     * first by calling {@link #setChildrenDrawingOrderEnabled(boolean)}.
     *
     * @param i The current iteration.
     * @return The index of the child to draw this iteration.
     *
     * @see #setChildrenDrawingOrderEnabled(boolean)
     * @see #isChildrenDrawingOrderEnabled()
     */
    
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
         return getChildDrawingOrder_old(childCount, i);
    }
    
    protected int getChildDrawingOrder_new(int childCount, int i) {
    	//Log.d(TAG,"getChildDrawingOrder(childCount="+childCount+",i="+i+")");
    	if (childCount < (mSideBufferY * 2 + 1) * (mSideBufferX * 2 + 1)) {
    		//TODO fix the drawing order
    		//Log.d(TAG,"returning "+ i);
    		return i;
    	}
    	//Log.d(TAG,"returning "+ drawOrderMatrix[i]);
    	return drawOrderMatrix[i];
    }
    
    protected int getChildDrawingOrder_old(int childCount, int i) {
    	//Log.d(TAG,"getChildDrawingOrder(childCount="+childCount+",i="+i+")");
    	int row = i / mNumColumns;
    	int col = i % mNumColumns;
    	
    	//if ((row == mCurrentRow-mRowBegin ) && (col==mCurrentCol-mColBegin)) {
    	    //Log.d(TAG,"returning "+ (childCount-1));
    		//return childCount-1;
    	//}
    	
    	if (i == childCount/2) {
    	    //Log.d(TAG,"returning "+ (childCount-1));
    		return childCount-1;
    	}
    	
    	//if (i==childCount-1) {
    	    //Log.d(TAG,"returning "+ ((mCurrentRow-mRowBegin) * mNumColumns-mColBegin + mCurrentCol));
    		//return (mCurrentRow - mRowBegin) * mNumColumns + mCurrentCol - mColBegin;
    	//}
    	
    	if (i==childCount-1) {
    	    //Log.d(TAG,"returning "+ (childCount/2));
    		return childCount/2;
    	}
    	
   	    //Log.d(TAG,"returning "+ i);
        return i;
    }


	private boolean mFirstLayout = true;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
 		Log.d(TAG, "onLayout(changed="+changed+",l="+left+",t="+top+",r="+right+",b="+bottom);
 		layout_real();
 		if ((changed || mFirstLayout) && !this.currentlyZooming) {
 			mFirstLayout = false;
 			this.snapToScreen();
 		}
 		//showLayout();
    }
    
    protected void onLayout_old(boolean changed, int l, int t, int r, int b) {
		Log.d(TAG, "onLayout(changed="+changed+",l="+l+",t="+t+",r="+r+",b="+b);
        
        int nextChildOverlapY[] = new int[mNumColumns];
	    for (int column = 0; column < mNumColumns; column++) {
	    	nextChildOverlapY[column] = 0;
	    }
	    
    	//populateOverlaps(mCurrentColumn, mCurrentRow);
	    
        int childTop = (int) (mRowBegin * getHeight() * scaleRatio) ;
        int childMaxHeight = 0;
		for (int row = mRowBegin; row <= mRowEnd; row++) {
			SparseIntArray overlapYRow =  overlapYs.get(row);
			if (overlapYRow == null) {
				overlapYRow =  new SparseIntArray(6);
				overlapYs.put(row, overlapYRow);
			}
				
			int overlapY = 0;
            int childLeft = (int) (mColBegin * getWidth() * scaleRatio);
		    for (int col = mColBegin; col <= mColEnd; col++) {
		    	final int childIndex = (row - mRowBegin) * mNumColumns + (col-mColBegin);
				final View child = getChildAt(childIndex);
				
				if (child.getVisibility() != View.GONE) {
					overlapY = nextChildOverlapY[col - mColBegin];
					final int childWidth = child.getMeasuredWidth();
					final int childHeight = child.getMeasuredHeight();
					if (childHeight > childMaxHeight)
						childMaxHeight = childHeight;
					final int childRight = childLeft+childWidth;
					final int childBottom = childTop+childHeight;
	
					Log.d(TAG, childIndex+"= ("+childLeft+","+(childTop-overlapY)+") and ("+childRight+","+(childBottom-overlapY)+")");
					child.layout(childLeft, childTop-overlapY, childRight, childBottom-overlapY);
					
					overlapY =  mAdapter.getOverlapWithNextY(child, col, row);
					overlapYRow.put(col, overlapY);
					nextChildOverlapY[col - mColBegin] += overlapY;
				
					Log.d(TAG, "onLayout:row="+row+",column="+col+",overlapY="+overlapY);
					childLeft += childWidth;
				}
			}
            childTop = childTop + childMaxHeight;
		}
        
		if (changed || mFirstLayout) {
			mFirstLayout = false;
			this.snapToScreen();
			/*
			int x  = mCurrentColumn * getWidth();
			x *= scaleRatio;
			int dx = x - getScrollX();
			int y  = mCurrentRow * getHeight();      
			y *= scaleRatio;      
			int dy = y - getScrollY();

			if ((dx != 0) || (dy != 0)) {
				Log.i(TAG, "moving to screen "+mCurrentRow+","+mCurrentColumn);
				Log.i(TAG, "scrollBy dx="+dx+",dy="+dy);
				scrollBy(dx, dy);
				//mScroller.startScroll(getScrollX(),getScrollY(), dx, dy, 800);
				//postInvalidate();
				//this.snapToScreen();
			}
			*/
		}
    }
    
    private void layout_real() {
 		int childWidth;
 		int childHeight;
 		View child;
 		int row = mCurrentRow;
 		int col = mCurrentCol;
 		//int thisChildTop = (int) (row * getHeight() * scaleRatio) ;
 		//int thisChildLeft = (int) (col * getWidth() * scaleRatio) ;
 		
		child = getChildAt(row, col);
 		int thisChildTop = child.getTop();
 		int thisChildLeft = child.getLeft();
 		
 		for(row=mCurrentRow; row >= mRowBegin; row--) {
 			child = getChildAt(row, col);
 			childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 			childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 			//thisChildLeft=Math.max(0, thisChildLeft);
 			//thisChildTop=Math.max(0, thisChildTop);
 			if (child.getVisibility() != View.GONE) 
 				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);

 			col = mCurrentCol+1;
 			for (; col<=mColEnd; col++) {
 				child = getChildAt(row, col);
 				childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 				childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 				thisChildLeft = getChildAt(row, col-1).getLeft() + (childWidth - mAdapter.getOverlapWithPrevX(child, col, row));
 				//thisChildLeft=Math.max(0, thisChildLeft);
 				//thisChildTop=Math.max(0, thisChildTop);
 				if (child.getVisibility() != View.GONE) 
 					child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
 			}


 			col = mCurrentCol-1;
 			for (; col>=mColBegin; col--) {
 				child = getChildAt(row, col);
 				childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 				childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 				thisChildLeft = getChildAt(row, col+1).getLeft() - (childWidth - mAdapter.getOverlapWithNextX(child, col, row));
 				//thisChildLeft=Math.max(0, thisChildLeft);
 				//thisChildTop=Math.max(0, thisChildTop);
 				if (child.getVisibility() != View.GONE) 
 					child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
 			}
 			
 		    col = mCurrentCol;
 			child = getChildAt(row, col);
 			childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 			childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 		    thisChildTop = child.getTop() - (childHeight - mAdapter.getOverlapWithPrevY(child, col, row));
 		    thisChildLeft = (int) (child.getLeft());
 		}
 		
 		
 		row = mCurrentRow;
 		col = mCurrentCol;
 		child = getChildAt(row, col);
 		childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 		thisChildTop = child.getTop() + (childHeight - mAdapter.getOverlapWithNextY(child, col, row));
 		thisChildLeft = child.getLeft();
 		for(row=mCurrentRow+1; row <= mRowEnd; row++) {
 			child = getChildAt(row, col);
 			childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 			childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 			//thisChildLeft=Math.max(0, thisChildLeft);
 			//thisChildTop=Math.max(0, thisChildTop);
 			if (child.getVisibility() != View.GONE) 
 				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);

 			col = mCurrentCol+1;
 			thisChildLeft=0;

 			for (; col<=mColEnd; col++) {
 				child = getChildAt(row, col);
 				childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 				childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 				thisChildLeft = getChildAt(row, col-1).getLeft() +
 						(childWidth - mAdapter.getOverlapWithPrevX(child, col, row));
 				//thisChildLeft=Math.max(0, thisChildLeft);
 				//thisChildTop=Math.max(0, thisChildTop);
 				if (child.getVisibility() != View.GONE) 
 					child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
 			}


 			col = mCurrentCol-1;
 			for (; col>=mColBegin; col--) {
 				child = getChildAt(row, col);
 				childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 				childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 				thisChildLeft = getChildAt(row, col+1).getLeft() - (childWidth - mAdapter.getOverlapWithNextX(child, col, row));
 				//thisChildLeft=Math.max(0, thisChildLeft);
 				//thisChildTop=Math.max(0, thisChildTop);
 				if (child.getVisibility() != View.GONE) 
 					child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
 			}
 			
 		    col = mCurrentCol;
 			child = getChildAt(row, col);
 			childWidth = (int) (child.getMeasuredWidth()); //* scaleRatio);
 			childHeight = (int) (child.getMeasuredHeight()); //* scaleRatio);
 		    thisChildTop = child.getTop() + (childHeight - mAdapter.getOverlapWithNextY(child, col, row));
 		    thisChildLeft = (int) (child.getLeft());
 		}

    }
   
    
    
    protected void onLayout_new1(boolean changed, int l, int t, int r, int b) {
		Log.d(TAG, "onLayout(changed="+changed+",l="+l+",t="+t+",r="+r+",b="+b);
        
	    
		int childWidth;
		int childHeight;
		View child;
		int row = mCurrentRow;
		int col = mCurrentCol;
		int thisChildTop = (int) (row * getHeight() * scaleRatio) ;
		int thisChildLeft = (int) (col * getWidth() * scaleRatio) ;
		
		
		for(row=mCurrentRow; row >= mRowBegin; row--) {
			child = getChildAt(row, col);
			childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
			childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
			thisChildLeft=Math.max(0, thisChildLeft);
			thisChildTop=Math.max(0, thisChildTop);
			child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);

			col = mCurrentCol+1;
			for (; col<=mColEnd; col++) {
				child = getChildAt(row, col);
				childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
				childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
				thisChildLeft = getChildAt(row, col-1).getLeft() +
						(int)(scaleRatio * (childWidth - mAdapter.getOverlapWithPrevX(child, col, row)));
				thisChildLeft=Math.max(0, thisChildLeft);
				thisChildTop=Math.max(0, thisChildTop);
				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
			}


			col = mCurrentCol-1;
			for (; col>=mColBegin; col--) {
				child = getChildAt(row, col);
				childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
				childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
				thisChildLeft = getChildAt(row, col+1).getLeft() - (int)(scaleRatio * (childWidth - mAdapter.getOverlapWithNextX(child, col, row)));
				thisChildLeft=Math.max(0, thisChildLeft);
				thisChildTop=Math.max(0, thisChildTop);
				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
			}
			
		    col = mCurrentCol;
			child = getChildAt(row, col);
			childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
			childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
		    thisChildTop = child.getTop() - (int)(scaleRatio * (childHeight - mAdapter.getOverlapWithPrevY(child, col, row)));
		    thisChildLeft = (int) (child.getLeft());
		}
		
		
		row = mCurrentRow;
		col = mCurrentCol;
		child = getChildAt(row, col);
		childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
		thisChildTop = child.getTop() + (int)(scaleRatio * (childHeight - mAdapter.getOverlapWithNextY(child, col, row)));
		thisChildLeft = child.getLeft();
		for(row=mCurrentRow+1; row <= mRowEnd; row++) {
			child = getChildAt(row, col);
			childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
			childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
			thisChildLeft=Math.max(0, thisChildLeft);
			thisChildTop=Math.max(0, thisChildTop);
			child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);

			col = mCurrentCol+1;
			thisChildLeft=0;

			for (; col<=mColEnd; col++) {
				child = getChildAt(row, col);
				childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
				childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
				thisChildLeft = getChildAt(row, col-1).getLeft() +
						(int)(scaleRatio * (childWidth - mAdapter.getOverlapWithPrevX(child, col, row)));
				thisChildLeft=Math.max(0, thisChildLeft);
				thisChildTop=Math.max(0, thisChildTop);
				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
			}


			col = mCurrentCol-1;
			for (; col>=mColBegin; col--) {
				child = getChildAt(row, col);
				childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
				childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
				thisChildLeft = getChildAt(row, col+1).getLeft() - (int)(scaleRatio * (childWidth - mAdapter.getOverlapWithNextX(child, col, row)));
				thisChildLeft=Math.max(0, thisChildLeft);
				thisChildTop=Math.max(0, thisChildTop);
				child.layout(thisChildLeft, thisChildTop, thisChildLeft+childWidth, thisChildTop+childHeight);
			}
			
		    col = mCurrentCol;
			child = getChildAt(row, col);
			childWidth = (int) (child.getMeasuredWidth() * scaleRatio);
			childHeight = (int) (child.getMeasuredHeight() * scaleRatio);
		    thisChildTop = child.getTop() + (int)(scaleRatio * (childHeight - mAdapter.getOverlapWithNextY(child, col, row)));
		    thisChildLeft = (int) (child.getLeft());
		}

		if (changed || mFirstLayout) {
			mFirstLayout = false;
			this.snapToScreen();
		}
		//showLayout();
    }
   
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	//Log.d(TAG, "onMeasure()");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 
        int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
        	if (!this.isInEditMode())
        		throw new IllegalStateException("error mode.");
        }
 
        int height = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
        	if (!this.isInEditMode())
        		throw new IllegalStateException("error mode.");
        }
        
        
        
		//Log.d(LOG_TAG, "MeasureSpec height="+height+" width="+width);
		height = (int)(height * scaleRatio);
		width = (int) (width * scaleRatio);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
 
        // The children are given the same width and height as the workspace
   		for (int row = 0; row < mNumRows; row++) {
		    for (int column = 0; column < mNumColumns; column++) {
		    	final int childIndex = row * mNumColumns + column;
		    	View child = this.getChildAt(childIndex);
		    	if (child != null) {
                    child.measure(widthMeasureSpec, heightMeasureSpec);
			        //child.setDrawingCacheEnabled(true);
			        //child.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
		    	}
                
				//Log.d(LOG_TAG, childIndex+":MeasureSpec=("+widthMeasureSpec+","+heightMeasureSpec+")");
				//Log.d(LOG_TAG, childIndex+":MeasureSpec height="+MeasureSpec.getSize(heightMeasureSpec));
				//Log.d(LOG_TAG, childIndex+":MeasureSpec width="+MeasureSpec.getSize(widthMeasureSpec));
		    }
        }
        //setMeasuredDimension(width, height);
        

        //Log.d(TAG, "onMeasure():return; childCount = "+getChildCount());
    }  
    

    private class RemoveViewsTask extends AsyncTask<Void, Void, Void> {
    	int waitingThreads = 0;
    	boolean done = false;
        @Override
        protected synchronized Void doInBackground(Void...voids) {
        	removeAllViewsInLayout();
        	done = true;
        	if (waitingThreads > 0)
        		this.notifyAll();
			return null;
        }
        public synchronized void waitForFinish() throws InterruptedException {
        	if (!done) {
        		waitingThreads++;
        		wait();
        	}
        }
    }
    
    @SuppressLint("NewApi")
	boolean populate_new(int posx, int posy, boolean background) {
		Log.d(TAG, "populate_new(posx="+posx+",posy="+posy+")");
  		
		final RemoveViewsTask rmTask = new RemoveViewsTask();
		if (background) {
			if (android.os.Build.VERSION.SDK_INT  <= 10 ) {
				rmTask.execute((Void[]) null);
			}
			else  {
				//Log.d(TAG,"SDK_INT > 10");
				rmTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			}
		} else 
			this.removeAllViewsInLayout();

    	boolean modified = false;
   		
   		final int rowBegin = Math.max(posy - mSideBufferY, 0);
   		final int rowEnd = Math.min(posy + mSideBufferY, mAdapter.getRowCount()-1);
   		final int colBegin = Math.max(posx - mSideBufferX, 0);
   		final int colEnd = Math.min(posx + mSideBufferX, mAdapter.getColCount()-1);
   		int col, row;
   		
   		Log.d(TAG,"new values="+rowBegin+","+rowEnd+"-"+colBegin+","+colEnd);
 

   		Log.d(TAG,"insert new items");
    	for (row=rowBegin; row<=rowEnd; row++){
    		SparseArray<View> viewRow = views.get(row);
    		if (viewRow == null) {
    			modified = true;
    			viewRow = new SparseArray<View>(6);
    		    views.put(row, viewRow);
    		}
    	    for (col=colBegin; col<=colEnd; col++){
    	    	if (viewRow.get(col) == null){
    	    		modified = true;
    	    		View v = mAdapter.instantiateItem(col, row, this);
    	    		viewRow.put(col, v);
    	    	}
    	    }
    	}
    	
   		
     	for (row=rowBegin-mSideBufferY; row<rowBegin; row++){
    		final SparseArray<View> viewRow = views.get(row);
    		if (viewRow == null) {
    			continue;
    		}
    		else {
    			modified = true;
    			//delete all columns for the row
    	        for (col=colBegin - mSideBufferX; col<=colEnd+mSideBufferX; col++){
    	    	    if (viewRow.get(col) != null){
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    	        //delete the row
    	        views.delete(row);
    	    }
    	}
     	
    	for (row=rowEnd+1; row<=rowEnd+mSideBufferY; row++){
    		final SparseArray<View> viewRow = views.get(row);
    		if (viewRow == null) {
    			continue;
    		}
    		else {
    			modified = true;
    			//delete all columns for the row
    	        for (col=colBegin - mSideBufferX; col<=colEnd+mSideBufferX; col++){
    	    	    if (viewRow.get(col) != null){
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    	        //delete the row
    	        views.delete(row);
    	    }
    	}
     	
	
    	for (row=rowBegin; row<=rowEnd; row++){
    		final SparseArray<View> viewRow = views.get(row);
    		if (viewRow != null) {
    			//delete old columns for the row
    	        for (col=colBegin - mSideBufferX; col<colBegin; col++){
    	    	    if (viewRow.get(col) != null){
    	    	    	modified = true;
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    	        for (col=colEnd+1; col<colEnd+mSideBufferX; col++){
    	    	    if (viewRow.get(col) != null){
    	    	    	modified = true;
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    	    }
    		else {
    			Log.wtf(TAG, "..This should never happen!!");
    		}
    	}
     	
    	try {
    		if (background) 
    			rmTask.waitForFinish();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	int ii=0,jj=0;
    	for (ii=0,row=rowBegin; row<=rowEnd; ii++, row++){
    		final SparseArray<View> viewRow = views.get(row);
    		if (viewRow != null) {
    			for (jj=0,col=colBegin; col<=colEnd; col++,jj++){
    				final View v = viewRow.get(col);
    				if (v != null){
    					addViewInLayout(v,ii, jj);
    				} else {
    					Log.wtf(TAG, "..This should never happen!!");
    				}
    				if (posx==col && posy==row) {
    					//TODO: anything special for the centerView
    				}
    			}
    		}
    		else {
    			Log.wtf(TAG, "--This should never happen!!");
    	    }
    	}
    	
    	//showLayout();
		Log.d(TAG, "modified="+modified);
 
    	return modified;
    }
    
    
    boolean populate(int posx, int posy) {
		Log.d(TAG, "populate(posx="+posx+",posy="+posy+")");
   		SparseArray<View> viewRow;
   		int oldRowBegin = mRowBegin;
   		int oldRowEnd = mRowEnd;
   		int oldColBegin = mColBegin;
   		int oldColEnd = mColEnd;
   		
   		int rowBegin = Math.max(posy - mSideBufferY, 0);
   		int rowEnd = Math.min(posy + mSideBufferY, mAdapter.getRowCount()-1);
   		int colBegin = Math.max(posx - mSideBufferX, 0);
   		int colEnd = Math.min(posx + mSideBufferX, mAdapter.getColCount()-1);
   		int col, row;
   		
   		Log.d(TAG,"new values="+rowBegin+","+rowEnd+"-"+colBegin+","+colEnd + "  old values="+oldRowBegin+","+oldRowEnd+"-"+oldColBegin+","+oldColEnd);
   		
   		if ((rowBegin == oldRowBegin) &&
   		    (colBegin == oldColBegin) &&
   		    (rowEnd == oldRowEnd) &&
   		    (colEnd == oldColEnd))
   			return false;
   		//new RemoveViewsTask().execute((Void[]) null);
     	this.removeAllViewsInLayout();

   		Log.d(TAG,"insert new items");
    	for (row=rowBegin; row<=rowEnd; row++){
    		viewRow = views.get(row);
    		if (viewRow == null) {
    			viewRow = new SparseArray<View>(6);
    		    views.put(row, viewRow);
    		}
    	    for (col=colBegin; col<=colEnd; col++){
    	    	if (viewRow.get(col) == null){
    	    		View v = mAdapter.instantiateItem(col, row, this);
    	    		viewRow.put(col, v);
    	    	}
    	    }
    	}
    	
   		
   		Log.d(TAG,"delete old items");
   		if (oldRowBegin < rowBegin) {
   			for (row=oldRowBegin; row< rowBegin; row++){
   				viewRow = views.get(row);
   				if (viewRow != null) {
   					for (col=oldColBegin; col<=oldColEnd; col++){
   						mAdapter.destroyItem(viewRow.get(col),col, row, this);
   						//viewRow.put(col, null);
   						viewRow.delete(col);
   					}
   					//views.put(row, null);
   					views.delete(row);
   				}
   			}
   		}
   		
   		if (oldRowEnd > rowEnd) {
     	    for (row=rowEnd+1; row<=oldRowEnd; row++){
    	        viewRow = views.get(row);
    	        if (viewRow != null) {
    	            for (col=oldColBegin; col<=oldColEnd; col++){
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	        	    //viewRow.put(col, null);
    	        	    viewRow.delete(col);
    	            }
    	            views.delete(row);
    	            //views.put(row, null);
    	        }
    	    }
   		}
     	
     	for (row=rowBegin; row<=rowEnd; row++){
    		viewRow = views.get(row);
    		if (viewRow == null) {
    			Log.wtf(TAG, "This should never happen!!");
    		}
    		if (oldColBegin < colBegin) {
    	        for (col=oldColBegin; col<colBegin; col++){
    	    	    if (viewRow.get(col) != null){
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    		}
    		if (oldColEnd > colEnd) {
    	        for (col=colEnd+1; col<=oldColEnd; col++){
    	    	    if (viewRow.get(col) != null){
    	        	    mAdapter.destroyItem(viewRow.get(col),col, row, this);
    	    		    //viewRow.put(col, null);
    	    		    viewRow.delete(col);
    	    	    }
    	        }
    	    }
    	}
     	
     	int ii=0,jj=0;
    	for (ii=0,row=rowBegin; row<=rowEnd; ii++, row++){
    		viewRow = views.get(row);
    		if (viewRow != null) {
    			for (jj=0,col=colBegin; col<=colEnd; col++,jj++){
    				View v = viewRow.get(col);
    				if (v != null){
    					addViewInLayout(v,ii, jj);
    				} else {
    					Log.wtf(TAG, "..This should never happen!!");
    				}
    				if (posx==col && posy==row) {
    					//TODO: anything special for the centerView
    				}
    			}
    		}
    		else {
    			Log.wtf(TAG, "--This should never happen!!");
    	    }
    	}
    	
    	//populateOverlaps(posx, posy);
 
    	return true;
    }
    
    

    boolean populateOverlaps(int posx, int posy) {
		Log.d(TAG, "populateOverlaps(posx="+posx+",posy="+posy+")");
   		SparseArray<View> viewRow;
   		SparseIntArray overlapYRow;
   		SparseIntArray overlapXRow;
   		int rowBegin = Math.max(posy - mSideBufferY, 0);
   		int rowEnd = Math.min(posy + mSideBufferY, mAdapter.getRowCount()-1);
   		int colBegin = Math.max(posx - mSideBufferX, 0);
   		int colEnd = Math.min(posx + mSideBufferX, mAdapter.getColCount()-1);
   		int col, row;
   		int oldRowBegin = Math.max(mCurrentRow - mSideBufferY, 0);
   		int oldRowEnd = Math.min(mCurrentRow + mSideBufferY, mAdapter.getColCount()-1);
   		int oldColBegin = Math.max(mCurrentCol - mSideBufferX, 0);
   		int oldColEnd = Math.min(mCurrentCol + mSideBufferX, mAdapter.getRowCount()-1);
   		
   		Log.d(TAG,"new values="+rowBegin+","+rowEnd+"-"+colBegin+","+colEnd);
   		Log.d(TAG,"old values="+oldRowBegin+","+oldRowEnd+"-"+oldColBegin+","+oldColEnd);
   		
    	for (row=rowBegin; row<=rowEnd; row++){
    		viewRow = views.get(row);
   			overlapYRow = overlapYs.get(row);
   			overlapXRow = overlapXs.get(row);
    		if (overlapYRow == null) {
    			overlapYRow = new SparseIntArray (6);
    		    overlapYs.put(row, overlapYRow);
    		}
    		if (overlapXRow == null) {
    			overlapXRow = new SparseIntArray (6);
    		    overlapXs.put(row, overlapXRow);
    		}
 
    	    for (col=colBegin; col<=colEnd; col++){
    	    	View v = views.get(row).get(col);
    	    	if (overlapYRow.get(col, -1) == -1){
    		        overlapYRow.put(col, mAdapter.getOverlapWithNextY(v, col, row));
    	    	}
    	    	if (overlapXRow.get(col, -1) == -1){
    		        overlapXRow.put(col, mAdapter.getOverlapWithNextX(v, col, row));
    	    	}
    	    }
    	}
    	
   		
   		if (oldRowBegin < rowBegin) {
   			for (row=oldRowBegin; row< rowBegin; row++){
   				viewRow = views.get(row);
   				overlapYRow = overlapYs.get(row);
   				overlapXRow = overlapXs.get(row);
   				if (overlapYRow != null) {
   					overlapYs.put(row, null);
   				}
   				if (overlapXRow != null) {
   					overlapXs.put(row, null);
   				}
   			}
   		}
   		
   		if (oldRowEnd > rowEnd) {
     	    for (row=rowEnd+1; row<=oldRowEnd; row++){
    	        viewRow = views.get(row);
    	        overlapYRow = overlapYs.get(row);
    	        overlapXRow = overlapXs.get(row);
    	        if (overlapYRow != null) {
   					overlapYs.put(row, null);
    	        }
    	        if (overlapXRow != null) {
   					overlapXs.put(row, null);
    	        }
    	    }
   		}
     	
     	for (row=rowBegin; row<=rowEnd; row++){
    		viewRow = views.get(row);
    		overlapYRow = overlapYs.get(row);
    		overlapXRow = overlapXs.get(row);
    		if (overlapYRow == null || overlapXRow == null) {
    			Log.wtf(TAG, "This should never happen!!");
    		}
    		if (oldColBegin < colBegin) {
    	        for (col=oldColBegin; col<colBegin; col++){
    	    	    if (overlapYRow.get(col, -1) != -1){
    	    	    	overlapYRow.delete(col);
    	    	    }
    	    	    if (overlapXRow.get(col, -1) != -1){
    	    		    overlapXRow.delete(col);
    	    	    }
    	        }
    		}
    		if (oldColEnd > colEnd) {
    	        for (col=colEnd+1; col<=oldColEnd; col++){
    	    	    if (overlapYRow.get(col, -1) != -1){
    	    		    overlapYRow.delete(col);
    	    	    }
    	    	    if (overlapXRow.get(col, -1) != -1){
    	    		    overlapXRow.delete(col);
    	    	    }
    	        }
    	    }
    	}
     	
     	int ii=0,jj=0;
     	ii=rowBegin;jj=colBegin;
    	for (ii=rowBegin; ii<20+rowBegin; ii++){
    		viewRow = views.get(ii);
    		if (viewRow == null) {
    			continue;
    		}
     	    StringBuffer sb = new StringBuffer();
    		sb.append("row=").append(ii).append(":");
    	    for (jj=colBegin; jj<=20+colBegin; jj++){
   	    		View v = viewRow.get(jj);
    	    	if (v == null)
    			    continue;
    	    	sb.append(" ").append(jj);
    	    	sb.append("-").append(overlapYs.get(ii).get(jj)).append(",").append(overlapXs.get(ii).get(jj));
    	    }
    	    sb.append("\n");
    	    Log.d(TAG, sb.toString());
    	}
    	return true;
    }
    
    
    private void showLayout() {
    	int ii=0,jj=0;
     	ii=mRowBegin;jj=mColBegin;
    	for (ii=mRowBegin; ii<=mRowEnd; ii++){
    		SparseArray<View> viewRow;
    		viewRow = views.get(ii);
    		if (viewRow == null) {
    			continue;
    		}
     	    StringBuffer sb = new StringBuffer();
    		sb.append("row=").append(ii).append(">");
    	    for (jj=mColBegin; jj<=mColEnd; jj++){
   	    		View v = viewRow.get(jj);
    	    	if (v == null)
    			    continue;
    	    	sb.append(" ").append(jj);
    	    	sb.append(":").append(v.getLeft()).append(",").append(v.getTop());
    	    }
    	    sb.append("\n");
    	    Log.d(TAG, sb.toString());
    	}
    	for (ii=0; ii<mNumRows; ii++){
     	    StringBuffer sb = new StringBuffer();
    		sb.append("row=").append(ii).append(">");
    	    for (jj=0; jj<mNumColumns; jj++){
   	    		View v = getChildAt(ii*mNumColumns+jj);
    	    	if (v == null)
    			    continue;
    	    	sb.append(" ").append(jj);
    	    	sb.append(":").append(v.getLeft()).append(",").append(v.getTop());
    	    }
    	    sb.append("\n");
    	    Log.i(TAG, sb.toString());
    	}
    }
    

	/**
	 * Set the FlowIndicator
	 * 
	 * @param flowIndicator
	 */
	public void setChangeListener(PageChangeListener l) {
		mChangeListener = l;
	} 
 
	@Override
	protected void onScrollChanged(int h, int v, int oldh, int oldv) {
		//Log.d(TAG,"onScrollChanged:Current="+mCurrentRow+","+mCurrentCol);
		super.onScrollChanged(h, v, oldh, oldv);
		int width = getWidth();
		int height = getHeight();
		float hFraction,vFraction;
        
        /*
        int childTop = (int) (mCurrentRow * height * scaleRatio) ;
        int childLeft = (int) (mCurrentColumn * width * scaleRatio);
		hFraction  = (float)(h-childLeft);
		hFraction  = hFraction/((float)width);
		vFraction  = (float)(v-childTop);
		vFraction  = vFraction/((float)height);
		*/
		
		hFraction = (((float)h)/((float)width));
		hFraction = hFraction - ((float)mCurrentCol * scaleRatio);
		vFraction = (((float)v)/((float)height));
		vFraction = vFraction - ((float)mCurrentRow * scaleRatio);
		
		
		if (mChangeListener != null) {
			mChangeListener.onScrolled(h, v, oldh, oldv, hFraction, vFraction);
		}
	}
	

    /**
     * Return the parceable instance to be saved
     */
    @Override
    protected Parcelable onSaveInstanceState() {
      final SavedState state = new SavedState(super.onSaveInstanceState());
      state.currentRow = mCurrentRow;
      state.currentColumn = mCurrentCol;
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
        mCurrentCol = savedState.currentColumn;
      }
    }
 
    // ========================= INNER CLASSES ==============================
 
 
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
    
	public BigPageAdapter getAdapter() {
		return mAdapter;
	}

    /*
	class AdapterDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			View v = getChildAt(mCurrentBufferIndex);
			if (v != null) {
				for (int index = 0; index < mAdapter.getCount(); index++) {
					if (v.equals(mAdapter.getItem(index))) {
						mCurrentAdapterIndex = index;
						break;
					}
				}
			}
			resetFocus();
		}

		@Override
		public void onInvalidated() {
			// Not yet implemented!
		}

	}

	private void logBuffer() {

		Log.d("viewflow", "Size of mLoadedViews: " + mLoadedViews.size() +
				", Size of mRecycledViews: " + mRecycledViews.size() +
				", X: " + mScroller.getCurrX() + ", Y: " + mScroller.getCurrY());
		Log.d("viewflow", "IndexInAdapter: " + mCurrentAdapterIndex
				+ ", IndexInBuffer: " + mCurrentBufferIndex);
	}
	*/
	
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
}
