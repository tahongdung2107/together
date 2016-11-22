package com.example.dung.togetherfinal11.Realm;

import android.app.Activity;
import android.app.Application;

import android.support.v4.app.Fragment;
import android.util.Log;

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

    //Refresh the realm istance
    public void refresh() {

        realm.isAutoRefresh();
    }

    public void clearAll(){
        realm.beginTransaction();
//        realm.delete(UserEntity.class);
        realm.deleteAll();
//        realm.clear(Messages.class);
//        realm.clear(ChatMessage.class);
        realm.commitTransaction();
        Log.d("RealmController","Clear all realm database");
    }


    public void clearAll(String realmName) {
        if (realmName.equals(UserEntity.class.getSimpleName())) {
            realm.beginTransaction();
            realm.delete(UserEntity.class);
            realm.commitTransaction();
        } else if (realmName.equals(TeamEntity.class.getSimpleName())) {
            realm.beginTransaction();
            realm.delete(TeamEntity.class);
            realm.commitTransaction();
        }
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
        }



    //find all objects in the Messages.class
//    public RealmResults<Messages> getMessages() {
//
//        return realm.where(Messages.class).findAll();
//    }

//    public RealmResults<Messages> getMessages(String toID, String teamID) {
//        return realm.where(Messages.class).equalTo("toID", toID).or().equalTo("teamID", teamID).findAll();
//    }

    public RealmResults<TeamEntity> getTeams() {
        return realm.where(TeamEntity.class).findAll();
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
        return realm.where(UserEntity.class).contains("Name",name ).findAll();
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
