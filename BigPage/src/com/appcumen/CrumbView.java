package com.appcumen;

import com.appcumen.BreadCrumb;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

public class CrumbView extends LinearLayout {
	private static final int MAX_CRUMBS=9;
	private int mNumCrumbs=0;

	public CrumbView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}
	public CrumbView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}
	
	public void init() {
		this.setOrientation(HORIZONTAL);
		this.setBackgroundResource(R.drawable.breadcrumb_background1);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.setClipChildren(false);
		this.setClipToPadding(false);
		this.setMinimumHeight(90);
		this.setPadding(0, 0,0,0);
		this.setSaveEnabled(true);
		this.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		this.setVerticalFadingEdgeEnabled(false);
		this.setHorizontalFadingEdgeEnabled(false);
		this.setGravity(Gravity.LEFT);
		this.setHorizontalGravity(Gravity.LEFT);
	}
	public boolean addCrumb(BreadCrumb bc) {
	    final int totalCrumbs = this.getChildCount();
	    if (totalCrumbs >= MAX_CRUMBS)
	    	return false;
	    addView(bc,totalCrumbs);
	    return true;
	}
	

}
