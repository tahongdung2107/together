package com.example.dung.togetherfinal11.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Adapter.ComunityPageAdapter;
import com.example.dung.togetherfinal11.ChatActivity;
import com.example.dung.togetherfinal11.ChatGroupActivity;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.InviteActivity;
import com.example.dung.togetherfinal11.R;

/**
 * Created by dung on 18/11/2016.
 */

public class ComunityFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private int tabId = 0;
    SearchView searchView;
    FloatingActionButton floatbuidteam;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        floatbuidteam = (FloatingActionButton) x.findViewById(R.id.fabBuildTeam);
        viewPager.setAdapter(new ComunityPageAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        setEventFloat();
        return x;
    }
private void setEventFloat (){
    floatbuidteam.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Config.TeamID.equals("0")){
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
                startActivity(intent);
            }
        }
    });
}
          /*
        lay vi tri tablayout
         */
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                    floatingActionButton.show();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
}
