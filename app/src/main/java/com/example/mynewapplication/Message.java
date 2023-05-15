package com.example.mynewapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class Message {
    public String message_id;
    public String img;
    public String Username;
    public String message_body;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatTime;
    public String timeStamp;
    private Date date;
    public String device_id;
    private int viewType;



    public Message()
    {
    }
    public Message(String msg_body,String msg_time,String msg_img, String msg_Username)
    {
        this.Username = msg_Username;
        this.message_body=msg_body;
        this.img = msg_img;
        this.timeStamp = msg_time;
    }


    @SuppressLint("SimpleDateFormat")
    public Message(String id,String d_id, String m_body)
    {
        this.message_id=id;
        this.device_id=d_id;
        this.message_body=m_body;
        date = new Date();
        formatTime = new SimpleDateFormat("hh:mm aa");
        this.timeStamp = formatTime.format(date);
    }

    public void setViewType(int vt)
    {
        this.viewType=vt;
    }
    public int getViewType()
    {
        return viewType;
    }

}
