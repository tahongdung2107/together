package com.example.dung.togetherfinal11.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dung on 04/08/2016.
 */
public class UserEntity extends RealmObject{
    @PrimaryKey
    private String Id;
    private String username;
    private String birthday;
    private String gender;
    private String avatar;
    private String quote;
    private String goal;
    private String toeic_level_id;
    private String team_id;
    private String created_datetime;
    private String updated_datetime;

    public UserEntity() {
    }

    public UserEntity(String username, String birthday, String id, String gender, String avatar, String quote, String goal, String toeic_level_id, String team_id, String created_datetime, String updated_datetime) {
        this.username = username;
        this.birthday = birthday;
        Id = id;
        this.gender = gender;
        this.avatar = avatar;
        this.quote = quote;
        this.goal = goal;
        this.toeic_level_id = toeic_level_id;
        this.team_id = team_id;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getToeic_level_id() {
        return toeic_level_id;
    }

    public void setToeic_level_id(String toeic_level_id) {
        this.toeic_level_id = toeic_level_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }

    public void setUpdated_datetime(String updated_datetime) {
        this.updated_datetime = updated_datetime;
    }
}
