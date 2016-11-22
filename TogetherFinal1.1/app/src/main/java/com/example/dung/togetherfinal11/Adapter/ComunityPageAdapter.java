package com.example.dung.togetherfinal11.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.SearchView;

import com.example.dung.togetherfinal11.Fragment.IndivisualsFragment;
import com.example.dung.togetherfinal11.Fragment.MessageFragment;
import com.example.dung.togetherfinal11.Fragment.TeamsFragment;
import com.example.dung.togetherfinal11.R;

import butterknife.InjectView;

/**
 * Created by dung on 18/11/2016.
 */

public class ComunityPageAdapter extends FragmentPagerAdapter {
    int int_items = 3;

    public ComunityPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IndivisualsFragment();
            case 1:
                return new TeamsFragment();
            case 2:
                return new MessageFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Indivisual";
            case 1:
                return "Teams";
            case 2:
                return "Message";
        }
        return null;
    }
}
