package com.example.dung.togetherfinal11.Fragment;



import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.ModelManagerListener;
import com.example.dung.togetherfinal11.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dung on 29/11/2016.
 */

public class EditProfileFragment extends Fragment {
    View view;
    Calendar myCalendar;
    TextView Birthday ,Username;
    RadioGroup Radiogroup;
    EditText Wordcategory,Nickname,Email,Quote,Goal,Toiec;
    RadioButton radioMale,radioFemale;
    String username,wordcategory,nickname,email,gender,quote,goal,toiec,birthday,myGender;
    String Editusername,Editwordcategory,Editnickname,Editemail,Editquote,Editgoal,Edittoiec,Editbirthday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        Birthday = (TextView) view.findViewById(R.id.txtBirthDay);
        Username = (TextView) view.findViewById(R.id.txtUsername);
        Wordcategory = (EditText) view.findViewById(R.id.editWordCategory);
        Nickname = (EditText) view.findViewById(R.id.editNickName);
        Radiogroup = (RadioGroup) view.findViewById(R.id.radioGroupGender);
        Email = (EditText) view.findViewById(R.id.editEmail);
        Quote = (EditText) view.findViewById(R.id.edtQuote);
        Goal = (EditText) view.findViewById(R.id.edtGoal);
        Toiec = (EditText) view.findViewById(R.id.edtLvToic);
        radioMale = (RadioButton) view.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);
        setHasOptionsMenu(true);
        myCalendar = Calendar.getInstance();
        setdate();
        setData();
        return view;
    }
    private  void setData(){
        Username.setText(username);
        Wordcategory.setText(wordcategory);
        Nickname.setText(nickname);
        Email.setText(email);
        if (gender.equals("0")){
            radioMale.setChecked(true);
            myGender ="0";
        }else if (gender.equals("1")){
            radioFemale.setChecked(true);
            myGender = "1";
        }
        Quote.setText(quote);
        Goal.setText(goal);
        Toiec.setText(toiec);
        Birthday.setText(birthday);

    }
    private void updateProfile (){
        Editnickname = Nickname.getText().toString();
        Editwordcategory = Wordcategory.getText().toString();
        Editemail = Email.getText().toString();
        checkgender();
        Editquote = Quote.getText().toString();
        Editgoal = Goal.getText().toString();
        Edittoiec = Toiec.getText().toString();
        Editbirthday = Birthday.getText().toString();
        ModelManager.getInstance(getActivity()).updateUser(ConfigsApi.UPDATE_URL, myGender, Editbirthday, Editquote, Editgoal,
                Editwordcategory, Editemail, Edittoiec, new ModelManagerListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        ModelManager.getInstance(getActivity()).login(ConfigsApi.LOGIN_URL, Config.UserName, Config.PassWord, new ModelManagerListener() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                                fragmentTransaction.replace(R.id.container_profile,new ProfileFragment());
                                fragmentTransaction.commit();
                            }
                            @Override
                            public void onError(String error) {

                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
    private void checkgender (){
        Radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check) {
                if (check == R.id.radioMale){
                    myGender = "0";
                }else if (check == R.id.radioFemale){
                    myGender = "1";
                }
            }
        });
    }
    private  void setdate (){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }
    private void updateLabel() {

        String myFormat = "dd/mm/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Birthday.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile_edit,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_save_profile){
            updateProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        username = args.getString(Config.KEY_BUNDLE_username);
        wordcategory = args.getString(Config.KEY_BUNDLE_wordcategory);
        nickname = args.getString(Config.KEY_BUNDLE_nickname);
        gender = args.getString(Config.KEY_BUNDLE_gender);
        quote = args.getString(Config.KEY_BUNDLE_quote);
        goal = args.getString(Config.KEY_BUNDLE_goal);
        toiec = args.getString(Config.KEY_BUNDLE_toiec);
        birthday = args.getString(Config.KEY_BUNDLE_birthday);
        email = args.getString(Config.KEY_BUNDLE_email);
    }
}
