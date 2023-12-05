package com.example.mobilesoftware;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.Random;

public class InputFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    String[] locationItems = {"상록원 1층", "상록원 2층", "상록원 3층", "기숙사 식당","가든쿡","가온누리","카페ing","두리터","블루포트","그루터기"};
    String[] mealTypeItems = {"식사", "음료"};
    Uri uri;
    ImageView imageView;

    EditText mealNameEdit;
    EditText mealOpinionEdit;
    EditText yearEdit;
    EditText monthEdit;
    EditText dayEdit;
    EditText hourEdit;
    EditText minuteEdit;
    EditText costEdit;
    int calorie;

    Spinner locationSpinner;
    Spinner mealTypeSpinner;

    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);

        locationSpinner = rootView.findViewById(R.id.location_spinner);
        mealTypeSpinner = rootView.findViewById(R.id.meal_type_spinner);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, locationItems);

        locationAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        locationSpinner.setAdapter(locationAdapter);

        ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, mealTypeItems);

        mealTypeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        mealTypeSpinner.setAdapter(mealTypeAdapter);

        mealNameEdit = rootView.findViewById(R.id.meal_name_edit);
        mealOpinionEdit = rootView.findViewById(R.id.meal_opinion_edit);
        yearEdit = rootView.findViewById(R.id.year);
        monthEdit = rootView.findViewById(R.id.month);
        dayEdit = rootView.findViewById(R.id.day);
        hourEdit = rootView.findViewById(R.id.hour);
        minuteEdit = rootView.findViewById(R.id.minute);
        costEdit = rootView.findViewById(R.id.cost_edit);
        calorie = generateRandomCalorie();

        databaseHelper = new DatabaseHelper(requireContext());

        imageView = rootView.findViewById(R.id.select_image);
        Button selectImageBtn = rootView.findViewById(R.id.select_image_btn);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button inputBtn = rootView.findViewById(R.id.input_btn);
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mealName = mealNameEdit.getText().toString();
                String mealOpinion = mealOpinionEdit.getText().toString();
                String year = yearEdit.getText().toString();
                String month = monthEdit.getText().toString();
                String day = dayEdit.getText().toString();
                String hour = hourEdit.getText().toString();
                String minute = minuteEdit.getText().toString();
                String cost = costEdit.getText().toString();
                String selectedLocation = locationSpinner.getSelectedItem().toString();
                String selectedMealType = mealTypeSpinner.getSelectedItem().toString();

                if (!isNumeric(year) || !isNumeric(month) || !isNumeric(day) || !isNumeric(hour) || !isNumeric(minute) || !isNumeric(cost)) {
                    showToast("숫자로 입력해주세요");
                    return;
                }

                long result = databaseHelper.addMeal(selectedLocation, mealName, mealOpinion, year, month, day, hour, minute, cost, calorie, selectedMealType, uri.toString());

                if (result != -1) {
                    mealNameEdit.getText().clear();
                    mealOpinionEdit.getText().clear();
                    yearEdit.getText().clear();
                    monthEdit.getText().clear();
                    dayEdit.getText().clear();
                    hourEdit.getText().clear();
                    minuteEdit.getText().clear();
                    costEdit.getText().clear();

                    // 이미지 뷰 지우기
                    imageView.setImageDrawable(null);

                    showToast("식사 기록이 추가되었습니다.");
                } else {
                    showToast("식사 기록 추가 실패");
                }
            }
        });

        return rootView;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            Glide.with(this)
                    .load(uri)
                    .into(imageView);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int generateRandomCalorie() {
        Random random = new Random();
        return random.nextInt(501) + 300;
    }

    @Override
    public void onDestroyView() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroyView();
    }
}
