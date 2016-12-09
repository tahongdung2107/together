package com.example.dung.togetherfinal11.Fragment;


import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MainFragment extends AppCompatActivity {
    private NestedScrollView nestedScrollProfile;
    private Toolbar toolbarCollapsing;
    ImageView Imageavatar;
    String imageavatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nestedScrollProfile=(NestedScrollView)findViewById(R.id.nestedScrollProfile);
        Imageavatar = (ImageView) findViewById(R.id.imgCover);
        nestedScrollProfile.setFillViewport(true);
        toolbarCollapsing = (Toolbar) findViewById(R.id.toolbarProfile) ;
        toolbarCollapsing.setTitle("");
        setSupportActionBar(toolbarCollapsing);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        setImageavatar();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.container_profile, new ProfileFragment());
        fragmentTransaction.commit();
    }
    private void setImageavatar(){
        try {
            JSONObject json = new JSONObject(Config.Profile);
            JSONObject jsonData = json.getJSONObject("data");
            JSONObject jsonUser = jsonData.getJSONObject("user");
            JSONObject jsonProfile = jsonUser.getJSONObject("profile");
            imageavatar = jsonProfile.getString("avatar");
            Glide.with(getApplication()).load(imageavatar).asBitmap().into(Imageavatar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
