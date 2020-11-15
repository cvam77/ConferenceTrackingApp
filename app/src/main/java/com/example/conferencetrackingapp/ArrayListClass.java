package com.example.conferencetrackingapp;

import java.util.ArrayList;

public class ArrayListClass
{
    ArrayList<EachDownloadedConference> eachDownloadedConferenceArrayList = new ArrayList<>();

    public ArrayList<EachDownloadedConference> getEachDownloadedConferenceArrayList() {
        return eachDownloadedConferenceArrayList;
    }

    public void setEachDownloadedConferenceArrayList(ArrayList<EachDownloadedConference> eachDownloadedConferenceArrayList) {
        this.eachDownloadedConferenceArrayList = eachDownloadedConferenceArrayList;
    }
}
