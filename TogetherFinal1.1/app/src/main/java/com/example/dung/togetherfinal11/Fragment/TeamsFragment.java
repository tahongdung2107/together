package com.example.dung.togetherfinal11.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.togetherfinal11.Adapter.TeamAdapter;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.ITeamListener;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 17/11/2016.
 */

public class TeamsFragment extends Fragment {
    View view;
    RecyclerView recyclerViewTeam;
    private SwipeRefreshLayout swipeRefreshTeam;
    int number = 0;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount, myLastVisiblePos;
    Realm realm;
    ArrayList<TeamEntity> arrayListTeam;
    TeamAdapter teamAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.team_layout, container, false);
        recyclerViewTeam = (RecyclerView) view.findViewById(R.id.gridviewTeams);
        swipeRefreshTeam = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshTeam);
        getTeambypage();
        refreshTeam();
        setEventRecylerview();
        return view;
    }
private void getTeambypage(){
    realm = RealmController.getInstance().getRealm();
    RealmController.getInstance().refresh();
    RealmResults<TeamEntity> realmResults = RealmController.getInstance().getTeams();
    arrayListTeam = new ArrayList<>();
    teamAdapter = new TeamAdapter(getActivity(),arrayListTeam);
    mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    recyclerViewTeam.setLayoutManager(mLayoutManager);
    recyclerViewTeam.setAdapter(teamAdapter);
    if (realmResults.size() == 0){
        initData(number);

    }else {
        arrayListTeam.addAll(realmResults);
        teamAdapter.notifyDataSetChanged();
    }
}
    private void setEventRecylerview() {
        recyclerViewTeam.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                final int lastItem = mFirstVisibleItem + mVisibleItemCount;
                if (lastItem == mTotalItemCount && scrollState == 0) {
                    Log.d("individualNume", number + "!");
                    number++;
                    //load more
                    initData(number);
                    //get next 10-20 items(your choice)items
                }
            }
        });
    }
    private void initData(int number) {
        String url = ConfigsApi.GET_Teams_BY_PAGE;
        if (number == 0) {
            ModelManager.getInstance(getActivity()).getTeamsbypage(url, "" + number, "7", new ITeamListener() {
                @Override
                public void onSuccess(ArrayList<TeamEntity> arrTeam) {
                    swipeRefreshTeam.setRefreshing(false);
                    if (arrTeam.size()>0){
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                        arrayListTeam.clear();
                        arrayListTeam.addAll(arrTeam);
                        for (TeamEntity teamEntity : arrTeam) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(teamEntity);
                            realm.commitTransaction();
                        }
                        teamAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError() {

                }
            });
        }else {
            ModelManager.getInstance(getActivity()).getTeamsbypage(url, "" + number, "7", new ITeamListener() {
                @Override
                public void onSuccess(ArrayList<TeamEntity> arrTeam) {
                    arrayListTeam.addAll(arrTeam);
                    for (TeamEntity teamEntity : arrTeam) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(teamEntity);
                        realm.commitTransaction();
                    }

                    teamAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
    private void refreshTeam() {
        swipeRefreshTeam.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshTeam.setPadding(0, 0, 0, 10);
                number = 0;
                Log.d("IndividuaFragment", "Refresh list");
                initData(number);
            }
        });
    }
}