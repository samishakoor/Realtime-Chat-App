package com.example.mynewapplication;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
public class MessagesActivity extends AppCompatActivity implements FireBaseDAO.MessagesListener{

    RecyclerView recycler;
    MessagesRecyclerAdapter adapter;
    ArrayList<Message> messages_arr;

    FireBaseDAO DB;
    String last_message=null;
    String last_message_time=null;
    String myDeviceId;
    String contact_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        DB=new FireBaseDAO(this);

        recycler = findViewById(R.id.recyclerview2);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessagesRecyclerAdapter(MessagesActivity.this);
        recycler.setAdapter(adapter);

        myDeviceId=Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        contact_id=getIntent().getStringExtra("contact_id");
        DB.LoadMessages(contact_id, this);

        EditText edtMessage = findViewById(R.id.createMessage);
        Button btnAction = findViewById(R.id.send_message);

        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String actualMessage = edtMessage.getText().toString();

                String msg="";

                if (!actualMessage.equals("")) {
                    msg = actualMessage;
                } else {
                    Toast.makeText(MessagesActivity.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                }

                if (!msg.equals("")) {
                    Message obj = new Message(null,myDeviceId,msg);
                    boolean saveSuccess=DB.AddMessage(obj,contact_id);
                    if(saveSuccess) {
                        DB.LoadMessages(contact_id, MessagesActivity.this);
                    }
                    edtMessage.setText("");
                }
            }
        });

    }

    @Override
    public void onMessagesLoaded(ArrayList<Message> messages)
    {

     ArrayList<Message> messagesList=new ArrayList<>();
     for(Message msg:messages)
     {
         String senderDeviceId=msg.device_id;
         Message message=null;

         if (senderDeviceId!=null && senderDeviceId.equals(myDeviceId))
         {
             String SenderImage = Base64.encodeToString(extractSenderImage(), Base64.DEFAULT);
             String SenderName="You";
             message=new Message(msg.message_body,msg.timeStamp,SenderImage,SenderName);
             message.setViewType(0);
         }
         else
         {
             String receiver_name=getIntent().getStringExtra("contact_name");
             String receiver_img=getIntent().getStringExtra("contact_img");
             message=new Message(msg.message_body,msg.timeStamp,receiver_img,receiver_name);
             message.setViewType(1);
         }

         if(message!=null)
         {
             last_message=message.message_body;
             last_message_time=message.timeStamp;
             messagesList.add(message);
         }
     }

        messages_arr=new ArrayList<>();
        messages_arr.addAll(messagesList);
        adapter.setMessages(messages_arr);
        adapter.notifyDataSetChanged();
        recycler.scrollToPosition(messages_arr.size() - 1);

    }


    public byte[] extractSenderImage()
    {
        Uri selectedImageUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.sender);
        Bitmap img = null;
        try {
            img = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] imageBytes = stream.toByteArray();
        return imageBytes;
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("last_message_body", last_message);
        intent.putExtra("contact_id",contact_id);
        intent.putExtra("msg_timeStamp",last_message_time);
        setResult(RESULT_OK,intent);
        finish();
    }



}


