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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchConferenceResults extends AppCompatActivity implements AdapterBrowseConference.ItemClickListener{

    String searchWord = "";
    TextView mTextView;

    int timeCounter = 0;

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<EachConference> alForSearchAdapter = new ArrayList<>();

    RecyclerView mRecyclerView;
    AdapterBrowseConference conferenceSearchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_conference_results);

        mTextView = findViewById(R.id.textView);

        Intent intent = getIntent();
        if(intent.hasExtra("searchWord"))
        {
            searchWord = intent.getStringExtra("searchWord");
        }

        mRecyclerView = findViewById(R.id.rvConferencesSearchResults);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        conferenceSearchResultsAdapter = new AdapterBrowseConference(this,this);
        mRecyclerView.setAdapter(conferenceSearchResultsAdapter);

        CallAsync();

    }

    private void CallAsync() {
        SearchConferenceAsyncTask searchConferenceAsyncTask = new SearchConferenceAsyncTask();
        AsyncTaskReturnArrayList asyncTaskReturnArrayList = new AsyncTaskReturnArrayList();

        searchConferenceAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        asyncTaskReturnArrayList.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onItemClickListener(String userId, String conferenceId) {
        Intent conferenceClicked = new Intent(this,CreateConferenceActivity.class);
        conferenceClicked.putExtra(CreateConferenceActivity.USER_ID_EXTRA,userId);
        conferenceClicked.putExtra(CreateConferenceActivity.CONFERENCE_ID_EXTRA,conferenceId);
        startActivity(conferenceClicked);
    }

    class SearchConferenceAsyncTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseReference.child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.exists())
                    {
                        if(!snapshot.getKey().equals(currentUser.getUid())){
                            String userKey = snapshot.getKey();

                            for(DataSnapshot childSnapshot : snapshot.getChildren())
                            {


                                    for(DataSnapshot secondLevelChildSnapshot : childSnapshot.getChildren())
                                    {
                                        String valueFirst = secondLevelChildSnapshot.getValue().toString();
                                        if (valueFirst.toLowerCase().contains(searchWord.toLowerCase())) {
                                            String conferenceKey = secondLevelChildSnapshot.getKey();
                                            Log.d("theresafire",conferenceKey);
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

                                            String title = eachConference.getTitle();

                                            alForSearchAdapter.add(eachConference);
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
            while(alForSearchAdapter.isEmpty() && timeCounter < 4)
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

    class AsyncTaskReturnArrayList extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conferenceSearchResultsAdapter.setConferenceList(alForSearchAdapter);
                }
            });

            return null;
        }
    }

}