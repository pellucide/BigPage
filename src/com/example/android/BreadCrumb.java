package com.example.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;


/**
 * Button with click-animation effect.
 */
class BreadCrumb extends Button implements OnClickListener {
    private static final String LOG_TAG = "BreadCrumb";
    int CLICK_FEEDBACK_COLOR;
    int BORDER_COLOR;
    int mColorFilter = 0xFFFF00FF;
    static final int CLICK_FEEDBACK_INTERVAL = 10;
    static final int CLICK_FEEDBACK_DURATION = 350;
    Drawable rightArrow;
    Path mPath;
    
    float mTextX;
    float mTextY;
    long mAnimStart;
    OnClickListener mListener;
    Paint mFeedbackPaint;
    static Paint mBorderPaint;
    
    public BreadCrumb(Context context) {
        this(context, null);
    }

    public BreadCrumb(Context context, AttributeSet attrs) {
        this(context, attrs,R.style.button_style);
    }

    public BreadCrumb(Context context, AttributeSet attrs, int defStyle) {
    	super(context,attrs, defStyle);
    	init();
    }

    public void onClick(View view) {
        if (mListener != null)
            mListener.onClick(this);
        else
        	Toast.makeText(getContext(), getText(), Toast.LENGTH_LONG).show();	
        
    }

    private void init() {
        Resources res = getResources();
        this.setMinimumWidth(50);

        CLICK_FEEDBACK_COLOR = res.getColor(R.color.green);
        BORDER_COLOR = res.getColor(R.color.magic_flame);
        mFeedbackPaint = new Paint();
        mFeedbackPaint.setStyle(Style.FILL_AND_STROKE);
        mFeedbackPaint.setAlpha(0x25);
        mFeedbackPaint.setStrokeWidth(4);
        getPaint().setColor(res.getColor(R.color.milk));
        
        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setStrokeWidth(1);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(BORDER_COLOR);

        //setDrawingCacheEnabled(true);
        //Bitmap bitmap = getDrawingCache(true);
        //bitmap = Bitmap.createBitmap(bitmap);
        //setDrawingCacheEnabled(false);
        setOnClickListener(this);
        mPath = null;
        this.setClickable(true);
        mAnimStart = -1;
        this.setBackgroundResource(R.drawable.breadcrumb_arrow2);
    }


    @Override 
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        measureText();
    }

    private void measureText() {
        Paint paint = getPaint();
        mTextX = (getWidth() - paint.measureText(getText().toString())) / 2;
        mTextY = (getHeight() - paint.ascent() - paint.descent()) / 2;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        measureText();
        mPath=null;
    }

    private void drawMagicFlame(int duration, Canvas canvas) {
    	int w = getWidth();
    	int h = getHeight();
        int alpha = 255 - 255 * duration / CLICK_FEEDBACK_DURATION;
        int color = CLICK_FEEDBACK_COLOR | (alpha << 24);

        mFeedbackPaint.setColor(color);
        this.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00003B));
        //canvas.drawRect(1, 1, w-1, h-1, mFeedbackPaint);
        
        if (mPath == null) {
        	mPath = new Path();
            mPath.moveTo(-10, 1);
            mPath.lineTo(w-15, 1);
            mPath.lineTo(w-1, h/2);
            mPath.lineTo(w-15, h-1);
            mPath.lineTo(1, h-1);
            mPath.lineTo(1,1);
        }
        canvas.drawPath(mPath, mFeedbackPaint);
    }


    @Override
    public void onDraw(Canvas canvas) {
    	int w = getWidth();
    	int h = getHeight();
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        
        if (mAnimStart != -1) {
            int animDuration = (int) (System.currentTimeMillis() - mAnimStart);
            
            if (animDuration >= CLICK_FEEDBACK_DURATION) {
                mAnimStart = -1;
            } else {
                drawMagicFlame(animDuration, canvas);
                postInvalidateDelayed(CLICK_FEEDBACK_INTERVAL);
                this.getBackground().clearColorFilter();
            }
        } else if (isPressed()) {
            drawMagicFlame(0, canvas);
        }
        
        CharSequence text = getText();
        canvas.drawText(text, 0, text.length(), mTextX, mTextY, getPaint());
 
        /* This will change color based on the color filter */
        //this.getBackground().setColorFilter(mColorFilter, android.graphics.PorterDuff.Mode.DARKEN);
    }

    public void animateClickFeedback() {
        mAnimStart = System.currentTimeMillis();
        invalidate();        
    } 
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                animateClickFeedback();
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_CANCEL:
                invalidate();
                break;
        }

        return result;
    }
}