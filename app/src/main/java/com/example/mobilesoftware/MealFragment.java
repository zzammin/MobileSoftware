package com.example.mobilesoftware;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
    private ImageView imageView;

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
        imageView = rootView.findViewById(R.id.imageMeal);

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

                // Load image from Uri into ImageView
                if (!meal.getImageUri().isEmpty()) {
                    // 이미지를 설정하기 전에 권한을 확인하고 요청
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkAndRequestPermission();
                    }
                }
            }
        }

        return rootView;
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // 이미 권한이 부여된 경우
            // 이미지 설정 등의 작업 수행
            loadImage();
        }
    }

    private void loadImage() {
        // 이미지를 설정하는 코드 추가
        // 이 메서드는 이미 권한이 부여된 상태에서만 호출되어야 함
        // 예: 이미지를 외부 저장소에서 로드하여 ImageView에 표시
        Meal meal = getArguments().getParcelable(ARG_MEAL);
        if (meal != null && !meal.getImageUri().isEmpty()) {
            Uri imageUri = Uri.parse(meal.getImageUri());
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            // 권한 요청 결과 처리
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인된 경우
                loadImage(); // 이미지 설정 등의 작업 수행
            } else {
                // 권한이 거부된 경우
                // 사용자에게 권한이 필요한 이유를 설명하거나 다른 조치를 취할 수 있음
            }
        }
    }
}
