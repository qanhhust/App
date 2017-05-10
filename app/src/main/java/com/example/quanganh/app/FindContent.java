package com.example.quanganh.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by QuangAnh on 5/3/2017.
 */

public class FindContent implements Serializable {

    private int deID;
    private String content;
    private String chapName;

    public void setDeID(int deID) {
        this.deID = deID;
    }

    public int getDeID() {
        return deID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public FindContent(int deID, String content, String chapName) {
        this.deID = deID;
        this.content = content;
        this.chapName = chapName;
    }

    public FindContent() {

    }

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

}
