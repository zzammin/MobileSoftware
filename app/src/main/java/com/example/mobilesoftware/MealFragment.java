package com.example.mobilesoftware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

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

        if (getArguments() != null) {
            Meal meal = getArguments().getParcelable(ARG_MEAL);
            if (meal != null) {
                // Display Meal information in TextViews
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
            }
        }

        return rootView;
    }

}
