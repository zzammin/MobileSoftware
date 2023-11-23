package com.example.mobilesoftware;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class InputFragment extends Fragment {

    String[] items = {"상록원 1층", "상록원 2층", "상록원 3층", "기숙사 식당"};
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

    // DatabaseHelper 객체 추가
    private DatabaseHelper databaseHelper;

    private final ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);

        Spinner spinner = rootView.findViewById(R.id.location_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        String selectedLocation = spinner.getSelectedItem().toString();

        Button selectImageBtn = rootView.findViewById(R.id.select_image_btn);
        imageView = rootView.findViewById(R.id.select_image);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult.launch(intent);
            }
        });

        mealNameEdit = rootView.findViewById(R.id.meal_name_edit);
        mealOpinionEdit = rootView.findViewById(R.id.meal_opinion_edit);
        yearEdit = rootView.findViewById(R.id.year);
        monthEdit = rootView.findViewById(R.id.month);
        dayEdit = rootView.findViewById(R.id.day);
        hourEdit = rootView.findViewById(R.id.hour);
        minuteEdit = rootView.findViewById(R.id.minute);
        costEdit = rootView.findViewById(R.id.cost_edit);

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

                if (year.isEmpty() || month.isEmpty() || day.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
                    showToast("숫자를 입력되지 않았습니다");
                    return;
                }

                if (!isNumeric(year) || !isNumeric(month) || !isNumeric(day) || !isNumeric(hour) || !isNumeric(minute) || !isNumeric(cost)) {
                    showToast("숫자를 입력되지 않았습니다");
                    return;
                }

                // SQLite 데이터베이스에 데이터 추가
                long result = databaseHelper.addMeal(selectedLocation, mealName, mealOpinion, year, month, day, hour, minute, cost);

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

    @Override
    public void onDestroyView() {
        // DatabaseHelper 사용 후 반드시 close
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroyView();
    }
}
