package com.example.mobilesoftware;

import android.os.Bundle;

public class MealItem {
    private String mealName;
    private String mealOpinion;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String cost;

    // 기본 생성자
    public MealItem() {
        // 기본 생성자
    }

    // Bundle에서 데이터를 받아와서 객체 생성하는 생성자
    public MealItem(Bundle bundle) {
        if (bundle != null) {
            mealName = bundle.getString("mealName", "");
            mealOpinion = bundle.getString("mealOpinion", "");
            year = bundle.getString("year", "");
            month = bundle.getString("month", "");
            day = bundle.getString("day", "");
            hour = bundle.getString("hour", "");
            minute = bundle.getString("minute", "");
            cost = bundle.getString("cost", "");
        }
    }

    // Getter 메서드들
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
