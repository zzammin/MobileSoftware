package com.example.mobilesoftware;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InputFragment extends Fragment {
    String[] items = {"상록원 1층","상록원 2층","상록원 3층","기숙사 식당"};
    Uri uri;
    ImageView imageView;
    TextView textView;

    EditText mealNameEdit;
    EditText mealOpinionEdit;
    EditText yearEdit;
    EditText monthEdit;
    EditText dayEdit;
    EditText hourEdit;
    EditText minuteEdit;
    EditText costEdit;

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
                requireContext(), android.R.layout.simple_spinner_item,items);

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

        // 하단의 입력 버튼 누르면 모두 전달
        Button inputBtn = rootView.findViewById(R.id.input_btn);
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 값들을 읽어와서 다른 프래그먼트로 전달하는 코드 추가
                String mealName = mealNameEdit.getText().toString();
                String mealOpinion = mealOpinionEdit.getText().toString();
                String year = yearEdit.getText().toString();
                String month = monthEdit.getText().toString();
                String day = dayEdit.getText().toString();
                String hour = hourEdit.getText().toString();
                String minute = minuteEdit.getText().toString();
                String cost = costEdit.getText().toString();

                // 값이 비어있는지 확인하고 비어있으면 Toast를 통해 알림
                if (year.isEmpty() || month.isEmpty() || day.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
                    showToast("숫자를 입력되지 않았습니다");
                    return; // 빈 값이 있으면 더 이상 진행하지 않음
                }
                // year가 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(year)) {
                    showToast("년도는 숫자로 입력되어야 합니다.");
                    return; // 숫자가 아니면 더 이상 진행하지 않음
                }

                // month가 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(month)) {
                    showToast("월은 숫자로 입력되어야 합니다.");
                    return;
                }

                // day가 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(day)) {
                    showToast("일은 숫자로 입력되어야 합니다.");
                    return;
                }

                // hour가 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(hour)) {
                    showToast("시간은 숫자로 입력되어야 합니다.");
                    return;
                }

                // minute이 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(minute)) {
                    showToast("분은 숫자로 입력되어야 합니다.");
                    return;
                }
                
                // cost가 숫자가 아닌 경우 Toast를 통해 알림
                if (!isNumeric(cost)) {
                    showToast("비용은 숫자로 입력되어야 합니다.");
                    return; // 숫자가 아니면 더 이상 진행하지 않음
                }


                // 값들을 Bundle에 담아서 다른 프래그먼트로 전달
                // 잘 전달됨
                Bundle bundle = new Bundle();
                bundle.putString("selectedLocation", selectedLocation);
                bundle.putString("mealName", mealName);
                bundle.putString("mealOpinion", mealOpinion);
                bundle.putString("year", year);
                bundle.putString("month", month);
                bundle.putString("day", day);
                bundle.putString("hour", hour);
                bundle.putString("minute", minute);
                bundle.putString("cost", cost);

                // CalendarFragment로 직접 데이터 전달
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.setArguments(bundle);

                // FragmentTransaction을 사용하여 CalendarFragment로 전환
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_linear, calendarFragment)
                        .addToBackStack(null)
                        .commit();
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
}
