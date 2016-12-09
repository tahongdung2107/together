package com.example.dung.togetherfinal11;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.dung.togetherfinal11.Adapter.ChatAdapter;
import com.example.dung.togetherfinal11.Chat.MyService;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Config.ConfigsApi;
import com.example.dung.togetherfinal11.Config.MultipartRequest;
import com.example.dung.togetherfinal11.Fragment.MainFragment;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Realm.RealmController;
import com.example.dung.togetherfinal11.TimeAudio.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 22/11/2016.
 */

public class ChatActivity extends AppCompatActivity {
    ImageView mic, photo, chat;
    EditText editchat;
    Toolbar toolbar;
    MediaRecorder myAudioRecorder;
    Timer timer = new Timer();
    LayoutInflater layoutInflater;
    RecyclerView recyclerView;
    String img, name, idto, idfrom, mgs, Content, filePath, user_id, URL, Full_URL;
    MyService myService;
    Realm realm;
    MediaPlayer mediaPlayer;
    private String outputFile = null;
    private Handler myHandler = new Handler();
    private int nCounter = 0;
    private final int PICK_IMAGE_REQUEST = 1;
    ImageView imgselect;
    private List<Object> mData = new ArrayList<>();
    Utilities utilities;
    private final int MY_PERMISSIONS_REQUEST_RECORD = 1295;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        mic = (ImageView) findViewById(R.id.imgVoice);
        photo = (ImageView) findViewById(R.id.imgSelectImage);
        chat = (ImageView) findViewById(R.id.sendTextImage);
        imgselect = (ImageView) findViewById(R.id.imgSelectImage);
        editchat = (EditText) findViewById(R.id.edtMessage);
        recyclerView = (RecyclerView) findViewById(R.id.gridviewChat);
        final Bundle b = getIntent().getExtras();
        img = b.getString("ImageMessage");
        name = b.getString("NameMessage");
        idto = b.getString("User_Id_To");
        Log.d("Chatactivity", "Result :" + "Idto :" + idto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadMessage();
        seteventRecycler();
        selectimage();
        eventmic();
    }


