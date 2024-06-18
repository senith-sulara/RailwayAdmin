package com.example.trainadmin.models;

public class MessageModel {
    private String msgId;
    private String senderId;
    private String message;

    public MessageModel(  String msgId,  String senderId,  String message){
        this.msgId=msgId;
        this.senderId=senderId;
        this.message=message;
    }
public  MessageModel(){}

    public void setMsgId(String msgId){
        this.msgId=msgId;
    }

    public String getMsgId(){
        return msgId;
    }
    public void setsenderId(String senderId){
        this.senderId=senderId;
    }

    public String getsenderId(){
        return senderId;
    }

    public void setsmessage(String message){
        this.message=message;
    }

    public String getmessage(){
        return message;
    }
}
