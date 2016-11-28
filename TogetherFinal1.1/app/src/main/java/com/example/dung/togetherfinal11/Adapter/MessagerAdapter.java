package com.example.dung.togetherfinal11.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.Model.TeamEntity;
import com.example.dung.togetherfinal11.Model.UserEntity;
import com.example.dung.togetherfinal11.R;
import com.example.dung.togetherfinal11.Realm.RealmController;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dung on 23/11/2016.
 */

public class MessagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Object> mObjects;
    public static final int INDIVISUAL = 2;
    public static final int TEAM = 0;
    public static final int MESSAGER = 1;
    String type,content,name,image,namefault,imagedefault,user_id_to;
    Realm realm;
    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof TeamEntity)
            return TEAM;
        else if (mObjects.get(position) instanceof Messages)
            return MESSAGER;
        else if (mObjects.get(position) instanceof UserEntity)
            return INDIVISUAL;
        return -1;

    }
    public MessagerAdapter(Context context, List<Object> objects) {
        mContext = context;
        mObjects = objects;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        switch (viewType) {
            case TEAM:
                View itemView0 = li.inflate(R.layout.items_messagerteam, parent, false);
                return new TeamViewHolder(itemView0);
            case MESSAGER:
                View itemView1 = li.inflate(R.layout.item_inviteteam, parent, false);
                return new MessageViewHolder(itemView1);
            case INDIVISUAL:
                View itemView2 = li.inflate(R.layout.user_community, parent, false);
                return new IndivisualViewHolder(itemView2);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TEAM:
                break;
            case MESSAGER:
                MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                type = ((Messages) mObjects.get(position)).getType();
                content = ((Messages) mObjects.get(position)).getContent();
                name = ((Messages) mObjects.get(position)).getName();
                image = ((Messages) mObjects.get(position)).getUrlImageAvatar();
                messageViewHolder.cancel.setTag(position);
                messageViewHolder.accept.setTag(position);
                Log.d("MessagerTest","Result :"  + name);
                if (type.equals("text")) {
                    messageViewHolder.accept.setVisibility(View.GONE);
                    messageViewHolder.cancel.setVisibility(View.GONE);
                    messageViewHolder.contentinvite.setVisibility(View.GONE);
                    messageViewHolder.contentmessager.setVisibility(View.VISIBLE);
                    messageViewHolder.contentmessager.setText(content);
                    if (image == (null) || image.equals("null") || image.equals("")) {
                        imagedefault = "http://together.codelovers.vn:8010/uploads/files/2016/09/30/57ee36b2a7c41.jpg";
                        Glide.with(mContext).load(imagedefault).asBitmap().into(messageViewHolder.imageAvatar);
                    }
                    if (name == (null) || name.equals("null") || name.equals("")){
                        namefault = "Dung";
                        messageViewHolder.nameUser.setText(namefault);
                    }
                }else if (type.equals("invite_to_team")){
                    messageViewHolder.accept.setVisibility(View.VISIBLE);
                    messageViewHolder.cancel.setVisibility(View.VISIBLE);
                    messageViewHolder.contentinvite.setVisibility(View.VISIBLE);
                    messageViewHolder.contentmessager.setVisibility(View.GONE);
                    if (image == (null) || image.equals("null") || image.equals("")) {
                        imagedefault = "http://together.codelovers.vn:8010/uploads/files/2016/09/30/57ee36b2a7c41.jpg";
                        Glide.with(mContext).load(imagedefault).asBitmap().into(messageViewHolder.imageAvatar);
                    }
                    if (name == (null) || name.equals("null") || name.equals("")){
                        namefault = "Dung";
                        messageViewHolder.nameUser.setText(namefault);
                    }
                    messageViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int location = (int) view.getTag();
                            user_id_to = ((Messages) mObjects.get(location)).getFromID();
                            realm = RealmController.getInstance().getRealm();
                            RealmController.getInstance().refresh();
                            RealmResults<ChatModel> realmResultsModel = RealmController.getInstance().getmediatype("invite_to_team",user_id_to);
                            realm.beginTransaction();
                            realmResultsModel.deleteAllFromRealm();
                            realm.commitTransaction();
                        }
                    });
                }
                break;
            case INDIVISUAL:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }
    public class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView notifine,nameTeam,contentMessage;

        public TeamViewHolder(View itemView) {
            super(itemView);
            imageAvatar = (ImageView) itemView.findViewById(R.id.imAgvatar);
            nameTeam = (TextView) itemView.findViewById(R.id.txtName);
            notifine = (TextView) itemView.findViewById(R.id.txtMissMessage);
            contentMessage = (TextView) itemView.findViewById(R.id.txtContent);
        }
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView nameUser,contentmessager,contentinvite;
        Button accept,cancel;

        public MessageViewHolder(View itemView) {
            super(itemView);
            imageAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            nameUser = (TextView) itemView.findViewById(R.id.txtName);
            accept = (Button) itemView.findViewById(R.id.btnAccept);
            cancel = (Button) itemView.findViewById(R.id.btnCancel);
            contentmessager = (TextView) itemView.findViewById(R.id.txtContent);
            contentinvite = (TextView) itemView.findViewById(R.id.txtContentInvite);
        }
    }
    public class IndivisualViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public IndivisualViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgUserindivisual);
            name = (TextView) itemView.findViewById(R.id.textviewNameUSerindivisual);
        }
    }
}
