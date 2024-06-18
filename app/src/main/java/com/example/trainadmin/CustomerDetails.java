package com.example.trainadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainadmin.Adapters.UserAdapter;
import com.example.trainadmin.databinding.ActivityChatBinding;
import com.example.trainadmin.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetails extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private RecyclerView userRecyclerView;
    private DatabaseReference usersRef;
    private List<User> userList;
    private UserAdapter userAdapter;
    private ImageView home, msg,profile,shc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this); // Pass 'this' as the listener
        userRecyclerView.setAdapter(userAdapter);


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

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference().child("usersC");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                  // String iconUrl = snapshot.child("iconUrl").getValue(String.class);
                    // Assuming you have the user ID available in a variable called userId
                    String userId = snapshot.getKey(); // or however you get the user ID

// Modify the constructor call to include both the ID and username
                    userList.add(new User(userId, username));

                 //   userList.add(new User(username));
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
    // Implement the interface method
    @Override
    public void onUserClick(User user) {
        // Handle click event here
        // Start AdminChatActivity with necessary data
      /*  Intent intent = new Intent(this, AdminChatActivity.class);
        intent.putExtra("username", user.getUsername());
        startActivity(intent);*/
        Intent intent = new Intent(this, AdminChatActivity.class);
        intent.putExtra("id", user.getId()); // Pass the user's ID
        startActivity(intent);
    }


}
