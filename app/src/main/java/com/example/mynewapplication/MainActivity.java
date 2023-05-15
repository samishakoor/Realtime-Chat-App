package com.example.mynewapplication;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.app.Dialog;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import androidx.appcompat.widget.SearchView;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.util.Log;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements FireBaseDAO.ContactsListener {

    RecyclerView recycler;
    RecyclerContactAdapter adapter;
    ArrayList<Contact> contacts_arr;
    FireBaseDAO DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DB=new FireBaseDAO(this);

        recycler = findViewById(R.id.recyclerview1);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerContactAdapter(MainActivity.this);
        recycler.setAdapter(adapter);

        DB.LoadContacts(this);
        addContact(adapter,recycler);

    }


    @Override
    public void onContactsLoaded(ArrayList<Contact> contacts) {
        contacts_arr=new ArrayList<>();
        contacts_arr.addAll(contacts);
        adapter.setContacts(contacts_arr);
        adapter.notifyDataSetChanged();
        recycler.scrollToPosition(contacts_arr.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //getting response from messages activity
        if(requestCode == 1 )
        {
            if(resultCode==RESULT_OK )
            {
                if(data!=null) {
                    String lastMessageBody = data.getStringExtra("last_message_body");
                    String messageTime = data.getStringExtra("msg_timeStamp");
                    String contactId = data.getStringExtra("contact_id");

                    if (lastMessageBody != null && messageTime != null) {

                        DB.updateContact(contactId, lastMessageBody, messageTime);
                        DB.LoadContacts(MainActivity.this);
                    }
                }
            }
            if(resultCode == RESULT_CANCELED)
            {
            }
        }

        //getting response from contacts application
        if (requestCode == 2 && resultCode == RESULT_OK && data!=null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = this.getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                if (cursor.getCount() == 0) return;
                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (contactName != null)
                {
                    if(!checkContactAlreadyExists(contactName))
                    {
                        String UserName = contactName;

                        String user_phoneNumber = null;
                        Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
                        while (phoneCursor.moveToNext()) {
                            user_phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        }
                        phoneCursor.close();

                        String image = Base64.encodeToString(extractImage(), Base64.DEFAULT);
                        String body = "No Messages Yet";
                        String conversation_id = null;

                        Contact contact = new Contact(conversation_id, image, UserName,user_phoneNumber, body);
                        boolean saveSuccess = DB.AddContact(contact);
                        if (saveSuccess) {
                            DB.LoadContacts(MainActivity.this);
                            Toast.makeText(MainActivity.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Contact not added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Contact already exists!", Toast.LENGTH_SHORT).show();
                    }
                    contactName = null;
               }
            }
            finally
            {
                cursor.close();
             }
        }
    }

    private void addContact(RecyclerContactAdapter adapter ,RecyclerView recycler)
    {
        FloatingActionButton btnOpenContacts=findViewById(R.id.btnOpenContacts);
        btnOpenContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent contactsIntent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
                if (contactsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(contactsIntent,2);
                }
            }
        });
    }

    public boolean checkContactAlreadyExists(String contactName)
    {
        for(Contact contact:contacts_arr)
        {
            if (contact.Username.equals(contactName))
            {
                return true;
            }
        }
        return false;
    }
    public byte[] extractImage()
    {
        Uri selectedImageUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.user);
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


}






