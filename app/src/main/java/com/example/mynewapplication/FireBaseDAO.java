package com.example.mynewapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;
import java.util.HashMap;

public class FireBaseDAO {
    private Context context;
    public FireBaseDAO(Context con) {
        this.context = con;
    }
    public boolean AddContact(Contact obj) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");
        String userId = usersRef.push().getKey();
        Contact contact = new Contact(userId, obj.img, obj.Username,obj.ph_no, obj.body);
        usersRef.child(userId).setValue(contact);
        return true;
    }
    public boolean AddMessage(Message obj,String contactId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(contactId);
        DatabaseReference messagesRef = userRef.child("messages");
        String messageId = messagesRef.push().getKey();
        Message message = new Message(messageId,obj.device_id, obj.message_body);
        messagesRef.child(messageId).setValue(message);
        return true;
    }

    public void updateContact(String c_id,String contact_body,String contact_timeStamp)
    {
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference().
                child("users")
                .child(c_id);
        Map<String, Object> updates = new HashMap<>();
        updates.put("body",contact_body);
        updates.put("timeStamp", contact_timeStamp);
        messageRef.updateChildren(updates);
    }

    public interface MessagesListener
    {
        void onMessagesLoaded(ArrayList<Message> messages);
    }
    public void LoadMessages( String contactId,MessagesListener listener)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef = database.getReference("users/"+contactId+"/messages");
        ArrayList<Message> messagesList = new ArrayList<>();
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message obj = snapshot.getValue(Message.class);
                    messagesList.add(obj);
                }
                listener.onMessagesLoaded(messagesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public interface ContactsListener
    {
        void onContactsLoaded(ArrayList<Contact> contacts);
    }
    public void LoadContacts(ContactsListener listener) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users");

        ArrayList<Contact> contacts = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contact obj = snapshot.getValue(Contact.class);
                    contacts.add(obj);
                }
                listener.onContactsLoaded(contacts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

    }


}