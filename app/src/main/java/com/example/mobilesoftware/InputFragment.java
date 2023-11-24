package com.example.mobilesoftware;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mobilesoftware.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private String savedImagePath;

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
                            // 이미지를 파일로 저장
                            savedImagePath = saveImageToFile(uri);

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

        Button selectImageBtn = rootView.findViewById(R.id.select_image_btn);
        imageView = rootView.findViewById(R.id.select_image);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지를 선택하기 전에 권한을 확인하고 요청
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없을 경우 권한 요청
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                } else {
                    // 이미 권한이 부여된 경우
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult.launch(intent);
                }
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

                // 이미지를 바이트 배열로 변환
                byte[] imageByteArray = null;
                if (savedImagePath != null) {
                    // 파일에서 ByteArray 획득
                    imageByteArray = getByteArrayFromFile(savedImagePath);
                }

                // SQLite 데이터베이스에 데이터 추가
                long result = databaseHelper.addMeal(selectedLocation, mealName, mealOpinion, year, month, day, hour, minute, cost, imageByteArray, calorie,selectedMealType);

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

    // Bitmap을 바이트 배열로 변환
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

    // 이미지를 파일로 저장
    private String saveImageToFile(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

            // 파일 이름을 생성 (예: image_20231201_123456.png)
            String fileName = "image_" +
                    System.currentTimeMillis() +
                    "." +
                    getFileExtension(imageUri);

            // 파일 경로 생성
            File file = new File(requireContext().getFilesDir(), fileName);

            // 파일로 이미지 복사
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // 파일에서 ByteArray로 변환
    private byte[] getByteArrayFromFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }

            fis.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
