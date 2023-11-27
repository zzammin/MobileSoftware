package com.example.mobilesoftware;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.util.Calendar;
import java.util.List;

public class MealFragment extends Fragment {

    private static final String ARG_MEAL = "argMeal";
    private static final int REQUEST_STORAGE_PERMISSION = 1;

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
    private Uri imageUri;

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

                // 이미지 불러오기 및 표시
                loadAndDisplayImage(meal);
            }
        }

        return rootView;
    }

    // 이미지 불러오기 및 표시 메서드 수정
    private void loadAndDisplayImage(Meal meal) {
        // SharedPreferences에서 이미지 URI를 가져오기
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String mealName = meal.getMealName();
        String imageUriString = preferences.getString("imageUri_" + mealName, null);

        // 이미지 URI가 설정되어 있는지 확인
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);

            Log.d("image",""+imageUri);

            Glide.with(this)
                    .load(imageUri)
                    .into(imageView);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 이미지 불러오기
                loadAndDisplayImage((Meal) getArguments().getParcelable(ARG_MEAL));
            }
        }
    }

}
