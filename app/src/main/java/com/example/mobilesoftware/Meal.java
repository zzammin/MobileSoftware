package com.example.mobilesoftware;

import android.os.Parcel;
import android.os.Parcelable;

public class Meal implements Parcelable {

    private String location;
    private String mealName;
    private String mealOpinion;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String cost;
    private int calorie;
    private byte[] image;
    private String mealtime;
    private String mealType;

    public Meal(String location, String mealName, String mealOpinion, String year, String month, String day, String hour, String minute, String cost, int calorie, byte[] image, String mealtime, String mealType) {
        this.location = location;
        this.mealName = mealName;
        this.mealOpinion = mealOpinion;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.cost = cost;
        this.calorie = calorie;
        this.image = image;
        this.mealtime = mealtime;
        this.mealType = mealType;
    }

    protected Meal(Parcel in) {
        location = in.readString();
        mealName = in.readString();
        mealOpinion = in.readString();
        year = in.readString();
        month = in.readString();
        day = in.readString();
        hour = in.readString();
        minute = in.readString();
        cost = in.readString();
        calorie = in.readInt();
        image = in.createByteArray();
        mealtime = in.readString();
        mealType = in.readString(); // 새로운 필드 추가
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public byte[] getImage() {
        return image;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealOpinion() {
        return mealOpinion;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getCost() {
        return cost;
    }

    public int getCalorie() {
        return calorie;
    }

    public String getMealTime() {
        return mealtime;
    }

    public String getMealType() {
        return mealType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(mealName);
        dest.writeString(mealOpinion);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeString(hour);
        dest.writeString(minute);
        dest.writeString(cost);
        dest.writeInt(calorie);
        dest.writeByteArray(image);
        dest.writeString(mealtime);
        dest.writeString(mealType); // 새로운 필드 추가
    }
}
