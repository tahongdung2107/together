package com.example.dung.togetherfinal11.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.togetherfinal11.R;

/**
 * Created by dung on 22/11/2016.
 */

public class ChatFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }
}
