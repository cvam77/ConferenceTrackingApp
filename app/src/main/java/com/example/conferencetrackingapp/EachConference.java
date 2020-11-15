package com.example.conferencetrackingapp;

public class EachConference
{
    String conferenceHolderId, eachConferenceId, conferenceHolderName = "", conferenceHolderEmail="",
            conferenceImage,title,description,speakers, otherInfo, date,time;

    public EachConference(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public EachConference(String conferenceHolderId, String eachConferenceId,
                          String conferenceImage, String title, String description,
                          String speakers, String otherInfo, String date, String time)
    {
        this.conferenceHolderId = conferenceHolderId;
        this.eachConferenceId = eachConferenceId;
        this.conferenceImage = conferenceImage;
        this.title = title;
        this.description = description;
        this.speakers = speakers;
        this.otherInfo = otherInfo;
        this.date = date;
        this.time = time;
    }

    public String getConferenceHolderId() {
        return conferenceHolderId;
    }

    public void setConferenceHolderId(String conferenceHolderId) {
        this.conferenceHolderId = conferenceHolderId;
    }

    public String getEachConferenceId() {
        return eachConferenceId;
    }

    public void setEachConferenceId(String eachConferenceId) {
        this.eachConferenceId = eachConferenceId;
    }

    public String getConferenceHolderName() {
        return conferenceHolderName;
    }

    public void setConferenceHolderName(String conferenceHolderName) {
        this.conferenceHolderName = conferenceHolderName;
    }

    public String getConferenceHolderEmail() {
        return conferenceHolderEmail;
    }

    public void setConferenceHolderEmail(String conferenceHolderEmail) {
        this.conferenceHolderEmail = conferenceHolderEmail;
    }

    public String getConferenceImage() {
        return conferenceImage;
    }

    public void setConferenceImage(String conferenceImage) {
        this.conferenceImage = conferenceImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeakers() {
        return speakers;
    }

    public void setSpeakers(String speakers) {
        this.speakers = speakers;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
