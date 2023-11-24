package com.example.mobilesoftware;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.mobilesoftware.DatabaseHelper;

import java.util.Random;

public class InputFragment extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    String[] locationItems = {"상록원 1층", "상록원 2층", "상록원 3층", "기숙사 식당"};
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
    // 추가된 Calorie 변수
    int calorie;

    Spinner locationSpinner;
    Spinner mealTypeSpinner;

    // 파일로 저장된 이미지의 경로
    private static final int REQUEST_CODE = 0;

    // DatabaseHelper 객체 추가
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
        // 추가된 Calorie 변수 초기화
        calorie = generateRandomCalorie();

        // DatabaseHelper 초기화
        databaseHelper = new DatabaseHelper(requireContext());

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

                // 숫자 여부 확인 후 Toast 띄우기
                if (!isNumeric(year) || !isNumeric(month) || !isNumeric(day) || !isNumeric(hour) || !isNumeric(minute) || !isNumeric(cost)) {
                    showToast("숫자로 입력해주세요");
                    return;  // 숫자가 아니면 더 이상 진행하지 않음
                }

                // SQLite 데이터베이스에 데이터 추가
                long result = databaseHelper.addMeal(selectedLocation, mealName, mealOpinion, year, month, day, hour, minute, cost, null, calorie,selectedMealType);

                if (result != -1) {
                    showToast("식사 기록이 추가되었습니다.");
                } else {
                    showToast("식사 기록 추가 실패");
                }
            }
        });
        return rootView;
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

    // 랜덤한 칼로리 값 생성
    private int generateRandomCalorie() {
        Random random = new Random();
        return random.nextInt(501) + 300; // 300에서 800 사이의 랜덤한 값
    }

    @Override
    public void onDestroyView() {
        // DatabaseHelper 사용 후 반드시 close
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroyView();
    }
}
