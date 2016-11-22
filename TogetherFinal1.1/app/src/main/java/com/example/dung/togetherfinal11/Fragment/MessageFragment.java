package com.example.dung.togetherfinal11.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.togetherfinal11.R;

/**
 * Created by dung on 17/11/2016.
 */

public class MessageFragment extends Fragment {
    View view;
    RecyclerView recyclerViewTeam;
    private SwipeRefreshLayout swipeRefreshTeam;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.message_layout, container, false);
        return  view;
    }
}
