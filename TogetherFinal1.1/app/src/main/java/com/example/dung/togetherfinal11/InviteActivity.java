package com.example.dung.togetherfinal11;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Adapter.IndivusualAdapter;
import com.example.dung.togetherfinal11.Adapter.InviteAdapter;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.IUserListener;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 23/11/2016.
 */

public class InviteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserEntity> lstUser;
    Toolbar toolbar;
    InviteAdapter inviteAdapter;
    private SwipeRefreshLayout swipeRefreshIndividual;
    Realm realm;
    private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount, myLastVisiblePos;
    private RecyclerView.LayoutManager mLayoutManager;
    int number = 0;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inviteteam_layout);
        recyclerView = (RecyclerView) findViewById(R.id.gridviewInvite);
        swipeRefreshIndividual = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshInvite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getUserInvite();
        setEventRecycler();

        refreshInvite();
    }

    private void getUserInvite() {
        realm = RealmController.getInstance().getRealm();
        RealmController.getInstance().refresh();
        RealmResults<UserEntity> realmResults = RealmController.getInstance().getuserInvite("0", Config.USER_ID);
        lstUser = new ArrayList<>();
        inviteAdapter = new InviteAdapter(getApplication(), lstUser);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(inviteAdapter);
        if (realmResults.size() == 0) {
            initData(number);

        } else {
            lstUser.addAll(realmResults);
            inviteAdapter.notifyDataSetChanged();
        }

    }

    private void setEventRecycler() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                final int lastItem = mFirstVisibleItem + mVisibleItemCount;
                if (lastItem == mTotalItemCount && scrollState == 0) {
                    number++;
                    initData(number);
                }
            }
        });
    }

    private void initData(int numberPage) {
        String url = ConfigsApi.GET_USERS_BY_PAGE;
        Log.d("IndividualFragment", "initData arrList: " + url);
        if (numberPage == 0) {
            ModelManager.getInstance(getApplication()).getUserbypage(url, numberPage + "", "20", new IUserListener() {
                @Override
                public void onSuccess(ArrayList<UserEntity> arrUser) {
                    Log.d("IndividualFragment", "initData arrList: " + arrUser.size());
                    swipeRefreshIndividual.setRefreshing(false);
                    if (arrUser.size() > 0) {
                        RealmController.getInstance().clearAll(UserEntity.class.getSimpleName());
                        lstUser.clear();
                        lstUser.addAll(arrUser);
                        for (UserEntity userEntity : arrUser) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(userEntity);
                            realm.commitTransaction();
                        }
                        inviteAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError() {
                    swipeRefreshIndividual.setRefreshing(false);
                }
            });
        } else {
            ModelManager.getInstance(getApplication()).getUserbypage(url, numberPage + "", "20", new IUserListener() {
                @Override
                public void onSuccess(ArrayList<UserEntity> arrUser) {
                    lstUser.addAll(arrUser);
                    for (UserEntity userEntity : arrUser) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(userEntity);
                        realm.commitTransaction();
                    }

                    inviteAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError() {
                    swipeRefreshIndividual.setRefreshing(false);
                }
            });
        }
    }

    private void refreshInvite() {
        swipeRefreshIndividual.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshIndividual.setPadding(0, 0, 0, 10);
                number = 0;
                Log.d("IndividuaFragment", "Refresh list");
                initData(number);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*
    luu lai trang thai adapter
     */
@Override
protected void onPause()
{
    super.onPause();
    mBundleRecyclerViewState = new Bundle();
    Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
    mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
}

    @Override
    protected void onResume()
    {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
