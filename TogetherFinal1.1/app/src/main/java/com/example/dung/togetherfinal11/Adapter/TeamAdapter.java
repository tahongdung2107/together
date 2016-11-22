package com.example.dung.togetherfinal11.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dung.togetherfinal11.Chat.MyService;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Config.Covertime;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by dung on 21/11/2016.
 */

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TeamEntity> mitemList;
    protected Context mContext;
    String username1, username2, username3, imageuser1, imageuser2, imageuser3, iduser1, iduser2, iduser3 , imageUser,nameUser;
    JSONObject json;
    String DATA = "data";
    String USER = "user";
    String PROFILE = "profile";
    String AVATAR = "avatar";
    String USERNAME = "username";
    MyService myService;
    public TeamAdapter(Context context, ArrayList<TeamEntity> itemList) {
        mContext = context;
        mitemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        viewHolder = new TeamAdapter.TeamViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TeamAdapter.TeamViewHolder teamViewHolder = (TeamAdapter.TeamViewHolder) holder;
        teamViewHolder.teamName.setText(mitemList.get(position).getTitle());
        long timestart = Long.parseLong(mitemList.get(position).getCreated_datetime());
        teamViewHolder.firstTimeCreated.setText(Covertime.getInstance().getDateTime(timestart).toString());
        try {
            json = new JSONObject(Config.Profile);
            JSONObject jsonObjData = json.getJSONObject(DATA);
            JSONObject jsonObjUser = jsonObjData.getJSONObject(USER);
            JSONObject jsonObjProfile = jsonObjUser.getJSONObject(PROFILE);
            imageUser = jsonObjProfile.getString(AVATAR);
            nameUser = jsonObjProfile.getString(USERNAME);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        username1 = mitemList.get(position).getMember1_name();
        username2 = mitemList.get(position).getMember2_name();
        username3 = mitemList.get(position).getMember3_name();
        imageuser1 = mitemList.get(position).getMember1_avatar();
        imageuser2 = mitemList.get(position).getMember2_avatar();
        imageuser3 = mitemList.get(position).getMember3_avatar();
        iduser1 = mitemList.get(position).getMember1_id();
        iduser2 = mitemList.get(position).getMember2_id();
        iduser3 = mitemList.get(position).getMember3_id();
        Log.d("TeamAdapter123","Result :" + " iduser :" + iduser3 );
        /*
        user 1
         */
        if (!iduser1.equals("null")) {
            if (imageuser1.equals("") || imageuser1.equals("null")) {
                String url = "http://together.codelovers.vn:8010/uploads/images/2016/10/19/5806dcbfa14d6.jpg";
                Glide.with(mContext).load(url).asBitmap().into(teamViewHolder.imageUser1);
            } else {
                Glide.with(mContext).load(imageuser1).asBitmap().into(teamViewHolder.imageUser1);
            }
            if (username1.equals("") || username1.equals(null)) {
                String name = "Dung69";
                teamViewHolder.nameUSer1.setText(name);
            }else {
                teamViewHolder.nameUSer1.setText(username1);
            }
        }
        /*
        user 2
         */
        if (!iduser2.equals("null")){
            teamViewHolder.nameUser2.setVisibility(View.VISIBLE);
            teamViewHolder.imageUSer2.setVisibility(View.VISIBLE);
            teamViewHolder.btnjoinuser2.setVisibility(View.GONE);
            if (imageuser2.equals("") || imageuser2.equals("null")) {
                String url = "http://together.codelovers.vn:8010/uploads/images/2016/10/19/5806dcbfa14d6.jpg";
                Glide.with(mContext).load(url).asBitmap().into(teamViewHolder.imageUSer2);
            } else {
                Glide.with(mContext).load(imageuser2).asBitmap().into(teamViewHolder.imageUSer2);
            }
            if (username2.equals("") || username2.equals(null)) {
                String name = "Dung69";
                teamViewHolder.nameUser2.setText(name);
            }else {
                teamViewHolder.nameUser2.setText(username1);
            }
        }else {
            teamViewHolder.nameUser2.setVisibility(View.GONE);
            teamViewHolder.imageUSer2.setVisibility(View.GONE);
            teamViewHolder.btnjoinuser2.setVisibility(View.VISIBLE);
            if (imageuser2.equals("") || imageuser2.equals("null")) {
                String url = "http://together.codelovers.vn:8010/uploads/images/2016/10/19/5806dcbfa14d6.jpg";
                Glide.with(mContext).load(url).asBitmap().into(teamViewHolder.imageUSer2);
            } else {
                Glide.with(mContext).load(imageuser2).asBitmap().into(teamViewHolder.imageUSer2);
            }
            if (username2.equals("") || username2.equals(null)) {
                String name = "Dung69";
                teamViewHolder.nameUser2.setText(name);
            }else {
                teamViewHolder.nameUser2.setText(username2);
            }
            teamViewHolder.btnjoinuser2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Config.TeamID.equals("0")){
                        new AlertDialog.Builder(mContext)
                                .setMessage("you have to team")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setFrom(Config.USER_ID);
                        chatModel.setType("invite_to_team");
                        chatModel.setMessage_uuid(UUID.randomUUID().toString());
                        chatModel.setRecipient_type("User");
                        chatModel.setMedia_type("invite_to_team");
                        chatModel.setTo(iduser1);
                        chatModel.setRecipient_id(iduser1);
                        chatModel.setText_content("");
                        chatModel.setMsg("Invite to team");
                        chatModel.setMsgID("123");
                        chatModel.setToeic_level("450");
                        chatModel.setTeam_id("");
                        chatModel.setUser_id(Config.USER_ID);
                        chatModel.setAvatar(imageUser);
                        chatModel.setName(nameUser);
                        chatModel.setMine(true);
                        myService.xmpp.sendMessage(chatModel);
                        new AlertDialog.Builder(mContext)
                                .setMessage("\n" +
                                        "send invitations to team success, team captain confirmed")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                }
            });
        }
        /*
        user 3
         */
        if (!iduser3.equals("null")){
            teamViewHolder.nameUser3.setVisibility(View.VISIBLE);
            teamViewHolder.imageUser3.setVisibility(View.VISIBLE);
            teamViewHolder.btnjoinuser3.setVisibility(View.GONE);
            if (imageuser3.equals("") || imageuser3.equals("null")) {
                String url = "http://together.codelovers.vn:8010/uploads/images/2016/10/25/580ec7aa9783a.jpg";
                Glide.with(mContext).load(url).asBitmap().into(teamViewHolder.imageUser3);
            } else {
                Glide.with(mContext).load(imageuser3).asBitmap().into(teamViewHolder.imageUser3);
            }
            if (username3.equals("") || username3.equals(null)) {
                String name = "Dung69";
                teamViewHolder.nameUser3.setText(name);
            }else {
                teamViewHolder.nameUser3.setText(username3);
            }
        }else {
            teamViewHolder.nameUser3.setVisibility(View.GONE);
            teamViewHolder.imageUser3.setVisibility(View.GONE);
            teamViewHolder.btnjoinuser3.setVisibility(View.VISIBLE);
            if (imageuser3.equals("") || imageuser3.equals("null")) {
                String url = "http://together.codelovers.vn:8010/uploads/images/2016/10/19/5806dcbfa14d6.jpg";
                Glide.with(mContext).load(url).asBitmap().into(teamViewHolder.imageUser3);
            } else {
                Glide.with(mContext).load(imageuser3).asBitmap().into(teamViewHolder.imageUser3);
            }
            if (username3.equals("") || username3.equals(null)) {
                String name = "Dung69";
                teamViewHolder.nameUser3.setText(name);
            }else {
                teamViewHolder.nameUser3.setText(username3);
            }
            teamViewHolder.btnjoinuser3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Config.TeamID.equals("0")){
                        new AlertDialog.Builder(mContext)
                                .setMessage("you have to team")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setFrom(Config.USER_ID);
                        chatModel.setType("invite_to_team");
                        chatModel.setMessage_uuid(UUID.randomUUID().toString());
                        chatModel.setRecipient_type("User");
                        chatModel.setMedia_type("invite_to_team");
                        chatModel.setTo(iduser1);
                        chatModel.setRecipient_id(iduser1);
                        chatModel.setText_content("");
                        chatModel.setMsg("Invite to team");
                        chatModel.setMsgID("123");
                        chatModel.setToeic_level("450");
                        chatModel.setTeam_id("");
                        chatModel.setUser_id(Config.USER_ID);
                        chatModel.setAvatar(imageUser);
                        chatModel.setName(nameUser);
                        chatModel.setMine(true);
                        myService.xmpp.sendMessage(chatModel);
                        new AlertDialog.Builder(mContext)
                                .setMessage("\n" +
                                        "send invitations to team success, team captain confirmed")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mitemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser1, imageUSer2, imageUser3;
        TextView nameUSer1, nameUser2, nameUser3, teamName, firstTimeCreated;
        Button btnjoinuser2, btnjoinuser3;

        public TeamViewHolder(View v) {
            super(v);
            imageUser1 = (ImageView) v.findViewById(R.id.ImageUser1);
            imageUSer2 = (ImageView) v.findViewById(R.id.ImageUser2);
            imageUser3 = (ImageView) v.findViewById(R.id.ImageUser3);
            nameUSer1 = (TextView) v.findViewById(R.id.txtUserName1);
            nameUser2 = (TextView) v.findViewById(R.id.txtUserName2);
            nameUser3 = (TextView) v.findViewById(R.id.txtUserName3);
            teamName = (TextView) v.findViewById(R.id.texviewTeamName);
            firstTimeCreated = (TextView) v.findViewById(R.id.txtFirstTimeCreated);
            btnjoinuser2 = (Button) v.findViewById(R.id.btnJoinUser2);
            btnjoinuser3 = (Button) v.findViewById(R.id.btnJoinUser3);
        }
    }
}
