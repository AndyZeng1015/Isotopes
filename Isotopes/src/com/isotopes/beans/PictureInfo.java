package com.isotopes.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PictureInfo implements Parcelable {
    private String picPath;//图片路径
    private boolean isSelect=false;//图片是否被选中

    public PictureInfo() {
    }

    public PictureInfo(boolean isSelect, String picPath) {
        this.isSelect = isSelect;
        this.picPath = picPath;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picPath);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected PictureInfo(Parcel in) {
        this.picPath = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<PictureInfo> CREATOR = new Creator<PictureInfo>() {
        @Override
        public PictureInfo createFromParcel(Parcel source) {
            return new PictureInfo(source);
        }

        @Override
        public PictureInfo[] newArray(int size) {
            return new PictureInfo[size];
        }
    };
}
