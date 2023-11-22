package com.example.mobilesoftware;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Spinner spinner = rootView.findViewById(R.id.location_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item,items);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = items[i];
                spinner.setSelection(i);
                sharedViewModel.setSelectedLocation(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("");
            }
        });

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

                // 값들을 Bundle에 담아서 다른 프래그먼트로 전달
                Bundle bundle = new Bundle();
                bundle.putString("mealName", mealName);
                bundle.putString("mealOpinion", mealOpinion);
                bundle.putString("year", year);
                bundle.putString("month", month);
                bundle.putString("day", day);
                bundle.putString("hour", hour);
                bundle.putString("minute", minute);
                bundle.putString("cost", cost);

                // SharedViewModel을 통해 데이터 전달
                sharedViewModel.setFormData(bundle);
            }
        });

        return rootView;
    }
}
