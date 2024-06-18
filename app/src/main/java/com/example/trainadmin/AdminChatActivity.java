package com.example.trainadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.trainadmin.Adapters.MessageAdapter;
import com.example.trainadmin.databinding.ActivityChatBinding;
import com.example.trainadmin.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String receiverId;
    DatabaseReference databaseReference;
    String senderRoom, receiverRoom;
    MessageAdapter messageAdapter;
    List<MessageModel> messageList;
    private ImageView home, msg,profile,shc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inside onCreate method
        receiverId = getIntent().getStringExtra("id");

        home = findViewById(R.id.imageView5);
        msg = findViewById(R.id.imageView6);
        profile = findViewById(R.id.imageView7);
        shc = findViewById(R.id.imageView8);
        String userKey = getIntent().getStringExtra("userKey");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomerDetails.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserD.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });
        shc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainSchedule.class);
                intent.putExtra("userKey", userKey);
                finish();
                startActivity(intent);
            }
        });

        senderRoom = receiverId + FirebaseAuth.getInstance().getUid();
        receiverRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        databaseReference = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        binding.recycler.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(linearLayoutManager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messageList.add(messageModel);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendMessageLayout.setVisibility(View.VISIBLE);

        // Set click listener for the send button
        binding.sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message from the EditText
                String messageText = binding.messageEd.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Create a unique message ID
                    String messageId = databaseReference.push().getKey();
                    // Create a MessageModel object with sender ID, message, and message ID
                    MessageModel message = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), messageText);
                    // Push the message to the database
                    databaseReference.child(messageId).setValue(message);
                    // Clear the EditText after sending the message
                    binding.messageEd.setText("");
                    // Display a toast message indicating the message was sent
                    Toast.makeText(AdminChatActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}