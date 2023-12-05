package com.example.mobilesoftware;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;

public class AnalysisFragment extends Fragment {
    private TextView caloriesTextView;
    private TextView breakfastCostTextView;
    private TextView lunchCostTextView;
    private TextView dinnerCostTextView;
    private TextView drinkCostTextView;
    private DatabaseHelper databaseHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.analysis_fragment, container, false);

        // TextView 초기화
        caloriesTextView = rootView.findViewById(R.id.caloriesTextView);
        breakfastCostTextView = rootView.findViewById(R.id.breakfastCostTextView);
        lunchCostTextView = rootView.findViewById(R.id.lunchCostTextView);
        dinnerCostTextView = rootView.findViewById(R.id.dinnerCostTextView);
        drinkCostTextView = rootView.findViewById(R.id.drinkCostTextView);
        // DatabaseHelper 초기화
        databaseHelper = new DatabaseHelper(requireContext());

        // 최근 1달 간의 칼로리 가져와서 TextView에 표시
        int totalCaloriesForLastMonth = getTotalCaloriesForLastMonth();
        caloriesTextView.setText(totalCaloriesForLastMonth + " Kcal");

        // 최근 1달 간의 비용 가져와서 각 TextView에 표시
        int[] totalCostForLastMonth = getTotalCostForLastMonth();

        // 각각의 TextView에 비용 표시
        breakfastCostTextView.setText(totalCostForLastMonth[0] + " ₩");
        lunchCostTextView.setText(totalCostForLastMonth[1] + " ₩");
        dinnerCostTextView.setText(totalCostForLastMonth[2] + " ₩");
        drinkCostTextView.setText(totalCostForLastMonth[3] + " ₩");

        return rootView;
    }

    private int getTotalCaloriesForLastMonth() {
        // 현재 날짜 가져오기
        Calendar currentDate = Calendar.getInstance();

        // 1달 전 날짜 계산
        currentDate.add(Calendar.MONTH, -1);

        // 날짜 정보 추출
        int lastMonthYear = currentDate.get(Calendar.YEAR);
        int lastMonthMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int lastMonthDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 해당 날짜부터 현재까지의 식사 데이터 가져오기
        List<Meal> mealsForLastMonth = databaseHelper.getMealsForDateRange(lastMonthYear, lastMonthMonth, lastMonthDay, lastMonthYear, lastMonthMonth, lastMonthDay);

        // 검색된 식사의 개수를 로그로 표시
        Log.d("AnalysisFragment", "지난 달 총 식사 칼로리 수: " + mealsForLastMonth.size());

        // 칼로리 합산
        int totalCalories = 0;
        for (Meal meal : mealsForLastMonth) {
            totalCalories += meal.getCalorie();
        }

        return totalCalories;
    }

    private int[] getTotalCostForLastMonth() {
        // 현재 날짜 가져오기
        Calendar currentDate = Calendar.getInstance();

        // 1달 전 날짜 계산
        currentDate.add(Calendar.MONTH, -1);

        // 날짜 정보 추출
        int lastMonthYear = currentDate.get(Calendar.YEAR);
        int lastMonthMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int lastMonthDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 해당 날짜부터 현재까지의 식사 데이터 가져오기
        List<Meal> mealsForLastMonth = databaseHelper.getMealsForDateRange(lastMonthYear, lastMonthMonth, lastMonthDay, lastMonthYear, lastMonthMonth, lastMonthDay);

        // 검색된 식사의 개수를 로그로 표시
        Log.d("AnalysisFragment", "지난 달 총 식사 비용 수: " + mealsForLastMonth.size());

        // 각 식사 종류에 따른 비용 합산
        int totalBreakfastCost = 0;
        int totalLunchCost = 0;
        int totalDinnerCost = 0;
        int totalDrinkCost = 0; // 음료 비용 추가

        for (Meal meal : mealsForLastMonth) {
            int cost = Integer.parseInt(meal.getCost());
            String mealtime = meal.getMealTime();
            String mealType = meal.getMealType(); // 식사 유형 가져오기

            if ("음료".equals(mealType)) {
                totalDrinkCost += cost;
            } else if ("식사".equals(mealType)) {
                // 식사 종류에 따라 비용 합산
                switch (mealtime) {
                    case "조식":
                        totalBreakfastCost += cost;
                        break;
                    case "중식":
                        totalLunchCost += cost;
                        break;
                    case "석식":
                        totalDinnerCost += cost;
                        break;
                }
            }
        }

        // 결과 출력
        Log.d("AnalysisFragment", "총 아침 식사 비용: " + totalBreakfastCost);
        Log.d("AnalysisFragment", "총 점심 식사 비용: " + totalLunchCost);
        Log.d("AnalysisFragment", "총 저녁 식사 비용: " + totalDinnerCost);
        Log.d("AnalysisFragment", "총 음료 비용: " + totalDrinkCost);

        // 각 식사 종류에 따른 비용을 배열에 저장
        int[] totalCosts = {totalBreakfastCost, totalLunchCost, totalDinnerCost, totalDrinkCost};

        return totalCosts;
    }
}
