package com.example.dung.togetherfinal11.Adapter;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dung.togetherfinal11.ChatActivity;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 16/11/2016.
 */

public class IndivusualAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<UserEntity> mitemList;
    protected Context mContext;
    Realm realm;

    public IndivusualAdapter(Context context, ArrayList<UserEntity> itemList) {
        mContext = context;
        mitemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_community, parent, false);
        viewHolder = new UserViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder) {
            UserViewHolder userViewHolder = (UserViewHolder) holder;
             String image = (mitemList.get(position)).getAvatar();
             String name = (mitemList.get(position)).getUsername();

            userViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    realm = RealmController.getInstance().getRealm();
                    RealmController.getInstance().refresh();
                    String user_id_to = mitemList.get(position).getId();
                    String image_to = mitemList.get(position).getAvatar();
                    String name_to = mitemList.get(position).getUsername();
//                    RealmResults<Messages> realmResultsMessage = RealmController.getInstance().getMessageuserTo(user_id_to);
                    Bundle bundle = new Bundle();
                        bundle.putString("ImageMessage", image_to);
                        bundle.putString("NameMessage", name_to);
                        bundle.putString("User_Id_To", user_id_to);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
//                    realm = Realm.getDefaultInstance();
//                    String user_id_to = mitemList.get(position).getId();
//                    String image_to = mitemList.get(position).getImage();
//                    String name_to = mitemList.get(position).getName();
//                    RealmResults<Messages> realmResultsMessage = realm.where(Messages.class).equalTo("FromID", user_id_to).findAll();
//                    lstUserMessage.clear();
//                    lstUserMessage.addAll(realmResultsMessage);
//                    if (lstUserMessage.size() != 0) {
//                        Intent intent = new Intent(mContext, ChatsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("ImageMessage", image_to);
//                        bundle.putString("NameMessage", name_to);
//                        bundle.putString("User_Id_To", user_id_to);
//                        bundle.putString("User_Id_From", Config.USER_ID);
//                        bundle.putString("TokenUser", Config.TOKEN);
//                        Log.d("TestUserIdAdapter", "Result :" +"user_id_to :"+ user_id_to + " user_id_from :" + Config.USER_ID + " Token :" + Config.TOKEN);
//                        intent.putExtras(bundle);
//                        mContext.startActivity(intent);
//                    }else {
//                        Intent intent = new Intent(mContext, ChatsActivity.class);
//                        Bundle bundle = new Bundle();
//                    bundle.putString("ImageMessage", image_to);
//                    bundle.putString("NameMessage", name_to);
//                    bundle.putString("User_Id_To", user_id_to);
//                    bundle.putString("User_Id_From", Config.USER_ID);
//                    bundle.putString("TokenUser", Config.TOKEN);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                    }
                }
            });

            if (image == null || image.equals("null")) {
                String imgUrl = "http://together.codelovers.vn:8010/uploads/files/2016/09/30/57ee36b2a7c41.jpg";
                Glide.with(mContext).load(imgUrl).asBitmap().into(userViewHolder.image);
                if (name.equals("")) {
                    String Namedefault = "Nguyễn Khánh Linh";
                    userViewHolder.name.setText(Namedefault);
                }
            } else {
                Glide.with(mContext).load((mitemList.get(position)).getAvatar()).asBitmap().into(userViewHolder.image);
                userViewHolder.name.setText((mitemList.get(position)).getUsername());
            }
        } else {
            ((ProgressViewHolder) holder).pBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return this.mitemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public UserViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.imgUserindivisual);
            name = (TextView) v.findViewById(R.id.textviewNameUSerindivisual);
            ;
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
