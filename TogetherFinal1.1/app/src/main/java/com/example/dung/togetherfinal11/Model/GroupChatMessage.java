package com.example.dung.togetherfinal11.Model;

import io.realm.annotations.PrimaryKey;

/**
 * Created by dung on 22/11/2016.
 */

public class GroupChatMessage {
    @PrimaryKey
    private String message_uuid;
    private String from;
    private String type;
    private String recipient_type;
    private String media_type;
    private String to;
    private String recipient_id;
    private String text_content;
    private String msg;
    private String msgID;
    private String toeic_level;
    private String team_id;
    private String user_id;
    private String avatar;
    private String name;
    private boolean isMine;

    public GroupChatMessage() {
    }

    public GroupChatMessage(String from, String type, String message_uuid, String recipient_type, String media_type, String to, String recipient_id, String text_content, String msg, String msgID, String toeic_level, String team_id, String user_id, String avatar, String name, boolean isMine) {
        this.from = from;
        this.type = type;
        this.message_uuid = message_uuid;
        this.recipient_type = recipient_type;
        this.media_type = media_type;
        this.to = to;
        this.recipient_id = recipient_id;
        this.text_content = text_content;
        this.msg = msg;
        this.msgID = msgID;
        this.toeic_level = toeic_level;
        this.team_id = team_id;
        this.user_id = user_id;
        this.avatar = avatar;
        this.name = name;
        this.isMine = isMine;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage_uuid() {
        return message_uuid;
    }

    public void setMessage_uuid(String message_uuid) {
        this.message_uuid = message_uuid;
    }

    public String getRecipient_type() {
        return recipient_type;
    }

    public void setRecipient_type(String recipient_type) {
        this.recipient_type = recipient_type;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getToeic_level() {
        return toeic_level;
    }

    public void setToeic_level(String toeic_level) {
        this.toeic_level = toeic_level;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
