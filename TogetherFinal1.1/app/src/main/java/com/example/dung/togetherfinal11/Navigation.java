package com.example.dung.togetherfinal11;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Fragment.ComunityFragment;
import com.example.dung.togetherfinal11.Model.Sreach_text;
import com.example.dung.togetherfinal11.Realm.RealmController;
import com.example.dung.togetherfinal11.SharedPreferences.AppPreference;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dung on 18/11/2016.
 */

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    AppPreference preference;
    JSONObject json;
    String DATA = "data";
    String USER = "user";
    String PROFILE = "profile";
    String USERNAME = "username";
    String AVATAR = "avatar";
    SearchView searchView;
    EventBus bus = EventBus.getDefault();
    TextView nameNav;
    ImageView imageNav;
    String name , image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        searchView = (SearchView) findViewById(R.id.searchview);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new ComunityFragment()).commit();
        mNavigationView.setNavigationItemSelectedListener(this);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        sreach();
        setProfile();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        if (item.getItemId() == R.id.nav_item_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Would you like to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            preference = new AppPreference(getApplicationContext());
                            preference.clear();
                            RealmController.getInstance().clearAll();
////                          stopService(new Intent(getBaseContext(),MyService.class));
                            Intent intent = new Intent(getApplication(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

//        if (item.getItemId() == R.id.nav_item_inbox) {
////                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
////                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
//        }
        return false;
    }
    private void sreach(){
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                bus.postSticky(new Sreach_text(searchQuery));
                return true;
            }
        });
    }
 private  void setProfile (){
     try {
         View header = mNavigationView.getHeaderView(0);
         nameNav = (TextView) header.findViewById(R.id.nameViewNav);
         imageNav = (ImageView) header.findViewById(R.id.imageViewNav);
         json = new JSONObject(Config.Profile);
         JSONObject jsonObjData = json.getJSONObject(DATA);
         JSONObject jsonObjUser = jsonObjData.getJSONObject(USER);
         JSONObject jsonObjProfile = jsonObjUser.getJSONObject(PROFILE);
         name = jsonObjProfile.getString(USERNAME);
         image = jsonObjProfile.getString(AVATAR);
         nameNav.setText(name);
         Glide.with(getApplication()).load(image).asBitmap().into(imageNav);
     } catch (JSONException e) {
         e.printStackTrace();
     }

 }
}
