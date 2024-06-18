package com.example.trainadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.trainadmin.Adapters.NewsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//public class ShowNews extends AppCompatActivity {
//    private Button addNews;
//    private DatabaseReference newsRef;
//    private LinearLayout newsContainer;
//
//    private RecyclerView newsview;
//
//    private Spinner newsSubjectSpinner;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news);
//
//        newsRef = FirebaseDatabase.getInstance().getReference().child("news");
//        newsContainer = findViewById(R.id.newsRecyclerView);
//        addNews=findViewById(R.id.addNewsBtn);
//        // Load news items from Firebase
//
//
//        // Set click listener for addNews button
//        addNews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to the news activity page
//                Intent intent = new Intent(ShowNews.this, News.class);
//                startActivity(intent);
//            }
//        });
//        loadNewsItems();
//    }
//
//
//    private void loadNewsItems() {
//        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    // Get news data
//                    String newsSubject = snapshot.child("subject").getValue(String.class);
//                    String newsContent = snapshot.child("content").getValue(String.class);
//
//                    // Create view for each news item
//                    View newsItemView = getLayoutInflater().inflate(R.layout.item_news, null);
//                    TextView newsSubjectTextView = newsItemView.findViewById(R.id.newsSubjectTextView);
//                    TextView newsContentTextView = newsItemView.findViewById(R.id.newsContentTextView);
//                    Button editNewsBtn = newsItemView.findViewById(R.id.editNewsBtn);
//                    Button deleteNewsBtn = newsItemView.findViewById(R.id.deleteNewsBtn);
//
//                    // Set news data to views
//                    newsSubjectTextView.setText(newsSubject);
//                    newsContentTextView.setText(newsContent);
//
//                    // Set click listener for edit button
//                    // Set click listener for edit button
//                    editNewsBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // Enable editing for newsSubjectTextView and newsContentTextView
//                            newsSubjectTextView.setFocusableInTouchMode(true);
//                            newsContentTextView.setFocusableInTouchMode(true);
//
//                            // Show soft keyboard
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(newsSubjectTextView, InputMethodManager.SHOW_IMPLICIT);
//
//                            // Change edit button text to save
//                            // Initially outside the listener
//                            editNewsBtn.setText("Edit");
//
//                            editNewsBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if ("Edit".equals(editNewsBtn.getText().toString())) {
//                                        // Enable editing
//                                        newsSubjectTextView.setFocusableInTouchMode(true);
//                                        newsContentTextView.setFocusableInTouchMode(true);
//                                        newsSubjectTextView.setFocusable(true);
//                                        newsContentTextView.setFocusable(true);
//
//                                        newsSubjectTextView.requestFocus();
//
//                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        imm.showSoftInput(newsSubjectTextView, InputMethodManager.SHOW_FORCED);
//
//                                        editNewsBtn.setText("Save");
//                                    } else {
//                                        // Assume you save changes here
//
//                                        // Disable editing
//                                        newsSubjectTextView.setFocusable(false);
//                                        newsContentTextView.setFocusable(false);
//
//                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        imm.hideSoftInputFromWindow(newsSubjectTextView.getWindowToken(), 0);
//
//                                        editNewsBtn.setText("Edit");
//
//                                        Toast.makeText(ShowNews.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//
//
//
//
//                        }
//                    });
//
//
//                    // Set click listener for delete button
//                    deleteNewsBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // Handle delete news button click
//                            // Remove news item from Firebase
//                            snapshot.getRef().removeValue();
//                            // Remove view from layout
//                            newsContainer.removeView(newsItemView);
//                        }
//                    });
//
//                    // Add news item view to layout
//                    newsContainer.addView(newsItemView);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle database error
//            }
//        });
//    }
//
//    private void saveChanges(TextView newsSubjectTextView, TextView newsContentTextView, Button editNewsBtn, DataSnapshot snapshot) {
//        // Get the updated subject and content
//        String updatedSubject = newsSubjectTextView.getText().toString();
//        String updatedContent = newsContentTextView.getText().toString();
//
//        // Perform the save operation (e.g., update Firebase database)
//
//        // Disable editing mode
//        newsSubjectTextView.setFocusable(false);
//        newsContentTextView.setFocusable(false);
//
//        // Hide soft keyboard
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(newsSubjectTextView.getWindowToken(), 0);
//
//        // Reset the button text to "Edit" for next edit action
//        editNewsBtn.setText("Edit");
//
//        // Reset the click listener to handle the next edit action
//        editNewsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Enable editing for newsSubjectTextView and newsContentTextView
//                newsSubjectTextView.setFocusableInTouchMode(true);
//                newsContentTextView.setFocusableInTouchMode(true);
//
//                // Show soft keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(newsSubjectTextView, InputMethodManager.SHOW_IMPLICIT);
//
//                // Change edit button text to save
//                editNewsBtn.setText("Save");
//
//                // Change edit button click listener to save changes
//                editNewsBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        saveChanges(newsSubjectTextView, newsContentTextView, editNewsBtn, snapshot);
//                    }
//                });
//            }
//        });
//    }
//}


public class ShowNews extends AppCompatActivity {
    private DatabaseReference newsRef;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private Button addNews;
    private ImageView home, msg,profile,shc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsRef = FirebaseDatabase.getInstance().getReference().child("news");
        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addNews=findViewById(R.id.addNewsBtn);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNewsItems();

            // After the refresh action is complete, call setRefreshing(false) to indicate that the refresh operation is finished
            swipeRefreshLayout.setRefreshing(false);
        });


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

        // Set click listener for addNews button
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the news activity page
                Intent intent = new Intent(ShowNews.this, News.class);
                startActivity(intent);
            }
        });
        loadNewsItems();
    }

    private void loadNewsItems() {
        newsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataSnapshot> newsList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    newsList.add(snapshot);
                }
                newsAdapter = new NewsAdapter(newsList, newsRef);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
