package com.example.mynewapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contact {
    public String img;
    public String ph_no;
    public String Username;
    public String body;
    private Date date;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatTime;
    public String contactId;
    public String timeStamp;
    public Contact()
    {

    }
    @SuppressLint("SimpleDateFormat")
    public Contact(String c_id,String img, String name, String phone_number,String body) {
        this.contactId=c_id;
        this.ph_no=phone_number;
        this.Username = name;
        this.img = img;
        this.body = body;
        date = new Date();
        formatTime = new SimpleDateFormat("hh:mm aa");
        this.timeStamp = formatTime.format(date);
    }
}

