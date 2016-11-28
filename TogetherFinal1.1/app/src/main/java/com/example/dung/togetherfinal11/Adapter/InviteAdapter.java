package com.example.dung.togetherfinal11.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.example.dung.togetherfinal11.Chat.MyService;
import com.example.dung.togetherfinal11.Config.Config;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 25/11/2016.
 */

public class InviteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<UserEntity> mitemList;
    protected Context mContext;
    String user_id_to, name, avatar, nameDefaul, avatarDefaul,name_to;
    MyService myService;
    int buttonid ;
    Realm realm;

    public InviteAdapter(Context context, ArrayList<UserEntity> itemList) {
        mContext = context;
        mitemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite, parent, false);
        viewHolder = new InviteAdapter.InviteViewHolder(layoutView);
        return viewHolder;
    }
    public void clearAdapter() {
        mitemList.clear();
        notifyDataSetChanged();
    }
    public void delete(int position) {
        mitemList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        final InviteAdapter.InviteViewHolder inviteViewHolder = (InviteAdapter.InviteViewHolder) holder;
        name = mitemList.get(position).getUsername();
        avatar = mitemList.get(position).getAvatar();
        realm = RealmController.getInstance().getRealm();
        if (name == null || name.equals("")) {
            nameDefaul = "Dung";
            inviteViewHolder.nameUser.setText(nameDefaul);
        } else {
            inviteViewHolder.nameUser.setText(name);
        }
        if (avatar == null || avatar.equals("null")) {
            avatarDefaul = "http://together.codelovers.vn:8010/uploads/files/2016/09/30/57ee36b2a7c41.jpg";
            Glide.with(mContext).load(avatarDefaul).asBitmap().into(inviteViewHolder.imageAvatar);
        } else {
            Glide.with(mContext).load(avatar).asBitmap().into(inviteViewHolder.imageAvatar);
        }
        /*
        su kien click invite
         */

        if (mitemList.get(position).getInvite()==true){
            inviteViewHolder.btninvite.setText("Invite");
            inviteViewHolder.btninvite.setBackgroundColor(Color.rgb(162,0,124));
        }else {
            inviteViewHolder.btninvite.setText("Cancel");
            inviteViewHolder.btninvite.setBackgroundColor(Color.RED);
        }
        inviteViewHolder.btninvite.setTag(position);
        inviteViewHolder.btninvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int location = (int) view.getTag();
                user_id_to = mitemList.get(location).getId();
                name_to = mitemList.get(location).getUsername();
                Log.d("InviteAdapter","Result :" + user_id_to);
                if(mitemList.get(location).getInvite() == true) {
                    inviteViewHolder.btninvite.setText("Cancel");
                    inviteViewHolder.btninvite.setBackgroundColor(Color.RED);
                    realm.beginTransaction();
                    mitemList.get(location).setInvite(false);
                    realm.commitTransaction();
                    ChatModel chatModel = new ChatModel();
                    chatModel.setFrom(Config.USER_ID);
                    chatModel.setType("invite_to_team");
                    chatModel.setMessage_uuid(UUID.randomUUID().toString());
                    chatModel.setRecipient_type("User");
                    chatModel.setMedia_type("invite_to_team");
                    chatModel.setTo(user_id_to);
                    chatModel.setRecipient_id(user_id_to);
                    chatModel.setText_content("invitations to teams");
                    chatModel.setMsg("invitations to teams");
                    chatModel.setMsgID("123");
                    chatModel.setToeic_level("123");
                    chatModel.setTeam_id(Config.TeamID);
                    chatModel.setName(Config.NAME_USER);
                    chatModel.setAvatar(Config.AVATAR_USER);
                    myService.xmpp.sendMessage(chatModel);
                }else {
                    inviteViewHolder.btninvite.setText("Invite");
                    inviteViewHolder.btninvite.setBackgroundColor(Color.rgb(162,0,124));
                    realm.beginTransaction();
                    mitemList.get(location).setInvite(true);
                    realm.commitTransaction();
                    realm = RealmController.getInstance().getRealm();
                    RealmController.getInstance().refresh();
                    RealmResults<ChatModel> realmResultsModel = RealmController.getInstance().getmediatype("invite_to_team",user_id_to);
                    realm.beginTransaction();
                    realmResultsModel.deleteAllFromRealm();
                    realm.commitTransaction();
                }
            }
        });
        /*
        su kien click uninvite
         */
//        inviteViewHolder.btnuninvite.setTag(position);
//        inviteViewHolder.btnuninvite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                inviteViewHolder.btninvite.setVisibility(View.VISIBLE);
//                inviteViewHolder.btnuninvite.setVisibility(View.GONE);
//                int location = (int) view.getTag();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mitemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class InviteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView nameUser, Score;
        Button btninvite, btnuninvite;

        public InviteViewHolder(View v) {
            super(v);
            imageAvatar = (ImageView) v.findViewById(R.id.imgAvatar);
            nameUser = (TextView) v.findViewById(R.id.txtName);
            Score = (TextView) v.findViewById(R.id.txtScore);
            btninvite = (Button) v.findViewById(R.id.btnInvite);
//            btnuninvite = (Button) v.findViewById(R.id.btnunInvite);
        }
    }

}
