package com.example.trainadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button login;

    FirebaseAuth auth;
    EditText lgemail, lgpass;
    TextView signin,forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        lgemail = findViewById(R.id.editTextText2);
        lgpass = findViewById(R.id.editTextTextPassword2);
        login = findViewById(R.id.loginbtn);
        signin= findViewById(R.id.textView7);
        forgot = findViewById(R.id.textView6);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lgemail.getText().toString();
                String pass = lgpass.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Retrieve TrainName from the database
                                DatabaseReference trainRef = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("trainName");
                                trainRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d("Firebase", "onDataChange triggered");
                                        if (dataSnapshot.exists()) {
                                            String trainName = dataSnapshot.getValue(String.class);
                                            Log.d("trainName", "trainName: " + trainName);

                                            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("TrainName", trainName);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Login.this, "TrainName not found ", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("TrainName", "Test");
                                            startActivity(intent);
                                            finish();
                                            Log.d("Firebase", "DataSnapshot: " + dataSnapshot.getValue());
                                            Log.d("Firebase", "User UID: " + auth.getCurrentUser().getUid());


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(Login.this, "Database error", Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase", "Database error: " + databaseError.getMessage());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else{
                        lgpass.setError("Password Cannot be empty");
                    }
                }else if(email.isEmpty()){
                    lgemail.setError("Email Cannot be empty");
                }else{
                    lgemail.setError("Please enter valid email");
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}