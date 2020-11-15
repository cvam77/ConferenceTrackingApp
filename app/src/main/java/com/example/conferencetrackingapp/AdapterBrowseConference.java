package com.example.conferencetrackingapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBrowseConference extends RecyclerView.Adapter<AdapterBrowseConference.BrowseConferenceViewHolder>{

    private Context mContext;
    List<EachConference> mConferenceList;

    final private ItemClickListener mItemClickListener;

    public AdapterBrowseConference(Context mContext, ItemClickListener listener) {
        this.mContext = mContext;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public BrowseConferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.each_conference_layout,
                parent,false);

        return new BrowseConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseConferenceViewHolder holder, int position) {
        EachConference eachConference = mConferenceList.get(position);

        String title = eachConference.getTitle();
        if(!title.isEmpty())
        {
            holder.mTitleTv.setText("Title: " + title);
        }

        String description = eachConference.getDescription();
        if(!description.isEmpty())
        {
            holder.mDescriptionTv.setText("Description: " + description);
        }

        String speakers = eachConference.getSpeakers();
        if(!speakers.isEmpty())
        {
            holder.mSpeakersTV.setText("Speakers: " + speakers);
        }
        String otherInfo = eachConference.getOtherInfo();
        if(!otherInfo.isEmpty())
        {
            holder.mOtherInfoTv.setVisibility(View.VISIBLE);
            holder.mOtherInfoTv.setText("Other Information: " + otherInfo);
        }
        String date = eachConference.getDate();
        if(!date.isEmpty())
        {
            holder.mDateTv.setText("Date: " + date);
        }

        String time = eachConference.getTime();
        if(!time.isEmpty())
        {
            holder.mTimeTv.setText("Time: " + time);
        }

        String logoUri = eachConference.getConferenceImage();
        if(!logoUri.isEmpty())
        {
            Uri uri = Uri.parse(logoUri);
            holder.mConfLogoIv.setVisibility(View.VISIBLE);
            Picasso.get().load(uri).fit().centerCrop().into(holder.mConfLogoIv);
        }

        String hostName = eachConference.getConferenceHolderName();

        String hostEmail = eachConference.getConferenceHolderEmail();
        if(!hostName.equals(""))
        {
            holder.mHostTv.setVisibility(View.VISIBLE);

            holder.mHostTv.setText("Host: " + hostName);
            if(!hostEmail.equals(""))
            {
                holder.mHostTv.setText("Host: " + hostName + " (" + hostEmail +")");
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mConferenceList == null)
            return 0;
        return mConferenceList.size();
    }

    public List<EachConference> getConferenceList()
    {
        return mConferenceList;
    }

    public void setConferenceList(List<EachConference> conferenceList)
    {
        mConferenceList = conferenceList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener
    {
        void onItemClickListener(String userId, String conferenceId);
    }

    class BrowseConferenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mConfLogoIv;
        TextView mTitleTv, mHostTv,  mDescriptionTv, mSpeakersTV, mOtherInfoTv, mDateTv, mTimeTv;

        public BrowseConferenceViewHolder(@NonNull View itemView) {
            super(itemView);

            mConfLogoIv = itemView.findViewById(R.id.logo);

            mTitleTv = itemView.findViewById(R.id.tvConfTitle);
            mHostTv = itemView.findViewById(R.id.tvConfHost);
            mDescriptionTv = itemView.findViewById(R.id.tvConfDescription);
            mSpeakersTV = itemView.findViewById(R.id.tvConfSpeakers);
            mOtherInfoTv = itemView.findViewById(R.id.tvConfOtherInfo);

            mDateTv = itemView.findViewById(R.id.tvConfDate);
            mTimeTv = itemView.findViewById(R.id.tvConfTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String userId = mConferenceList.get(getAdapterPosition()).getConferenceHolderId();
            String conferenceId = mConferenceList.get(getAdapterPosition()).getEachConferenceId();
            mItemClickListener.onItemClickListener(userId, conferenceId);
        }
    }
}
