package com.njcwking.box.scale.bus;

/**
 * Created by CM on 2016/4/22.
 */
public class Message {
    private String shortMsg;
    private Object object;

    public Message() {
    }

    public Message(String shortMsg, Object o) {
        this.shortMsg = shortMsg;
        this.object = o;
    }
    //getter and setter


    public String getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}