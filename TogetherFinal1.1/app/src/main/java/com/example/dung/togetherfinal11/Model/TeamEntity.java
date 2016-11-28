package com.example.dung.togetherfinal11.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dung on 21/11/2016.
 */

public class TeamEntity extends RealmObject {
    @PrimaryKey
    private String Idteam;
    private String Title;
    private String member1_id;
    private String member2_id;
    private String member3_id;
    private String member1_join_date;
    private String member2_join_date;
    private String member3_join_date;
    private String member1_leave_date;
    private String member2_leave_date;
    private String member3_leave_date;
    private String toeic_level_id;
    private String category_id;
    private String avatar;
    private String created_datetime;
    private String updated_datetime;
    private String member1_name;
    private String member1_created_date;
    private String member1_avatar;
    private String member2_name;
    private String member2_created_date;
    private String member2_avatar;
    private String member3_name;
    private String member3_created_date;
    private String member3_avatar;

    public TeamEntity() {
    }

    public TeamEntity(String idteam, String title, String member1_id, String member2_id, String member3_id, String member1_join_date, String member2_join_date, String member3_join_date, String member1_leave_date, String member2_leave_date, String member3_leave_date, String toeic_level_id, String category_id, String avatar, String created_datetime, String updated_datetime, String member1_name, String member1_created_date, String member1_avatar, String member2_name, String member2_created_date, String member2_avatar, String member3_name, String member3_created_date, String member3_avatar) {
        Idteam = idteam;
        Title = title;
        this.member1_id = member1_id;
        this.member2_id = member2_id;
        this.member3_id = member3_id;
        this.member1_join_date = member1_join_date;
        this.member2_join_date = member2_join_date;
        this.member3_join_date = member3_join_date;
        this.member1_leave_date = member1_leave_date;
        this.member2_leave_date = member2_leave_date;
        this.member3_leave_date = member3_leave_date;
        this.toeic_level_id = toeic_level_id;
        this.category_id = category_id;
        this.avatar = avatar;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.member1_name = member1_name;
        this.member1_created_date = member1_created_date;
        this.member1_avatar = member1_avatar;
        this.member2_name = member2_name;
        this.member2_created_date = member2_created_date;
        this.member2_avatar = member2_avatar;
        this.member3_name = member3_name;
        this.member3_created_date = member3_created_date;
        this.member3_avatar = member3_avatar;
    }

    public String getId() {
        return Idteam;
    }

    public void setId(String id) {
        Idteam = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMember1_id() {
        return member1_id;
    }

    public void setMember1_id(String member1_id) {
        this.member1_id = member1_id;
    }

    public String getMember2_id() {
        return member2_id;
    }

    public void setMember2_id(String member2_id) {
        this.member2_id = member2_id;
    }

    public String getMember3_id() {
        return member3_id;
    }

    public void setMember3_id(String member3_id) {
        this.member3_id = member3_id;
    }

    public String getMember1_join_date() {
        return member1_join_date;
    }

    public void setMember1_join_date(String member1_join_date) {
        this.member1_join_date = member1_join_date;
    }

    public String getMember2_join_date() {
        return member2_join_date;
    }

    public void setMember2_join_date(String member2_join_date) {
        this.member2_join_date = member2_join_date;
    }

    public String getMember3_join_date() {
        return member3_join_date;
    }

    public void setMember3_join_date(String member3_join_date) {
        this.member3_join_date = member3_join_date;
    }

    public String getMember2_leave_date() {
        return member2_leave_date;
    }

    public void setMember2_leave_date(String member2_leave_date) {
        this.member2_leave_date = member2_leave_date;
    }

    public String getMember1_leave_date() {
        return member1_leave_date;
    }

    public void setMember1_leave_date(String member1_leave_date) {
        this.member1_leave_date = member1_leave_date;
    }

    public String getMember3_leave_date() {
        return member3_leave_date;
    }

    public void setMember3_leave_date(String member3_leave_date) {
        this.member3_leave_date = member3_leave_date;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getToeic_level_id() {
        return toeic_level_id;
    }

    public void setToeic_level_id(String toeic_level_id) {
        this.toeic_level_id = toeic_level_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getMember1_name() {
        return member1_name;
    }

    public void setMember1_name(String member1_name) {
        this.member1_name = member1_name;
    }

    public String getMember1_created_date() {
        return member1_created_date;
    }

    public void setMember1_created_date(String member1_created_date) {
        this.member1_created_date = member1_created_date;
    }

    public String getMember1_avatar() {
        return member1_avatar;
    }

    public void setMember1_avatar(String member1_avatar) {
        this.member1_avatar = member1_avatar;
    }

    public String getMember2_name() {
        return member2_name;
    }

    public void setMember2_name(String member2_name) {
        this.member2_name = member2_name;
    }

    public String getMember2_created_date() {
        return member2_created_date;
    }

    public void setMember2_created_date(String member2_created_date) {
        this.member2_created_date = member2_created_date;
    }

    public String getMember2_avatar() {
        return member2_avatar;
    }

    public void setMember2_avatar(String member2_avatar) {
        this.member2_avatar = member2_avatar;
    }

    public String getMember3_name() {
        return member3_name;
    }

    public void setMember3_name(String member3_name) {
        this.member3_name = member3_name;
    }

    public String getMember3_created_date() {
        return member3_created_date;
    }

    public void setMember3_created_date(String member3_created_date) {
        this.member3_created_date = member3_created_date;
    }

    public String getMember3_avatar() {
        return member3_avatar;
    }

    public void setMember3_avatar(String member3_avatar) {
        this.member3_avatar = member3_avatar;
    }
}
