package com.example.mynewapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;

import de.hdodenhof.circleimageview.CircleImageView;
public class MessagesRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> msg_arr;

    MessagesRecyclerAdapter(Context con) {
        this.context = con;
        msg_arr=new ArrayList<>();
    }

    public void setMessages(ArrayList<Message> contacts)
    {
        this.msg_arr=contacts;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (msg_arr.get(position).getViewType()==0)
            return 0;
        else if (msg_arr.get(position).getViewType()==1)
            return 1;
        else
            return -1;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if (viewType==0)
        {
            v = LayoutInflater.from(context).inflate(R.layout.sender_message, parent, false);
            return new ViewHolderOne(v);
        }
        else if (viewType==1)
        {
            v = LayoutInflater.from(context).inflate(R.layout.receiver_message, parent, false);
            return new ViewHolderTwo(v);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message obj =msg_arr.get(position);

        if (obj!=null)
        {
           if (obj.getViewType()==0)
           {
               ((ViewHolderOne)holder).sender_img.setImageBitmap(extractImage(obj.img));
               ((ViewHolderOne)holder).sender_msg.setText(obj.message_body);
               ((ViewHolderOne)holder).sender_name.setText(obj.Username);
               ((ViewHolderOne)holder).sender_time.setText(obj.timeStamp);
           }
           else if(obj.getViewType()==1)
           {
               ((ViewHolderTwo)holder).receiver_img.setImageBitmap(extractImage(obj.img));
               ((ViewHolderTwo)holder).receiver_msg.setText(obj.message_body);
               ((ViewHolderTwo)holder).receiver_name.setText(obj.Username);
               ((ViewHolderTwo)holder).receiver_time.setText(obj.timeStamp);
           }
        }

    }

    @Override
    public int getItemCount() {
        return msg_arr.size();
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView sender_name, sender_msg, sender_time;
        CircleImageView sender_img;
        public ViewHolderOne(@NonNull View viewItem) {
            super(viewItem);
            sender_name = itemView.findViewById(R.id.textView1);
            sender_msg = itemView.findViewById(R.id.textView2);
            sender_time = itemView.findViewById(R.id.timeStampId);
            sender_img = itemView.findViewById(R.id.imgContact);

        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView receiver_name, receiver_msg, receiver_time;
        CircleImageView receiver_img;
        public ViewHolderTwo(@NonNull View viewItem) {
            super(viewItem);
            receiver_name = itemView.findViewById(R.id.textView3);
            receiver_msg = itemView.findViewById(R.id.textView4);
            receiver_time = itemView.findViewById(R.id.timeStampId2);
            receiver_img = itemView.findViewById(R.id.imgContact2);
        }
    }

    public Bitmap extractImage(String img)
    {
        byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

}
