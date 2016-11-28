package com.example.dung.togetherfinal11.Chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;


import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.ModelManager;
import com.example.dung.togetherfinal11.Interface.IGroupMessageListener;
import com.example.dung.togetherfinal11.Interface.IInfoUserListener;
import com.example.dung.togetherfinal11.Interface.IMessage;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.GroupChatMessage;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.R;
import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/*
 * Created by dung on 10/9/2016.
 */
public class TogetherXMPP implements ConnectionListener, ChatManagerListener, ChatMessageListener, MessageListener {

    private static TogetherXMPP togetherXMPP = null;
    private MyService myService;
    private XMPPTCPConnection connection;

    private String serverAddress;
    private String userNameLogin;
    private String passwordLogin;
    private String jID;
    private String GROUP_SERVICE = "together";
    private Realm realm;
    private IMessage iMessage;
    private IGroupMessageListener iGroupMessageListener;

    MultiUserChat multiUserChat;
    MultiUserChatManager multiUserChatManager;
    public static final String MY_USER_ID = "1";
    private int SEND = 1;
    private int RECEIPT = 0;
    private static final int NOTIFICATION_ID = 1;

    private final String RECIPIENT_TYPE = "recipient_type";
    private final String USER = "User";
    private final String TO = "to";
    private final String ID = "id";
    private final String FROM = "from";
    private final String TYPE = "type";
    private final String MEDIA_TYPE = "media_type";
    private final String TEXT_CONTENT = "text_content";
    private final String AVATAR = "avatar";
    private final String NAME = "name";
    private final String INVITE_TO_TEAM = "invite_to_team";
    private final String TEAM_ID = "team_id";
    private final String MESSAGE_UUID = "message_uuid";
    private final String RECIPIENT_ID = "recipient_id";
    private final String MSG = "msg";
    private final String MSGID = "msgID";
    private final String TOEIC_LEVEL = "toeic_level";
    private final String USER_ID = "user_id";
    private final String TEXT = "text";
    private final String FROM_USER = "from_user";
    private final String SERVER_REPLY = "server_reply";


    public static TogetherXMPP getInstance(MyService myService, String serverAddress, String userNameLogin, String passwordLogin, String jID) {
        if (togetherXMPP == null) {
            togetherXMPP = new TogetherXMPP(myService, serverAddress, userNameLogin, passwordLogin, jID);
            Log.d("togetherXMPP", "togetherXMPP result" + togetherXMPP);
        }
        return togetherXMPP;
    }

    private TogetherXMPP(MyService myService, String serverAddress, String userNameLogin, String passwordLogin, String jID) {
        Log.d("MyXMPP", serverAddress + "");
        this.myService = myService;
        this.serverAddress = serverAddress;
        this.userNameLogin = userNameLogin;
        this.passwordLogin = passwordLogin;
        this.jID = jID;
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        connection = new XMPPTCPConnection(config.build());
        connection.setPacketReplyTimeout(100000);
        connection.addConnectionListener(this);
        Log.d("CONNECT", "Trying to Login " + userNameLogin + " password " + passwordLogin);
        Log.d("CONNECT", "Trying to message");
        PacketFilter filter = MessageTypeFilter.GROUPCHAT;
        connection.addSyncPacketListener(new PacketListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                Log.d("Packet!!", "ok");
                Message message = (Message) packet;
                Log.d("Debugging", "Message3: " + message.toString());
                Log.d("CONNECT", "Message from: " + message.getFrom() + " to: " + message.getTo() + " " + message.getBody());
                if (message.getBody() != null) {
                    Log.d("processGroupMessage!!!!", "ok");
                    processGroupMessage(message.getBody());
                }
            }
        }, filter);
        realm = Realm.getDefaultInstance();
//        realm = RealmController.getInstance().getRealm();
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
//        if (!createdLocally){
        chat.addMessageListener(this);
