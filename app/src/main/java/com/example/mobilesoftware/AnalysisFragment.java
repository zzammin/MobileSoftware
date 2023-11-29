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
    private TextView drinkCostTextView; // Added TextView for drink cost
    private DatabaseHelper databaseHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.analysis_fragment, container, false);

        // TextView 초기화
        caloriesTextView = rootView.findViewById(R.id.caloriesTextView);
        breakfastCostTextView = rootView.findViewById(R.id.breakfastCostTextView);
        lunchCostTextView = rootView.findViewById(R.id.lunchCostTextView);
        dinnerCostTextView = rootView.findViewById(R.id.dinnerCostTextView);
        drinkCostTextView = rootView.findViewById(R.id.drinkCostTextView); // Initialize TextView for drink cost

        // DatabaseHelper 초기화
        databaseHelper = new DatabaseHelper(requireContext());

        // 최근 1달 간의 칼로리 가져와서 TextView에 표시
        int totalCaloriesForLastMonth = getTotalCaloriesForLastMonth();
        caloriesTextView.setText("Total Calories for Last Month: " + totalCaloriesForLastMonth);

        // 최근 1달 간의 비용 가져와서 각 TextView에 표시
        int[] totalCostForLastMonth = getTotalCostForLastMonth();

        // 각각의 TextView에 비용 표시
        breakfastCostTextView.setText("Breakfast Cost: " + totalCostForLastMonth[0]);
        lunchCostTextView.setText("Lunch Cost: " + totalCostForLastMonth[1]);
        dinnerCostTextView.setText("Dinner Cost: " + totalCostForLastMonth[2]);
        drinkCostTextView.setText("Drink Cost: " + totalCostForLastMonth[3]); // Display Drink Cost

        return rootView;
    }

    private int getTotalCaloriesForLastMonth() {
        // 현재 날짜 가져오기
        Calendar currentDate = Calendar.getInstance();

        // 날짜 정보 추출
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 1달 전 날짜 계산
        currentDate.add(Calendar.MONTH, -1);

        // 1달 전 날짜의 정보 추출
        int lastMonthYear = currentDate.get(Calendar.YEAR);
        int lastMonthMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int lastMonthDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 로그로 날짜 정보 확인
        Log.d("AnalysisFragment", "Current Year: " + currentYear + ", Month: " + currentMonth + ", Day: " + currentDay);
        Log.d("AnalysisFragment", "Last Month Year: " + lastMonthYear + ", Month: " + lastMonthMonth + ", Day: " + lastMonthDay);

        // 해당 날짜부터 현재까지의 식사 데이터 가져오기
        List<Meal> mealsForLastMonth = databaseHelper.getMealsForDateRange(lastMonthYear, lastMonthMonth, lastMonthDay, currentYear, currentMonth, currentDay);

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

        // 날짜 정보 추출
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 1달 전 날짜 계산
        currentDate.add(Calendar.MONTH, -1);

        // 1달 전 날짜의 정보 추출
        int lastMonthYear = currentDate.get(Calendar.YEAR);
        int lastMonthMonth = currentDate.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더해줌
        int lastMonthDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // 로그로 날짜 정보 확인
        Log.d("AnalysisFragment", "Current Year: " + currentYear + ", Month: " + currentMonth + ", Day: " + currentDay);
        Log.d("AnalysisFragment", "Last Month Year: " + lastMonthYear + ", Month: " + lastMonthMonth + ", Day: " + lastMonthDay);

        // 해당 날짜부터 현재까지의 식사 데이터 가져오기
        List<Meal> mealsForLastMonth = databaseHelper.getMealsForDateRange(lastMonthYear, lastMonthMonth, lastMonthDay, currentYear, currentMonth, currentDay);

        // 검색된 식사의 개수를 로그로 표시
        Log.d("AnalysisFragment", "지난 달 총 식사 비용 수: " + mealsForLastMonth.size());

        // 각 식사 종류에 따른 비용 합산
        int totalBreakfastCost = 0;
        int totalLunchCost = 0;
        int totalDinnerCost = 0;
        int totalDrinkCost = 0; // Added for drink cost

        for (Meal meal : mealsForLastMonth) {
            int cost = Integer.parseInt(meal.getCost());
            String mealtime = meal.getMealTime();
            String mealType = meal.getMealType(); // Added to get the meal type

            if ("음료".equals(mealType)) {
                totalDrinkCost += cost;
            }else if("식사".equals(mealType)) {
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
        Log.d("AnalysisFragment", "Total Breakfast Cost: " + totalBreakfastCost);
        Log.d("AnalysisFragment", "Total Lunch Cost: " + totalLunchCost);
        Log.d("AnalysisFragment", "Total Dinner Cost: " + totalDinnerCost);
        Log.d("AnalysisFragment", "Total Drink Cost: " + totalDrinkCost);

        // 각 식사 종류에 따른 비용을 배열에 저장
        int[] totalCosts = {totalBreakfastCost, totalLunchCost, totalDinnerCost, totalDrinkCost};

        return totalCosts;
    }
}
