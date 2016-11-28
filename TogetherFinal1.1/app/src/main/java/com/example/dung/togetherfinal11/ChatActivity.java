package com.example.dung.togetherfinal11;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dung.togetherfinal11.Adapter.ChatAdapter;
import com.example.dung.togetherfinal11.Chat.MyService;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 22/11/2016.
 */

public class ChatActivity extends AppCompatActivity {
    ImageView mic, photo, chat;
    EditText editchat;
    Toolbar toolbar;
    RecyclerView recyclerView;
    String img, name, idto, idfrom, mgs, Content, filePath, user_id, URL;
    MyService myService;
    Realm realm;
    private List<Object> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mic = (ImageView) findViewById(R.id.imgVoice);
        photo = (ImageView) findViewById(R.id.imgSelectImage);
        chat = (ImageView) findViewById(R.id.sendTextImage);
        editchat = (EditText) findViewById(R.id.edtMessage);
        recyclerView = (RecyclerView) findViewById(R.id.gridviewChat);
        final Bundle b = getIntent().getExtras();
        img = b.getString("ImageMessage");
        name = b.getString("NameMessage");
        idto = b.getString("User_Id_To");
        Log.d("Chatactivity", "Result :" + "Idto :" + idto );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadMessage();
        seteventRecycler();
    }

        private void loadMessage(){
            realm = RealmController.getInstance().getRealm();
            RealmController.getInstance().refresh();
            RealmResults<ChatModel> realmResults = RealmController.getInstance().getContentChatmodel(idto);
            mData.addAll(realmResults);
            Log.d("LoadmessageAcivity","Result clear :" + mData.size());
            ChatAdapter adapter = new ChatAdapter(getApplication(), mData);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.scrollToPosition(mData.size() - 1);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        private void seteventRecycler(){
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mgs = editchat.getText().toString();
                    if (mgs.equals("") || mgs.equals(" ")) {
                        editchat.setText("");
                    } else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setFrom(Config.USER_ID);
                        chatModel.setType("text");
                        chatModel.setMessage_uuid(UUID.randomUUID().toString());
                        chatModel.setRecipient_type("User");
                        chatModel.setMedia_type("text");
                        chatModel.setTo(idto);
                        chatModel.setRecipient_id(idto);
                        chatModel.setText_content(mgs);
                        chatModel.setMsg("Invite to team");
                        chatModel.setMsgID("123");
                        chatModel.setToeic_level("450");
                        chatModel.setTeam_id("");
                        chatModel.setUser_id(Config.USER_ID);
                        chatModel.setAvatar(img);
                        chatModel.setName(name);
                        chatModel.setMine(true);
                        myService.xmpp.sendMessage(chatModel);
                        Setmessage();
                        editchat.setText("");
                    }

                }
            });
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Setmessage() {
        ChatModel chatModel1 = new ChatModel();
        mgs = editchat.getText().toString();
        Log.d("ChatActivity ", "Result :" + mgs);
        chatModel1.setText_content(mgs);
        chatModel1.setMine(true);
        mData.add(chatModel1);
////      Collections.reverse(mData);
        ChatAdapter adapter = new ChatAdapter(getApplication(), mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPosition(mData.size() - 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
