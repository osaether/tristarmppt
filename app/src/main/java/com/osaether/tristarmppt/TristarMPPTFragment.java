/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.osaether.tristarmppt;

import com.osaether.common.view.SlidingTabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A basic sample which shows how to use {@link com.osaether.common.view.SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class TristarMPPTFragment extends Fragment {

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private String chargeState[] = {"START", "NIGHT_CHECK", "DISCONNECT", "NIGHT", "FAULT","MPPT", "ABSORPTION", "FLOAT", "EQUALIZE", "SLAVE"};
    private TristarData m_tristarData = null;
    private SlidingTabLayout mSlidingTabLayout;
    private int tabCount = 4;
    private int tabIds[] = {R.layout.pager_item_battery, R.layout.pager_item_array, R.layout.pager_item_temperatures, R.layout.pager_item_counters};
    private String tabTitles[] = {"Battery", "Array", "Temperatures", "Counters"};

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    public void setTristarData(TristarData tsd) {
        m_tristarData = tsd;
        UpdateTristarView(mViewPager.getCurrentItem());
    }

    private void UpdateTristarView(int position) {
        if (m_tristarData != null)
        {
            switch (position)
            {
                case 0:
                    TextView txtBox = (TextView) mViewPager.findViewById(R.id.vbat);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_vbat));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.vtarget);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_vtarget));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.icharge);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.1f A", m_tristarData.m_icharge));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.scharge);
                    if (txtBox != null) {
                        txtBox.setText(chargeState[m_tristarData.m_chargestate]);
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.pout);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f W", m_tristarData.m_pout));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.vbatmin);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_vmin));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.vbatmax);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_vmax));
                    }
                    break;
                case 1:
                    txtBox = (TextView) mViewPager.findViewById(R.id.varray);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_varray));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.iarray);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.1f A", m_tristarData.m_iarray));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.vmp);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_vmp));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.voc);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_voc));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.pmax);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f W", m_tristarData.m_spmax));
                    }
                    break;
                case 2:
                    txtBox = (TextView) mViewPager.findViewById(R.id.tbat);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format("%d ºF", m_tristarData.m_tbat));
                        }
                        else
                        {
                            txtBox.setText(String.format("%d ºC", m_tristarData.m_tbat));
                        }
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.thsink);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format("%d ºF", m_tristarData.m_thsink));
                        }
                        else
                        {
                            txtBox.setText(String.format("%d ºC", m_tristarData.m_thsink));
                        }
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.tbatmaxdaily);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format("%d ºF", m_tristarData.m_tbatmax));
                        }
                        else
                        {
                            txtBox.setText(String.format("%d ºC", m_tristarData.m_tbatmax));
                        }
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.tbatmindaily);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format("%d ºF", m_tristarData.m_tbatmin));
                        }
                        else
                        {
                            txtBox.setText(String.format("%d ºC", m_tristarData.m_tbatmin));
                        }
                    }
                    break;
                case 3:
                    txtBox = (TextView) mViewPager.findViewById(R.id.ahres);
                    if (txtBox != null) {
                        if (m_tristarData.m_ahres < 1000.0f)
                            txtBox.setText(String.format("%.1f Ah", m_tristarData.m_ahres));
                        else
                            txtBox.setText(String.format("%.0f Ah", m_tristarData.m_ahres));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.kwhres);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f kWh", m_tristarData.m_kwhres));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.ahdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.1f Ah", m_tristarData.m_ahdaily));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.whdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f Wh", m_tristarData.m_whdaily));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.ahtot);
                    if (txtBox != null) {
                        if (m_tristarData.m_ahres < 1000.0f)
                            txtBox.setText(String.format("%.1f Ah", m_tristarData.m_ahtot));
                        else
                            txtBox.setText(String.format("%.0f Ah", m_tristarData.m_ahtot));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.kwhtot);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f kWh", m_tristarData.m_kwhtot));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.pmaxdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.0f W", m_tristarData.m_pmaxdaily));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.maxpvvdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%.2f V", m_tristarData.m_pvvmaxdaily));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.hourmeter);
                    if (txtBox != null) {
                        long y = m_tristarData.m_hours/(24*365);
                        long d = (m_tristarData.m_hours - y*(24*365))/24;
                        long h = m_tristarData.m_hours - y*365*24 - d*24;
                        txtBox.setText(String.format("%dy%dd%dh", (int)y, (int)d, (int)h));
                    }
                    txtBox = (TextView) mViewPager.findViewById(R.id.timeeqdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%d min", m_tristarData.m_timeequalize/60));
                    }

                    txtBox = (TextView) mViewPager.findViewById(R.id.timeabsorbdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%d min", m_tristarData.m_timeabsorption/60));
                    }

                    txtBox = (TextView) mViewPager.findViewById(R.id.timefloatdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format("%d min", m_tristarData.m_timefloat/60));
                    }

                    break;
            }
        }
    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 4;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View  view = getActivity().getLayoutInflater().inflate(tabIds[position], container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            UpdateTristarView(position);
        }

    }
}
