package com.example.dung.togetherfinal11.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dung.togetherfinal11.Model.ChatModel;
import com.example.dung.togetherfinal11.Model.GroupChatMessage;
import com.example.dung.togetherfinal11.Model.Messages;
import com.example.dung.togetherfinal11.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dung on 25/11/2016.
 */

public class ChatgroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Object> mObjects;
    public static final int Chatgroup = 0;
    public static final int Chatmodel = 1;
    public static final int Message = 2;
    Matcher m;
    Pattern p;
    Handler myHandler = new Handler();
    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof GroupChatMessage)
            return Chatgroup;
        else if (mObjects.get(position) instanceof ChatModel)
            return Chatmodel;
        else if (mObjects.get(position) instanceof Messages)
            return Message;
        return -1;
    }
    public ChatgroupAdapter(Context context, List<Object> objects) {
        mContext = context;
        mObjects = objects;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        switch (viewType) {
            case Chatgroup:
                View itemView0 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatgroupAdapter.ChatGroupViewHolder(itemView0);
            case Chatmodel:
                View itemView1 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatgroupAdapter.ChatGroupViewHolder(itemView1);
            case Message:
                View itemView2 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatgroupAdapter.ChatGroupViewHolder(itemView2);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Chatgroup:
                break;
            case Chatmodel:
                break;
            case  Message:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }
    public class ChatGroupViewHolder extends RecyclerView.ViewHolder {
        TextView chat;
        LinearLayout parent_layout;
        ImageView iconchatleft , iconchatright;
        public ChatGroupViewHolder(View itemView) {
            super(itemView);
            chat = (TextView) itemView.findViewById(R.id.textViewChat);
            chat = (TextView) itemView.findViewById(R.id.textViewChat);
            iconchatleft = (ImageView) itemView.findViewById(R.id.iconLeft);
            iconchatright = (ImageView) itemView.findViewById(R.id.iconRight);
            parent_layout = (LinearLayout) itemView
                    .findViewById(R.id.items_chat);
        }
    }
}
