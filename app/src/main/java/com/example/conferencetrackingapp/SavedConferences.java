package com.example.conferencetrackingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SavedConferences extends AppCompatActivity {

    RecyclerView recyclerView;
    DownloadedConferencesAdapter downloadedConferencesAdapter;

    ArrayList<EachDownloadedConference> eachDownloadedConferenceArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_conferences);

        recyclerView = findViewById(R.id.rvSavedConferences);
        downloadedConferencesAdapter = new DownloadedConferencesAdapter(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        EachDownloadedConference eachDownloadedConference = new EachDownloadedConference("a","b",
                "c","d","e");

        eachDownloadedConferenceArrayList.add(eachDownloadedConference);

        downloadedConferencesAdapter.setmDownloadedConferenceList(getEachDownloadedConferenceArrayList());
        recyclerView.setAdapter(downloadedConferencesAdapter);
    }

    public ArrayList<EachDownloadedConference> getEachDownloadedConferenceArrayList() {
        return eachDownloadedConferenceArrayList;
    }

    public void setEachDownloadedConferenceArrayList(ArrayList<EachDownloadedConference> eachDownloadedConferenceArrayList) {
        this.eachDownloadedConferenceArrayList = eachDownloadedConferenceArrayList;
    }
}