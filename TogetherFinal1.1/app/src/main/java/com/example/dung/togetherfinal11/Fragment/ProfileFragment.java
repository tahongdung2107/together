package com.example.dung.togetherfinal11.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dung on 29/11/2016.
 */

public class ProfileFragment extends Fragment {
    View view;
    TextView Username,Wordcategory,Nickname,Email,Gender,Quote,Goal,Toiec,Birthday;
    String username,wordcategory,nickname,email,gender,quote,goal,toiec,birthday;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        Username = (TextView) view.findViewById(R.id.txtUsername);
        Wordcategory = (TextView) view.findViewById(R.id.txtWordCategory);
        Nickname = (TextView) view.findViewById(R.id.txtNickName);
        Email = (TextView) view.findViewById(R.id.txtEmail);
        Gender = (TextView) view.findViewById(R.id.txtGender);
        Quote = (TextView) view.findViewById(R.id.txtQuote);
        Goal = (TextView) view.findViewById(R.id.txtGoal);
        Toiec = (TextView) view.findViewById(R.id.txtLvToic);
        Birthday = (TextView) view.findViewById(R.id.txtBirthDay);
        setHasOptionsMenu(true);
        setDataProfile();
        return view;
    }

    private  void setDataProfile(){
        try {
            JSONObject json = new JSONObject(Config.Profile);
            JSONObject jsonData = json.getJSONObject("data");
            JSONObject jsonUser = jsonData.getJSONObject("user");
            JSONObject jsonProfile = jsonUser.getJSONObject("profile");
            username = jsonProfile.getString("username");
            wordcategory = jsonProfile.getString("word_category");
            nickname = jsonProfile.getString("nickname");
            email = jsonProfile.getString("email");
            gender = jsonProfile.getString("gender");
            quote = jsonProfile.getString("quote");
            goal = jsonProfile.getString("goal");
            toiec = jsonProfile.getString("toeic_level_id");
            birthday = jsonProfile.getString("birthday");
            Username.setText(username);
            Wordcategory.setText(wordcategory);
            Nickname.setText(nickname);
            if (gender.equals(0)){
                Gender.setText("Male");
            }else if (gender.equals(1)){
                Gender.setText("Female");
            }
            Quote.setText(quote);
            Goal.setText(goal);
            Toiec.setText(toiec);
            Birthday.setText(birthday);
            Email.setText(email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile,menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_edit_profile){
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Config.KEY_BUNDLE_username,username);
            bundle.putString(Config.KEY_BUNDLE_wordcategory,wordcategory);
            bundle.putString(Config.KEY_BUNDLE_nickname,nickname);
            bundle.putString(Config.KEY_BUNDLE_gender,gender);
            bundle.putString(Config.KEY_BUNDLE_quote,quote);
            bundle.putString(Config.KEY_BUNDLE_goal,goal);
            bundle.putString(Config.KEY_BUNDLE_toiec,toiec);
            bundle.putString(Config.KEY_BUNDLE_birthday,birthday);
            bundle.putString(Config.KEY_BUNDLE_email,email);
            editProfileFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentTransaction.replace(R.id.container_profile, editProfileFragment);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

}
