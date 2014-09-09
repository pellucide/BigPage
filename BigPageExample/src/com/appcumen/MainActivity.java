package com.appcumen;

import org.joda.time.DateTime;

import com.appcumen.DragableSpace1;
import com.appcumen.DragableSpace1.DirectionMode;
import com.appcumen.adapters.BigPageAdapter;
import com.appcumen.listeners.PageChangeListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements PageChangeListener {
    private static final String TAG = "BigPageExample";
    private BigPageAdapter mMyPagerAdapter;  
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        Log.d(TAG, "onCreate()");
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout bigpage=(LinearLayout)findViewById(R.id.bigpage);
        DragableSpace1 space = (DragableSpace1) findViewById(R.id.space);
        space.setChangeListener(this);
        DateTime dt = new DateTime();
        Log.d(TAG, "month=" +dt.getMonthOfYear()+", year="+dt.getYear());
        mMyPagerAdapter = new MyPagerAdapter(getApplicationContext());  
        space.setAdapter(mMyPagerAdapter,33,33);  
        //space.setDirectionMode(DirectionMode.HORIZONTAL_DIRECTION_MODE);
        //space.setDirectionMode(DirectionMode.VERTICAL_DIRECTION_MODE);
        //space.setDirectionMode(DirectionMode.FREE_FLOW_DIRECTION_MODE);
        space.setDirectionMode(DirectionMode.LOCK_FLOW_DIRECTION_MODE);
    }


	

	final class MyPagerAdapter extends com.appcumen.adapters.BigPageAdapter implements OnPreDrawListener {

		/* (non-Javadoc)
		 * @see com.appcumen.adapters.BigPageAdapter#getOverlapY(android.view.View, int, int)
		 */
		@Override
		public int getOverlapWithNextY(View v, int posx, int posy) {
			return 0;
		}

		/* (non-Javadoc)
		 * @see com.appcumen.adapters.BigPageAdapter#getOverlapY(android.view.View, int, int)
		 */
		@Override
		public int getOverlapWithPrevY(View v, int posx, int posy) {
			return 0;
		}

		public static final int NUMBER_OF_PAGES = 77;
		private ViewTreeObserver vto;
		/**
		 * 
		 */
		public final String TAG = "MyPagerAdapter";
		SparseArray<SparseArray<View>> views = new SparseArray<SparseArray<View>>();
		Context context;

		public MyPagerAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public Object getPageTitle(int xIndex, int yIndex) {
			return null;
		}

		@Override
		public View instantiateItem(int posx, int posy, ViewGroup container) {
			if (vto == null  || vto.isAlive() == false) {
				vto = container.getViewTreeObserver();
				//vto.addOnPreDrawListener(this);
			}
			
			Log.d(TAG, "instantiateItem(posx=" + posx + ",posy=" + posy + ")");
			SparseArray<View> itemsRow;
			if (posy >= views.size() || (views.get(posy) == null)) {
				itemsRow = new SparseArray<View>();
				views.put(posy, itemsRow);
			} else
				itemsRow = views.get(posy);

			if (posx >= itemsRow.size() || itemsRow.get(posx) == null) {
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				TextView tv = new TextView(context);
				tv.setLayoutParams(lp);
				tv.setGravity(Gravity.CENTER);
				tv.setText(String.valueOf(posy) + "," + String.valueOf(posx));
				tv.setTextColor(0xFF999999);
				tv.setTextSize(28);
				tv.setBackgroundColor(0x44000000);
				tv.setBackgroundResource(R.drawable.blackwood1);
				// container.addView(tv,position, lp);
				// container.addView(tv, lp);
				itemsRow.put(posx, tv);
				return tv;
			} else {
				return itemsRow.get(posx);
			}
		}

		@Override
		public void destroyItem(View view, int posx, int posy,
				ViewGroup container) {
			// Thread.dumpStack();
			Log.d(TAG, "destroyItem(posy=" + posy + ",posx=" + posx + ")");
			// showCurrentViewList();
			container.removeView(view);
			views.get(posy).remove(posx);
		}

		private void showCurrentViewList() {
			for (int j = 0; j < views.size(); j++) {
				Log.i(TAG, "row:" + j + " size=" + views.get(j).size());
			}
		}

		@Override
		public int getRowCount() {
			// Log.d(TAG, "getRowCount");
			return NUMBER_OF_PAGES;
		}

		@Override
		public int getColCount() {
			// Log.d(TAG, "getColCount");
			return NUMBER_OF_PAGES;
		}

		@Override
		public boolean onPreDraw() {
			// TODO Auto-generated method stub
			for (int ii = 0; ii < views.size(); ii++) {
				if (views.get(ii) != null) {
					for (int jj = 0; jj < views.get(ii).size(); jj++) {
						if (views.get(ii).get(jj) != null)
							Log.i(TAG, "view:" + jj + ","+ii+ " bottom=" + views.get(ii).get(jj).getBottom());
					}
				}
			}
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onSwitched(int posx, int posy) {
		Log.d(TAG, "onSwitched(posx=" + posx + ",posy=" + posy + ")");
	}

	@Override
	public void onScrolled(int h, int v, int oldh, int oldv, float hfraction,
			float vfraction) {
		//Log.d(TAG, "onScrolled(h=" + h + ",v=" + v + ",oldh=" + oldh + ",oldv=" + oldv + ")");
	}

	@Override
	public void onScrollBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollEnd() {
		// TODO Auto-generated method stub

	}

}
