/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appcumen.adapters;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base class providing the adapter to populate pages inside of
 * a {@link ViewPager}.  You will most likely want to use a more
 * specific implementation of this, such as
 * {@link android.support.v4.app.FragmentPagerAdapter} or
 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
 *
 * <p>When you implement a PagerAdapter, you must override the following methods
 * at minimum:</p>
 * <ul>
 * <li>{@link #instantiateItem(ViewGroup, int)}</li>
 * <li>{@link #destroyItem(ViewGroup, int, Object)}</li>
 * <li>{@link #getCount()}</li>
 * <li>{@link #isViewFromObject(View, Object)}</li>
 * </ul>
 *
 * <p>PagerAdapter is more general than the adapters used for
 * {@link android.widget.AdapterView AdapterViews}. Instead of providing a
 * View recycling mechanism directly ViewPager uses callbacks to indicate the
 * steps taken during an update. A PagerAdapter may implement a form of View
 * recycling if desired or use a more sophisticated method of managing page
 * Views such as Fragment transactions where each page is represented by its
 * own Fragment.</p>
 *
 * <p>ViewPager associates each page with a key Object instead of working with
 * Views directly. This key is used to track and uniquely identify a given page
 * independent of its position in the adapter. A call to the PagerAdapter method
 * {@link #startUpdate(ViewGroup)} indicates that the contents of the ViewPager
 * are about to change. One or more calls to {@link #instantiateItem(ViewGroup, int)}
 * and/or {@link #destroyItem(ViewGroup, int, Object)} will follow, and the end
 * of an update will be signaled by a call to {@link #finishUpdate(ViewGroup)}.
 * By the time {@link #finishUpdate(ViewGroup) finishUpdate} returns the views
 * associated with the key objects returned by
 * {@link #instantiateItem(ViewGroup, int) instantiateItem} should be added to
 * the parent ViewGroup passed to these methods and the views associated with
 * the keys passed to {@link #destroyItem(ViewGroup, int, Object) destroyItem}
 * should be removed. The method {@link #isViewFromObject(View, Object)} identifies
 * whether a page View is associated with a given key object.</p>
 *
 * <p>A very simple PagerAdapter may choose to use the page Views themselves
 * as key objects, returning them from {@link #instantiateItem(ViewGroup, int)}
 * after creation and adding them to the parent ViewGroup. A matching
 * {@link #destroyItem(ViewGroup, int, Object)} implementation would remove the
 * View from the parent ViewGroup and {@link #isViewFromObject(View, Object)}
 * could be implemented as <code>return view == object;</code>.</p>
 *
 * <p>PagerAdapter supports data set changes. Data set changes must occur on the
 * main thread and must end with a call to {@link #notifyDataSetChanged()} similar
 * to AdapterView adapters derived from {@link android.widget.BaseAdapter}. A data
 * set change may involve pages being added, removed, or changing position. The
 * ViewPager will keep the current page active provided the adapter implements
 * the method {@link #getItemPosition(Object)}.</p>
 */
public abstract class BigPageAdapter {
    /**
     * Return the number of rows available.
     */
    public abstract int getRowCount();

    /**
     * Return the number of rows available.
     */
    public abstract int getColCount();

 

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param posx The page position to be instantiated.
     * @param posy The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    public abstract View instantiateItem(int posx, int posy, ViewGroup container);

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
    public abstract void destroyItem(View view, int xIndex, int yIndex, ViewGroup container);



    /**
     * Save any instance state associated with this adapter and its pages that should be
     * restored if the current UI state needs to be reconstructed.
     *
     * @return Saved state for this adapter
     */
    public Parcelable saveState() {
        return null;
    }

    /**
     * Restore any instance state associated with this adapter and its pages
     * that was previously saved by {@link #saveState()}.
     *
     * @param state State previously saved by a call to {@link #saveState()}
     * @param loader A ClassLoader that should be used to instantiate any restored objects
     */
    public void restoreState(Parcelable state, ClassLoader loader) {
    }



    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param xIndex The x-axis position of the title requested
     * @param yIndex The y-axis position of the title requested
     * @return A Object for the requested page. The overridden method will have to define the exact class
     */
    public abstract Object getPageTitle(int xIndex, int yIndex) ;
    
	public int getOverlapWithNextX(View v, int posx, int posy) {
		return 0;
	}
	public int getOverlapWithNextY(View v, int posx, int posy) {
		return 0;
	}
	public int getOverlapWithPrevX(View v, int posx, int posy) {
		return 0;
	}
	public int getOverlapWithPrevY(View v, int posx, int posy) {
		return 0;
	}
}

