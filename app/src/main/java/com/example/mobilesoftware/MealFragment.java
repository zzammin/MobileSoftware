package com.example.mobilesoftware;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        textMealType = rootView.findViewById(R.id.textMealType); // Add this line
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
                textMealType.setText("Meal Type: " + meal.getMealType()); // Add this line

//            if (meal.getImage() != null && meal.getImage().length > 0) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    checkAndRequestPermission();
//                }
//            }
            }
        }

        return rootView;
    }

//    private void checkAndRequestPermission() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(),
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_STORAGE_PERMISSION);
//        } else {
//            loadImage();
//        }
//    }

    private Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private void loadImage() {
        Meal meal = getArguments().getParcelable(ARG_MEAL);
        if (meal != null && meal.getImage() != null && meal.getImage().length > 0) {
            String base64Image = new String(meal.getImage());
            byte[] decodedImage = DatabaseHelper.decodeBase64ToByteArray(base64Image);

            Bitmap bitmap = byteArrayToBitmap(decodedImage);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImage();
            }
        }
    }
}





