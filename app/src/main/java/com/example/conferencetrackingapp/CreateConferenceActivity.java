package com.example.conferencetrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateConferenceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String myGlobalName = "";
    boolean globalEnrolledSwitch;
    ImageView mImageViewLogo;
    EditText mTitleEt, mDescriptionEt, mSpeakersEt, mOtherInformationEt;
    Button mSaveButton, mSetDateButton, mSetTimeButton,mEnrollButton,mButtonDownload,mDeleteButton;
    TextView mDateTextview,mTimeTextView, mAttendeesTextView;

    public static final String CONFERENCE_ID_EXTRA = "conference_id";
    public static final String USER_ID_EXTRA = "user_id";

    public static final String CHAUDHARY = "conference_id";
    public static final String SHIVAM = "user_id";

    String downloadTitle = "", downloadDescription = "", downloadDate = "", downloadTime = "",downloadGuest = "";
    ArrayList<EachDownloadedConference> eachDownloadedConferencesAl = new ArrayList<>();

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    String logoUri = "";

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String id = currentUser.getUid();

    String dateString = "",timeString = "";

    private String DEFAULT_CONFERENCE_ID = "a";
    private String mConferenceId = DEFAULT_CONFERENCE_ID;

    private String DEFAULT_USER_ID = "b";
    private String mUserId = DEFAULT_USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conference);

        DownloadedConferencesAdapter downloadedConferencesAdapter = new DownloadedConferencesAdapter(this);

        mImageViewLogo = findViewById(R.id.logo);
        mImageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLogo();
            }
        });


        mTitleEt = findViewById(R.id.etTitle);
        mDescriptionEt = findViewById(R.id.etConferenceDescription);
        mSpeakersEt = findViewById(R.id.etConferenceSpeakers);
        mOtherInformationEt = findViewById(R.id.etConferenceOtherInfo);

        mSetDateButton = findViewById(R.id.buttonSetDate);
        mSetDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        mDateTextview = findViewById(R.id.tvDate);

        mSetTimeButton = findViewById(R.id.buttonSetTime);
        mSetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        mTimeTextView = findViewById(R.id.tvTime);

        mAttendeesTextView = findViewById(R.id.tvAttendeesList);
        mSaveButton = findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveButtonClicked();
            }
        });

        mButtonDownload = findViewById(R.id.buttonDownload);
        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EachDownloadedConference eachDownloadedConference = new EachDownloadedConference(downloadTitle,
                        downloadDescription,downloadDate,downloadTime,downloadGuest);

                Log.d("playbold",downloadTitle);


                SavedConferences savedConferences = new SavedConferences();
                savedConferences.getEachDownloadedConferenceArrayList().add(eachDownloadedConference);
