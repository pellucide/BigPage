package com.appcumen.listeners;

import android.view.View;

public interface PageChangeListener {
		/**
		 * This method is called when a new View has been scrolled to.
		 * 
		 * @param view
		 *            the {@link View} currently in focus.
		 * @param position
		 *            The position in the adapter of the {@link View} currently in focus.
		 */
		void onSwitched(int posx, int posy);

		/**
		 * The scroll position has been changed. A FlowIndicator may implement this
		 * method to reflect the current position
		 * 
		 * @param h
		 * @param v
		 * @param oldh
		 * @param oldv
		 */
		public void onScrolled(int h, int v, int oldh, int oldv, float hFraction, float  vFraction);
		public void onScrollBegin();
		public void onScrollEnd();

}
