package com.example.conferencetrackingapp;

public class EachDownloadedConference {
    String downloadTitle = "", downloadDescription = "", downloadDate = "", downloadTime = "",downloadGuest = "";

    public EachDownloadedConference(String downloadTitle, String downloadDescription, String downloadDate,
                                    String downloadTime, String downloadGuest) {
        this.downloadTitle = downloadTitle;
        this.downloadDescription = downloadDescription;
        this.downloadDate = downloadDate;
        this.downloadTime = downloadTime;
        this.downloadGuest = downloadGuest;
    }

    public String getDownloadTitle() {
        return downloadTitle;
    }

    public void setDownloadTitle(String downloadTitle) {
        this.downloadTitle = downloadTitle;
    }

    public String getDownloadDescription() {
        return downloadDescription;
    }

    public void setDownloadDescription(String downloadDescription) {
        this.downloadDescription = downloadDescription;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getDownloadGuest() {
        return downloadGuest;
    }

    public void setDownloadGuest(String downloadGuest) {
        this.downloadGuest = downloadGuest;
    }
}