//
//                eachDownloadedConferencesAl.add(eachDownloadedConference);
//
//                if(savedConferences.getEachDownloadedConferenceArrayList().isEmpty())
//                {
//                    savedConferences.setEachDownloadedConferenceArrayList(eachDownloadedConferencesAl);
//                }
//                else
//                {
//                    eachDownloadedConferencesAl = (ArrayList<EachDownloadedConference>) savedConferences.getEachDownloadedConferenceArrayList().clone();
//                    eachDownloadedConferencesAl.add(eachDownloadedConference);
//                    savedConferences.setEachDownloadedConferenceArrayList(eachDownloadedConferencesAl);
//                }

                Intent intent = new Intent(CreateConferenceActivity.this,home_screen.class);
                startActivity(intent);
            }
        });

        mDeleteButton = findViewById(R.id.buttonDelete);
        SetMyName();

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(SHIVAM) && intent.hasExtra(CHAUDHARY)
                && intent.hasExtra("myconference"))
        {
            Log.d("mainTumhara","main tumhara");

            mUserId = intent.getStringExtra(SHIVAM);
            mConferenceId = intent.getStringExtra(CHAUDHARY);
            loadFillWithEnabledViews(mUserId, mConferenceId);
        }
        else if(intent != null && intent.hasExtra(USER_ID_EXTRA) && intent.hasExtra(CONFERENCE_ID_EXTRA)
            && !intent.hasExtra("myconference"))
        {

            mUserId = intent.getStringExtra(USER_ID_EXTRA);
            mConferenceId = intent.getStringExtra(CONFERENCE_ID_EXTRA);
            checkEnrolled();
            loadFillAndDisableViews(mUserId, mConferenceId);
        }
        mEnrollButton = findViewById(R.id.mButtonEnroll);
        mEnrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGuest();
            }
        });
        
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteConference();
            }
        });

    }

    private void DeleteConference() {
        mDatabaseReference.child("users").child(mUserId).child("conference").child(mConferenceId).setValue(null);
        Intent intent = new Intent(this,home_screen.class);
        startActivity(intent);

    }

    public void SetMyName()
    {
        mDatabaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists())
                {
                    if(snapshot.getKey().equals(currentUser.getUid()))
                    {
                        for(DataSnapshot childSnapshot : snapshot.getChildren())
                        {
                            if(childSnapshot.getKey().equals("name"))
                            {
                                String name = childSnapshot.getValue().toString();
                                myGlobalName = name;
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
    }
    private void AddGuest() {
        AddGuestNameAsMyName(myGlobalName);
    }

    private void AddGuestNameAsMyName(String myName) {
        mDatabaseReference.child("users").child(mUserId).child("conference").child(mConferenceId)
                .child("guests").push().setValue(myName);
        Toast.makeText(getApplicationContext(),"You have been Enrolled!",Toast.LENGTH_LONG).show();
        mEnrollButton.setText("Enrolled");
        mEnrollButton.setClickable(false);
    }

    private void loadFillWithEnabledViews(String mUserId, String mConferenceId) {
        mDeleteButton.setVisibility(View.VISIBLE);
        mDatabaseReference.child("users").child(mUserId).child("conference").child(mConferenceId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String key = snapshot.getKey();
                    String value = snapshot.getValue().toString();

                    String logoUri = "",confTitle = "",confDescription = "",confSpeakers = "",confOtherInfo = "",
                            confDate = "",confTime = "";
                    switch (key)
                    {
                        case "logo":
                            logoUri = value;
                            break;
                        case "title":
                            confTitle = value;
                            downloadTitle = confTitle;

                            break;
                        case "description":
                            confDescription = value;
                            downloadDescription = confDescription;
                            break;
                        case "speakers":
                            confSpeakers = value;
                            break;
                        case "other_info":
                            confOtherInfo = value;
                            break;
                        case "date":
                            confDate = value;
                            downloadDate = confDate;
                            break;
                        case "time":
                            confTime = value;
                            downloadTime = confTime;
                            break;
                        default:
                            break;
                    }

                    if(!logoUri.equals(""))
                    {
                        Uri uri = Uri.parse(logoUri);
                        Picasso.get().load(uri).fit().centerCrop().into(mImageViewLogo);
                    }

                    if(!confTitle.equals(""))
                    {
                        mTitleEt.setText(confTitle);
                    }

                    if(!confDescription.equals(""))
                    {
                        mDescriptionEt.setText(confDescription);
                    }

                    if(!confSpeakers.equals(""))
                    {
                        mSpeakersEt.setText(confSpeakers);
                    }

                    if(!confOtherInfo.equals(""))
                    {
                        mOtherInformationEt.setText(confOtherInfo);
                    }

                    if(!confDate.equals(""))
                    {
                        mDateTextview.setText(confDate);
                    }

                    if(!confTime.equals(""))
                    {
                        mTimeTextView.setText(confTime);
                    }

                    String attendeesListString = "";
                    for(DataSnapshot childSnapshot : snapshot.getChildren())
                    {
                        String attendeesName = childSnapshot.getValue().toString();

                        if(!attendeesName.equals(""))
                        {
                            attendeesListString = attendeesListString + attendeesName;
                            mAttendeesTextView.setVisibility(View.VISIBLE);
                            mAttendeesTextView.setText("Attendees: " + attendeesListString);
                            attendeesListString = attendeesListString + ", ";
                        }
                    }
                    downloadGuest = attendeesListString;
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
    }

    private void loadFillAndDisableViews(String mUserId, String mConferenceId) {
        mDeleteButton.setVisibility(View.GONE);
        mDatabaseReference.child("users").child(mUserId).child("conference").child(mConferenceId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String key = snapshot.getKey();
                    String value = snapshot.getValue().toString();

                    String logoUri = "",confTitle = "",confDescription = "",confSpeakers = "",confOtherInfo = "",
                            confDate = "",confTime = "";
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

                    mImageViewLogo.setClickable(false);
                    if(!logoUri.equals(""))
                    {
                        Uri uri = Uri.parse(logoUri);
                        Picasso.get().load(uri).fit().centerCrop().into(mImageViewLogo);
                    }

                    mTitleEt.setKeyListener(null);
                    if(!confTitle.equals(""))
                    {
                        mTitleEt.setText(confTitle);
                    }
                    mDescriptionEt.setKeyListener(null);
                    if(!confDescription.equals(""))
                    {
                        mDescriptionEt.setText(confDescription);
                    }
                    mSpeakersEt.setKeyListener(null);
                    if(!confSpeakers.equals(""))
                    {
                        mSpeakersEt.setText(confSpeakers);
                    }
                    mOtherInformationEt.setKeyListener(null);
                    if(!confOtherInfo.equals(""))
                    {
                        mOtherInformationEt.setText(confOtherInfo);
                    }
                    mSetDateButton.setClickable(false);
                    mSetDateButton.setText("Date");
                    if(!confDate.equals(""))
                    {
                        mDateTextview.setText(confDate);
                    }
                    mSetTimeButton.setClickable(false);
                    mSetTimeButton.setText("Time");
                    if(!confTime.equals(""))
                    {
                        mTimeTextView.setText(confTime);
                    }
                    mSaveButton.setVisibility(View.GONE);
                    mEnrollButton.setVisibility(View.VISIBLE);

                    Log.d("zindagi","value got was = " + isGlobalEnrolledSwitch());
                    if(isGlobalEnrolledSwitch())
                    {
                        Log.d("zindagi","Got the switch");
                        mEnrollButton.setText("Enrolled");
                        mEnrollButton.setClickable(false);
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
    }

    public void setGlobalEnrolledSwitch(boolean globalEnrolledSwitch) {
        if(this.globalEnrolledSwitch == false)
        {
            this.globalEnrolledSwitch = globalEnrolledSwitch;
        }
    }

    public boolean isGlobalEnrolledSwitch() {
        return globalEnrolledSwitch;
    }

    private void checkEnrolled() {
        mDatabaseReference.child("users").
                child(mUserId).child("conference").child(mConferenceId).child("guests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String value = snapshot.getValue().toString();

                    if(value.toLowerCase().trim().equals(myGlobalName.toLowerCase().trim()))
                    {
                        Log.d("zindagi","Setting the switch");
                        setGlobalEnrolledSwitch(true);
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
    }

    private void showTimePicker() {
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,hour,minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    private void showDatePicker() {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, year,month,day);
        datePickerDialog.show();
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    private void SaveButtonClicked() {
        String title = mTitleEt.getText().toString();
        String description = mDescriptionEt.getText().toString();
        String speakers = mSpeakersEt.getText().toString();
        String otherInfo = mOtherInformationEt.getText().toString();

        DatabaseReference conferenceReference;

        if(title.isEmpty())
        {
            Toast.makeText(this,"Title cannot be empty!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            conferenceReference = mDatabaseReference.child("users").child(id).child("conference");
            String key =  conferenceReference.push().getKey();

            if(!mConferenceId.equals(DEFAULT_CONFERENCE_ID))
            {
                key = mConferenceId;
            }


            conferenceReference.child(key).child("title").setValue(title);

            if(!description.isEmpty())
            {
                conferenceReference.child(key).child("description").setValue(description);
            }
            if(!speakers.isEmpty())
            {
                conferenceReference.child(key).child("speakers").setValue(speakers);
            }
            if(!otherInfo.isEmpty())
            {
                conferenceReference.child(key).child("other_info").setValue(otherInfo);
            }

            if(!dateString.equals(""))
            {
                conferenceReference.child(key).child("date").setValue(dateString);
            }

            if(!timeString.equals(""))
            {
                conferenceReference.child(key).child("time").setValue(timeString);
            }

            if(!getLogoUri().equals(""))
            {
                conferenceReference.child(key).child("logo").setValue(getLogoUri());
            }

            Intent intent = new Intent(this,home_screen.class);
            startActivity(intent);
        }


    }

    private void pickLogo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference mStorage = FirebaseStorage.getInstance().getReference();

            final StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            setLogoUri(String.valueOf(uri));

                            Picasso.get().load(uri).fit().centerCrop().into(mImageViewLogo);
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR,i);
        mCalendar.set(Calendar.MONTH,i1);
        mCalendar.set(Calendar.DAY_OF_MONTH,i2);

        long calendarInLong = mCalendar.getTimeInMillis();

        Date longToDate = new Date(calendarInLong);
        dateString = DateFormat.getDateInstance(DateFormat.FULL).format(longToDate);
        mDateTextview.setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        timeString = i + " : " + i1;
        mTimeTextView.setText(timeString);
    }
}