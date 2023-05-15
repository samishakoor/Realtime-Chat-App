package com.example.mynewapplication;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import android.widget.Filterable;
import android.text.Spannable;
import android.graphics.Bitmap;
public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder>
{

    Context context;
    ArrayList<Contact> contacts_arr;

    RecyclerContactAdapter(Context con) {
        this.context = con;
        contacts_arr=new ArrayList<>();
    }

    public void setContacts(ArrayList<Contact> filtered_contacts)
    {
      this.contacts_arr=filtered_contacts;
      notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.conversation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int pos=holder.getAdapterPosition();

        holder.img.setImageBitmap(extractImage(contacts_arr.get(pos).img));
        holder.name.setText(contacts_arr.get(pos).Username);
        holder.body.setText(contacts_arr.get(pos).body);
        holder.time.setText(contacts_arr.get(pos).timeStamp);

       openConversation(holder);
    }

    @Override
    public int getItemCount() {
        return contacts_arr.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, body,time;
        CircleImageView img;
        LinearLayout clickable_recycler_item;

        public ViewHolder(@NonNull View viewItem) {
            super(viewItem);

            name = itemView.findViewById(R.id.userConversation);
            body = itemView.findViewById(R.id.bodyConversation);
            time = itemView.findViewById(R.id.timeStampConversation);
            img = itemView.findViewById(R.id.imgConversation);
            clickable_recycler_item=itemView.findViewById(R.id.conversationCard);
        }
    }

    public Bitmap extractImage(String img)
    {
        byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }


    private void openConversation(@NonNull ViewHolder holder){
        holder.clickable_recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact obj=contacts_arr.get(holder.getAdapterPosition());
                Intent intent=new Intent(context,MessagesActivity.class);
                intent.putExtra("contact_name",obj.Username);
                intent.putExtra("contact_id",obj.contactId);
                intent.putExtra("contact_img",obj.img);
                ((Activity )context).startActivityForResult(intent,1);
            }
        });}



}

