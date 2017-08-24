package com.bignerdranch.android.criminalintent;


import java.util.Date;
import java.util.UUID;

/**
 * Created by lawren on 01/08/17.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private String mSuspect;
    private long mSuspectId;
    private Date mDate;
    private Date mTime;
    private boolean mSolved;
    private boolean mRequiresPolice;

    public long getSuspectId() {
        return mSuspectId;
    }

    public void setSuspectId(long suspectId) {
        mSuspectId = suspectId;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }


    public Crime(){
       this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
        mTime = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getTime(){ return mTime;}

    public void setTime(Date time){ mTime = time; }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean doesRequirePolice(){ return mRequiresPolice; }

    public void setRequiresPolice(boolean required){ mRequiresPolice = required; }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
