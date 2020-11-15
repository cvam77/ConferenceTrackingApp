package com.example.conferencetrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class home_screen extends AppCompatActivity {

    private Button mLogoutButton;
    Button mSetProfileButton, mSearchConfButton, mCreateConferenceButton, mMyConferencesButton,
            mBrowseConferenceButton, mSavedConferenceButton;

    EditText mSearchWordConfEt;

    //FirebaseAuth variable
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseRefForBrowsing = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> guestAl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mSearchWordConfEt = findViewById(R.id.etSearchConference);

        mSetProfileButton = findViewById(R.id.buttonSetProfile);
        mSetProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_screen.this,Profile.class);
                startActivity(intent);
            }
        });

        mSearchConfButton = findViewById(R.id.searchConferenceButton);
        mSearchConfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWordConf = mSearchWordConfEt.getText().toString();
                if(searchWordConf.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Must enter a word to search!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(home_screen.this,SearchConferenceResults.class);
                    intent.putExtra("searchWord",searchWordConf);
                    startActivity(intent);
                }

            }
        });

        mCreateConferenceButton = findViewById(R.id.buttonCreateConference);
        mCreateConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_screen.this,CreateConferenceActivity.class);
                startActivity(intent);

            }
        });

        mMyConferencesButton = findViewById(R.id.myConferences);
        mMyConferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_screen.this,MyConferences.class);
                startActivity(intent);
            }
        });

        mBrowseConferenceButton = findViewById(R.id.buttonBrowseConferences);
        mBrowseConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrowseConferences();
            }
        });

        //Initializing firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Log out button clicked
        mLogoutButton = findViewById(R.id.log_out);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signing out the user
                mFirebaseAuth.signOut();

                //going back to the main activity screen
                Intent intent = new Intent(home_screen.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mSavedConferenceButton = findViewById(R.id.saved_conference_button);
        mSavedConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_screen.this,SavedConferences.class);
                startActivity(intent);
            }
        });

        GuestAlertWatcher();
    }

    private void GuestAlertWatcher() {
        mDatabaseRefForBrowsing.child("users").child(currentUser.getUid()).child("conference").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot childSnapshot : snapshot.getChildren())
                {
                    for (DataSnapshot childChildSnapshot : childSnapshot.getChildren())
                    {
                        String key = childChildSnapshot.getKey();
                        String value = childChildSnapshot.getValue().toString();
                        guestAl.add(value);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value;
                String conferenceName = "";
                String newPerson = "";
                for (DataSnapshot childSnapshot : snapshot.getChildren())
                {
                    String key = childSnapshot.getKey();

                    if(key.equals("title"))
                    {
                        conferenceName = childSnapshot.getValue().toString();
                    }

                    for (DataSnapshot childChildSnapshot : childSnapshot.getChildren())
                    {
                        value = childChildSnapshot.getValue().toString();
                        if(!guestAl.contains(value))
                        {
                            guestAl.add(value);
                            newPerson = value;

                        }
                    }
                }

                if(!newPerson.equals(""))
                {
                    MakeNotification(conferenceName,newPerson);
                }

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

    private void MakeNotification(String conferenceName, String attendeeName) {
        NotificationHandler.notificationCreator(getApplicationContext(),conferenceName, attendeeName);
    }

    private void BrowseConferences() {
        Intent intent = new Intent(home_screen.this,BrowseConferences.class);
        startActivity(intent);
    }
}