package com.example.mobilesoftware;

import android.os.Bundle;
import java.util.Calendar;

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

    // 수정된 메서드: year, month, day를 사용하여 Calendar 객체를 생성하여 반환
    public Calendar getDate() {
        // 예외 처리를 추가하여 유효한 숫자가 아닌 경우 기본값인 현재 날짜를 반환하도록 함
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1); // Calendar의 월은 0부터 시작하므로 1을 빼줍니다.
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            return calendar;
        } catch (NumberFormatException e) {
            // 예외가 발생하면 기본값인 현재 날짜를 반환
            return Calendar.getInstance();
        }
    }

}
