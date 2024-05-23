package com.example.connect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import Adapters.MessageAdapter;
import Models.MessageClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {

    String name, receiverUID, receiverImg, senderUID;
    CircleImageView profile;
    TextView receiverName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    public static String senderImg;
    public static String receiverIImg;
    String senderRoom, receiverRoom;
    RecyclerView mmessagesAdapter;
    ArrayList<MessageClass> messageClassArrayList;
    MessageAdapter messageAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize views
        mmessagesAdapter = findViewById(R.id.msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagesAdapter.setLayoutManager(linearLayoutManager);
        messageClassArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChatWindow.this, messageClassArrayList);
        mmessagesAdapter.setAdapter(messageAdapter);

        name = getIntent().getStringExtra("name");
        receiverUID = getIntent().getStringExtra("uid");
        // receiverImg = getIntent().getStringExtra("receiverImg");

        sendbtn = findViewById(R.id.sendbtn);
        textmsg = findViewById(R.id.textmsg);
        profile = findViewById(R.id.profileimg);
        receiverName = findViewById(R.id.receiverName);

        // Picasso.get().load(receiverImg).into(profile);
        receiverName.setText(name);

        // Get current user UID
        senderUID = firebaseAuth.getCurrentUser().getUid();
        senderRoom = senderUID + receiverUID;
        receiverRoom = receiverUID + senderUID;

        DatabaseReference chatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageClassArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageClass messages = dataSnapshot.getValue(MessageClass.class);
                    messageClassArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatWindow.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                MessageClass messages = new MessageClass(message, senderUID, date.getTime());

                DatabaseReference senderRoomRef = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");
                DatabaseReference receiverRoomRef = firebaseDatabase.getReference().child("chats").child(receiverRoom).child("messages");

                senderRoomRef.push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            receiverRoomRef.push().setValue(messages);
                        } else {
                            Toast.makeText(ChatWindow.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
