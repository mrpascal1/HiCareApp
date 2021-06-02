package com.ab.hicarerun.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutFlexItemBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.MessageModel.Message;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun Bhatt on 5/11/2020.
 */
public class MessageItemAdapter extends RecyclerView.Adapter {

    private ArrayList<Message> items = new ArrayList<>();
    Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView imgProfile;

        MyViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.message_body);
        }
    }

    public static class TheirViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView image_message_profile;

        TheirViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.message_body);
            this.image_message_profile = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }
    }

    public MessageItemAdapter(Context context) {
        this.mContext = context;
//        type = items.size();
    }

    public void add(Message message) {
        this.items.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Message.TYPE_MINE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_message, parent, false);
                return new MyViewHolder(view);
            case Message.TYPE_THEIR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_their_message, parent, false);
                return new TheirViewHolder(view);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (items.get(position).type) {
            case 0:
                return Message.TYPE_MINE;
            case 1:
                return Message.TYPE_THEIR;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, final int listPosition) {

        Message object = items.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Message.TYPE_MINE:
                    ((MyViewHolder) holder).txtType.setText(object.text);

                    break;
                case Message.TYPE_THEIR:
                    ((TheirViewHolder) holder).txtType.setText(object.text);
                    ((TheirViewHolder) holder).image_message_profile.setImageResource(R.mipmap.logo);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}