    private void selectimage() {
        imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });
    }

    private void loadMessage() {
        realm = RealmController.getInstance().getRealm();
        RealmController.getInstance().refresh();
        RealmResults<ChatModel> realmResults = RealmController.getInstance().getContentChatmodel(idto);
        mData.addAll(realmResults);
        Log.d("LoadmessageAcivity", "Result clear :" + realmResults);
        ChatAdapter adapter = new ChatAdapter(getApplication(), mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPosition(mData.size() - 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void seteventRecycler() {
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

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ChatActivity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                Log.d("TestLoadImage", "OK" + filePath);
                ContentValues params = new ContentValues();
                params.put(Config.KEY_ID, Config.USER_ID);
                params.put(Config.KEY_TOKEN, Config.TOKEN);
                MultipartRequest multipartRequest = new MultipartRequest(ConfigsApi.ATTACHMENTS, "file", new File(filePath), params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObjectData = null;
                        try {
                            jsonObjectData = response.getJSONObject("data");
                            URL = jsonObjectData.getString("full_url");
                            Log.d("FULL_URL", "RESULT :" + URL);
                            Setimage();
                            ChatModel chatModel = new ChatModel();
                            chatModel.setFrom(Config.USER_ID);
                            chatModel.setType("image");
                            chatModel.setMessage_uuid(UUID.randomUUID().toString());
                            chatModel.setRecipient_type("User");
                            chatModel.setMedia_type("image");
                            chatModel.setTo(idto);
                            chatModel.setRecipient_id(idto);
                            chatModel.setText_content(URL);
                            chatModel.setMsg(URL);
                            chatModel.setMsgID("123");
                            chatModel.setToeic_level("450");
                            chatModel.setTeam_id("");
                            chatModel.setUser_id(Config.USER_ID);
                            chatModel.setAvatar(img);
                            chatModel.setName(name);
                            chatModel.setMine(true);
                            myService.xmpp.sendMessage(chatModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(multipartRequest);
            }
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplication(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void Setimage() {
        ChatModel chatModel1 = new ChatModel();
        if (isValidUrl(URL)) {
            Log.d("ChatActivity ", "Result :" + URL);
            chatModel1.setText_content(URL);
            chatModel1.setMine(true);
            mData.add(chatModel1);
            Collections.reverse(mData);
            ChatAdapter adapter = new ChatAdapter(getApplication(), mData);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.scrollToPosition(mData.size() - 1);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d("ChatActivity ", "Result : K phai");
            Setmessage();
        }
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    private void eventmic(){
        utilities = new Utilities();
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_RECORD);
                } else {
                    layoutInflater = ChatActivity.this.getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.dialog_recorder, null, false);
                    final ImageButton btnRecord = (ImageButton) view.findViewById(R.id.btnRecord);
                    final ImageButton btnStopRecord = (ImageButton) view.findViewById(R.id.btnStopRecord);
                    final TextView txtTimeRecord = (TextView) view.findViewById(R.id.txtTimeRecord);
                    final TimerTask mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            myHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    nCounter++;
                                    txtTimeRecord.setText("" + nCounter);
                                }
                            });
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setView(view)
                            .setTitle("Voice Recorder");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (myAudioRecorder != null) {
                                myAudioRecorder.stop();
                                myAudioRecorder.release();
                                myAudioRecorder = null;
                            }
                        }
                    });
                    btnRecord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            outputFile = Environment.getExternalStorageDirectory() + "/audio" + System.currentTimeMillis() + ".mp3";
                            myAudioRecorder = new MediaRecorder();
                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                            myAudioRecorder.setOutputFile(outputFile);
                            try {
                                myAudioRecorder.prepare();
                                myAudioRecorder.start();
                                timer.schedule(mTimerTask, 100, 100);
                                btnRecord.setVisibility(View.GONE);
                                btnStopRecord.setVisibility(View.VISIBLE);
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Log.d("Recorder start", "Recording started");
                            Toast.makeText(ChatActivity.this, "Recording started", Toast.LENGTH_SHORT).show();

                        }
                    });
                    btnStopRecord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (myAudioRecorder != null) {
                                myAudioRecorder.stop();
                                if (mTimerTask != null) {
                                    nCounter = 0;
                                    mTimerTask.cancel();
                                }
                                myAudioRecorder.release();
                                myAudioRecorder = null;
                            }
                            Log.d("Recorder stop", "Audio recorded successfully");
                            Toast.makeText(ChatActivity.this, "Audio recorded successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            View itemView = layoutInflater.inflate(R.layout.dialog_listener, null, false);
                            mediaPlayer = MediaPlayer.create(ChatActivity.this, Uri.parse(outputFile));
                            final ImageButton btnPlay = (ImageButton) itemView.findViewById(R.id.btnPlay);
                            final TextView txtCurrentTime = (TextView) itemView.findViewById(R.id.txtCurrentTime);
                            final TextView txtTotalTime = (TextView) itemView.findViewById(R.id.txtTotalTime);
                            final SeekBar seekBarTimeAudio = (SeekBar) itemView.findViewById(R.id.seekBarTimeAudio);
                            AlertDialog.Builder builderListen = new AlertDialog.Builder(ChatActivity.this);

                            final Runnable mUpdateTimeTask = new Runnable() {
                                @Override
                                public void run() {
                                    long totalDuration = mediaPlayer.getDuration();
                                    long currentDuration = mediaPlayer.getCurrentPosition();

                                    // Displaying Total Duration time
                                    txtTotalTime.setText("" + utilities.milliSecondsToTimer(totalDuration));
                                    // Displaying time completed playing
                                    txtCurrentTime.setText("" + utilities.milliSecondsToTimer(currentDuration));

                                    // Updating progress bar
                                    int progress = (int) (utilities.getProgressPercentage(currentDuration, totalDuration));
                                    //Log.d("Progress", ""+progress);
                                    seekBarTimeAudio.setProgress(progress);

                                    // Running this thread after 100 milliseconds
                                    myHandler.postDelayed(this, 100);
                                }
                            };

                            builderListen.setView(itemView)
                                    .setTitle("Voice Recorder").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("pairChat", outputFile + "a");
                                    ContentValues params = new ContentValues();
                                    params.put(Config.KEY_ID, Config.USER_ID);
                                    params.put(Config.KEY_TOKEN, Config.TOKEN);
                                    MultipartRequest multipartRequest = new MultipartRequest(ConfigsApi.ATTACHMENTS, "file", new File(outputFile), params, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            JSONObject jsonObjectData = null;
                                            try {
                                                jsonObjectData = response.getJSONObject("data");
                                                URL = jsonObjectData.getString("full_url");
                                                Setimage();
                                                ChatModel chatModel = new ChatModel();
                                                chatModel.setFrom(Config.USER_ID);
                                                chatModel.setType("audio");
                                                chatModel.setMessage_uuid(UUID.randomUUID().toString());
                                                chatModel.setRecipient_type("User");
                                                chatModel.setMedia_type("audio");
                                                chatModel.setTo(idto);
                                                chatModel.setRecipient_id(idto);
                                                chatModel.setText_content(URL);
                                                chatModel.setMsg(URL);
                                                chatModel.setMsgID("123");
                                                chatModel.setToeic_level("450");
                                                chatModel.setTeam_id("");
                                                chatModel.setUser_id(Config.USER_ID);
                                                chatModel.setAvatar(img);
                                                chatModel.setName(name);
                                                chatModel.setMine(true);
                                                myService.xmpp.sendMessage(chatModel);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    );
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                                    requestQueue.add(multipartRequest);

                                }
                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialogListen = builderListen.create();
                            dialogListen.show();
                            btnPlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mediaPlayer.isPlaying()) {
                                        if (mediaPlayer != null) {
                                            mediaPlayer.pause();
                                            btnPlay.setImageResource(R.drawable.ic_chat);
                                        }
                                    } else {
                                        if (mediaPlayer != null) {
                                            mediaPlayer.start();
                                            btnPlay.setPadding(0, 0, 0, 0);
                                            btnPlay.setImageResource(R.drawable.ic_chat);
                                        }
                                    }
                                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            btnPlay.setImageResource(R.drawable.ic_chat);
                                        }
                                    });

                                    seekBarTimeAudio.setProgress(0);
                                    seekBarTimeAudio.setMax(100);

                                    myHandler.postDelayed(mUpdateTimeTask, 100);

                                }
                            });
                        }
                    });

                }

            }


        });

    }
}
