package com.example.dung.togetherfinal11.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dung on 22/11/2016.
 */

public class Messages extends RealmObject {
    @PrimaryKey
    private String Id;
    private String FromID;
    private String ToID;
    private String Content;
    private int MissMessage;
    private String Type;
    private String TeamID;
    private String Name;
    private String UrlImageAvatar;
    private boolean isMine;

    public Messages() {
    }

    public Messages(String id, String fromID, String toID, String content, int missMessage, String type, String teamID, String name, String urlImageAvatar, boolean isMine) {
        Id = id;
        FromID = fromID;
        ToID = toID;
        Content = content;
        MissMessage = missMessage;
        Type = type;
        TeamID = teamID;
        Name = name;
        UrlImageAvatar = urlImageAvatar;
        this.isMine = isMine;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFromID() {
        return FromID;
    }

    public void setFromID(String fromID) {
        FromID = fromID;
    }

    public String getToID() {
        return ToID;
    }

    public void setToID(String toID) {
        ToID = toID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getMissMessage() {
        return MissMessage;
    }

    public void setMissMessage(int missMessage) {
        MissMessage = missMessage;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTeamID() {
        return TeamID;
    }

    public void setTeamID(String teamID) {
        TeamID = teamID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrlImageAvatar() {
        return UrlImageAvatar;
    }

    public void setUrlImageAvatar(String urlImageAvatar) {
        UrlImageAvatar = urlImageAvatar;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
