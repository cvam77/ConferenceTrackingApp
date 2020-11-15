package com.example.conferencetrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DownloadedConferencesAdapter extends RecyclerView.Adapter<DownloadedConferencesAdapter.DownloadedConferenceViewHolder>
{

    private Context mContext;
    ArrayList<EachDownloadedConference> mDownloadedConferenceList;

    public DownloadedConferencesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DownloadedConferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.each_saved_conference_layout,
                parent,false);

        return new DownloadedConferencesAdapter.DownloadedConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedConferenceViewHolder holder, int position) {

        EachDownloadedConference eachDownloadedConference = mDownloadedConferenceList.get(position);

        String title = eachDownloadedConference.getDownloadTitle();
        if(!title.isEmpty())
        {
            holder.mSavedConfTitleTv.setText("Title: " + title);
        }

        String speakers = eachDownloadedConference.getDownloadDescription();
        if(!speakers.isEmpty())
        {
            holder.mSavedConfDescriptionTV.setText("Speakers: " + speakers);
        }

        String date = eachDownloadedConference.getDownloadDate();
        if(!date.isEmpty())
        {
            holder.mSavedConfDateTv.setText("Date: " + date);
        }

        String time = eachDownloadedConference.getDownloadTime();
        if(!time.isEmpty())
        {
            holder.mSavedConfTimeTv.setText("Time: " + time);
        }

        String attendees = eachDownloadedConference.getDownloadGuest();
        if(!attendees.isEmpty())
        {
            holder.mSavedConfAttendees.setText("Attendees: " + attendees);
        }

    }

    @Override
    public int getItemCount() {
        if(mDownloadedConferenceList == null)
            return 0;
        return mDownloadedConferenceList.size();
    }

    public ArrayList<EachDownloadedConference> getmDownloadedConferenceList() {
        return mDownloadedConferenceList;
    }

    public void setmDownloadedConferenceList(ArrayList<EachDownloadedConference> mDownloadedConferenceList) {
        this.mDownloadedConferenceList = mDownloadedConferenceList;
    }

    class DownloadedConferenceViewHolder extends RecyclerView.ViewHolder {

        TextView mSavedConfTitleTv, mSavedConfDescriptionTV,mSavedConfDateTv, mSavedConfTimeTv, mSavedConfAttendees;

        public DownloadedConferenceViewHolder(@NonNull View itemView) {
            super(itemView);

            mSavedConfTitleTv = itemView.findViewById(R.id.tvSavedConfTitle);
            mSavedConfDescriptionTV = itemView.findViewById(R.id.tvSavedConfDescription);
            mSavedConfDateTv = itemView.findViewById(R.id.tvSavedConfDate);
            mSavedConfTimeTv = itemView.findViewById(R.id.tvSavedConfTime);
            mSavedConfAttendees = itemView.findViewById(R.id.tvSavedConfAttendees);
        }


    }
}
