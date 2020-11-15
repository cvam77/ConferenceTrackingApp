package com.example.conferencetrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BrowseConferences extends AppCompatActivity implements AdapterBrowseConference.ItemClickListener {

    RecyclerView mRecyclerView;
    AdapterBrowseConference mConferenceAdapter;

    ArrayList<EachConference> alForBrowseAdapter = new ArrayList<>();

    String hostName = "", hostEmail = "";

    int timeCounter=0;

    DatabaseReference mDatabaseRefForBrowsing = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_conferences);

        mRecyclerView = findViewById(R.id.rvConferences);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mConferenceAdapter = new AdapterBrowseConference(this,this);
        mRecyclerView.setAdapter(mConferenceAdapter);

        CallAsync();
    }

    private void CallAsync() {
        AsyncTask1 asyncTask1 = new AsyncTask1();
        AsyncTask2 asyncTask2 = new AsyncTask2();

        asyncTask1.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        asyncTask2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onItemClickListener(String userId, String conferenceId) {
        Intent conferenceClicked = new Intent(this,CreateConferenceActivity.class);
        conferenceClicked.putExtra(CreateConferenceActivity.USER_ID_EXTRA,userId);
        conferenceClicked.putExtra(CreateConferenceActivity.CONFERENCE_ID_EXTRA,conferenceId);
        startActivity(conferenceClicked);
    }


    class AsyncTask1 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseRefForBrowsing.child("users").addChildEventListener(new ChildEventListener() {
                 @Override
                 public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                     if (snapshot.exists()) {

                         if (!snapshot.getKey().equals(currentUser.getUid())) {
                             String userKey = snapshot.getKey();

                             for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                 String nameKey = childSnapshot.getKey();
                                 String hostValue = childSnapshot.getValue().toString();

                                 if (nameKey.equals("name")) {
                                     hostName = hostValue;

                                 } else if (nameKey.equals("email")) {
                                     hostEmail = hostValue;
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


            mDatabaseRefForBrowsing.child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {

                        if (!snapshot.getKey().equals(currentUser.getUid())) {
                            String userKey = snapshot.getKey();

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                for (DataSnapshot secondLevelChildSnapshot : childSnapshot.getChildren()) {


                                    String conferenceKey = secondLevelChildSnapshot.getKey();
                                    Log.d("ajbhikoi", "conferenceKey = " + conferenceKey);

                                    String logoUri = "", confTitle = "", confDescription = "", confSpeakers = "",
                                            confOtherInfo = "", confDate = "", confTime = "";

                                    for (DataSnapshot conferenceSnapshot : secondLevelChildSnapshot.getChildren()) {
                                        Log.d("setFire", "3");
                                        String key = conferenceSnapshot.getKey();
                                        String value = conferenceSnapshot.getValue().toString();
                                        Log.d("rollingIn", value);

                                        switch (key) {
                                            case "logo":
                                                logoUri = value;
                                                break;
                                            case "title":
                                                confTitle = value;
                                                break;
                                            case "description":
                                                confDescription = value;
                                                break;
                                            case "speakers":
                                                confSpeakers = value;
                                                break;
                                            case "other_info":
                                                confOtherInfo = value;
                                                break;
                                            case "date":
                                                confDate = value;
                                                break;
                                            case "time":
                                                confTime = value;
                                                break;
                                            default:
                                                break;
                                        }

                                    }

                                    EachConference eachConference = new EachConference(userKey, conferenceKey,
                                            logoUri, confTitle, confDescription, confSpeakers, confOtherInfo, confDate, confTime);

                                    eachConference.setConferenceHolderName(hostName);

                                    eachConference.setConferenceHolderEmail(hostEmail);
                                    Log.d("congratulationation", "host email = " + eachConference.getConferenceHolderEmail());
                                    alForBrowseAdapter.add(eachConference);
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
            while(alForBrowseAdapter.isEmpty() && timeCounter < 4)
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeCounter++;
            }
            return null;
        }
    }

    class AsyncTask2 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConferenceAdapter.setConferenceList(alForBrowseAdapter);
                }
            });

            return null;
        }
    }

}