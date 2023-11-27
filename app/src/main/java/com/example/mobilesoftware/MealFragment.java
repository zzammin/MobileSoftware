package com.example.mobilesoftware;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class MealFragment extends Fragment {

    private static final String ARG_MEAL = "argMeal";

    private TextView textLocation;
    private TextView textMealName;
    private TextView textMealOpinion;
    private TextView textYear;
    private TextView textMonth;
    private TextView textDay;
    private TextView textHour;
    private TextView textMinute;
    private TextView textCost;
    private TextView textCalorie;
    private TextView textMealtime;
    private TextView textMealType;
    private ImageView imageView;

    // 이미지 URI를 저장할 변수 추가
    private String imageUriString;

    public static MealFragment newInstance(Meal meal) {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MEAL, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meal_fragment, container, false);

        textLocation = rootView.findViewById(R.id.textLocation);
        textMealName = rootView.findViewById(R.id.textMealName);
        textMealOpinion = rootView.findViewById(R.id.textMealOpinion);
        textYear = rootView.findViewById(R.id.textYear);
        textMonth = rootView.findViewById(R.id.textMonth);
        textDay = rootView.findViewById(R.id.textDay);
        textHour = rootView.findViewById(R.id.textHour);
        textMinute = rootView.findViewById(R.id.textMinute);
        textCost = rootView.findViewById(R.id.textCost);
        textCalorie = rootView.findViewById(R.id.textCalorie);
        textMealtime = rootView.findViewById(R.id.textMealtime);
        textMealType = rootView.findViewById(R.id.textMealType);
        imageView = rootView.findViewById(R.id.imageMeal);

        if (getArguments() != null) {
            Meal meal = getArguments().getParcelable(ARG_MEAL);
            if (meal != null) {
                textLocation.setText("Location: " + meal.getLocation());
                textMealName.setText("Meal Name: " + meal.getMealName());
                textMealOpinion.setText("Meal Opinion: " + meal.getMealOpinion());
                textYear.setText("Year: " + meal.getYear());
                textMonth.setText("Month: " + meal.getMonth());
                textDay.setText("Day: " + meal.getDay());
                textHour.setText("Hour: " + meal.getHour());
                textMinute.setText("Minute: " + meal.getMinute());
                textCost.setText("Cost: " + meal.getCost());
                textCalorie.setText("Calorie: " + meal.getCalorie());
                textMealtime.setText("Mealtime: " + meal.getMealTime());
                textMealType.setText("Meal Type: " + meal.getMealType());

                // 데이터베이스에서 이미지 URI를 가져와서 표시
                loadAndDisplayImage(meal);
            }
        }

        return rootView;
    }

    // 이미지 불러오기 및 표시 메서드 수정
    private void loadAndDisplayImage(Meal meal) {
        // 데이터베이스에서 이미지 URI를 가져오기
        imageUriString = meal.getMealImageUri();

        // 이미지 URI가 설정되어 있는지 확인
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Glide.with(this)
                    .load(imageUriString)
                    .into(imageView);
        }
    }
}
