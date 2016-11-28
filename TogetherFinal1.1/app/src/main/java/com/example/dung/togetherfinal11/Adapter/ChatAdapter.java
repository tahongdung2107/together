package com.example.dung.togetherfinal11.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
 * Created by dung on 22/11/2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Object> mObjects;
    public static final int Chatgroup = 0;
    public static final int Chatmodel = 1;
    public static final int Messages = 2;
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
            return Messages;
        return -1;
    }
    public ChatAdapter(Context context, List<Object> objects) {
        mContext = context;
        mObjects = objects;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        switch (viewType) {
            case Chatgroup:
                View itemView0 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatAdapter.ChatViewHolder(itemView0);
            case Chatmodel:
                View itemView1 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatAdapter.ChatViewHolder(itemView1);
            case Messages:
                View itemView2 = li.inflate(R.layout.items_chat, parent, false);
                return new ChatAdapter.ChatViewHolder(itemView2);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Chatgroup:
                ChatAdapter.ChatViewHolder chatgroupViewHolder = (ChatAdapter.ChatViewHolder) holder;
                break;
            case Chatmodel:
                ChatAdapter.ChatViewHolder chatmodelViewHolder = (ChatAdapter.ChatViewHolder) holder;
                String contentModel = ((ChatModel) mObjects.get(position)).getText_content();
                chatmodelViewHolder.chat.setText(contentModel);
                if (((ChatModel) mObjects.get(position)).isMine()) {
                    chatmodelViewHolder.chat.setBackgroundResource(R.drawable.bubble2);
                    chatmodelViewHolder.parent_layout.setGravity(Gravity.RIGHT);
                    chatmodelViewHolder.iconchatleft.setVisibility(View.GONE);
                    chatmodelViewHolder.iconchatright.setVisibility(View.VISIBLE);
                } else {
                    chatmodelViewHolder.chat.setBackgroundResource(R.drawable.bubble1);
                    chatmodelViewHolder.parent_layout.setGravity(Gravity.LEFT);
                    chatmodelViewHolder.iconchatleft.setVisibility(View.VISIBLE);
                    chatmodelViewHolder.iconchatright.setVisibility(View.GONE);
                }
                chatmodelViewHolder.chat.setTextColor(Color.BLACK);
                break;
            case Messages:
                ChatAdapter.ChatViewHolder messageViewHolder = (ChatAdapter.ChatViewHolder) holder;
                String contentMessages = ((Messages) mObjects.get(position)).getContent();
                messageViewHolder.chat.setText(contentMessages);
                if (((Messages) mObjects.get(position)).isMine() == false) {
                    messageViewHolder.chat.setBackgroundResource(R.drawable.bubble2);
                    messageViewHolder.parent_layout.setGravity(Gravity.RIGHT);
                    messageViewHolder.iconchatleft.setVisibility(View.GONE);
                    messageViewHolder.iconchatright.setVisibility(View.VISIBLE);
                } else {
                    messageViewHolder.chat.setBackgroundResource(R.drawable.bubble1);
                    messageViewHolder.parent_layout.setGravity(Gravity.LEFT);
                    messageViewHolder.iconchatleft.setVisibility(View.VISIBLE);
                    messageViewHolder.iconchatright.setVisibility(View.GONE);
                }
                messageViewHolder.chat.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chat;
        LinearLayout parent_layout;
        ImageView iconchatleft , iconchatright;
//        ImageButton btnPlayAudioChat;
//        SeekBar seekBarTimeAudio ;
//        TextView txtCurrentTime ;
//        LinearLayout parent_layoutaudio , getParent_layoutaudioChild;
        public ChatViewHolder(View itemView) {
            super(itemView);
            chat = (TextView) itemView.findViewById(R.id.textViewChat);
            iconchatleft = (ImageView) itemView.findViewById(R.id.iconLeft);
            iconchatright = (ImageView) itemView.findViewById(R.id.iconRight);
            parent_layout = (LinearLayout) itemView
                    .findViewById(R.id.items_chat);
//            parent_layoutaudio = (LinearLayout) itemView.findViewById(R.id.pair_chat_layout_parent);
//            getParent_layoutaudioChild = (LinearLayout) itemView.findViewById(R.id.pair_chat_layout_child);
//            btnPlayAudioChat = (ImageButton) itemView.findViewById(R.id.btnPlayAudioChat);
//            seekBarTimeAudio = (SeekBar) itemView.findViewById(R.id.seekBarTimeAudio);
//            txtCurrentTime = (TextView) itemView.findViewById(R.id.txtCurrentTime);
        }
    }
}
