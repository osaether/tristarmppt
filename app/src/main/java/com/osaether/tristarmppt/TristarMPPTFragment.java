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
import android.support.annotation.NonNull;
import java.util.Locale;

public class TristarMPPTFragment extends Fragment {

    private String chargeState[] = {"START", "NIGHT_CHECK", "DISCONNECT", "NIGHT", "FAULT","MPPT", "ABSORPTION", "FLOAT", "EQUALIZE", "SLAVE"};
    private TristarData m_tristarData = null;
    private int tabIds[] = {R.layout.pager_item_battery, R.layout.pager_item_array, R.layout.pager_item_temperatures, R.layout.pager_item_counters};
    private String tabTitles[] = {"Battery", "Array", "Temperatures", "Counters"};

    private ViewPager mViewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        SlidingTabLayout slidingTabLayout = view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(mViewPager);
    }

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
                    TextView txtBox = mViewPager.findViewById(R.id.vbat);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(),"%.2f V", m_tristarData.m_vbat));
                    }
                    txtBox = mViewPager.findViewById(R.id.vtarget);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_vtarget));
                    }
                    txtBox = mViewPager.findViewById(R.id.icharge);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.1f A", m_tristarData.m_icharge));
                    }
                    txtBox = mViewPager.findViewById(R.id.scharge);
                    if (txtBox != null) {
                        txtBox.setText(chargeState[m_tristarData.m_chargestate]);
                    }
                    txtBox = mViewPager.findViewById(R.id.pout);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f W", m_tristarData.m_pout));
                    }
                    txtBox = mViewPager.findViewById(R.id.vbatmin);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_vmin));
                    }
                    txtBox = mViewPager.findViewById(R.id.vbatmax);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_vmax));
                    }
                    break;
                case 1:
                    txtBox = mViewPager.findViewById(R.id.varray);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_varray));
                    }
                    txtBox = mViewPager.findViewById(R.id.iarray);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.1f A", m_tristarData.m_iarray));
                    }
                    txtBox = mViewPager.findViewById(R.id.vmp);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_vmp));
                    }
                    txtBox = mViewPager.findViewById(R.id.voc);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_voc));
                    }
                    txtBox = mViewPager.findViewById(R.id.pmax);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f W", m_tristarData.m_spmax));
                    }
                    break;
                case 2:
                    txtBox = mViewPager.findViewById(R.id.tbat);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºF", m_tristarData.m_tbat));
                        }
                        else
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºC", m_tristarData.m_tbat));
                        }
                    }
                    txtBox =  mViewPager.findViewById(R.id.thsink);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºF", m_tristarData.m_thsink));
                        }
                        else
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºC", m_tristarData.m_thsink));
                        }
                    }
                    txtBox = mViewPager.findViewById(R.id.tbatmaxdaily);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºF", m_tristarData.m_tbatmax));
                        }
                        else
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºC", m_tristarData.m_tbatmax));
                        }
                    }
                    txtBox = mViewPager.findViewById(R.id.tbatmindaily);
                    if (txtBox != null) {
                        if (m_tristarData.m_fahrenheit)
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºF", m_tristarData.m_tbatmin));
                        }
                        else
                        {
                            txtBox.setText(String.format(Locale.getDefault(), "%d ºC", m_tristarData.m_tbatmin));
                        }
                    }
                    break;
                case 3:
                    txtBox = mViewPager.findViewById(R.id.ahres);
                    if (txtBox != null) {
                        if (m_tristarData.m_ahres < 1000.0f)
                            txtBox.setText(String.format(Locale.getDefault(), "%.1f Ah", m_tristarData.m_ahres));
                        else
                            txtBox.setText(String.format(Locale.getDefault(), "%.0f Ah", m_tristarData.m_ahres));
                    }
                    txtBox = mViewPager.findViewById(R.id.kwhres);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f kWh", m_tristarData.m_kwhres));
                    }
                    txtBox = mViewPager.findViewById(R.id.ahdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.1f Ah", m_tristarData.m_ahdaily));
                    }
                    txtBox = mViewPager.findViewById(R.id.whdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f Wh", m_tristarData.m_whdaily));
                    }
                    txtBox = mViewPager.findViewById(R.id.ahtot);
                    if (txtBox != null) {
                        if (m_tristarData.m_ahres < 1000.0f)
                            txtBox.setText(String.format(Locale.getDefault(), "%.1f Ah", m_tristarData.m_ahtot));
                        else
                            txtBox.setText(String.format(Locale.getDefault(), "%.0f Ah", m_tristarData.m_ahtot));
                    }
                    txtBox = mViewPager.findViewById(R.id.kwhtot);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f kWh", m_tristarData.m_kwhtot));
                    }
                    txtBox = mViewPager.findViewById(R.id.pmaxdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.0f W", m_tristarData.m_pmaxdaily));
                    }
                    txtBox = mViewPager.findViewById(R.id.maxpvvdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%.2f V", m_tristarData.m_pvvmaxdaily));
                    }
                    txtBox = mViewPager.findViewById(R.id.hourmeter);
                    if (txtBox != null) {
                        long y = m_tristarData.m_hours/(24*365);
                        long d = (m_tristarData.m_hours - y*(24*365))/24;
                        long h = m_tristarData.m_hours - y*365*24 - d*24;
                        txtBox.setText(String.format(Locale.getDefault(), "%dy%dd%dh", (int)y, (int)d, (int)h));
                    }
                    txtBox = mViewPager.findViewById(R.id.timeeqdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%d min", m_tristarData.m_timeequalize/60));
                    }

                    txtBox = mViewPager.findViewById(R.id.timeabsorbdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%d min", m_tristarData.m_timeabsorption/60));
                    }

                    txtBox = mViewPager.findViewById(R.id.timefloatdaily);
                    if (txtBox != null) {
                        txtBox.setText(String.format(Locale.getDefault(), "%d min", m_tristarData.m_timefloat/60));
                    }
                    break;
            }
        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public @NonNull Object instantiateItem(@NonNull ViewGroup container, int position) {

            View  view = getActivity().getLayoutInflater().inflate(tabIds[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            UpdateTristarView(position);
        }
    }
}
