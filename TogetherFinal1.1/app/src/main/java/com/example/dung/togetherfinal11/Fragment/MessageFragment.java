package com.example.dung.togetherfinal11.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.togetherfinal11.Adapter.MessagerAdapter;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 17/11/2016.
 */

public class MessageFragment extends Fragment {
    View view;
    RecyclerView recyclerViewMessager;
    Realm realm;
    String user_toid,user_fromid;
    private List<Object> mData = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshMessager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.messager_layout, container, false);
        recyclerViewMessager = (RecyclerView) view.findViewById(R.id.gridviewMessager);
        setData();
        return  view;
    }
    private void setData(){
        realm = RealmController.getInstance().getRealm();
        RealmController.getInstance().refresh();
//        RealmResults<Messages> realmResults = RealmController.getInstance().getMessageuserTo(Config.USER_ID);
        RealmResults<Messages> realmResultsText = RealmController.getInstance().getMessageuserType();
        RealmResults<Messages> realmResultsInvite = RealmController.getInstance().getMessageuserInvite();
        RealmResults<Messages> realmResultsmessage = RealmController.getInstance().getMessages();

        RealmResults<TeamEntity> resultsTeam = RealmController.getInstance().getTeams();
        RealmResults<ChatModel> realmResultsmodel = RealmController.getInstance().getModel();
        Log.d("LoadDataMessager","Result :" + realmResultsmessage);

        MessagerAdapter adapter = new MessagerAdapter(getActivity(), mData);
        mData.clear();
        //
        for (int i = 0; i < realmResultsmessage.size(); i++) {
            user_toid = realmResultsmessage.get(i).getToID();
            user_fromid = realmResultsmessage.get(i).getFromID();
        }
            if (user_toid.equals(Config.USER_ID)){
                mData.addAll(realmResultsText);
                recyclerViewMessager.setAdapter(adapter);
                recyclerViewMessager.setLayoutManager(new LinearLayoutManager(getContext()));
            }else if (user_fromid.equals(Config.USER_ID)){
                mData.addAll(realmResultsText);
                mData.addAll(realmResultsInvite);
                recyclerViewMessager.setAdapter(adapter);
                recyclerViewMessager.setLayoutManager(new LinearLayoutManager(getContext()));
            }
    }
}
