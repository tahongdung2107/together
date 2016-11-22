package com.example.dung.togetherfinal11.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Adapter.IndivusualAdapter;
import com.example.dung.togetherfinal11.Chat.MyService;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.IUserListener;
import com.example.dung.togetherfinal11.Model.Sreach_text;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 17/11/2016.
 */

public class IndivisualsFragment extends Fragment {
    TextView test;
    String UserId = "UserId";
    String DATA = "data";
    String USER = "user";
    String PROFILE = "profile";
    String ID = "id";
    String NAMEREALM = "Indivisual";
    String CID = "CID";
    String USERNAME = "userName";
    String PASSWORD = "passWord";
    RecyclerView grdUser;
    String profile1;
    String userName;
    String passWord;
    String content;
    String userId;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount, myLastVisiblePos;
    EventBus bus = EventBus.getDefault();
    IndivusualAdapter adapterIndivisual;
    ProgressBar progressBar;
    ArrayList<UserEntity> lstUser;
    private SwipeRefreshLayout swipeRefreshIndividual;
    private RecyclerView.LayoutManager mLayoutManager;
    View view;
    int numberPage = 0;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.indivisual_layout, container, false);
        grdUser = (RecyclerView) view.findViewById(R.id.gridviewindivisual);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        swipeRefreshIndividual = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().startService(intent);
        refreshIndividual();
        GetUsersByPage();
        setEventGridView();
        return view;
    }

    private void GetUsersByPage() {
        realm = RealmController.getInstance().getRealm();
        RealmController.getInstance().refresh();
        RealmResults<UserEntity> realmResults = RealmController.getInstance().getIndividuals();
        lstUser = new ArrayList<>();
        adapterIndivisual = new IndivusualAdapter(getActivity(), lstUser);
        mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        grdUser.setLayoutManager(mLayoutManager);
        grdUser.setAdapter(adapterIndivisual);
        if (realmResults.size() == 0) {
            Log.d("Individual", "size=0");
            initData(numberPage);
        } else {
            lstUser.addAll(realmResults);
            adapterIndivisual.notifyDataSetChanged();
        }
    }

    private void initData(int numberPage) {
        String url = ConfigsApi.GET_USERS_BY_PAGE;
        Log.d("IndividualFragment", "initData arrList: " + url);
        if (numberPage == 0) {
            ModelManager.getInstance(getContext()).getUserbypage(url, numberPage + "", "16", new IUserListener() {
                @Override
                public void onSuccess(ArrayList<UserEntity> arrUser) {
                    Log.d("IndividualFragment", "initData arrList: " + arrUser.size());
                    swipeRefreshIndividual.setRefreshing(false);
                    if (arrUser.size() > 0) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                        lstUser.clear();
                        lstUser.addAll(arrUser);
                        for (UserEntity userEntity : arrUser) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(userEntity);
                            realm.commitTransaction();
                        }
                        adapterIndivisual.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError() {
                    swipeRefreshIndividual.setRefreshing(false);
                }
            });
        } else {
            ModelManager.getInstance(getContext()).getUserbypage(url, numberPage + "", "16", new IUserListener() {
                @Override
                public void onSuccess(ArrayList<UserEntity> arrUser) {
                    lstUser.addAll(arrUser);
                    for (UserEntity userEntity : arrUser) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(userEntity);
                        realm.commitTransaction();
                    }

                    adapterIndivisual.notifyDataSetChanged();
                }

                @Override
                public void onError() {
                    swipeRefreshIndividual.setRefreshing(false);
                }
            });
        }
    }

    private void setEventGridView() {
        grdUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                final int lastItem = mFirstVisibleItem + mVisibleItemCount;
                if (lastItem == mTotalItemCount && scrollState == 0) {
                    Log.d("individualNume", numberPage + "!");
                    numberPage++;
                    //load more
                    initData(numberPage);
                    //get next 10-20 items(your choice)items
                }
            }
        });
    }

    private void refreshIndividual() {
        swipeRefreshIndividual.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshIndividual.setPadding(0, 0, 0, 10);
                numberPage = 0;
                Log.d("IndividuaFragment", "Refresh list");
                initData(numberPage);
            }
        });
    }

    @Subscribe
    public void Filter(Sreach_text sreach_text) {
        Log.d("Search :", "Result :" + sreach_text);
        lstUser.clear();
        String sreach = sreach_text.getSreach();
//        RealmResults<UserEntity> realmSreach = realm.where(UserEntity.class).contains("Name", sreach).findAll();
        RealmResults<UserEntity> realmSreach = RealmController.getInstance().getName(sreach);
        lstUser.addAll(realmSreach);
        adapterIndivisual.notifyDataSetChanged();
        Log.d("TextLOGFILTER", realmSreach.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }
}
