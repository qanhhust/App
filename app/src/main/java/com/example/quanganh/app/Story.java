package com.example.quanganh.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Quang Anh on 2/23/2017.
 */

public class Story implements Serializable {

    private int stId;
    private String stName;
    private int auId;
    private String stImage;
    private String stDescribe;

    public Story(int stId, String stName, int auId, String stImage, String stDescribe) {
        this.stId = stId;
        this.stName = stName;
        this.auId = auId;
        this.stImage = stImage;
        this.stDescribe = stDescribe;
    }

    public Story() {

    }

    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public int getAuId() {
        return auId;
    }

    public void setAuId(int auId) {
        this.auId = auId;
    }

    public String getStImage() {
        return stImage.substring(0, stImage.length() - 4);
    }

    public void setStImage(String stImage) {
        this.stImage = stImage;
    }

    public String getStDescribe() {
        return stDescribe;
    }

    public void setStDescribe(String stDescribe) {
        this.stDescribe = stDescribe;
    }

    public String toString() {
        return stName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.stId == ((Story) obj).stId) {
            return true;
        }
        return false;
    }
}
