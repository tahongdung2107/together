package com.example.dung.togetherfinal11.Config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.dung.togetherfinal11.Interface.IInfoUserListener;
import com.example.dung.togetherfinal11.Interface.ITeamListener;
import com.example.dung.togetherfinal11.Interface.IUserListener;
import com.example.dung.togetherfinal11.Interface.ModelManagerListener;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.SharedPreferences.AppPreference;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dung on 08/09/2016.
 */
public class ModelManager {
    private static ModelManager modelManager;
    private  Context context;
    private final String _USER_ID = "_user_id";
    private final String _TOKEN = "_token";
    private final String PAGE = "page";
    private final String LIMIT = "limit";
    String DATA = "data";
    String USER = "user";
    String PROFILE = "profile";
    String TOIEC = "toeic";
    String OPENFILE = "openfire";
    String TOKEN = "token";
    String SERVER = "server";
    String FROM_ID = "user_id";
    String USERNAME_OPENFILE = "username";
    String PLAINPASSWORD = "plainPassword";
    String ID = "id";
    String TEAM_ID = "team_id";
    String user_id,team_id,username_openfile,plainPassword,server,token;
    private AppPreference preference;
    public static ModelManager getInstance(Context context) {
         modelManager = new ModelManager(context);
        return modelManager;
    }
    public ModelManager(Context context) {
        this.context = context;

    }
    public void login(String url, final String userName, final String password, final ModelManagerListener modelManagerListener) {
        Ion.with(context).load(url)
                .setBodyParameter(Config.KEY_ACCOUNT,userName)
                .setBodyParameter(Config.KEY_PASSWORD,password)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e==null && result!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(result.toString());
                                if (jsonObject.getString("code").equals("0")){
                                    JSONObject jsonObjData = jsonObject.getJSONObject(DATA);
                                    JSONObject jsonObjUser = jsonObjData.getJSONObject(USER);
                                    JSONObject jsonObjProfile = jsonObjUser.getJSONObject(PROFILE);
                                    JSONObject jsonObjToeic = jsonObjUser.getJSONObject(TOIEC);
                                    JSONObject jsonObjOpenfile = jsonObjUser.getJSONObject(OPENFILE);
                                     user_id = jsonObjProfile.getString(ID);
                                     team_id = jsonObjProfile.getString(TEAM_ID);
                                    token = jsonObjData.getString(TOKEN);
                                    username_openfile = jsonObjOpenfile.getString(USERNAME_OPENFILE);
                                    plainPassword = jsonObjOpenfile.getString(PLAINPASSWORD);
                                    server = jsonObjOpenfile.getString(SERVER);
                                    Config.Profile = result;
                                    Config.UserName = userName;
                                    Config.PassWord = password;
                                    Config.TeamID = team_id;
                                    Config.USER_ID = user_id;
                                    Config.Username_Openfile = username_openfile;
                                    Config.Plainpassword = plainPassword;
                                    Config.Server = server;
                                    Config.TOKEN = token;
                                    modelManagerListener.onSuccess(jsonObject);
                                }else {
                                    Toast.makeText(context, "User or password invalid", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void getUserbypage(String url, String page ,String limit, final IUserListener iUserListener) {
        Log.d("Ion","result :" +Config.USER_ID + "/" + Config.TOKEN);
        Ion.with(context).load(url)
                .setBodyParameter(_USER_ID, Config.USER_ID)
                .setBodyParameter(_TOKEN, Config.TOKEN)
                .setBodyParameter(LIMIT,limit)
                .setBodyParameter(PAGE , page)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null && result != null){
                            try {
                                JSONObject response = new JSONObject(result);
                                if (response.getString("code").equals("0")){
                                    iUserListener.onSuccess(parserGetUsers(response));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }
    public  void  getTeamsbypage(String url , String page , String limit , final ITeamListener iTeamListener){
        Ion.with(context).load(url)
                .setBodyParameter(_USER_ID,Config.USER_ID)
                .setBodyParameter(_TOKEN,Config.TOKEN)
                .setBodyParameter(PAGE,page)
                .setBodyParameter(LIMIT,limit)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    if (e == null && result!=null){
                        try {
                            JSONObject response = new JSONObject(result);
                            iTeamListener.onSuccess(parserGetTeam(response));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    }
                });
    }

    public  void getInfoUser(String url , String from_id , final IInfoUserListener iInfoUserListener){
        Ion.with(context).load(url)
                .setBodyParameter(_USER_ID, Config.USER_ID)
                .setBodyParameter(_TOKEN,Config.TOKEN)
                .setBodyParameter(FROM_ID,from_id)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null && result != null){
                            try {
                                JSONObject response = new JSONObject(result);
                                JSONObject jsonData = response.getJSONObject("data");
                                iInfoUserListener.onInfoListener(jsonData.getString("nickname"),jsonData.getString("avatar"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }
    private ArrayList<UserEntity> parserGetUsers(JSONObject jsonObject) {
        ArrayList<UserEntity> arrUserentity = new ArrayList<>();
        if (jsonObject != null){
            try {
                if (jsonObject.getString("code").equals("0")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null && jsonArray.length()>0){
                        for (int i = 0 ; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            UserEntity userEntity = new UserEntity();
                            userEntity.setId(object.getString("id"));
                            userEntity.setAvatar(object.getString("avatar"));
                            userEntity.setUsername(object.getString("name"));
                            userEntity.setQuote(object.getString("quote"));
                            userEntity.setGoal(object.getString("goal"));
                            userEntity.setToeic_level_id(object.getString("toeic_level_id"));
                            userEntity.setTeam_id(object.getString("team_id"));
                            userEntity.setBirthday(object.getString("birthday"));
                            userEntity.setGender(object.getString("gender"));
                            userEntity.setToeic_level_id(object.getString("toeic_level_id"));
                            userEntity.setCreated_datetime(object.getString("created_datetime"));
                            userEntity.setUpdated_datetime(object.getString("updated_datetime"));
                            arrUserentity.add(userEntity);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrUserentity;
    }
    private ArrayList<TeamEntity> parserGetTeam (JSONObject jsonObject){
        ArrayList<TeamEntity> arrTeamentity = new ArrayList<>();
        if (jsonObject != null){
            try {
                if (jsonObject.getString("code").equals("0")){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null && jsonArray.length()>0){
                        for (int i = 0 ; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            TeamEntity teamEntity = new TeamEntity();
                            teamEntity.setId(object.getString("id"));
                            teamEntity.setTitle(object.getString("title"));
                            teamEntity.setMember1_id(object.getString("member1_id"));
                            teamEntity.setMember2_id(object.getString("member2_id"));
                            teamEntity.setMember3_id(object.getString("member3_id"));
                            teamEntity.setMember1_join_date(object.getString("member1_join_date"));
                            teamEntity.setMember2_join_date(object.getString("member2_join_date"));
                            teamEntity.setMember3_join_date(object.getString("member3_join_date"));
                            teamEntity.setMember1_leave_date(object.getString("member1_leave_date"));
                            teamEntity.setMember2_leave_date(object.getString("member2_leave_date"));
                            teamEntity.setMember3_leave_date(object.getString("member3_leave_date"));
                            teamEntity.setToeic_level_id(object.getString("toeic_level_id"));
                            teamEntity.setAvatar(object.getString("avatar"));
                            teamEntity.setCreated_datetime(object.getString("created_datetime"));
                            teamEntity.setUpdated_datetime(object.getString("updated_datetime"));
                            teamEntity.setMember1_name(object.getString("member1_name"));
                            teamEntity.setMember2_name(object.getString("member2_name"));
                            teamEntity.setMember3_name(object.getString("member3_name"));
                            teamEntity.setMember1_avatar(object.getString("member1_avatar"));
                            teamEntity.setMember2_avatar(object.getString("member2_avatar"));
                            teamEntity.setMember3_avatar(object.getString("member3_avatar"));
                            arrTeamentity.add(teamEntity);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrTeamentity;
    }

}
