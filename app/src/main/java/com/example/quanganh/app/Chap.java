package com.example.quanganh.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Quang Anh on 2/23/2017.
 */

public class Chap implements Serializable {

    private int deId;
    private String deName;
    private String deSource;
    private String deContent;
    private int stId;
    private String deDate;

    public Chap() {

    }

    public Chap(int deId, String deName, String deSource, String deContent, int stId, String deDate) {
        this.deId = deId;
        this.deName = deName;
        this.deSource = deSource;
        this.deContent = deContent;
        this.stId = stId;
        this.deDate = deDate;
    }

    public int getDeId() {
        return deId;
    }

    public void setDeId(int deId) {
        this.deId = deId;
    }

    public String getDeName() {
        return deName;
    }

    public void setDeName(String deName) {
        this.deName = deName;
    }

    public String getDeSource() {
        return deSource;
    }

    public void setDeSource(String deSource) {
        this.deSource = deSource;
    }

    public String getDeContent() {
        return deContent;
    }

    public void setDeContent(String deContent) {
        this.deContent = deContent;
    }

    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getDeDate() {
        return deDate;
    }

    public void setDeDate(String deDate) {
        this.deDate = deDate;
    }

}
