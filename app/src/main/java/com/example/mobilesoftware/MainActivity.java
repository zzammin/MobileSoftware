package com.example.mobilesoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // fragment를 사용하려면 FragmentManager가 필요
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // Fragment 클래스와 참조변수들
    InputFragment inputFragment;
    CalendarFragment calendarFragment;
    AnalysisFragment analysisFragment;
    // 이전에 선택된 버튼을 저장할 변수
    private Button previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputFragment = new InputFragment();
        calendarFragment = new CalendarFragment();
        analysisFragment = new AnalysisFragment();

        fragmentManager.beginTransaction().replace(R.id.fragment_linear, new InputFragment()).commit();
        // 하단 바 버튼들
        Button input_button = (Button) findViewById(R.id.input_button);
        Button calendar_button = (Button) findViewById(R.id.calendar_button);
        Button analysis_button = (Button) findViewById(R.id.analysis_button);
        // 초기 버튼 선택 상태를 설정
        previousButton = input_button;
        previousButton.setTextColor(Color.parseColor("#0000FF"));

        // 각 버튼 onClick 메소드 설정
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewId = view.getId();

                // 이전에 선택된 버튼의 텍스트 색상을 검정색으로 변경
                previousButton.setTextColor(Color.BLACK);

                if (viewId == R.id.input_button) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_linear, inputFragment).commit();
                    input_button.setTextColor(Color.parseColor("#0000FF"));
                    previousButton = input_button;
                } else if (viewId == R.id.calendar_button) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_linear, calendarFragment).commit();
                    calendar_button.setTextColor(Color.parseColor("#0000FF"));
                    previousButton = calendar_button;
                } else if (viewId == R.id.analysis_button) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_linear, analysisFragment).commit();
                    analysis_button.setTextColor(Color.parseColor("#0000FF"));
                    previousButton = analysis_button;
                }
            }
        };
        // 각 버튼의 setOnClickListener 설정
        input_button.setOnClickListener(buttonClickListener);
        calendar_button.setOnClickListener(buttonClickListener);
        analysis_button.setOnClickListener(buttonClickListener);
    }


}