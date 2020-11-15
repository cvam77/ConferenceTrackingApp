package com.example.conferencetrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    EditText mEdittextName, mEdittextEmailAddress, mEdittextRole;
    Button mSaveButton;

    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEdittextName = findViewById(R.id.etName);
        mEdittextEmailAddress = findViewById(R.id.etEmailAddress);
        mEdittextRole = findViewById(R.id.etRole);

        loadAndFillViews();
        mSaveButton = findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mEdittextName.getText().toString();
                String userEmailAddress = mEdittextEmailAddress.getText().toString();
                String role = mEdittextRole.getText().toString();

                if(userName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Name field cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else if(userEmailAddress.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Email Address field cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else if(role.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Role field cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String id = currentUser.getUid();
                    mDatabaseRef.child("users").child(id).child("name").setValue(userName);
                    mDatabaseRef.child("users").child(id).child("email").setValue(userEmailAddress);
                    mDatabaseRef.child("users").child(id).child("role").setValue(role);
                }

                Intent intent = new Intent(getApplicationContext(),home_screen.class);
                startActivity(intent);
            }
        });
    }

    private void loadAndFillViews()
    {
        mDatabaseRef.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists())
                {
                    if(snapshot.getKey().equals(currentUser.getUid()))
                    {
                        for(DataSnapshot childSnapshot : snapshot.getChildren())
                        {
                            if(childSnapshot.getKey().equals("name"))
                            {
                                String name = childSnapshot.getValue().toString();
                                if(!name.isEmpty())
                                {
                                    mEdittextName.setText(name);
                                }

                            }

                            if(childSnapshot.getKey().equals("email"))
                            {
                                String email = childSnapshot.getValue().toString();
                                if(!email.isEmpty())
                                {
                                    mEdittextEmailAddress.setText(email);
                                }

                            }
                            if(childSnapshot.getKey().equals("role"))
                            {
                                String role = childSnapshot.getValue().toString();
                                if(!role.isEmpty())
                                {
                                    mEdittextRole.setText(role);
                                }

                            }
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}