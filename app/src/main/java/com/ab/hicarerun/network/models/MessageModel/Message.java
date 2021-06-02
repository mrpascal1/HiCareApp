package com.ab.hicarerun.network.models.MessageModel;

/**
 * Created by Arjun Bhatt on 5/9/2020.
 */
public class Message {
    public String text; // message bodyprivate boolean belongsToCurrentUser; // is this message sent by us?
    private boolean belongsToCurrentUser;
    public int type;

    public static final int TYPE_MINE =0;
    public static final int TYPE_THEIR =1;

    public Message(String text, boolean belongsToCurrentUser, int type) {
        this.text = text;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.type = type;
    }


//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public boolean isBelongsToCurrentUser() {
//        return belongsToCurrentUser;
//    }
//
//    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
//        this.belongsToCurrentUser = belongsToCurrentUser;
//    }
}
