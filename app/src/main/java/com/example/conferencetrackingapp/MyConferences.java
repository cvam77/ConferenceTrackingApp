package com.example.conferencetrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class MyConferences extends AppCompatActivity implements AdapterBrowseConference.ItemClickListener {

    RecyclerView mRecyclerView;
    AdapterBrowseConference mConferenceAdapter;

    ArrayList<EachConference> alForBrowseAdapter = new ArrayList<>();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    int timeCounter=0;

    DatabaseReference mDatabaseRefForBrowsing = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conferences);

        mRecyclerView = findViewById(R.id.rvMyConferences);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mConferenceAdapter = new AdapterBrowseConference(this, (AdapterBrowseConference.ItemClickListener) this);
        mRecyclerView.setAdapter(mConferenceAdapter);

        CallAsync();
    }

    private void CallAsync() {
        AsyncTask3 asyncTask3 = new AsyncTask3();
        AsyncTask4 asyncTask4 = new AsyncTask4();

        asyncTask3.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        asyncTask4.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

    }

    @Override
    public void onItemClickListener(String userId, String conferenceId) {
        Intent conferenceClicked = new Intent(this,CreateConferenceActivity.class);
        conferenceClicked.putExtra(CreateConferenceActivity.SHIVAM,userId);
        conferenceClicked.putExtra(CreateConferenceActivity.CHAUDHARY,conferenceId);
        conferenceClicked.putExtra("myconference",1);
        startActivity(conferenceClicked);
    }



    class AsyncTask3 extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseRefForBrowsing.child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.exists())
                    {

                        if(snapshot.getKey().equals(currentUser.getUid())){
                            String userKey = snapshot.getKey();
                            Log.d("ajbhikoi","userKey = " + userKey);

                            for(DataSnapshot childSnapshot : snapshot.getChildren())
                            {
                                Log.d("setFire","1");
                                for(DataSnapshot secondLevelChildSnapshot : childSnapshot.getChildren())
                                {
                                    Log.d("setFire","2");
                                    String conferenceKey = secondLevelChildSnapshot.getKey();
                                    Log.d("ajbhikoi","conferenceKey = " + conferenceKey);

                                    String logoUri = "", confTitle = "",confDescription = "",confSpeakers = "",
                                            confOtherInfo = "", confDate = "",confTime = "";

                                    for(DataSnapshot conferenceSnapshot : secondLevelChildSnapshot.getChildren())
                                    {
                                        Log.d("setFire","3");
                                        String key = conferenceSnapshot.getKey();
                                        String value = conferenceSnapshot.getValue().toString();
                                        Log.d("rollingIn",value);

                                        switch (key)
                                        {
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
                                    EachConference eachConference = new EachConference(userKey,conferenceKey,
                                            logoUri,confTitle,confDescription,confSpeakers,confOtherInfo,confDate,confTime);

                                    alForBrowseAdapter.add(eachConference);

                                }
                            }
                            EachConference eachConference = alForBrowseAdapter.get(0);
                            Log.d("adele",eachConference.getTitle());
                            Log.d("setFire","4");
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
            while(alForBrowseAdapter.isEmpty() && timeCounter < 8)
            {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeCounter++;
            }
            return null;
        }
    }

    class AsyncTask4 extends AsyncTask<Void,Void,Void>
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