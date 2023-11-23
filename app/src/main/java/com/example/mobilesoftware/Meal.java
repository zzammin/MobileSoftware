package com.example.mobilesoftware;

public class Meal {

    private String location;
    private String mealName;
    private String mealOpinion;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String cost;

    public Meal(String location, String mealName, String mealOpinion, String year, String month, String day, String hour, String minute, String cost) {
        this.location = location;
        this.mealName = mealName;
        this.mealOpinion = mealOpinion;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.cost = cost;
    }

    // 게터 메서드들 추가
    public String getLocation() {
        return location;
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
}