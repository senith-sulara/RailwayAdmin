package com.example.trainadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email, password, conpass;
    Spinner tname;
    Button sinbtn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        email = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextTextPassword);
        conpass = findViewById(R.id.editTextTextPassword3);
        sinbtn = findViewById(R.id.signbtn);
        tname = findViewById(R.id.spinner);

        // Dummy data for train names
        String[] trainNames = {"Rajina", "Udarata Manike", "Ruhunu Kumari", "Yal Devi"};

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trainNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tname.setAdapter(adapter);


        sinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eml = email.getText().toString().trim();
                String psw = password.getText().toString().trim();
                String cpsw = conpass.getText().toString().trim();
                String train = tname.getSelectedItem().toString();

                if(eml.isEmpty()){
                    email.setError("Email Cannot be empty");
                }
                if(psw.isEmpty()){
                    password.setError("Password Cannot be empty");
                }
                if(!psw.equals(cpsw)){
                    conpass.setError("Password should be matched");
                }else{
                    auth.createUserWithEmailAndPassword(eml, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // Save user data to database
                                String userId = auth.getCurrentUser().getUid();
                                DatabaseReference currentUserDB = databaseReference.child(userId);
                                currentUserDB.child("email").setValue(eml);
                                currentUserDB.child("trainName").setValue(train);

                                Toast.makeText(RegisterActivity.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, Login.class));
                            }else{
                                Toast.makeText(RegisterActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });



    }
}