//        }

    }


    @Override
    public void connected(XMPPConnection connection) {
        LogTogetherXMPP("Together connected");
        if (!connection.isAuthenticated()) {
            login();
            join(jID);
        }
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        LogTogetherXMPP("Together authenticated");
    }

    @Override
    public void connectionClosed() {
        LogTogetherXMPP("Together connection closed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        LogTogetherXMPP("Together authenticated");
    }

    @Override
    public void reconnectionSuccessful() {
        LogTogetherXMPP("Together Reconnection successful ");
        if (!connection.isAuthenticated()) {
            login();
            if (multiUserChat.isJoined()) {
                join(jID);
            }
        }
    }

    @Override
    public void reconnectingIn(int seconds) {
        LogTogetherXMPP("Together reconnecting " + seconds);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        LogTogetherXMPP("Together reconnection fail");
        if (!connection.isConnected()) {
            connect();
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        LogTogetherXMPP("processMessage Body" + message.getBody());
        if (message.getType() == Message.Type.chat && message.getBody() != null) {
            try {
                JSONObject jsonObjContentMessage = new JSONObject(message.getBody());
                JSONObject jsonTo = jsonObjContentMessage.getJSONObject(TO);
                String toID = jsonTo.getString(ID);
                JSONObject jsonFrom = jsonObjContentMessage.getJSONObject(FROM);
                String fromID = jsonFrom.getString(ID);
                String type = jsonObjContentMessage.getString(TYPE);
                String media_type = jsonObjContentMessage.getString(MEDIA_TYPE);
                String textContent = jsonObjContentMessage.getString(TEXT_CONTENT);
                String avatar = jsonObjContentMessage.getString(AVATAR);
                String name = jsonFrom.getString(NAME);
                String message_uid = jsonObjContentMessage.getString(MESSAGE_UUID);
                String teamID = jsonObjContentMessage.getString("team_id");
                Log.d("TogetheTest", "Result :" + teamID);
                Log.d("ContentProcessMessage", "Result :" + textContent + "NameProcessMessage :" + name);
                //pair Chat
                ChatModel chatMessage = new ChatModel();
                chatMessage.setTo(toID);
                chatMessage.setFrom(fromID);
                chatMessage.setType(type);
                chatMessage.setMessage_uuid(message_uid);
                chatMessage.setMedia_type(media_type);
                chatMessage.setAvatar(avatar);
                chatMessage.setName(name);
                //
                chatMessage.setTeam_id(teamID);
                //
                if (type.equals(INVITE_TO_TEAM)) {
                    chatMessage.setTeam_id(jsonObjContentMessage.getString(TEAM_ID));
                }
                chatMessage.setText_content(textContent);
                processMessage(chatMessage);
                Log.d("Luu thanh cong", "OKKKKK");
                //notification
//                if (!fromID.equals(userNameLogin)){
//                    showNotification(chatMessage);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void login() {
        if (!TextUtils.isEmpty(userNameLogin) && !TextUtils.isEmpty(passwordLogin)) {
            if (!connection.isAuthenticated()) {
                try {
                    Log.d("XMPP", userNameLogin + "/" + passwordLogin);
                    connection.login(userNameLogin, passwordLogin);
                    ChatManager.getInstanceFor(connection).addChatListener(this);
                } catch (XMPPException e) {
                    LogTogetherXMPP("Login fail" + e.getMessage());
                    e.printStackTrace();
                } catch (SmackException e) {
                    LogTogetherXMPP("Login fail" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    LogTogetherXMPP("Login fail" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean join(String jID) {
        Log.d("JoinTogetherXmpp", "OK" + jID);
        if (!jID.equals("0")) {
            multiUserChatManager = MultiUserChatManager.getInstanceFor(connection);
            multiUserChat = multiUserChatManager.getMultiUserChat(jID + "@" + GROUP_SERVICE + "." + serverAddress);
            if (!multiUserChat.isJoined()) {
                Log.d("CONNECT", "Joining room !! " + jID + " and username " + userNameLogin);
                boolean createNow = false;
                try {
                    multiUserChat.createOrJoin(userNameLogin);
                    createNow = true;
                    LogTogetherXMPP("Team was join");
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                    Log.d("CONNECT", "Error while creating the room " + jID + e.getMessage());
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                    Log.d("CONNECT", "Error while creating the room " + jID + e.getMessage());
                } catch (SmackException e) {
                    e.printStackTrace();
                    Log.d("CONNECT", "Error while creating the room " + jID + e.getMessage());
                }
                if (createNow) {
                    try {
                        multiUserChat.sendConfigurationForm(new Form(DataForm.Type.submit));
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("CONNECT", "Room created!!");
        }
        return false;
    }

    public void sendGroupMessage(GroupChatMessage groupChatMessage) {
        Gson gson = new Gson();
        String body = gson.toJson(groupChatMessage);
        Log.d("sendGroupMessage1", "ok :" + body);
        LogTogetherXMPP("sendMessage body: " + body + "-" + serverAddress);
        try {
            if (connection.isAuthenticated() && multiUserChat.isJoined()) {
                multiUserChat.sendMessage(body);
                LogTogetherXMPP("full-content" + body);
            } else {
                login();
                if (!multiUserChat.isJoined()) {
                    join(jID);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            LogTogetherXMPP("send message fail" + e.getMessage());
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
//        JSONObject jsonObjBody = new JSONObject();
//        try {
//            jsonObjBody.put(FROM, groupChatMessage.getFrom());
//            Log.d("jsonObjBody", "jsonObjBody Result From id " + groupChatMessage.getFrom());
//            jsonObjBody.put(TYPE, groupChatMessage.getType());
//            jsonObjBody.put(MESSAGE_UUID, groupChatMessage.getMessage_uuid());
//            jsonObjBody.put(RECIPIENT_TYPE, groupChatMessage.getRecipient_type());
//            jsonObjBody.put(MEDIA_TYPE, groupChatMessage.getMedia_type());
//            jsonObjBody.put(TO, groupChatMessage.getTo());
//            Log.d("jsonObjBody", "jsonObjBody Result To id " + groupChatMessage.getTo());
//            jsonObjBody.put(RECIPIENT_ID, groupChatMessage.getRecipient_id());
//            jsonObjBody.put(TEXT_CONTENT, groupChatMessage.getText_content());
//            jsonObjBody.put(MSG, groupChatMessage.getMsg());
//            jsonObjBody.put(MSGID, groupChatMessage.getMsgID());
//            jsonObjBody.put(AVATAR, groupChatMessage.getAvatar());
//            jsonObjBody.put(TOEIC_LEVEL, groupChatMessage.getToeic_level());
//            jsonObjBody.put(TEAM_ID, groupChatMessage.getTeam_id());
//            jsonObjBody.put(USER_ID, groupChatMessage.getUser_id());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String body = jsonObjBody.toString();
//        LogTogetherXMPP("sendMessage body: " + body + "-" + serverAddress);
//        Chat chat = ChatManager.getInstanceFor(connection).createChat(groupChatMessage.getTo() + "@" + serverAddress, this);
//        Message message = new Message();
//        message.setBody(body);
//        message.setStanzaId(groupChatMessage.getMsg());
//        message.setType(Message.Type.chat);
//        try {
//            if (connection.isAuthenticated()) {
//                chat.sendMessage(message);
//            } else {
//                login();
//            }
//        } catch (SmackException.NotConnectedException e) {
//            LogTogetherXMPP("send message fail" + e.getMessage());
//            e.printStackTrace();
//        }
    }

    public void sendMessage(ChatModel chatMessage) {
        JSONObject jsonObjBody = new JSONObject();
        try {
            jsonObjBody.put(FROM, chatMessage.getFrom());
            Log.d("jsonObjBody", "jsonObjBody Result From id " + chatMessage.getFrom());
            jsonObjBody.put(TYPE, chatMessage.getType());
            jsonObjBody.put(MESSAGE_UUID, chatMessage.getMessage_uuid());
            jsonObjBody.put(RECIPIENT_TYPE, chatMessage.getRecipient_type());
            jsonObjBody.put(MEDIA_TYPE, chatMessage.getMedia_type());
            jsonObjBody.put(TO, chatMessage.getTo());
            Log.d("jsonObjBody", "jsonObjBody Result To id " + chatMessage.getTo());
            jsonObjBody.put(RECIPIENT_ID, chatMessage.getRecipient_id());
            jsonObjBody.put(TEXT_CONTENT, chatMessage.getText_content());
            jsonObjBody.put(MSG, chatMessage.getMsg());
            jsonObjBody.put(MSGID, chatMessage.getMsgID());
            jsonObjBody.put(AVATAR, chatMessage.getAvatar());
            jsonObjBody.put(TOEIC_LEVEL, chatMessage.getToeic_level());
            jsonObjBody.put(TEAM_ID, chatMessage.getTeam_id());
            jsonObjBody.put(USER_ID, chatMessage.getUser_id());
//            String id_Team = jsonObjBody.getString("teamid");
//            Log.d("TogetherXMPPTest","Result :" + id_Team);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String body = jsonObjBody.toString();
        LogTogetherXMPP("sendMessage body1: " + body + "-" + serverAddress);
        Chat chat = ChatManager.getInstanceFor(connection).createChat(chatMessage.getTo() + "@" + serverAddress, this);
        Message message = new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.getMsg());
        message.setType(Message.Type.chat);
        try {
            if (connection.isAuthenticated()) {
                chat.sendMessage(message);
            } else {
                login();
            }
        } catch (SmackException.NotConnectedException e) {
            LogTogetherXMPP("send message fail" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void LogTogetherXMPP(String log) {
        Log.d("Together-XMPP ", log);
    }

    public void connect() {
        LogTogetherXMPP("Together is connected");
        final AsyncTask<Void, Void, Void> myConnect = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (connection != null && !connection.isConnected()) {
                        connection.connect();
                    }
                } catch (XMPPException e) {
                    LogTogetherXMPP("connect fail" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    LogTogetherXMPP("connect fail" + e.getMessage());
                    e.printStackTrace();
                } catch (SmackException e) {
                    LogTogetherXMPP("connect fail" + e.getMessage());
                    e.printStackTrace();
                }
                return null;


            }

        };
        myConnect.execute();
    }

    private void processMessage(final ChatModel chatMessage) {
        Log.d("Vao", "processMessage");
        if (chatMessage.getTo().equals(userNameLogin) || chatMessage.getFrom().equals(userNameLogin)) {
            chatMessage.setMine(false);
            if (iMessage != null) {
                iMessage.onMessageListener(chatMessage);
            }
            if (chatMessage.getType().equals(TEXT)) {
                Log.d("TogetherTest", "Server_reply4");
                saveMessageChat(chatMessage, chatMessage.getFrom(), RECEIPT);
                saveConversationChat(chatMessage, RECEIPT);
//                showNotification(chatMessage.getText_content());
            } else if (chatMessage.getType().equals(SERVER_REPLY)) {
                Log.d("TogetherTest", "Server_reply");
                saveConversationChat(chatMessage, SEND);
                Log.d("TogetherTest", "Server_reply2");
                saveMessageChat(chatMessage, chatMessage.getTo(), SEND);
                Log.d("TogetherTest", "Server_reply3");
            }
        }
    }

    private void processGroupMessage(String body) {
        try {
            Log.d("processGroupMessage", "Vaoroi" + body.toString());
            JSONObject jsonObject = new JSONObject(body.toString());
            GroupChatMessage groupChatMessage = new GroupChatMessage();
            groupChatMessage.setType(jsonObject.getString(TYPE));
            groupChatMessage.setFrom(jsonObject.getString(FROM_USER));
            groupChatMessage.setMessage_uuid(jsonObject.getString(MESSAGE_UUID));
            groupChatMessage.setRecipient_type(jsonObject.getString(RECIPIENT_TYPE));
            groupChatMessage.setMedia_type(jsonObject.getString(MEDIA_TYPE));
            groupChatMessage.setTo(jsonObject.getString(TO));
            groupChatMessage.setRecipient_id(jsonObject.getString(RECIPIENT_ID));
            groupChatMessage.setText_content(jsonObject.getString(TEXT_CONTENT));
            groupChatMessage.setMsg(jsonObject.getString(MSG));
            groupChatMessage.setMine(false);
            if (iGroupMessageListener != null) {
                iGroupMessageListener.onGroupMessageListener(groupChatMessage);
            }
            saveGroupMessagesChat(groupChatMessage, RECEIPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(ChatModel chatMessage) {
        int notifyId = 0;
        String title = myService.getString(R.string.app_name);
        String type = chatMessage.getType();
        LogTogetherXMPP("showNotification " + type);
        Intent resultIntent = null;
        if (type.equalsIgnoreCase("text")) {
            resultIntent = new Intent(myService, Notification.class);
            resultIntent.putExtra("PairChatFragment", "PairChatFragment");
            resultIntent.putExtra("id", notifyId);
            resultIntent.putExtra("title", title);
//        } else if (type.equalsIgnoreCase(Config.MessageType.TRAVEL_STATE)) {
//            JSONObject jsoFrom = JsonUtil.getJSONObject(jsoData, "from");
//            notifyId = JsonUtil.getInt(jsoFrom, "id", 0);
//
//            int userType = JsonUtil.getInt(JsonUtil.getJSONObject(jsoData, "receiver"), "user_type", Config.UserType.PASSENGER);
//            int travelState = JsonUtil.getInt(jsoData, "travel_state", 0);
//            if (travelState < Trip.TRAVEL_STATE_DEPARTED || travelState > Trip.TRAVEL_STATE_ARRIVED) {
//                resultIntent = new Intent(mContext, GenerationActivity.class);
//                resultIntent.putExtra(Config.Key.FID, GenerationActivity.FID_MATCHING);
//                resultIntent.putExtra(Config.Key.USER_TYPE, userType);
//            } else if (travelState <= Trip.TRAVEL_STATE_ARRIVED) {
//                resultIntent = new Intent(mContext, GenerationActivity.class);
//                if (userType == Config.UserType.PASSENGER)
//                    resultIntent.putExtra(Config.Key.FID, GenerationActivity.FID_DEPARTURE_PASSENGER);
//                else resultIntent.putExtra(Config.Key.FID, GenerationActivity.FID_DEPARTURE_DRIVER);
//            }
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(myService)
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.ic_menu_manage : R.mipmap.ic_launcher)
                .setColor(Color.BLACK)
                .setContentTitle(title)
                .setContentText(chatMessage.getText_content())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(chatMessage.getText_content()))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(myService);
        stackBuilder.addParentStack(Notification.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) myService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notificationBuilder.build());
    }
/*
    private void showNotification(String content) {
//        int numMessages = 0;
        Intent intent = new Intent(myService, ChatsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(myService, 0, intent, 0);
        NotificationManager mNotifM = (NotificationManager) myService.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(myService)
                        .setSmallIcon(R.drawable.ic_menu_manage)
                        .setContentTitle("ada")
                        .setContentText(content)
                        .setNumber(232);
        mBuilder.setContentIntent(contentIntent);
//        mBuilder.setContentText(content)
//                .setNumber(++numMessages);
        mNotifM.notify(NOTIFICATION_ID, mBuilder.build());

    }
*/
    private void saveConversationChat(final ChatModel chatMessage, final int send) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("saveConversationChat", "OK ");
                if (send == RECEIPT) {
                    if (chatMessage.getTo().equalsIgnoreCase(userNameLogin)) {
                        LogTogetherXMPP("Create new Conversation");
                        realm.beginTransaction();
                        realm.copyToRealm(chatMessage);
                        realm.commitTransaction();
                    }
                    RealmResults<ChatModel> resultMessageText = realm.where(ChatModel.class).equalTo("from", chatMessage.getFrom()).findAll();
                    Log.d("TogetherXMPP", "conversation1" + resultMessageText.toString() + "!");
                } else if (send == SEND) {
                    if (chatMessage.getFrom().equalsIgnoreCase(userNameLogin)) {
                        String to = chatMessage.getFrom();
                        String from = chatMessage.getTo();
                        chatMessage.setTo(to);
                        chatMessage.setFrom(from);
                        chatMessage.setMine(true);
                        chatMessage.setMessage_uuid(UUID.randomUUID().toString());
                        realm.beginTransaction();
                        realm.copyToRealm(chatMessage);
                        realm.commitTransaction();
                        RealmResults<ChatModel> resultMessageText = realm.where(ChatModel.class).equalTo("from", from).findAll();
                        Log.d("TogetherXMPP", "conversation send3" + resultMessageText.toString() + "!");
                    }
                }
            }
        });
    }

    private void saveMessageChat(final ChatModel chatMessage, final String fromID, final int send) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                Log.d("VAO", "FromId :" + fromID + " " + "Chatmessage :" + chatMessage.getType());
                Log.d("TOKENTOGETHERXMPP", "" + Config.TOKEN);
                Messages realmMessage = realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findFirst();
                Log.d("TESTTTTTTTTTTTTTTTT", "Result :" + userNameLogin + " :" + fromID);
                //test
                RealmResults<Messages> realmResultMessage = realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findAll();
                //type text
                Log.d("SendTogetgerXMPP :", String.valueOf(send) + " RECEIPT :" + RECEIPT);
                if (send == RECEIPT) {
                    //type invite

                    final Messages messages = new Messages();
                    if (realmMessage == null) {
                        if (!fromID.equalsIgnoreCase(userNameLogin)) {
                            Log.d("Send", "Send=Recipt");
                            LogTogetherXMPP("Create new Message");
                            messages.setId(UUID.randomUUID().toString());
                            messages.setFromID(fromID);
                            messages.setToID(chatMessage.getTo());
                            messages.setContent(chatMessage.getText_content());
                            messages.setMissMessage(1);
                            messages.setType(chatMessage.getMedia_type());
                            messages.setTeamID(chatMessage.getTeam_id());
                            messages.setName(chatMessage.getName());
                            messages.setUrlImageAvatar(chatMessage.getAvatar());
                            realm.beginTransaction();
                            realm.copyToRealm(messages);
                            realm.commitTransaction();
                            LogTogetherXMPP("message receipt " + realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findFirst());
                        }
                    } else {
                        String id = realmMessage.getId();
                        String avatar = realmMessage.getUrlImageAvatar();
                        messages.setId(id);
                        messages.setFromID(fromID);
                        messages.setToID(chatMessage.getTo());
                        messages.setContent(chatMessage.getText_content());
                        messages.setMissMessage(realmMessage.getMissMessage() + 1);
                        messages.setType(chatMessage.getMedia_type());
                        messages.setTeamID(chatMessage.getTeam_id());
                        messages.setName(chatMessage.getName());
                        messages.setUrlImageAvatar(avatar);
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(messages);
                        realm.commitTransaction();
                        Log.d("Update :", "Result :");
//                        RealmResults<Messages> realmResults=realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findAll();
//                        LogTogetherXMPP("message receipt #null " + realmResults.get(realmResults.size()-1).);
                    }
                } else if (send == SEND) {
                    Log.d("Send=Send", "OK" + realmMessage);
                    final Messages messages = new Messages();
                    if (realmMessage == null) {
                        if (!fromID.equalsIgnoreCase(userNameLogin)) {
                            LogTogetherXMPP("Create new Message" + chatMessage.getTo() + ": " + chatMessage.getFrom());
                            messages.setId(UUID.randomUUID().toString());
                            messages.setFromID(fromID);
                            messages.setToID(chatMessage.getTo());
                            messages.setContent(chatMessage.getText_content());
                            messages.setMissMessage(0);
                            messages.setType(chatMessage.getMedia_type());
                            if (realmMessage == null) {
                                Log.d("Params", "Result : " + "UserId :" + userNameLogin + "UseridFrom :" + fromID + "Token :" + Config.TOKEN);
                                ModelManager.getInstance(myService).getInfoUser(ConfigsApi.LINK_GET_USER_INFO, fromID, new IInfoUserListener() {
                                    @Override
                                    public void onInfoListener(String name, String urlImageAvatar) {
                                        Log.d("ModelManager", "OK");
                                        messages.setUrlImageAvatar(urlImageAvatar);
                                        messages.setName(name);
                                        realm.beginTransaction();
                                        realm.copyToRealm(messages);
                                        realm.commitTransaction();
                                        LogTogetherXMPP("message send Test " + realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findFirst());
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("Send", "Realm khong null :" + fromID + "/" + userNameLogin);
                        if (!fromID.equalsIgnoreCase(userNameLogin)) {
                            Log.d("Send", "FromID # userNameLogin");
                            String id = realmMessage.getId();
                            LogTogetherXMPP("Update Message :" + id);
                            messages.setId(id);
                            messages.setFromID(fromID);
                            messages.setToID(chatMessage.getTo());
                            messages.setContent(chatMessage.getText_content());
                            Log.d("UpdateMessage", " Result :" + chatMessage.getText_content() + "Reaml");
                            messages.setMissMessage(0);
                            messages.setType(chatMessage.getMedia_type());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(messages);
                            realm.commitTransaction();
                            Log.d("QUAdayroidungk", "uk" + realmMessage);
                            if (realmMessage == null) {

                                Log.d("PramsTest", "Result :" + userNameLogin + ":" + fromID + ":" + Config.TOKEN);
                                ModelManager.getInstance(myService).getInfoUser(ConfigsApi.LINK_GET_USER_INFO, fromID, new IInfoUserListener() {
                                    @Override
                                    public void onInfoListener(String name, String urlImageAvatar) {
                                        messages.setUrlImageAvatar(urlImageAvatar);
                                        messages.setName(name);
                                        realm.beginTransaction();
                                        realm.copyToRealmOrUpdate(messages);
                                        realm.commitTransaction();
                                        LogTogetherXMPP("message send " + realm.where(Messages.class).equalTo("FromID", fromID).equalTo("Type", chatMessage.getMedia_type()).findFirst());
                                    }
                                });
                            }

                        }
                    }

                }
            }
        });
    }

    private void saveGroupMessagesChat(final GroupChatMessage groupChatMessage, final int send) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("Savemessegachat", "ok");
                //type text
                Messages realmSelectTypeText = realm.where(Messages.class).equalTo("FromID", groupChatMessage.getFrom()).equalTo("Type", groupChatMessage.getType()).findFirst();
                RealmResults<Messages> resultMessageText = realm.where(Messages.class).equalTo("FromID", groupChatMessage.getFrom()).equalTo("Type", groupChatMessage.getType()).findAll();
                Log.d("ChatgroupTogether ", "Result :" + resultMessageText);
                //type invite
                Log.d("GroupChat", "Result :" + userNameLogin + "/" + groupChatMessage.getFrom());
                Log.d("messageFragment1", realmSelectTypeText + "!");
                final Messages messages = new Messages();
                if (realmSelectTypeText == null && groupChatMessage.getFrom().equalsIgnoreCase(userNameLogin)) {
                    LogTogetherXMPP("Create new Message");
                    messages.setFromID(groupChatMessage.getFrom());
                    messages.setToID(groupChatMessage.getTo());
                    messages.setContent(groupChatMessage.getText_content());
                    messages.setMissMessage(+1);
                    messages.setType(groupChatMessage.getType());
                    messages.setTeamID(groupChatMessage.getTeam_id());
                    ModelManager.getInstance(myService).getInfoUser(ConfigsApi.LINK_GET_USER_INFO, groupChatMessage.getFrom(), new IInfoUserListener() {
                        @Override
                        public void onInfoListener(String name, String urlImageAvatar) {
                            Log.d("TogetherChat", "TeamID :" + Config.TeamID);
                            messages.setId(UUID.randomUUID().toString());
                            messages.setName(name);
                            messages.setUrlImageAvatar(urlImageAvatar);
                            realm.beginTransaction();
                            realm.copyToRealm(messages);
                            realm.commitTransaction();
                            Log.d("SaveChatGroupOk", "ok1");
                        }
                    });
                } else {
                    Log.d("TestGroupchat", "Result :" + send + "/" + RECEIPT + "/" + SEND + "/" + realmSelectTypeText);
                    if (send == RECEIPT && realmSelectTypeText != null) {
                        messages.setId(UUID.randomUUID().toString());
                        messages.setToID(groupChatMessage.getRecipient_id());
                        messages.setContent(groupChatMessage.getText_content());
                        messages.setMissMessage(realmSelectTypeText.getMissMessage() + 1);
                        realm.beginTransaction();
//                        resultMessageText.get(0).setId(UUID.randomUUID().toString());
//                        resultMessageText.get(0).setToID(groupChatMessage.getRecipient_id());
//                        resultMessageText.get(0).setContent(groupChatMessage.getText_content());
//                        resultMessageText.get(0).setMissMessage(realmSelectTypeText.getMissMessage() + 1);
                        realm.copyToRealm(messages);
                        realm.commitTransaction();
                        Log.d("messageFragmentMiss1", "ToId" + realmSelectTypeText.getToID() + "!");
                        Log.d("messageFragmentMiss1", "Content" + realmSelectTypeText.getContent() + "!");
                        Log.d("messageFragmentMiss1", "MissMessage" + realmSelectTypeText.getMissMessage() + "!");
                        Log.d("messageFragmentMiss1", "ID" + UUID.randomUUID().toString() + "!");
                    } else if (send == SEND && realmSelectTypeText != null) {
                        messages.setId(UUID.randomUUID().toString());
                        messages.setToID(groupChatMessage.getRecipient_id());
                        messages.setContent(groupChatMessage.getText_content());
                        messages.setMissMessage(realmSelectTypeText.getMissMessage() + 1);
                        realm.beginTransaction();
//                        resultMessageText.get(0).setId(UUID.randomUUID().toString());
//                        resultMessageText.get(0).setToID(groupChatMessage.getRecipient_id());
//                        resultMessageText.get(0).setContent(groupChatMessage.getText_content());
//                        resultMessageText.get(0).setMissMessage(realmSelectTypeText.getMissMessage() + 1);
                        realm.copyToRealm(messages);
                        realm.commitTransaction();
                        Log.d("messageFragmentMiss2", "MissMessage" + realmSelectTypeText.getMissMessage() + "!");
                    }
                }
            }
        });
    }

    @Override
    public void processMessage(Message message) {
        multiUserChat.addMessageListener(this);
        LogTogetherXMPP("processMessage Body" + message.getBody());
    }


    public void disconnect() {
        if (connection.isConnected()) {
            connection.disconnect();
            togetherXMPP = null;
            LogTogetherXMPP("Together is Disconnected");
        }
    }

    public void sendMessageListener(IMessage iMessage) {
        this.iMessage = iMessage;
    }

    public void GroupMessageListener(IGroupMessageListener iGroupMessageListener) {
        this.iGroupMessageListener = iGroupMessageListener;
    }

}
