package com.example.dung.togetherfinal11.Realm;

import android.app.Activity;
import android.app.Application;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;
import io.realm.Realm;
import io.realm.RealmResults;
/**
 * Created by Dung
 */
public class RealmController {

    private static RealmController instance;
    private final Realm realm;
    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public Realm getRealm() {

        return realm;
    }
//    RealmController.getInstance().clearAll(Invite.class.getSimpleName());
    //Refresh the realm istance
    public void refresh() {

        realm.isAutoRefresh();
    }

    public void delete(){
        realm.beginTransaction();
        realm.delete(UserEntity.class);
        realm.delete(TeamEntity.class);
        realm.delete(Messages.class);
        realm.delete(ChatModel.class);
        realm.commitTransaction();
    }


    public void clearAll(String realmName) {
        if (realmName.equals(UserEntity.class.getSimpleName())) {
            realm.beginTransaction();
            realm.delete(UserEntity.class);
            realm.commitTransaction();
        }
//        else if (realmName.equals(TeamEntity.class.getSimpleName())) {
//            realm.beginTransaction();
//            realm.delete(TeamEntity.class);
//            realm.commitTransaction();
//        }else if (realmName.equals(Messages.class.getSimpleName())){
//            realm.beginTransaction();
//            realm.delete(Messages.class);
//            realm.commitTransaction();
//        }
//        } else if (realmName.equals(Messages.class.getSimpleName())) {
//            realm.beginTransaction();
//            realm.clear(Messages.class);
//            realm.commitTransaction();
//        }else if (realmName.equals(ChatMessage.class.getSimpleName())) {
//            realm.beginTransaction();
//            realm.clear(ChatMessage.class);
//            realm.commitTransaction();
//        }else if (realmName.equals(Invite.class.getSimpleName())){
//            realm.beginTransaction();
//            realm.clear(Invite.class);
//            realm.commitTransaction();
//        }
    }


    //find all objects in the Messages.class
    public RealmResults<Messages> getMessages() {

        return realm.where(Messages.class).findAll();
    }
    public RealmResults<ChatModel> getModel() {

        return realm.where(ChatModel.class).findAll();
    }
//    public RealmResults<Messages> getMessages(String toID, String teamID) {
//        return realm.where(Messages.class).equalTo("toID", toID).or().equalTo("teamID", teamID).findAll();
//    }

    public RealmResults<TeamEntity> getTeams() {
        return realm.where(TeamEntity.class).findAll();
    }

    public RealmResults<Messages> getMessageuserTo (String user_to){
        return realm.where(Messages.class).equalTo("ToID",user_to).notEqualTo("Type","invite_to_team").findAll();
    }
    public RealmResults<Messages> getMessageuserType (){
        return realm.where(Messages.class).equalTo("Type","text").findAll();
    }
    public RealmResults<Messages> getMessageuserInvite (){
        return realm.where(Messages.class).equalTo("Type","invite_to_team").findAll();
    }
    public RealmResults<ChatModel> getContentChatmodel (String user_to){
        return realm.where(ChatModel.class).equalTo("from", user_to).findAll();
    }
    public RealmResults<ChatModel> getmediatype (String mediatype , String user_to){
        return realm.where(ChatModel.class).equalTo("media_type", mediatype).equalTo("from",user_to).findAll();
    }
    public RealmResults<UserEntity> getUserNoTeam (String teamid){
        return realm.where(UserEntity.class).equalTo("team_id", teamid).findAll();
    }
    public RealmResults<UserEntity> getuserInvite(String teamid,String user_id){
        return realm.where(UserEntity.class).equalTo("team_id",teamid).notEqualTo("Id",user_id).findAllSorted("username");
    }
//    public RealmResults<GroupChatMessage> getConversationGroupChat(String teamID) {
//        return realm.where(GroupChatMessage.class).equalTo("to",teamID).findAll();
//    }

    //query a single item with the given id
//    public Messages getMessage(String name) {
//
//        return realm.where(Messages.class).equalTo("name", name).findFirst();
//    }

    public RealmResults<UserEntity> getIndividuals() {
        return realm.where(UserEntity.class).findAll();
    }
    public  RealmResults<UserEntity> getName(String name){
        return realm.where(UserEntity.class).contains("username",name).findAll();
    }

//    public RealmResults<Invite> getInvite(){
//        return realm.where(Invite.class).findAll();
//    }

    //check if Message.class is empty
//    public boolean hasMessage() {
//
//        return !realm.allObjects(Messages.class).isEmpty();
//    }

    //query example
//    public RealmResults<Messages> queryMessage() {
//
//        return realm.where(Messages.class)
//                .contains("name", "aaa 0")
//                .or()
//                .contains("title", "Realm")
//                .findAll();
//
//    }

}
